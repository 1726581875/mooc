package cn.edu.lingnan.mooc.message.service;

import cn.edu.lingnan.mooc.common.enums.UserTypeEnum;
import cn.edu.lingnan.mooc.common.model.NoticeDTO;
import cn.edu.lingnan.mooc.message.handler.BaseNoticeHandler;
import cn.edu.lingnan.mooc.message.handler.NoticeHandlerFactory;
import cn.edu.lingnan.mooc.message.mapper.SendNoticeMapper;
import cn.edu.lingnan.mooc.message.model.entity.Notice;
import cn.edu.lingnan.mooc.message.websock.MessageDTO;
import cn.edu.lingnan.mooc.message.websock.MessageFactory;
import cn.edu.lingnan.mooc.message.websock.WebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
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
    private WebSocketHandler webSocket;

    @Autowired
    private MessageFactory messageFactory;

    @Autowired
    private NoticeHandlerFactory noticeHandlerFactory;


    /**
     * @param noticeDTO
     * 根据消息类型，发送对应消息通知
     */
    public void saveAndSendNotice(NoticeDTO noticeDTO) {

        if (noticeDTO == null) {
            log.error("===== failed to save and send message. because notice object is null ======");
            return;
        }

        if (noticeDTO.getType() == null) {
            log.error("===== message type cannot be null. notice={} ======", noticeDTO);
            return;
        }
        //根据不同消息类型发送消息
        int type = noticeDTO.getType();

        BaseNoticeHandler noticeHandler = noticeHandlerFactory.getNoticeHandler(type);

        try {
            noticeHandler.handle(noticeDTO);
        }catch (Exception e){
            log.error("处理消息发生异常,notice={}", noticeDTO, e);
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
    public boolean sendCreateCourseNotice(Long senderId, Long courseId, String content) {

        Notice notice = messageFactory.getCreateCourseNotice(senderId, courseId, content);
        int insert = noticeService.insert(notice);
        if (insert == 0) {
            log.error("==== 插入新增课程消息发生失败 notice={}====", notice);
            return false;
        }
        //创建一个页头停留消息
        MessageDTO messageDTO = MessageDTO.createStay(content);
        //获取有课程管理权限的管理员
        List<Long> courseManagerIdList = sendNoticeMapper.getCourseManagerIdList();
        //把超级管理员加上
        courseManagerIdList.add(0L);
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
    public boolean sendQuestionNotice(Long senderId, Long acceptId, Long courseId, Long commentId, String content) {

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
    public boolean sendReplyNotice(Long senderId, Long acceptId, Long courseId, Long commentId, Long replyId, String content) {

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


    public void sendOfflineNotice(List<Long> userIdList, Boolean isManager) {

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
