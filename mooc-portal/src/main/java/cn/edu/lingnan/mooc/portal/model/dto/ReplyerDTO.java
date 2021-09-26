package cn.edu.lingnan.mooc.portal.model.dto;

import lombok.Data;

import java.util.Date;

/**
* @author xmz
* 2020-04-01
* 封装回复者的信息
* 
*/
@Data
public class ReplyerDTO {

	private Long commentId;

	private Long parentId;

	private Long replyId;
	
	private Long userId;
	
	private String replyerName;
	
	private String replyerImage;
	
	private Long toUserId;
	
	private String toUserName;
	
	private String toUserImage;
	
	private String replyContent;
	
	private Integer replyStar;
	
	private Date createTime;
	
    private boolean isStar;
}
