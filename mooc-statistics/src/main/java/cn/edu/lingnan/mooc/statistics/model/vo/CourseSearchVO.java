package cn.edu.lingnan.mooc.statistics.model.vo;

import lombok.Data;

import java.util.Date;

@Data
public class CourseSearchVO {

    private Integer courseId;
    /**
     * 课程名
     */
    private String courseName;
    /**
     * 课程简介
     */
    private String summary;

    private String courseImage;

    private Integer teacherId;

    private String teacherName;

    private Date createTime;

}
