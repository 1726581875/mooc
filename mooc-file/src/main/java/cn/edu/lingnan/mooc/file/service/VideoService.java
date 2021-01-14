package cn.edu.lingnan.mooc.file.service;

import cn.edu.lingnan.mooc.file.entity.MoocFile;
import cn.edu.lingnan.mooc.file.entity.Section;
import cn.edu.lingnan.mooc.file.enums.FileStatusEnum;
import cn.edu.lingnan.mooc.file.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author xmz
 * @date: 2020/12/27
 */
@Service
public class VideoService {

    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private MoocFileService moocFileService;
    /**
     * 检查是否还有小节指向该文件
     * 1、如果有则不删除
     * 2、否则逻辑删除
     */
    public void checkSectionExists(Integer fileId){
        List<Section> sectionList = sectionRepository.findAll(Example.of(new Section().setFileId(fileId)));

        for (Section section:sectionList) {
            if(!StringUtils.isEmpty(section.getVideo())){
                return;
            }
        }
        if(fileId != 0) {
            MoocFile moocFile = new MoocFile();
            moocFile.setId(fileId);
            moocFile.setStatus(FileStatusEnum.DELETED.getStatus());
            moocFileService.update(moocFile);
        }
    }

}
