package cn.edu.lingnan.mooc.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;
import java.util.Date;

@Data
@Entity(name = "course")
public class Course{
    // 主键id   
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    // 课程名称   
    private String name;
    // 讲师id   
    private Integer teacherId;
    // 课程概述   
    private String summary;
    // 时长|单位秒   
    private Integer duration;
    // 封面图片   
    private String image;
    // 学习人数   
    private Integer learningNum;
    // 评论数   
    private Integer commentNum;
    // 状态|0草稿/1发布/2禁用/3已删除   
    private Integer status;
    // 创建时间   
    private Date createTime;
    // 修改时间   
    private Date updateTime;
    
}