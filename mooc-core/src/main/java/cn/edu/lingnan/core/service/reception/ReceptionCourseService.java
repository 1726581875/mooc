package cn.edu.lingnan.core.service.reception;

import cn.edu.lingnan.core.authentication.util.UserUtil;
import cn.edu.lingnan.core.constant.RedisPrefixConstant;
import cn.edu.lingnan.core.entity.Course;
import cn.edu.lingnan.core.entity.MonitorRecord;
import cn.edu.lingnan.core.entity.MoocUser;
import cn.edu.lingnan.core.entity.reception.Collection;
import cn.edu.lingnan.core.repository.CourseRepository;
import cn.edu.lingnan.core.repository.MonitorRecordRepository;
import cn.edu.lingnan.core.repository.MoocUserRepository;
import cn.edu.lingnan.core.repository.reception.CollectionRepository;
import cn.edu.lingnan.core.util.CopyUtil;
import cn.edu.lingnan.core.util.RedisUtil;
import cn.edu.lingnan.core.vo.ChapterVO;
import cn.edu.lingnan.core.vo.reception.CourseDetailVO;
import cn.edu.lingnan.core.vo.reception.ReceptionCourseVO;
import cn.edu.lingnan.mooc.common.model.PageVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author xmz
 * @date: 2021/01/31
 * 前台系统 课程service类
 */
@Slf4j
@Service
public class ReceptionCourseService {

    @Resource
    private CourseRepository courseRepository;
    @Resource
    private MoocUserRepository moocUserRepository;
    @Resource
    private CollectionRepository collectionRepository;
    @Autowired
    private ReceptionChapterService receptionChapterService;
    @Autowired
    private MonitorRecordRepository monitorRecordRepository;

    /**
     *  课程的收藏或者取消收藏
     * 1、检查redis里是否有该收藏数缓存，没有则设置
     * -- 不存在则设置
     * 2、根据courseId和userId查询收藏表，确认记录在数据库是否存在
     * -- 若收藏记录不存在，表示是收藏，插入记录，redis计数+1
     * -- 若收藏记录存在，表示是取消收藏操作，删除记录，redis计数-1
     * @param courseId
     */
    public void collectionOrCancel(Integer courseId){

        String collectionNumKey = RedisPrefixConstant.COLLECTION_NUM_PRE + courseId;
        //1、检查redis里是否有该收藏数缓存，没有则设置
        this.getCollectionNumByCache(courseId);

        //2、根据courseId和userId查询收藏表，确认记录在数据库是否存在
        Collection collection = new Collection()
                .setCourseId(courseId)
                .setUserId(UserUtil.getUserId());
        Optional<Collection> collectionOptional = collectionRepository.findOne(Example.of(collection));
        if (!collectionOptional.isPresent()){
            //如果不存在，表示是收藏操作
            collectionRepository.save(collection);
            //缓存的收藏数量加1
            RedisUtil.getRedisTemplate().opsForValue().increment(collectionNumKey, 1L);
            //记录达到监控记录
            this.insertMonitorRecord(courseId,UserUtil.getUserId(),"收藏");
        }else {
            //如果已经存在，表示是取消收藏操作
            collection = collectionOptional.get();
            collectionRepository.deleteById(collection.getId());
            //缓存的收藏数量减1
            if(RedisUtil.get(collectionNumKey,Integer.class) > 0){
            RedisUtil.getRedisTemplate().opsForValue().increment(collectionNumKey, -1L);
            //记录达到监控记录
            this.insertMonitorRecord(courseId,UserUtil.getUserId(),"取消收藏");
         }
        }

    }


