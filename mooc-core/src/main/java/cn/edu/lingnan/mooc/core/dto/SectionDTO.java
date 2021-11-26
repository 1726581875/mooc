package cn.edu.lingnan.mooc.core.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
public class SectionDTO{

    private Integer id;
    
    private String title;
    
    private Integer courseId;
    
    private Integer chapterId;
    
    private String video;
    
    private Integer duration;
    
    private Integer sort;
    
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
    
}