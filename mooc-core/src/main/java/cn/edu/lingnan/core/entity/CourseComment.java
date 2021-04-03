package cn.edu.lingnan.core.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
public class CourseComment {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    private Integer courseId;

    private String commentContent;
    /**
     * 点赞数
     */
    private Integer commentStar;
    /**
     * 回复数
     */
    private Integer replyNum;

    private Integer status;

    private Date createTime;

    private Date updateTime;

}
