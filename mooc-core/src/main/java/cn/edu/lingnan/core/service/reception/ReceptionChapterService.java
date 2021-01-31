package cn.edu.lingnan.core.service.reception;

import cn.edu.lingnan.core.entity.Chapter;
import cn.edu.lingnan.core.entity.Section;
import cn.edu.lingnan.core.repository.ChapterRepository;
import cn.edu.lingnan.core.service.SectionService;
import cn.edu.lingnan.core.service.TagService;
import cn.edu.lingnan.core.util.CopyUtil;
import cn.edu.lingnan.core.vo.ChapterVO;
import cn.edu.lingnan.core.vo.SectionVO;
import org.springframework.beans.factory.annotation.Autowired;
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
public class ReceptionChapterService {

    @Resource
    private ChapterRepository chapterRepository;
    @Autowired
    private SectionService sectionService;

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
    public List<ChapterVO> findAllChapterByCourseId(Integer courseId){
        //查询全部大章 List
        Example<Chapter> example = Example.of(new Chapter().setCourseId(courseId));
        List<Chapter> chapterList = chapterRepository.findAll(example);
        List<ChapterVO> chapterVOList = CopyUtil.copyList(chapterList, ChapterVO.class);
        if(!CollectionUtils.isEmpty(chapterList)) {
            //大章List按sort字段升序排序
            chapterVOList.sort(Comparator.comparingInt(ChapterVO::getSort));
            //获取大章对应的小节,并赋值给VO对象
            List<Integer> chapterIdList = chapterList.stream().map(Chapter::getId).collect(Collectors.toList());
            Map<Integer, List<SectionVO>> sectionMap = sectionService.findSectionMapByChapterIdList(chapterIdList);
            chapterVOList.forEach(chapterVO-> {
                List<SectionVO> sectionVOList = sectionMap.getOrDefault(chapterVO.getId(), new ArrayList<>());
                sectionVOList.sort(Comparator.comparingInt(SectionVO::getSort));
                chapterVO.setSectionList(sectionVOList);
            });
        }
        return  chapterVOList;
    }




}
