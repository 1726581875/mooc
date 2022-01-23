package cn.edu.lingnan.mooc.message.handler.impl;

import cn.edu.lingnan.mooc.common.enums.UserTypeEnum;
import cn.edu.lingnan.mooc.common.model.NoticeDTO;
import cn.edu.lingnan.mooc.message.handler.BaseNoticeHandler;
import cn.edu.lingnan.mooc.message.handler.NoticeHandlerEnum;
import cn.edu.lingnan.mooc.message.websock.MessageDTO;
import cn.edu.lingnan.mooc.message.websock.WebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 被踢除下线通知
 * @author xiaomingzhang
 * @date 2022/1/10
 */
@Slf4j
@Component(NoticeHandlerEnum.OFFLINE_NOTICE_NAME)
public class OfflineNoticeHandler implements BaseNoticeHandler {

    @Autowired
    private WebSocketHandler webSocket;

    @Override
    public void handle(NoticeDTO noticeDTO) {
        log.info("处理【踢除下线通知】 start...");
        log.info("================= 发送踢除下线消息,userId={},要发送的IdList={}", noticeDTO.getAcceptId());
        //创建一个踢除下线消息
        MessageDTO messageDTO = MessageDTO.createOut(getOfflineNotice());
        if (UserTypeEnum.MANAGER.equals(noticeDTO.getUserType())) {
            webSocket.sendMessageToManager(messageDTO, noticeDTO.getAcceptId());
        } else {
            webSocket.sendMessageToUser(messageDTO, noticeDTO.getAcceptId());
        }
        log.info("处理【踢除下线通知】 start...");
    }


    /**
     * 创建下线消息
     * @return
     */
    private String getOfflineNotice(){
        return "亲~~您已经被踢除下线..";
    }

}
