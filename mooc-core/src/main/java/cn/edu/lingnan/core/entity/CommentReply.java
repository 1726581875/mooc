package cn.edu.lingnan.core.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * 评论回复实体类
 *
 */
@Entity
@Data
public class CommentReply {
    
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer replyId;
	
	private Integer commentId;
	
	private Integer toUserId;
	
	private Integer userId;
	
	private String replyContent;
	
	private Integer replyStar;
	
	private Integer status;
	
	private Date createTime;

	private Date updateTime;
	
}
