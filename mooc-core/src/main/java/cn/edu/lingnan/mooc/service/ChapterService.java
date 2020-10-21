package cn.edu.lingnan.mooc.service;

import cn.edu.lingnan.mooc.common.model.PageVO;
import cn.edu.lingnan.mooc.entity.Chapter;
import cn.edu.lingnan.mooc.repository.ChapterRepository;
import cn.edu.lingnan.mooc.util.CopyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @author xmz
 * @date: 2020/10/08
 */
@Slf4j
@Service
public class ChapterService {

    @Resource
    private ChapterRepository chapterRepository;

    /**
     * 根据Id查找
     * @param id
     * @return 如果找不到返回null
     */
    public Chapter findById(Integer id){
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
    public PageVO<Chapter> findPage(Chapter matchObject, Integer pageIndex, Integer pageSize){
        // 1、构造条件
         // 1.1 设置匹配策略，name属性模糊查询
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.startsWith());//startsWith右模糊(name%)/contains全模糊(%name%)
         // 1.2 构造匹配条件Example对象
        Example<Chapter> example = Example.of(matchObject,matcher);

        // 2、 构造分页参数 ,第几页,每页大小,排序（按创建时间倒序）
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize,Sort.Direction.DESC ,"createTime");
        // 3、 传入条件、分页参数，调用方法
        Page<Chapter> chapterPage = chapterRepository.findAll(example, pageable);
        //获取page对象里的list
        List<Chapter> chapterList = chapterPage.getContent();
        /* 4. 封装到自定义分页结果 */
        PageVO<Chapter> pageVO = new PageVO<>();
        pageVO.setContent(chapterList);
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


    public Integer deleteById(Integer id){
        chapterRepository.deleteById(id);
        return  findById(id) == null ? 1 : 0;
    }

    /**
     * 批量删除
     * @param chapterIdList  id list
     * @return 删除条数
     */
    public Integer deleteAllByIds(List<Integer> chapterIdList){
        List<Chapter> delChapterList = chapterRepository.findAllById(chapterIdList);
        chapterRepository.deleteInBatch(delChapterList);
        return delChapterList.size();
    }


}
