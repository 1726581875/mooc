package cn.edu.lingnan.mooc.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.util.Date;

@Data
public class CourseDTO{

    private Integer id;
    
    private String name;
    
    private Integer teacherId;
    
    private String summary;
    
    private Integer duration;
    
    private String image;
    
    private Integer learningNum;
    
    private Integer commentNum;
    
    private Integer status;
    
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;
    
}