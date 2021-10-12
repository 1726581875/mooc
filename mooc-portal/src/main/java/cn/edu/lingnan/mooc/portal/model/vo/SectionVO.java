package cn.edu.lingnan.mooc.portal.model.vo;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author xmz
 * @date: 2020/12/19
 */
@Data
@ToString
public class SectionVO {
    private Long id;
    /**
     * 标题
     */
    private String title;
    /**
     *
     */
    private Long courseId;
    /**
     * 章节id
     */
    private Long chapterId;
    /**
     * 视频
     */
    private String video;
    /**
     * 对应文件表ID
     */
    private Integer fileId;
    /**
     * 时长|单位秒
     */
    private Integer duration;
    /**
     * 顺序
     */
    private Integer sort;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;


}
