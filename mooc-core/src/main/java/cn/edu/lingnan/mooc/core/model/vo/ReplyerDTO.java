package cn.edu.lingnan.mooc.core.model.vo;

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

	private Integer commentId;

	private Integer parentId;

	private Integer replyId;
	
	private Integer userId;
	
	private String replyerName;
	
	private String replyerImage;
	
	private Integer toUserId;
	
	private String toUserName;
	
	private String toUserImage;
	
	private String replyContent;
	
	private Integer replyStar;
	
	private Date createTime;
	
    private boolean isStar;
}
