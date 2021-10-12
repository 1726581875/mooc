package cn.edu.lingnan.mooc.portal.model.entity;

import cn.edu.lingnan.mooc.portal.model.param.CommentParam;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
@DynamicInsert
@DynamicUpdate
public class CourseComment {

    /**
     * 评论id
     */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    /**
     * 评论者id
     */
    private Long userId;
    /**
     * 对应课程id
     */
    private Long courseId;
    /**
     * 评论内容
     */
    private String commentContent;
    /**
     * 回复数
     */
    private Integer replyNum;
    /**
     * 点赞数
     */
    private Integer commentStar;
    /**
     * 状态是否已读,0未读,1已读,2已回复
     */
    private Integer status;
    /**
     * 0课程评论,1课程问答
     */
    private Integer type;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;

    public CourseComment build(CommentParam commentParam){
        this.courseId = commentParam.getCourseId();
        this.userId = commentParam.getUserId();
        this.commentContent = commentParam.getContent();
        this.type = commentParam.getType();
        return this;
    }

}
