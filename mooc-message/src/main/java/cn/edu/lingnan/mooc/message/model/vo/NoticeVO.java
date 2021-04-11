package cn.edu.lingnan.mooc.message.model.vo;

import lombok.Data;

import java.util.Date;

@Data
public class NoticeVO {

    private Integer id;

    private Integer sendId;

    private Integer acceptId;

    private Integer courseId;

    private Integer replyId;

    private String content;
    /**
     * 消息所属用户类型，1管理员、2教师
     */
    private Integer type;
    /**
     * 通知状态，0未读，1已读，3已删除
     */
    private Integer status;

    private Date updateTime;

    private Date createTime;

}
