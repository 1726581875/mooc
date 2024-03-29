package cn.edu.lingnan.core.service;
import cn.edu.lingnan.core.entity.Tag;
import cn.edu.lingnan.core.repository.TagRepository;
import cn.edu.lingnan.core.util.CopyUtil;
import cn.edu.lingnan.mooc.common.model.PageVO;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author xmz
 * @date: 2020/12/05
 */
@Service
public class TagService {

    @Resource
    private TagRepository tagRepository;

    /**
     * 根据分类id查找标签
     * @param categoryIdList 分类id list
     * @return
     */
    public Map<Integer,List<Tag>> findTagList(List<Integer> categoryIdList){
        List<Tag> tagList = tagRepository.findAllByCategoryIdIn(categoryIdList);
        return tagList.stream().collect(Collectors.groupingBy(Tag::getCategoryId));
    }

    /**
     * 根据Id查找
     * @param id
     * @return 如果找不到返回null
     */
    public Tag findById(Integer id){
        Optional<Tag> optional = tagRepository.findById(id);
        if(!optional.isPresent()){
            return null;
        }
        return optional.get();
    }

    public List<Tag> findAll(){
        return tagRepository.findAll();
    }

    /**
     * 根据匹配条件查询所有
     * @param matchObject
     * @return
     */
    public List<Tag> findAllByCondition(Tag matchObject){
        return tagRepository.findAll(Example.of(matchObject));
    }

    /**
     * 条件分页查询
     * @param matchObject 匹配对象
     * @param pageIndex 第几页
     * @param pageSize 每页大小
     * @return
     */
    public PageVO<Tag> findPage(Tag matchObject, Integer pageIndex, Integer pageSize){
        // 1、构造条件
         // 1.1 设置匹配策略，name属性模糊查询
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.startsWith());//startsWith右模糊(name%)/contains全模糊(%name%)
         // 1.2 构造匹配条件Example对象
        Example<Tag> example = Example.of(matchObject,matcher);

        // 2、 构造分页参数 ,第几页,每页大小
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        // 3、 传入条件、分页参数，调用方法
        Page<Tag> tagPage = tagRepository.findAll(example, pageable);
        //获取page对象里的list
        List<Tag> tagList = tagPage.getContent();
        /* 4. 封装到自定义分页结果 */
        PageVO<Tag> pageVO = new PageVO<>();
        pageVO.setContent(tagList);
        pageVO.setPageIndex(pageIndex);
        pageVO.setPageSize(pageSize);
        pageVO.setPageCount(tagPage.getTotalPages());
        return pageVO;
    }

    /**
     * 插入数据
     * @param tag
     * @return 返回成功数
     */
    public Integer insert(Tag tag){
        if (tag == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        Tag newTag = tagRepository.save(tag);
        return newTag == null ? 0 : 1;
    }

    /**
     * 插入或更新数据
     * 说明:如果参数带id表示是更新，否则就是插入
     * @param tag
     * @return 返回成功数
     */
    public Integer insertOrUpdate(Tag tag){
        if (tag == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        // id不为空，表示更新操作
        if(tag.getId() != null){
          return this.update(tag);
        }
        Tag newTag = tagRepository.save(tag);
        return newTag == null ? 0 : 1;
    }


    /**
     *  选择性更新
     * @param tag
     * @return 返回成功条数
     */
    public Integer update(Tag tag){
        // 入参校验
        if(tag == null || tag.getId() == null){
            throw new IllegalArgumentException("更新的对象不能为null");
        }
        // 是否存在
        Optional<Tag> optional = tagRepository.findById(tag.getId());
        if(!optional.isPresent()){
            throw new RuntimeException("找不到id为"+ tag.getId() +"的Tag");
        }
        Tag dbTag = optional.get();
        //把不为null的属性拷贝到dbTag
        CopyUtil.notNullCopy(tag, dbTag);
        //执行保存操作
        Tag updateTag = tagRepository.save(dbTag);
        return updateTag == null ? 0 : 1;
    }


    public Integer deleteById(Integer id){
        tagRepository.deleteById(id);
        return  findById(id) == null ? 1 : 0;
    }

    /**
     * 批量删除
     * @param tagIdList  id list
     * @return 删除条数
     */
    public Integer deleteAllByIds(List<Integer> tagIdList){
        List<Tag> delTagList = tagRepository.findAllById(tagIdList);
        tagRepository.deleteInBatch(delTagList);
        return delTagList.size();
    }


}
