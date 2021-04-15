package cn.edu.lingnan.mooc.statistics.entity.vo;

import lombok.Data;

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

}
