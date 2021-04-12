package cn.edu.lingnan.mooc.message.model.vo;

import lombok.Data;

@Data
public class ReplyNoticeVO {
    /**
     * 对应课程ID
     */
    private Integer courseId;
    /**
     * 课程名
     */
    private String courseName;
    /**
     * 对应的评论表的ID
     */
    private Integer commentId;
    /**
     * 对应的回复表的ID
     */
    private Integer replyId;
    /**
     * 该消息的内容
     */
    private String replyContent;
    /**
     * 哪个用户发的
     */
    private Integer fromUserId;
    /**
     * 哪个用户发的
     */
    private String fromUserName;


}
