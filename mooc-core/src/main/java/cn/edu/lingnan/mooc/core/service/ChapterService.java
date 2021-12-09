package cn.edu.lingnan.mooc.core.service;

import cn.edu.lingnan.mooc.core.repository.ChapterRepository;
import cn.edu.lingnan.mooc.core.util.CopyUtil;
import cn.edu.lingnan.mooc.core.model.vo.ChapterVO;
import cn.edu.lingnan.mooc.core.model.vo.CourseVO;
import cn.edu.lingnan.mooc.common.model.PageVO;
import cn.edu.lingnan.mooc.core.model.entity.Chapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xmz
 * @date: 2020/10/08
 */
@Slf4j
@Service
public class ChapterService {

    @Resource
    private ChapterRepository chapterRepository;
    @Autowired
    private CourseService courseService;

    /**
     * 交换排序位置
     * @param id1
     * @param sort1
     * @param id2
     * @param sort2
     */
    @Transactional
    public void chapterSortSwap(Long id1,Integer sort1, Long id2, Integer sort2){
        Chapter chapter1 = new Chapter();
        chapter1.setId(id1);
        chapter1.setSort(sort2);
        Chapter chapter2 = new Chapter();
        chapter2.setId(id2);
        chapter2.setSort(sort1);
        // 保存交换信息
        this.update(chapter1);
        this.update(chapter2);
    }
    /**
     * 根据Id查找
     * @param id
     * @return 如果找不到返回null
     */
    public Chapter findById(Long id){
        Optional<Chapter> optional = chapterRepository.findById(id);
        if(!optional.isPresent()){
            return null;
        }
        return optional.get();
    }

    public List<Chapter> findAll(){
        return chapterRepository.findAll();
    }

    /**
     * 根据匹配条件查询所有
     * @param matchObject
     * @return
     */
    public List<Chapter> findAllByCondition(Chapter matchObject){
        return chapterRepository.findAll(Example.of(matchObject));
    }

    /**
     * 条件分页查询
     * @param matchObject 匹配对象
     * @param pageIndex 第几页
     * @param pageSize 每页大小
     * @return
     */
    public PageVO<ChapterVO> findPage(Chapter matchObject, Integer pageIndex, Integer pageSize){
        // 1、构造条件
         // 1.1 设置匹配策略，name属性模糊查询
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.contains());//startsWith右模糊(name%)/contains全模糊(%name%)
         // 1.2 构造匹配条件Example对象
        Example<Chapter> example = Example.of(matchObject,matcher);

        // 二次排序
        List<Sort.Order> orders= Arrays.asList(
                new Sort.Order(Sort.Direction.DESC,"courseId"),
                new Sort.Order(Sort.Direction.ASC,"sort")
        );
        // 2、 构造分页参数 ,第几页,每页大小,排序（按创建时间倒序）
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.by(orders));
        // 3、 传入条件、分页参数，调用方法
        Page<Chapter> chapterPage = chapterRepository.findAll(example, pageable);
        //获取page对象里的list
        List<Chapter> chapterList = chapterPage.getContent();
        // 大章对应课程Map<课程id，课程对象>
        Map<Long, CourseVO> courseMap = new HashMap<>(10);
        if(!CollectionUtils.isEmpty(chapterList)) {
            Set<Long> chapterIdSet = chapterList.stream().map(Chapter::getCourseId).collect(Collectors.toSet());
            chapterIdSet.forEach(id -> courseMap.put(id,courseService.findById(id)));
        }
        // 转换成VO对象
        List<ChapterVO> chapterVOList = CopyUtil.copyList(chapterList, ChapterVO.class);
        // 设置课程名
        chapterVOList.forEach(e -> {
            e.setCourseName(courseMap.getOrDefault(e.getCourseId(), new CourseVO()).getName());
            e.setSectionList(new ArrayList<>());
        });
        // 排序，章节顺序按照sort字段升序排
       // Collections.sort(chapterVOList , Comparator.comparingInt(ChapterVO::getSort));
        /* 4. 封装到自定义分页结果 */
        PageVO<ChapterVO> pageVO = new PageVO<>();
        pageVO.setContent(chapterVOList);
        pageVO.setPageIndex(pageIndex);
        pageVO.setPageSize(pageSize);
        pageVO.setPageCount(chapterPage.getTotalPages());
        return pageVO;
    }

    /**
     * 插入或更新数据
     * 说明:如果参数带id表示是更新，否则就是插入
     * @param chapter
     * @return 返回成功数
     */
    public Integer insertOrUpdate(Chapter chapter){
        if (chapter == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        // id不为空，表示更新操作
        if(chapter.getId() != null){
          return this.update(chapter);
        }
        Chapter newChapter = chapterRepository.save(chapter);
        return newChapter == null ? 0 : 1;
    }

    /**
     *  选择性更新
     * @param chapter
     * @return 返回成功条数
     */
    public Integer update(Chapter chapter){
        // 入参校验
        if(chapter == null || chapter.getId() == null){
            throw new IllegalArgumentException("更新的对象不能为null");
        }
        // 是否存在
        Optional<Chapter> optional = chapterRepository.findById(chapter.getId());
        if(!optional.isPresent()){
            throw new RuntimeException("找不到id为"+ chapter.getId() +"的Chapter");
        }
        Chapter dbChapter = optional.get();
        //把不为null的属性拷贝到dbChapter
        CopyUtil.notNullCopy(chapter, dbChapter);
        //执行保存操作
        Chapter updateChapter = chapterRepository.save(dbChapter);
        return updateChapter == null ? 0 : 1;
    }


    public Integer deleteById(Long id){
        chapterRepository.deleteById(id);
        return  findById(id) == null ? 1 : 0;
    }

    /**
     * 批量删除
     * @param chapterIdList  id list
     * @return 删除条数
     */
    public Integer deleteAllByIds(List<Long> chapterIdList){
        List<Chapter> delChapterList = chapterRepository.findAllById(chapterIdList);
        chapterRepository.deleteInBatch(delChapterList);
        return delChapterList.size();
    }


}
