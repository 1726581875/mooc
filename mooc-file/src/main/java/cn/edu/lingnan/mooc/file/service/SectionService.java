package cn.edu.lingnan.mooc.file.service;

import cn.edu.lingnan.mooc.file.entity.Section;
import cn.edu.lingnan.mooc.file.repository.SectionRepository;
import cn.edu.lingnan.mooc.file.util.CopyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author xmz
 * @date: 2020/12/27
 */
@Service
public class SectionService {

    @Autowired
    private SectionRepository sectionRepository;


    public void setSectionVideoUrl(Integer sectionId,String url){
        Section section = new Section();
        section.setId(sectionId);
        section.setVideo(url);
        // 设置时间为0
        section.setDuration(0);
        update(section);
    }


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


}
