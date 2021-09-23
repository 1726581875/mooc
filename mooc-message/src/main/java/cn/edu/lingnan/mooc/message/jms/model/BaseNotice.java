package cn.edu.lingnan.mooc.message.jms.model;

import lombok.Data;

/**
 * @author xiaomingzhang
 * @date 2021/9/22
 */
@Data
public class BaseNotice {

    /**
     * 消息所属用户类型，1管理员、2教师、3普通用户
     */
    private Integer userType;
    /**
     * 消息类型,1新增课程，2课程提问，3评论回复，4、系统通知, 5已回复, 6下线通知
     */
    private Integer type;
}
