package cn.edu.lingnan.mooc.core.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Id;
import java.util.Date;

@DynamicInsert
@DynamicUpdate
@Entity(name = "chapter_section")
@Data
@ToString
public class Section{
    // id   
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    // 标题   
    private String title;
    // 课程id   
    private Integer courseId;
    // 章节id   
    private Integer chapterId;
    // 视频   
    private String video;
    // 时长|单位秒   
    private Integer duration;
    // 顺序   
    private Integer sort;
    // 创建时间   
    private Date createTime;
    // 修改时间   
    private Date updateTime;
    /**
     * 对应文件表id
     */
    private Integer fileId;

}