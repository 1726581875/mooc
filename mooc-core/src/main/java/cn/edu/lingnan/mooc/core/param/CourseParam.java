package cn.edu.lingnan.mooc.core.param;

import cn.edu.lingnan.mooc.core.entity.Tag;
import lombok.Data;

import java.util.List;

/**
 * @author xmz
 * @date: 2021/01/17
 */
@Data
public class CourseParam {

    private Integer id;
    // 封面图片
    private String image;
    // 课程名称
    private String name;
    // 讲师id
    private Integer teacherId;
    /**
     *  课程概述
     */
    private String summary;
    // 学习人数
    private Integer learningNum;
    // 状态|0草稿/1发布/2禁用/3已删除
    private String status;

    /**
     * 所属标签list
     */
    private List<Tag> tagList;

}
