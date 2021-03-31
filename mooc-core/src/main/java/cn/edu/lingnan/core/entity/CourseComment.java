package cn.edu.lingnan.core.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class CourseComment {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private Integer userId;

    private Integer courseId;

    private String commentContent;

    private Integer commentStar;

    private Integer status;

    private Date createTime;

    private Date updateTime;

}
