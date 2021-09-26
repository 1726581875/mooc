package cn.edu.lingnan.mooc.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author xiaomingzhang
 * @date 2021/09/14
 */
@Data
@Accessors(chain = true)
public class NoticeDTO implements Serializable {

    private static final long serialVersionUID = 6343950671330615279L;

    private Long id;

    private Long sendId;

    private Long acceptId;

    private Long courseId;

    private Long replyId;

    private Long commentId;

    private String content;
    /**
     * 消息所属用户类型，1管理员、2教师、3普通用户
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
