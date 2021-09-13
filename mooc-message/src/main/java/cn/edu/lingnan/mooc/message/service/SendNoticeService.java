package cn.edu.lingnan.mooc.message.service;

import cn.edu.lingnan.mooc.message.mapper.SendNoticeMapper;
import cn.edu.lingnan.mooc.message.model.entity.Notice;
import cn.edu.lingnan.mooc.message.websock.MessageDTO;
import cn.edu.lingnan.mooc.message.websock.MessageFactory;
import cn.edu.lingnan.mooc.message.websock.MyWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * @author xmz
 * @date: 2020/4/28
 */
@Slf4j
@Service
public class SendNoticeService {

    @Autowired
    private NoticeService noticeService;

    @Resource
    private SendNoticeMapper sendNoticeMapper;

    @Autowired
    private MyWebSocket webSocket;

    @Autowired
    private MessageFactory messageFactory;


    /**
     * @param notice
     * 根据消息类型，发送对应消息通知
     */
    public void saveAndSendNotice(Notice notice) {

        if (notice == null) {
            log.error("===== failed to save and send message. because notice object is null ======");
            return;
        }

        if (notice.getType() == null) {
            log.error("===== message type cannot be null. notice={} ======", notice);
            return;
        }
        //根据不同消息类型发送消息
        int flag = notice.getType();
        switch (flag) {
            case 1:
                //创建课程通知
                this.sendCreateCourseNotice(notice.getSendId(), notice.getCourseId(), notice.getContent());
                break;
            case 2:
                //课程提问消息
                this.sendQuestionNotice(notice.getSendId(),notice.getAcceptId(),notice.getCourseId(),notice.getCommentId(),notice.getContent());
                break;
            case 3:
                //评论回复通知
                this.sendReplyNotice(notice.getSendId(),notice.getAcceptId(),notice.getCourseId(),notice.getCommentId(),notice.getReplyId(),notice.getContent());
                break;
            case 6:
                //踢除消息消息通知
                List<Integer> userIdList = new ArrayList<>(1);
                userIdList.add(notice.getAcceptId());
                //判断类型
                boolean isManager = notice.getUserType() != null && notice.getUserType() == 1 ? true : false;
                this.sendOfflineNotice(userIdList,isManager);
                break;
            default:
                log.error("===== 未知类型通知. notice={} ======", notice);
                break;

        }


    }


    /**
     * 发送一条创建课程消息
     *
     * @param senderId
     * @param courseId
     * @param content
     * @return
     */
    public boolean sendCreateCourseNotice(Integer senderId, Integer courseId, String content) {

        Notice notice = messageFactory.getCreateCourseNotice(senderId, courseId, content);
        int insert = noticeService.insert(notice);
        if (insert == 0) {
            log.error("==== 插入新增课程消息发生失败 notice={}====", notice);
            return false;
        }
        //创建一个页头停留消息
        MessageDTO messageDTO = MessageDTO.createStay(content);
        //获取有课程管理权限的管理员
        List<Integer> courseManagerIdList = sendNoticeMapper.getCourseManagerIdList();
        //把超级管理员加上
        courseManagerIdList.add(0);
        //向有权限的管理员发送推送消息
        webSocket.sendMessageToManager(messageDTO, courseManagerIdList);
        return true;
    }

    /**
     * 发送一条课程提问消息
     *
     * @param senderId
     * @param courseId
     * @param content
     * @return
     */
    public boolean sendQuestionNotice(Integer senderId, Integer acceptId, Integer courseId, Integer commentId, String content) {

        Notice notice = messageFactory.getQuestionNotice(senderId, acceptId, courseId, commentId, content);
        int insert = noticeService.insert(notice);
        if (insert == 0) {
            log.error("==== 插入新提问消息发生失败 notice={}====", notice);
            return false;
        }
        //创建一个页头停留消息
        MessageDTO messageDTO = MessageDTO.createStay(content);
        //向教师推送消息
        webSocket.sendMessageToUser(messageDTO, acceptId);
        return true;
    }


    /**
     * 发送一条回复消息
     *
     * @param senderId
     * @param courseId
     * @param content
     * @return
     */
    public boolean sendReplyNotice(Integer senderId, Integer acceptId, Integer courseId, Integer commentId, Integer replyId, String content) {

        Notice notice = messageFactory.getReplyNotice(senderId, acceptId, courseId, commentId, replyId, content);
        int insert = noticeService.insert(notice);
        if (insert == 0) {
            log.error("==== 插入新回复消息发生失败 notice={}====", notice);
            return false;
        }
        //创建一个页头停留消息
        MessageDTO messageDTO = MessageDTO.createStay(content);
        //向教师推送消息
        webSocket.sendMessageToUser(messageDTO, acceptId);
        return true;
    }


    public void sendOfflineNotice(List<Integer> userIdList, Boolean isManager) {

        log.info("================= 发送踢除下线消息,用户数={},要发送的IdList={}", userIdList.size(), userIdList);
        //创建一个踢除下线消息
        MessageDTO messageDTO = MessageDTO.createOut(messageFactory.getOfflineNotice());
        if (isManager) {
            webSocket.sendMessageToManager(messageDTO, userIdList);
        } else {
            webSocket.sendMessageToUser(messageDTO, userIdList);
        }

    }
}
