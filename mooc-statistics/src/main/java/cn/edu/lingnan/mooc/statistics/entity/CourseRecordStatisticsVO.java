package cn.edu.lingnan.mooc.statistics.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: Carlson
 * @Date: 2020/3/5 15:10
 * 统计概览-每天数量展示模型
 */
@Data
public class CourseRecordStatisticsVO {
    /**
     * 课程Id
     */
    private Integer courseId;
    /**
     * 教师名 + 账号
     */
    private String teacherName;
    /**
     * 课程名
     */
    private String courseName;
    /**
     * 收藏数
     */
    private Integer collectionNum;
    /**
     * 观看人数
     */
    private Integer viewNum;
}
