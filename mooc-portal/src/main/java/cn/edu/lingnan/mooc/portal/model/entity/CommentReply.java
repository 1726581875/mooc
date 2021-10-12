package cn.edu.lingnan.mooc.portal.model.entity;

import cn.edu.lingnan.mooc.portal.model.param.ReplyParam;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * 评论回复实体类
 *
 */
@Entity
@Data
@DynamicInsert
@DynamicUpdate
public class CommentReply {

	/**
	 * 回复表主键
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	/**
	 * 对应的评论id
	 */
	private Long commentId;
	/**
	 * 对应父回复id | 0表示回复对象是comment表下的回复
	 */
	private Long parentId;
	/**
	 * 回复者id
	 */
	private Long userId;
	/**
	 * 回复对象id
	 */
	private Long toUserId;
	/**
	 * 评论内容
	 */
	private String replyContent;
	/**
	 * 回复点赞数
	 */
	private Integer replyStar;
	/**
	 * 状态是否已读,0未读,1已读,2已回复
	 */
	private Integer status;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 修改时间
	 */
	private Date updateTime;


	public CommentReply build(ReplyParam replyParam){
		this.commentId = replyParam.getCommentId();
		this.userId = replyParam.getUserId();
		this.toUserId = replyParam.getToUserId();
		this.replyContent = replyParam.getContent();
		if(replyParam.getReplyId() != null) {
			this.parentId = replyParam.getReplyId();
		}
		return this;
	}
	
}
