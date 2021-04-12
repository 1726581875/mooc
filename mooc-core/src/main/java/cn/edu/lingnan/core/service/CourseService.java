package cn.edu.lingnan.core.service;

import cn.edu.lingnan.core.authentication.util.UserTokenUtil;
import cn.edu.lingnan.core.authentication.util.UserUtil;
import cn.edu.lingnan.core.client.NoticeServiceClient;
import cn.edu.lingnan.core.entity.*;
import cn.edu.lingnan.core.enums.CourseEnum;
import cn.edu.lingnan.core.param.CourseParam;
import cn.edu.lingnan.core.repository.CourseRepository;
import cn.edu.lingnan.core.repository.CourseTagRelRepository;
import cn.edu.lingnan.core.repository.MonitorRecordRepository;
import cn.edu.lingnan.core.repository.TagRepository;
import cn.edu.lingnan.core.util.CopyUtil;
import cn.edu.lingnan.core.vo.CourseVO;
import cn.edu.lingnan.core.vo.reception.ReceptionCourseVO;
import cn.edu.lingnan.mooc.common.model.PageVO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xmz
 * @date: 2020/10/13
 */
@Slf4j
@Service
public class CourseService {

    @Resource
    private CourseRepository courseRepository;
    @Resource
    private TagRepository tagRepository;

    @PersistenceContext
    private EntityManager entityManager;
    @Resource
    private MonitorRecordRepository monitorRecordRepository;
    @Resource
    private CourseTagRelRepository courseTagRelRepository;
    @Autowired
    private MoocUserService moocUserService;
    @Autowired
    private NoticeServiceClient noticeServiceClient;

