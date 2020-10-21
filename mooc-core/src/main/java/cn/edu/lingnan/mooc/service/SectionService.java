package cn.edu.lingnan.mooc.service;

import cn.edu.lingnan.mooc.common.model.PageVO;
import cn.edu.lingnan.mooc.entity.Section;
import cn.edu.lingnan.mooc.repository.SectionRepository;
import cn.edu.lingnan.mooc.util.CopyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * @author xmz
 * @date: 2020/10/11
 */
@Slf4j
@Service
public class SectionService {

    @Resource
    private SectionRepository sectionRepository;

    /**
     * 根据Id查找
     * @param id
     * @return 如果找不到返回null
     */
    public Section findById(Integer id){
        Optional<Section> optional = sectionRepository.findById(id);
        if(!optional.isPresent()){
            return null;
        }
        return optional.get();
    }

    public List<Section> findAll(){
        return sectionRepository.findAll();
    }

    /**
     * 根据匹配条件查询所有
     * @param matchObject
     * @return
     */
    public List<Section> findAllByCondition(Section matchObject){
        return sectionRepository.findAll(Example.of(matchObject));
    }

    /**
     * 条件分页查询
     * @param matchObject 匹配对象
     * @param pageIndex 第几页
     * @param pageSize 每页大小
     * @return
     */
    public PageVO<Section> findPage(Section matchObject, Integer pageIndex, Integer pageSize){
        // 1、构造条件
         // 1.1 设置匹配策略，name属性模糊查询
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.startsWith());//startsWith右模糊(name%)/contains全模糊(%name%)
         // 1.2 构造匹配条件Example对象
        Example<Section> example = Example.of(matchObject,matcher);

        // 2、 构造分页参数 ,第几页,每页大小
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        // 3、 传入条件、分页参数，调用方法
        Page<Section> sectionPage = sectionRepository.findAll(example, pageable);
        //获取page对象里的list
        List<Section> sectionList = sectionPage.getContent();
        /* 4. 封装到自定义分页结果 */
        PageVO<Section> pageVO = new PageVO<>();
        pageVO.setContent(sectionList);
        pageVO.setPageIndex(pageIndex);
        pageVO.setPageSize(pageSize);
        pageVO.setPageCount(sectionPage.getTotalPages());
        return pageVO;
    }

    /**
     * 插入数据
     * @param section
     * @return 返回成功数
     */
    public Integer insert(Section section){
        if (section == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        Section newSection = sectionRepository.save(section);
        return newSection == null ? 0 : 1;
    }

    /**
     * 插入或更新数据
     * 说明:如果参数带id表示是更新，否则就是插入
     * @param section
     * @return 返回成功数
     */
    public Integer insertOrUpdate(Section section){
        if (section == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        // id不为空，表示更新操作
        if(section.getId() != null){
          return this.update(section);
        }
        Section newSection = sectionRepository.save(section);
        return newSection == null ? 0 : 1;
    }


    /**
     *  选择性更新
     * @param section
     * @return 返回成功条数
     */
    public Integer update(Section section){
        // 入参校验
        if(section == null || section.getId() == null){
            throw new IllegalArgumentException("更新的对象不能为null");
        }
        // 是否存在
        Optional<Section> optional = sectionRepository.findById(section.getId());
        if(!optional.isPresent()){
            throw new RuntimeException("找不到id为"+ section.getId() +"的Section");
        }
        Section dbSection = optional.get();
        //把不为null的属性拷贝到dbSection
        CopyUtil.notNullCopy(section, dbSection);
        //执行保存操作
        Section updateSection = sectionRepository.save(dbSection);
        return updateSection == null ? 0 : 1;
    }


    public Integer deleteById(Integer id){
        sectionRepository.deleteById(id);
        return  findById(id) == null ? 1 : 0;
    }

    /**
     * 批量删除
     * @param sectionIdList  id list
     * @return 删除条数
     */
    public Integer deleteAllByIds(List<Integer> sectionIdList){
        List<Section> delSectionList = sectionRepository.findAllById(sectionIdList);
        sectionRepository.deleteInBatch(delSectionList);
        return delSectionList.size();
    }


}
