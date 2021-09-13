package cn.edu.lingnan.mooc.message.model.entity;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
@Accessors(chain = true)
public class Notice {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private Integer sendId;

    private Integer acceptId;

    private Integer courseId;

    private Integer replyId;

    private Integer commentId;

    private String content;
    /**
     * 消息所属用户类型，1管理员、2教师
     */
    private Integer userType;
    /**
     * 消息类型,1新增课程，2课程提问，3评论回复，4、系统通知, 5已回复, 6下线通知
     */
    private Integer type;
    /**
     * 通知状态，0未读，1已读，3已删除
     */
    private Integer status;

    private Date updateTime;

    private Date createTime;


}
