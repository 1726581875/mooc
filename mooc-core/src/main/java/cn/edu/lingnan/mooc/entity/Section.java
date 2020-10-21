package cn.edu.lingnan.mooc.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

@Data
@Entity(name = "chapter_section")
@DynamicInsert
@DynamicUpdate
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
    
}