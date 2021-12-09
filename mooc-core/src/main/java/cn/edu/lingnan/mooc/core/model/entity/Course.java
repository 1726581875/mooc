package cn.edu.lingnan.mooc.core.model.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Id;
import java.util.Date;

@DynamicInsert
@DynamicUpdate
@Entity(name = "course")
@Data
public class Course{
    // 主键id   
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    // 课程名称   
    private String name;
    // 讲师id   
    private Long teacherId;
    // 课程概述   
    private String summary;
    // 时长|单位秒   
    private Integer duration;
    // 封面图片   
    private String image;
    // 学习人数（观看人数）
    private Integer learningNum;
    // 评论数   
    private Integer commentNum;
    /**
     * 课程问答数
     */
    private Integer questionNum;
    /**
     * 收藏人数
     */
    private Integer collectionNum;

    // 状态|0未审核/1已审核/2禁用/3已删除/4审核不通过
    private Integer status;
    // 创建时间   
    private Date createTime;
    // 修改时间   
    private Date updateTime;

}