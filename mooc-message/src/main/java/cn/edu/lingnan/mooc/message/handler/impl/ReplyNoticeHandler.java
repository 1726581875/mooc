package cn.edu.lingnan.mooc.message.handler.impl;

import cn.edu.lingnan.mooc.common.enums.UserTypeEnum;
import cn.edu.lingnan.mooc.common.model.NoticeDTO;
import cn.edu.lingnan.mooc.message.handler.BaseNoticeHandler;
import cn.edu.lingnan.mooc.message.handler.NoticeHandlerEnum;
import cn.edu.lingnan.mooc.message.menus.NoticeTypeEnum;
import cn.edu.lingnan.mooc.message.model.entity.Notice;
import cn.edu.lingnan.mooc.message.service.NoticeService;
import cn.edu.lingnan.mooc.message.websock.MessageDTO;
import cn.edu.lingnan.mooc.message.websock.WebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author xiaomingzhang
 * @date 2022/1/10
 */
@Slf4j
@Component(NoticeHandlerEnum.REPLY_NOTICE_NAME)
public class ReplyNoticeHandler implements BaseNoticeHandler {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private WebSocketHandler webSocket;

    @Override
    public void handle(NoticeDTO noticeDTO) {
        log.info("处理【提问回复通知】 start...");
        Notice replyNotice = getReplyNotice(noticeDTO);
        int insert = noticeService.insert(replyNotice);
        if (insert == 0) {
            log.error("==== 插入新回复消息发生失败 notice={}====", replyNotice);
            return;
        }
        //创建一个页头停留消息
        MessageDTO messageDTO = MessageDTO.createStay(replyNotice.getContent());
        //向教师推送消息
        webSocket.sendMessageToUser(messageDTO, replyNotice.getAcceptId());
        log.info("处理【提问回复通知】 end...");
    }


    private Notice getReplyNotice(NoticeDTO noticeDTO) {
        Notice notice = new Notice();
        notice.setCourseId(noticeDTO.getCourseId());
        notice.setCommentId(noticeDTO.getCommentId());
        notice.setReplyId(noticeDTO.getReplyId());
        notice.setAcceptId(noticeDTO.getAcceptId());
        notice.setUserType(UserTypeEnum.USER);
        notice.setType(NoticeTypeEnum.REPLY.getType());
        notice.setSendId(noticeDTO.getSendId());
        notice.setContent("【回复消息】 " + noticeDTO.getContent());
        return notice;
    }
}
