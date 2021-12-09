package cn.edu.lingnan.mooc.core.model.vo;

import cn.edu.lingnan.mooc.core.model.entity.Tag;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @author xmz
 * @date: 2020/12/03
 */
@Data
@ToString
public class CourseVO {
    private Long id;
    // 封面图片
    private String image;
    // 课程名称
    private String name;
    // 讲师id
    private Long teacherId;
    // 讲师名字
    private String teacherName;
    /**
     * 课程概述
     */
    private String summary;
    // 时长|单位秒
    private Integer duration;
    // 学习人数
    private Integer learningNum;
    // 状态|0草稿/1发布/2禁用/3已删除
    private String status;

    /**
     * 所属标签list
     */
    private List<Tag> tagList;

    // 创建时间
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

}
