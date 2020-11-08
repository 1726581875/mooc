package cn.edu.lingnan.mooc.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Id;
import java.util.Date;

@DynamicInsert
@DynamicUpdate
@Entity(name = "course")
public class Course{
    // 主键id   
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    // 课程名称   
    private String name;
    // 讲师id   
    private Integer teacherId;
    // 课程概述   
    private String summary;
    // 时长|单位秒   
    private Integer duration;
    // 封面图片   
    private String image;
    // 学习人数   
    private Integer learningNum;
    // 评论数   
    private Integer commentNum;
    // 状态|0草稿/1发布/2禁用/3已删除   
    private Integer status;
    // 创建时间   
    private Date createTime;
    // 修改时间   
    private Date updateTime;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getLearningNum() {
        return learningNum;
    }

    public void setLearningNum(Integer learningNum) {
        this.learningNum = learningNum;
    }

    public Integer getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(Integer commentNum) {
        this.commentNum = commentNum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", teacherId=").append(teacherId);
        sb.append(", summary=").append(summary);
        sb.append(", duration=").append(duration);
        sb.append(", image=").append(image);
        sb.append(", learningNum=").append(learningNum);
        sb.append(", commentNum=").append(commentNum);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }

}