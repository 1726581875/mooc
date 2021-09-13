package cn.edu.lingnan.mooc.message.websock;

import cn.edu.lingnan.mooc.message.menus.NoticeTypeEnum;
import cn.edu.lingnan.mooc.message.menus.UserTypeEnum;
import cn.edu.lingnan.mooc.message.model.entity.Notice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class MessageFactory {


    /**
     * 创建一条系统通知
     * @return
     */
    public Notice getSystemNotice(Integer senderId,Integer acceptId,Integer type, String content){
        Notice notice = new Notice();
        notice.setCourseId(null);
        notice.setCommentId(null);
        notice.setReplyId(null);
        notice.setAcceptId(acceptId);
        notice.setUserType(type);
        notice.setType(NoticeTypeEnum.SYSTEM.getType());
        notice.setSendId(senderId);
        notice.setContent("【系统通知】 " + content);
        return notice;
    }

    /**
     * 创建一条新增课程通知
     * @param senderId
     * @param courseId
     * @param content
     * @return
     */
    public Notice getCreateCourseNotice(Integer senderId,Integer courseId, String content){
        Notice notice = new Notice();
        notice.setCourseId(courseId);
        notice.setCommentId(null);
        notice.setReplyId(null);
        notice.setAcceptId(null);
        notice.setUserType(UserTypeEnum.MANAGER.getType());
        notice.setType(NoticeTypeEnum.CREATE_COURSE.getType());
        notice.setSendId(senderId);
        notice.setContent("【新增课程】 " + content);
        return notice;
    }

    /**
     * 创建一条提问消息
     * @param senderId
     * @param acceptId
     * @param courseId
     * @param commentId
     * @param content
     * @return
     */
    public Notice getQuestionNotice(Integer senderId,Integer acceptId,Integer courseId,Integer commentId, String content){
        Notice notice = new Notice();
        notice.setCourseId(courseId);
        notice.setCommentId(commentId);
        notice.setReplyId(null);
        notice.setAcceptId(acceptId);
        notice.setUserType(UserTypeEnum.USER.getType());
        notice.setType(NoticeTypeEnum.QUESTION.getType());
        notice.setSendId(senderId);
        notice.setContent("【课程提问】 " + content);
        return notice;
    }

    public Notice getReplyNotice(Integer senderId,Integer acceptId,Integer courseId,Integer commentId,Integer replyId, String content){
        Notice notice = new Notice();
        notice.setCourseId(courseId);
        notice.setCommentId(commentId);
        notice.setReplyId(replyId);
        notice.setAcceptId(acceptId);
        notice.setUserType(UserTypeEnum.USER.getType());
        notice.setType(NoticeTypeEnum.REPLY.getType());
        notice.setSendId(senderId);
        notice.setContent("【回复消息】 " + content);
        return notice;
    }


    /**
     * 创建下线消息
     * @return
     */
    public String getOfflineNotice(){
        return "亲~~您已经被踢除下线..";
    }


}