    public void insertMonitorRecord(Integer courseId,Integer userId,String type){
        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if(!courseOptional.isPresent()){
            log.error("插入监控记录失败，点赞的课程不存在，courseId={}",courseId);
            return;
        }
        Optional<MoocUser>userOptional = moocUserRepository.findById(userId);
        if(!userOptional.isPresent()){
            log.error("插入监控记录失败，用户不存在，userId={}",userId);
            return;
        }

        Course course = courseOptional.get();
        MoocUser user = userOptional.get();
        // 保存监控记录
        MonitorRecord monitorRecord = new MonitorRecord();
        monitorRecord.setCourseId(course.getId());
        monitorRecord.setRecordType("课程收藏");
        monitorRecord.setCreateTime(course.getCreateTime());
        monitorRecord.setMessage(user.getName() + " "+ type +"了课程《" + course.getName() +"》");
        monitorRecord.setTeacherId(course.getTeacherId());
        monitorRecord.setIp("127.0.0.1");
        monitorRecordRepository.save(monitorRecord);
    }


    /**
     * 获取缓存里的课程收藏数
     * 如果没有缓存，则设置缓存
     * @param courseId
     */
    public Integer getCollectionNumByCache(Integer courseId){
        String collectionNumKey = RedisPrefixConstant.COLLECTION_NUM_PRE + courseId;
        if(RedisUtil.isNotExist(collectionNumKey)){
            Optional<Course> courseOptional = courseRepository.findById(courseId);
            if(!courseOptional.isPresent()){
                log.error("收藏/取消收藏课程-添加缓存失败,该课程不存在，courseId={}",courseId);
                throw new RuntimeException("收藏课程失败，该课程不存在");
            }
            Course course = courseOptional.get();
            //缓存两天
            RedisUtil.set(collectionNumKey,course.getCollectionNum(),RedisPrefixConstant.CACHE_DAY_NUM, TimeUnit.DAYS);
        }
        return RedisUtil.get(collectionNumKey,Integer.class);
    }



    /**
     * 根据可从Id，查询课程详情
     * 1、获取课程基本信息
     * 2、获取课程教师信息
     * 3、获取章节信息
     * 4、判断课程是否已经收藏
     * @param courseId
     * @return
     */
    public CourseDetailVO findCourseDetailById(Integer courseId){

        Optional<Course> courseOptional = courseRepository.findById(courseId);
        if(courseOptional.isPresent()){
            //1、获取课程基本信息
            Course course = courseOptional.get();
            CourseDetailVO courseDetailVO = CopyUtil.copy(course, CourseDetailVO.class);
            //从缓存里获取收藏数/观看数（redis缓存）
            Integer collectionNum = this.getCollectionNumByCache(courseId);
            courseDetailVO.setCollectionNum(collectionNum);
            //设置观看数（redis缓存）
            if(RedisUtil.isExist(RedisPrefixConstant.VIEW_NUM_PRE + courseId)) {
                Long learningNum = RedisUtil.getRedisTemplate().opsForValue()
                        .increment(RedisPrefixConstant.VIEW_NUM_PRE + courseId,1L);
                courseDetailVO.setLearningNum(Integer.valueOf(Math.toIntExact(learningNum)));
            }else {
                //缓存两天
                RedisUtil.set(RedisPrefixConstant.VIEW_NUM_PRE + courseId, course.getCollectionNum(),RedisPrefixConstant.CACHE_DAY_NUM, TimeUnit.DAYS);
             }
            //设置评论数(缓存)
            if(RedisUtil.setIfAbsent(RedisPrefixConstant.COMMENT_NUM_PRE + courseId,String.valueOf(course.getCommentNum()),
                    RedisPrefixConstant.CACHE_DAY_NUM, TimeUnit.DAYS)){
                courseDetailVO.setCommentNum(course.getCommentNum());
            }else{
                //去缓存中拿
                courseDetailVO.setCommentNum(RedisUtil.get(RedisPrefixConstant.COMMENT_NUM_PRE + courseId,Integer.class));
            }
            //设置课程问答数缓存
            if(RedisUtil.setIfAbsent(RedisPrefixConstant.QUESTION_NUM_PRE + courseId,String.valueOf(course.getQuestionNum()))){
                courseDetailVO.setQuestionNum(course.getQuestionNum());
            }else{
                //去缓存中拿
                courseDetailVO.setQuestionNum(RedisUtil.get(RedisPrefixConstant.QUESTION_NUM_PRE + courseId,Integer.class));
            }

            //2、获取教师基本信息,设置到VO对象
            Optional<MoocUser> teacherOptional = moocUserRepository.findById(course.getTeacherId());
            courseDetailVO.setTeacher(teacherOptional.get());
            //3、获取章节信息,设置到VO对象
            List<ChapterVO> chapterVOList = receptionChapterService.findAllChapterByCourseId(courseId);
            courseDetailVO.setChapterList(chapterVOList);
            //4、判断是否已经收藏
            if(UserUtil.getUserToken() != null) {
                Collection collection = new Collection().setCourseId(courseId).setUserId(UserUtil.getUserId());
                Optional<Collection> optional = collectionRepository.findOne(Example.of(collection));
                Boolean isCollection = optional.isPresent() ? true : false;
                courseDetailVO.setCollection(isCollection);
            }else {
                courseDetailVO.setCollection(false);
            }

            return courseDetailVO;
        }


        return null;
    }

