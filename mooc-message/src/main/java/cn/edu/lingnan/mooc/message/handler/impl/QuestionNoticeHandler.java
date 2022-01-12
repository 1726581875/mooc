package cn.edu.lingnan.mooc.message.handler.impl;

import cn.edu.lingnan.mooc.common.enums.UserTypeEnum;
import cn.edu.lingnan.mooc.common.model.NoticeDTO;
import cn.edu.lingnan.mooc.message.handler.BaseNoticeHandler;
import cn.edu.lingnan.mooc.message.mapper.NoticeMapper;
import cn.edu.lingnan.mooc.message.menus.NoticeTypeEnum;
import cn.edu.lingnan.mooc.message.model.entity.Notice;
import cn.edu.lingnan.mooc.message.websock.MessageDTO;
import cn.edu.lingnan.mooc.message.websock.WebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 课程提问消息处理
 * @author xiaomingzhang
 * @date 2022/1/10
 */
@Slf4j
@Component("questionNoticeHandler")
public class QuestionNoticeHandler implements BaseNoticeHandler {

    @Resource
    private NoticeMapper noticeMapper;

    @Autowired
    private WebSocketHandler webSocket;

    @Override
    public void handle(NoticeDTO noticeDTO) {
        log.info("处理【课程提问通知】 start...");
        Notice questionNotice = getQuestionNotice(noticeDTO);
        int insert = noticeMapper.insert(questionNotice);
        if (insert == 0) {
            log.error("==== 插入新提问消息发生失败 notice={}====", noticeDTO);
            return;
        }
        //创建一个页头停留消息
        MessageDTO messageDTO = MessageDTO.createStay(questionNotice.getContent());
        //向教师推送消息
        webSocket.sendMessageToUser(messageDTO, questionNotice.getAcceptId());
        log.info("处理【课程提问通知】 end...");
    }


    /**
     * 创建一条提问消息
     * @param noticeDTO
     * @return
     */
    private Notice getQuestionNotice(NoticeDTO noticeDTO){
        Notice notice = new Notice();
        notice.setCourseId(noticeDTO.getCourseId());
        notice.setCommentId(noticeDTO.getCommentId());
        notice.setReplyId(null);
        notice.setAcceptId(noticeDTO.getAcceptId());
        notice.setUserType(UserTypeEnum.USER);
        notice.setType(NoticeTypeEnum.QUESTION.getType());
        notice.setSendId(noticeDTO.getSendId());
        notice.setContent("【课程提问】 " + noticeDTO.getContent());
        return notice;
    }
}
