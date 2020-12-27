package cn.edu.lingnan.core.vo;

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
    private Integer id;
    // 标题
    private String title;
    // 课程id
    private Integer courseId;
    // 章节id
    private Integer chapterId;
    // 视频
    private String video;
    // 对应文件表ID
    private Integer fileId;
    // 时长|单位秒
    private Integer duration;
    // 顺序
    private Integer sort;
    // 创建时间
    private Date createTime;
    // 修改时间
    private Date updateTime;


}
