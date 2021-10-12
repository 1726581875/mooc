package cn.edu.lingnan.mooc.portal.model.vo;

import cn.edu.lingnan.mooc.portal.model.dto.ReplyerDTO;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
* @author xmz
* 2020-03-12
* 封装评论并携带该评论的回复
* 
*/
@Data
public class CommentAndReplyVO {

	private Long commentId;
	
	private Long userId;
	
	private String userName;
	
	private String userImage;
	
	private String commentContent;
	
	private Integer commentStar;
	
	private Date createTime;
	
	private List<ReplyerDTO> replyList;

    private boolean isStar;
	
}
