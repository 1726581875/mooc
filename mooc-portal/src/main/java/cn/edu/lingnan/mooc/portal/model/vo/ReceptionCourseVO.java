package cn.edu.lingnan.mooc.portal.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author xiaomingzhang
 * @data 2021/01/29
 */
@Data
@ToString
public class ReceptionCourseVO {

    /**
     * id
     */
    private Integer id;
    /**
     * 封面图片
     */
    private String image;
    /**
     * 课程名称
     */

    private String name;
    /**
     * 讲师id
     */
    private Integer teacherId;
    /**
     * 讲师名字
     */
    private String teacherName;
    /**
     * 课程概述
     */
    private String summary;
    /**
     * 时长|单位秒
     */
    private Integer duration;
    /**
     * 学习人数
     */
    private Integer learningNum;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;


}
