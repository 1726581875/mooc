package cn.edu.lingnan.mooc.portal.model.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author xmz
 * @date: 2021/09/16
 */
@Data
@DynamicInsert
@DynamicUpdate
@Entity(name = "course")
public class Course {
    /**
     * 主键id
     */
    @Id
  	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    /**
     * 课程名称
     */
    private String name;
    /**
     * 讲师id
     */
    private Long teacherId;
    /**
     * 课程概述
     */
    private String summary;
    /**
     * 时长|单位秒
     */
    private Integer duration;
    /**
     * 封面图片
     */
    private String image;
    /**
     * 学习人数（观看视频人数）
     */
    private Long learningNum;
    /**
     * 收藏人数
     */
    private Long collectionNum;
    /**
     * 评论数
     */
    private Long commentNum;
    /**
     * 课程问答数
     */
    private Long questionNum;
    /**
     * 状态|0草稿/1发布/2禁用/3已删除
     */
    private Integer status;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;

}