    /**
     * 根据教师id获取他/她开设的课程
     * @param teacherId
     * @param pageIndex
     * @param pageSize
     * @return
     */
   public PageVO<ReceptionCourseVO> findCourseByTeachId(Integer teacherId,Integer pageIndex,Integer pageSize){
       //构造匹配条件Example对象
       Course matchObject = new Course();
       matchObject.setTeacherId(teacherId);
       Example<Course> example = Example.of(matchObject);
       //构造pageable分页条件、排序
       Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.Direction.DESC,"createTime");
       Page<Course> coursePage = courseRepository.findAll(example, pageable);
       List<Course> courseList = coursePage.getContent();
       //对象转换，属性值复制
       List<ReceptionCourseVO> courseVOList = CopyUtil.copyList(courseList,ReceptionCourseVO.class);

       /* 4. 封装到自定义分页结果 */
       PageVO<ReceptionCourseVO> pageVO = new PageVO<>();
       pageVO.setContent(courseVOList);
       pageVO.setPageIndex(pageIndex);
       pageVO.setPageSize(pageSize);
       pageVO.setPageCount(coursePage.getTotalPages());
       return pageVO;
   }


    /**
     * 根据普通用户id获取他/她开设的课程
     * @param userId
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageVO<ReceptionCourseVO> findCollectionCourseByUserId(Integer userId,Integer pageIndex,Integer pageSize){

        //获取课程id
        Collection collection = new Collection();
        collection.setUserId(userId);
        List<Collection> collectionList = collectionRepository.findAll(Example.of(collection));
        List<Integer> courseIdList = collectionList.stream().map(Collection::getCourseId).collect(Collectors.toList());

        //构造查询条件 in查询
        Specification<Course> specification = new Specification<Course>() {
            @Override
            public Predicate toPredicate(Root<Course> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                CriteriaBuilder.In<Integer> in = cb.in(root.get("id"));
                courseIdList.forEach(id->in.value(id));
                return  cb.and(in);
            }
        };

        //构造pageable分页条件、排序，获取课程信息
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.Direction.DESC,"createTime");
        Page<Course> coursePage = courseRepository.findAll(specification, pageable);
        List<Course> courseList = coursePage.getContent();
        //对象转换，属性值复制
        List<ReceptionCourseVO> courseVOList = CopyUtil.copyList(courseList,ReceptionCourseVO.class);
        /* 4. 封装到自定义分页结果 */
        PageVO<ReceptionCourseVO> pageVO = new PageVO<>();
        pageVO.setContent(courseVOList);
        pageVO.setPageIndex(pageIndex);
        pageVO.setPageSize(pageSize);
        pageVO.setPageCount(coursePage.getTotalPages());
        return pageVO;
    }


}
