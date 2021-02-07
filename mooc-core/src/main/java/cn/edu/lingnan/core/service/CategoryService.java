package cn.edu.lingnan.core.service;

import cn.edu.lingnan.core.authentication.util.UserUtil;
import cn.edu.lingnan.core.entity.Category;
import cn.edu.lingnan.core.entity.Tag;
import cn.edu.lingnan.core.param.CategoryParam;
import cn.edu.lingnan.core.repository.CategoryRepository;
import cn.edu.lingnan.core.repository.TagRepository;
import cn.edu.lingnan.core.util.CopyUtil;
import cn.edu.lingnan.core.vo.CategoryVO;
import cn.edu.lingnan.mooc.common.model.PageVO;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author xmz
 * @date: 2020/12/05
 */
@Service
public class CategoryService {

    @Resource
    private CategoryRepository categoryRepository;
    @Resource
    private TagRepository tagRepository;
    @Autowired
    private TagService tagService;

    /**
     * 根据Id查找
     * @param id
     * @return 如果找不到返回null
     */
    public Category findById(Integer id){
        Optional<Category> optional = categoryRepository.findById(id);
        if(!optional.isPresent()){
            return null;
        }
        return optional.get();
    }

    public List<CategoryVO> findAll(){
        // get all categoryList
        List<Category> categoryList = categoryRepository.findAll();
        // get categoryIdList
        List<Integer> categoryIdList = categoryList.stream().map(Category::getId).collect(Collectors.toList());
        // get tagList
        List<Tag> tagList = tagRepository.findAllByCategoryIdIn(categoryIdList);
        // categoryId/tagList map
        Map<Integer, List<Tag>> tagMap = tagList.stream().collect(Collectors.groupingBy(Tag::getCategoryId));
        //categoryList转换为categoryVOList
        List<CategoryVO> categoryVOList = CopyUtil.copyList(categoryList, CategoryVO.class);
        //set tagList
        categoryVOList.stream().forEach(e->e.setTagList(tagMap.getOrDefault(e.getId(), Lists.newArrayList())));
        return categoryVOList;
    }

    /**
     * 根据匹配条件查询所有
     * @param matchObject
     * @return
     */
    public List<Category> findAllByCondition(Category matchObject){
        return categoryRepository.findAll(Example.of(matchObject));
    }

    /**
     * 条件分页查询
     * @param matchObject 匹配对象
     * @param pageIndex 第几页
     * @param pageSize 每页大小
     * @return
     */
    public PageVO<CategoryVO> findPage(Category matchObject, Integer pageIndex, Integer pageSize){
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.contains());
        Example<Category> example = Example.of(matchObject,matcher);
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.Direction.DESC,"createTime");
        Page<Category> categoryPage = categoryRepository.findAll(example, pageable);
        //获取page对象里的list
        List<Category> categoryList = categoryPage.getContent();
        // 构造返回CategoryVO对象
        List<CategoryVO> categoryVOList = CopyUtil.copyList(categoryList,CategoryVO.class);
        Map<Integer, List<Tag>> tagMap = tagService.findTagList(categoryList.stream().map(Category::getId).collect(Collectors.toList()));
        categoryVOList.forEach(e->e.setTagList(tagMap.get(e.getId())));
        /* 4. 封装到自定义分页结果 */
        PageVO<CategoryVO> pageVO = new PageVO<>();
        pageVO.setContent(categoryVOList);
        pageVO.setPageIndex(pageIndex);
        pageVO.setPageSize(pageSize);
        pageVO.setPageCount(categoryPage.getTotalPages());
        return pageVO;
    }

    /**
     * 插入数据
     * @param category
     * @return 返回成功数
     */
    public Integer insert(Category category){
        if (category == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        Category newCategory = categoryRepository.save(category);
        return newCategory == null ? 0 : 1;
    }

    /**
     * 插入或更新数据
     * 说明:如果参数带id表示是更新，否则就是插入
     * @param categoryParam
     * @return 返回成功数
     */
    public Integer insertOrUpdate(CategoryParam categoryParam){
        if (categoryParam == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        Category category = CopyUtil.copy(categoryParam, Category.class);
        // 获取标签 list
        List<Tag> tagList = categoryParam.getTagList();

        // id不为空，表示更新操作
        if(category.getId() != null){
            List<Tag> dbTagList = tagRepository.findAllByCategoryId(category.getId());
            // 插入或更新标签
            if(tagList != null && !tagList.isEmpty()){
                tagList.forEach(tag -> tag.setCategoryId(category.getId()));

                // 逻辑删除不存在的标签关系
                List<Tag> deleteTagList = new ArrayList<>();
                for(int i = 0; i < dbTagList.size(); i++){
                    boolean notExists = true;
                    for (int j = 0; j < tagList.size(); j++){
                        // 是否存在
                        if(dbTagList.get(i).getId().equals(tagList.get(j).getId())
                                || dbTagList.get(i).getName().equals(tagList.get(j).getName())){
                            notExists = false;
                        }
                    }
                    // 拿到需要删除的tag
                    if(notExists) {
                        deleteTagList.add(dbTagList.get(i));
                    }
                }

                tagRepository.deleteInBatch(deleteTagList);
                tagRepository.saveAll(tagList);
            }

          return this.update(category);
        }
        // 插入新分类
        Category newCategory = categoryRepository.save(category);
        if(tagList != null && !tagList.isEmpty()){
            // 插入标签
            tagList.forEach(tag -> tag.setCategoryId(newCategory.getId()));
            tagRepository.saveAll(tagList);
        }
        return newCategory == null ? 0 : 1;
    }


    /**
     *  选择性更新
     * @param category
     * @return 返回成功条数
     */
    public Integer update(Category category){
        // 入参校验
        if(category == null || category.getId() == null){
            throw new IllegalArgumentException("更新的对象不能为null");
        }
        // 是否存在
        Optional<Category> optional = categoryRepository.findById(category.getId());
        if(!optional.isPresent()){
            throw new RuntimeException("找不到id为"+ category.getId() +"的Category");
        }
        Category dbCategory = optional.get();
        //把不为null的属性拷贝到dbCategory
        CopyUtil.notNullCopy(category, dbCategory);
        //执行保存操作
        Category updateCategory = categoryRepository.save(dbCategory);
        return updateCategory == null ? 0 : 1;
    }


    public Integer deleteById(Integer id){
        categoryRepository.deleteById(id);
        return  findById(id) == null ? 1 : 0;
    }

    /**
     * 批量删除
     * @param categoryIdList  id list
     * @return 删除条数
     */
    public Integer deleteAllByIds(List<Integer> categoryIdList){
        List<Category> delCategoryList = categoryRepository.findAllById(categoryIdList);
        categoryRepository.deleteInBatch(delCategoryList);
        return delCategoryList.size();
    }


}
