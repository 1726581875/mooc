package cn.edu.lingnan.core.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
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
    
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Integer id;
	
	private Integer commentId;
	
	private Integer toUserId;
	
	private Integer userId;
	
	private String replyContent;
	
	private Integer replyStar;
	
	private Integer status;
	
	private Date createTime;

	private Date updateTime;
	
}
