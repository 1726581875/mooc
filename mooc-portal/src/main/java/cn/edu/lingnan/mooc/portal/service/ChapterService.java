package cn.edu.lingnan.mooc.portal.service;

import cn.edu.lingnan.mooc.portal.authorize.util.CopyUtil;
import cn.edu.lingnan.mooc.portal.dao.ChapterRepository;
import cn.edu.lingnan.mooc.portal.dao.SectionRepository;
import cn.edu.lingnan.mooc.portal.model.entity.Chapter;
import cn.edu.lingnan.mooc.portal.model.entity.Section;
import cn.edu.lingnan.mooc.portal.model.vo.ChapterVO;
import cn.edu.lingnan.mooc.portal.model.vo.SectionVO;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xmz
 * @date: 2021/01/31
 */
@Service
public class ChapterService {

    @Resource
    private ChapterRepository chapterRepository;

    @Resource
    private SectionRepository sectionRepository;
    /**
     * 根据课程Id获取课程章节信箱chapterVOList
     * 1、获取课程对应的大章List
     * 2、大章按照sort字段升序排序
     * 3、获取每个大章对应的小节List
     * 4、小节List按sort字段升序排序
     * 5、赋值构造返回的VO对象
     * @param courseId
     * @return
     */
    public List<ChapterVO> findAllChapterByCourseId(Long courseId) {
        //查询全部大章 List
        Example<Chapter> example = Example.of(new Chapter().setCourseId(courseId));
        List<Chapter> chapterList = chapterRepository.findAll(example);
        List<ChapterVO> chapterVOList = CopyUtil.copyList(chapterList, ChapterVO.class);
        if(!CollectionUtils.isEmpty(chapterList)) {
            //大章List按sort字段升序排序
            chapterVOList.sort(Comparator.comparingInt(ChapterVO::getSort));
            //获取大章对应的小节,并赋值给VO对象
            List<Long> chapterIdList = chapterList.stream().map(Chapter::getId).collect(Collectors.toList());
            Map<Integer, List<SectionVO>> sectionMap = findSectionMapByChapterIdList(chapterIdList);
            chapterVOList.forEach(chapterVO-> {
                List<SectionVO> sectionVOList = sectionMap.getOrDefault(chapterVO.getId(), new ArrayList<>());
                sectionVOList.sort(Comparator.comparingInt(SectionVO::getSort));
                chapterVO.setSectionList(sectionVOList);
            });
        }
        return  chapterVOList;
    }

    /**
     * 根据chapterIdList 大章idList查询对应小节List
     * @return
     */
    public Map<Integer,List<SectionVO>> findSectionMapByChapterIdList(List<Long> chapterIdList){
        List<Section> sectionList = sectionRepository.findAllByChapterIdIn(chapterIdList);
        List<SectionVO> sectionVOList = CopyUtil.copyList(sectionList, SectionVO.class);
        if(CollectionUtils.isEmpty(sectionVOList)){
            return new HashMap<>();
        }
        return sectionVOList.stream().collect(Collectors.groupingBy(SectionVO::getChapterId));
    }




}
