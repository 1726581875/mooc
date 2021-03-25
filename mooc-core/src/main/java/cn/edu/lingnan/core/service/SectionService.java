package cn.edu.lingnan.core.service;

import cn.edu.lingnan.core.entity.Chapter;
import cn.edu.lingnan.core.entity.MoocFile;
import cn.edu.lingnan.core.vo.SectionVO;
import cn.edu.lingnan.mooc.common.model.PageVO;
import cn.edu.lingnan.core.entity.Section;
import cn.edu.lingnan.core.repository.SectionRepository;
import cn.edu.lingnan.core.util.CopyUtil;
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
 * @date: 2020/10/11
 */
@Slf4j
@Service
public class SectionService {

    @Resource
    private SectionRepository sectionRepository;
    @Autowired
    private MoocFileService moocFileService;

    /**
     * 根据chapterIdList 大章idList查询对应小节List
     * @return
     */
    public Map<Integer,List<SectionVO>> findSectionMapByChapterIdList(List<Integer> chapterIdList){
        List<Section> sectionList = sectionRepository.findAllByChapterIdIn(chapterIdList);
        List<SectionVO> sectionVOList = CopyUtil.copyList(sectionList, SectionVO.class);
        if(CollectionUtils.isEmpty(sectionVOList)){
            return new HashMap<>();
        }
        return sectionVOList.stream().collect(Collectors.groupingBy(SectionVO::getChapterId));
    }


    /**
     * 交换排序位置
     * @param id1
     * @param sort1
     * @param id2
     * @param sort2
     */
    @Transactional
    public void sectionSortSwap(Integer id1,Integer sort1, Integer id2, Integer sort2){
        Section section1 = new Section();
        section1.setId(id1);
        section1.setSort(sort2);
        Section section2 = new Section();
        section2.setId(id2);
        section2.setSort(sort1);
        // 保存交换信息
        this.update(section1);
        this.update(section2);
    }

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
    public List<SectionVO> findAllByCondition(Section matchObject) {
        List<Section> sectionList = sectionRepository.findAll(Example.of(matchObject));
        //按照sort字段升序排序
        sectionList.sort(Comparator.comparingInt(Section::getSort));
        return CopyUtil.copyList(sectionList, SectionVO.class);
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


    /**
     * 删除视频小节
     * 1、更改视频文件状态为已删除
     * 2、物理删除小节信息
     * @param id
     * @return
     */
    @Transactional
    public Integer deleteById(Integer id){
        Optional<Section> option = sectionRepository.findById(id);
        if(option.isPresent()) {
            // 更新视频文件表状态为已删除（逻辑删除）
            Section section = option.get();
            MoocFile file = new MoocFile();
            file.setId(section.getFileId());
            file.setStatus(2);
            moocFileService.update(file);
            // 删除小节(物理删除)
            sectionRepository.deleteById(id);
        }
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