    /**
     *
     * @param tagIdList
     * @param pageIndex
     * @param pageSize
     * @return
     */
   public PageVO<ReceptionCourseVO> getCourseByTagList(List<Integer> tagIdList, Integer pageIndex, Integer pageSize){

       Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.Direction.DESC,"create_time");
       Page<Course> coursePage = courseRepository.findCourseByTagList(tagIdList,pageable);
       List<Course> courseList = coursePage.getContent();
       List<ReceptionCourseVO> courseVOList = CopyUtil.copyList(courseList,ReceptionCourseVO.class);
       /* 4. 封装到自定义分页结果 */
       PageVO<ReceptionCourseVO> pageVO = new PageVO<>();
       pageVO.setContent(courseVOList);
       pageVO.setPageIndex(pageIndex);
       pageVO.setPageSize(pageSize);
       pageVO.setPageCount(coursePage.getTotalPages());
       pageVO.setPageTotal(coursePage.getNumberOfElements());
       return pageVO;
   }


    /**
     * 前台调用
     * 根据课程id list 获取对应课程名
     * @param courseIdList
     * @return
     */
    public Map<Integer,String> getCourseNameMap(List<Integer> courseIdList){
        // 构造sql
        StringBuilder sqlBuilder = new StringBuilder("select id,name from course where id in (");
        sqlBuilder.append(courseIdList.stream().map(String::valueOf).collect(Collectors.joining(",")));
        sqlBuilder.append(")");
        log.info("get course name sql is:{}",sqlBuilder.toString());
        // 执行语句
        Query query = entityManager.createNativeQuery(sqlBuilder.toString());
        // 获取和构造返回结果
        List<Object[]> resultList = query.getResultList();
        Map<Integer,String> resultMap = new HashMap<>();
        resultList.forEach(o -> resultMap.put(Integer.valueOf(o[0].toString()),o[1].toString()));
        return resultMap;
    }


    /**
     * 根据Id查找
     * @param id
     * @return 如果找不到返回null
     */
    public CourseVO findById(Integer id){
        Optional<Course> optional = courseRepository.findById(id);
        if(!optional.isPresent()){
            return null;
        }
        Course course = optional.get();
        CourseVO courseVO = CopyUtil.copy(course, CourseVO.class);
        courseVO.setStatus(CourseEnum.getText(course.getStatus()));
        //获取课程标签
        List<Tag> tagList = tagRepository.findTagListByCourseId(id);
        courseVO.setTagList(tagList);
        return courseVO;
    }

    public List<Course> findAll(){
        return courseRepository.findAll();
    }

    /**
     * 根据匹配条件查询所有
     * @param matchObject
     * @return
     */
    public List<Course> findAllByCondition(Course matchObject){
        return courseRepository.findAll(Example.of(matchObject));
    }

    /**
     * 条件分页查询
     * @param matchObject 匹配对象
     * @param pageIndex 第几页
     * @param pageSize 每页大小
     * @return
     */
    public PageVO<CourseVO> findPage(Course matchObject, Integer pageIndex, Integer pageSize){
        // 1、构造条件
        //如果是教师，只查询该教师的课程
        if(UserUtil.isTeacher()){
            matchObject.setTeacherId(UserUtil.getUserId());
        }
         // 1.1 设置匹配策略，name属性模糊查询,startsWith右模糊(name%)/contains全模糊(%name%)
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.contains());

         // 1.2 构造匹配条件Example对象
        Example<Course> example = Example.of(matchObject,matcher);
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.Direction.DESC,"createTime");
        Page<Course> coursePage = courseRepository.findAll(example, pageable);
        List<Course> courseList = coursePage.getContent();

        List<CourseVO> courseVOList = new ArrayList<>();
        List<Integer> teacherIdList = courseList.stream().map(Course::getTeacherId).collect(Collectors.toList());
        // 获取教师名字map
        Map<Integer, MoocUser> teacherMap = moocUserService.getUserMap(teacherIdList);

        // courseList -> courseVOList
        courseList.forEach(course -> courseVOList.add(createCourseVO(course, teacherMap)));

        /* 4. 封装到自定义分页结果 */
        PageVO<CourseVO> pageVO = new PageVO<>();
        pageVO.setContent(courseVOList);
        pageVO.setPageIndex(pageIndex);
        pageVO.setPageSize(pageSize);
        pageVO.setPageCount(coursePage.getTotalPages());
        return pageVO;
    }

    private CourseVO createCourseVO(Course course,Map<Integer, MoocUser> teacherMap){
        // 基本信息
        CourseVO courseVO = CopyUtil.copy(course, CourseVO.class);
        // 设置教师名
        MoocUser moocUser = teacherMap.getOrDefault(course.getTeacherId(), new MoocUser());
        courseVO.setTeacherName(moocUser.getName() == null ? "未知教师" : moocUser.getName() );
        // 转换状态为文本
        courseVO.setStatus(CourseEnum.getText(course.getStatus()));
        return courseVO;
    }


    /**
     * 插入数据
     * @param course
     * @return 返回成功数
     */
    public Course insert(Course course){
        if (course == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        // 新增课程
        Course newCourse = courseRepository.save(course);
        // todo 需要去优化
        // 保存监控记录
        MonitorRecord monitorRecord = new MonitorRecord();
        monitorRecord.setCourseId(newCourse.getId());
        monitorRecord.setRecordType("新增课程");
        monitorRecord.setCreateTime(newCourse.getCreateTime());
        monitorRecord.setMessage("新增了课程《" + newCourse.getName() +"》");
        monitorRecord.setTeacherId(newCourse.getTeacherId());
        monitorRecord.setIp("127.0.0.1");
        monitorRecordRepository.save(monitorRecord);
        return newCourse;
    }







    /**
     * 插入或更新数据
     * 说明:如果参数带id表示是更新，否则就是插入
     * @param courseParam
     * @return 返回成功数
     */
    @Transactional(rollbackFor = Exception.class)
    public Integer insertOrUpdate(CourseParam courseParam){
        if (courseParam == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }

        Course course = CopyUtil.copy(courseParam, Course.class);
        List<Tag> tagList = courseParam.getTagList();
        // id不为空，表示更新操作,更新课程标签、课程基本信息
        if(course.getId() != null){
            //先删标签关系
            courseTagRelRepository.deleteAllByCourseId(course.getId());
            //再保存
            if(!CollectionUtils.isEmpty(tagList)){
                List<CourseTagRel> tagRelList = tagList.stream().map(tag -> new CourseTagRel(course.getId(),tag.getId())).collect(Collectors.toList());
                courseTagRelRepository.saveAll(tagRelList);
            }
          //更新课程
          return this.update(course);
        }
        //否则就是插入课程，设置课程标签
        Integer userId = UserUtil.getUserId();
        if(StringUtils.isEmpty(course.getImage())){
            //如果没有上传图片，设置默认头像
            course.setImage("/file/default.png");
        }
        course.setTeacherId(userId);
        course.setStatus(CourseEnum.DRAFT.getStatus());
        //插入课程、课程标签关系
        Course newCourse = this.insert(course);
        if(!CollectionUtils.isEmpty(tagList)){
            List<CourseTagRel> tagRelList = tagList.stream().map(tag -> new CourseTagRel(newCourse.getId(),tag.getId())).collect(Collectors.toList());
            courseTagRelRepository.saveAll(tagRelList);
        }
        Map<Integer, MoocUser> userMap = moocUserService.getUserMap(Lists.newArrayList(UserUtil.getUserId()));
        //发送的消息内容
        String content = "用户" + userMap.getOrDefault(userId,new MoocUser()).getName() +"新增了课程《" + course.getSummary() + "》，需要审核";
        //创建临时token
        String token = UserTokenUtil.createToken();
        noticeServiceClient.sendCreateCourseNotice(token,userId,course.getId(), content);

        return newCourse == null ? 0 : 1;
    }


    /**
     *  选择性更新
     * @param course
     * @return 返回成功条数
     */
    public Integer update(Course course){
        // 入参校验
        if(course == null || course.getId() == null){
            throw new IllegalArgumentException("更新的对象不能为null");
        }
        // 是否存在
        Optional<Course> optional = courseRepository.findById(course.getId());
        if(!optional.isPresent()){
            throw new RuntimeException("找不到id为"+ course.getId() +"的Course");
        }
        Course dbCourse = optional.get();
        //把不为null的属性拷贝到dbCourse
        CopyUtil.notNullCopy(course, dbCourse);
        //执行保存操作
        Course updateCourse = courseRepository.save(dbCourse);

        // todo 暂时不记录
        // 保存修改课程的记录到监控记录表
/*        MonitorRecord monitorRecord = new MonitorRecord();
        monitorRecord.setCourseId(updateCourse.getId());
        monitorRecord.setRecordType("新增课程");
        monitorRecord.setCreateTime(updateCourse.getCreateTime());
        monitorRecord.setMessage("更新了课程名");
        monitorRecord.setTeacherId(updateCourse.getTeacherId());
        monitorRecord.setIp("127.0.0.1");
        monitorRecordRepository.save(monitorRecord);*/

        return updateCourse == null ? 0 : 1;
    }


    public Integer deleteById(Integer id){
        courseRepository.deleteById(id);
        return  findById(id) == null ? 1 : 0;
    }

    /**
     * 批量删除
     * @param courseIdList  id list
     * @return 删除条数
     */
    public Integer deleteAllByIds(List<Integer> courseIdList){
        List<Course> delCourseList = courseRepository.findAllById(courseIdList);
        courseRepository.deleteInBatch(delCourseList);
        return delCourseList.size();
    }


}
