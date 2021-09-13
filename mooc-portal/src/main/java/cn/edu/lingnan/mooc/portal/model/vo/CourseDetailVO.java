package cn.edu.lingnan.mooc.portal.model.vo;

import cn.edu.lingnan.mooc.portal.authorize.model.entity.MoocUser;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xmz
 * @date: 2021/01/31
 */
@Data
public class CourseDetailVO {

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
     * 收藏人数
     */
    private Integer collectionNum;
    /**
     * 评论人数
     */
    private Integer commentNum;
    /**
     * 课程问答人数
     */
    private Integer questionNum;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 章节List
     */
    private List<ChapterVO> chapterList;
    /**
     * 该课程对应的教师
     */
    private MoocUser teacher;
    /**
     * 是否已经收藏
     */
    private Boolean collection;

}
