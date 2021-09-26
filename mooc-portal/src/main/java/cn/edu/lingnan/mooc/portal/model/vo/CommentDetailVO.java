package cn.edu.lingnan.mooc.portal.model.vo;

import cn.edu.lingnan.mooc.portal.model.dto.ReplyerDTO;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xmz
 * @date: 2021/04/05
 */
@Data
public class CommentDetailVO {

    private Long commentId;

    private Long userId;

    private String userName;

    private String userImage;
    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 点赞数
     */
    private Integer starNum;
    /**
     * 评论时间
     */
    private Date createTime;

    /**
     * 回复数
     */
    private Integer replyNum;
    /**
     * 是否已经点赞
     */
    private boolean isStar;
    /**
     * 课程id
     */
    private Long courseId;
    /**
     * 课程名
     */
    private String courseName;
    /**
     * 回复List
     */
    private List<ReplyerDTO> replyList;

}
