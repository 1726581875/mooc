package cn.edu.lingnan.mooc.statistics.model.vo;

import lombok.Data;

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
    private Long courseId;
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
    private Long collectionNum;
    /**
     * 观看人数
     */
    private Long viewNum;
}
