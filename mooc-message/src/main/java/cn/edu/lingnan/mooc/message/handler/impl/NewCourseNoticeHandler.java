package cn.edu.lingnan.mooc.message.handler.impl;

import cn.edu.lingnan.mooc.common.enums.UserTypeEnum;
import cn.edu.lingnan.mooc.common.model.NoticeDTO;
import cn.edu.lingnan.mooc.message.handler.BaseNoticeHandler;
import cn.edu.lingnan.mooc.message.mapper.SendNoticeMapper;
import cn.edu.lingnan.mooc.message.menus.NoticeTypeEnum;
import cn.edu.lingnan.mooc.message.model.entity.Notice;
import cn.edu.lingnan.mooc.message.service.NoticeService;
import cn.edu.lingnan.mooc.message.websock.MessageDTO;
import cn.edu.lingnan.mooc.message.websock.WebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2022/1/10
 */
@Slf4j
@Component("newCourseNoticeHandler")
public class NewCourseNoticeHandler implements BaseNoticeHandler {

    @Autowired
    private NoticeService noticeService;

    @Resource
    private SendNoticeMapper sendNoticeMapper;

    @Autowired
    private WebSocketHandler webSocket;

    @Override
    public void handle(NoticeDTO noticeDTO) {
        log.info("处理【新建课程通知】 start...");
        Notice newCourseNotice = getCreateCourseNotice(noticeDTO);
        int insert = noticeService.insert(newCourseNotice);
        if (insert == 0) {
            log.error("==== 插入新增课程消息发生失败 notice={}====", noticeDTO);
            return;
        }
        //创建一个页头停留消息,通过WebSocket推送给用户
        MessageDTO messageDTO = MessageDTO.createStay(noticeDTO.getContent());
        //获取有课程管理权限的管理员
        List<Long> courseManagerIdList = sendNoticeMapper.getCourseManagerIdList();
        //把超级管理员加上
        courseManagerIdList.add(0L);
        //向有权限的管理员发送推送消息
        webSocket.sendMessageToManager(messageDTO, courseManagerIdList);
        log.info("处理【新建课程通知】 end...");
    }


    private Notice getCreateCourseNotice(NoticeDTO noticeDTO){
        Notice notice = new Notice();
        notice.setCourseId(noticeDTO.getCourseId());
        notice.setCommentId(null);
        notice.setReplyId(null);
        notice.setAcceptId(null);
        notice.setUserType(UserTypeEnum.MANAGER);
        notice.setType(NoticeTypeEnum.CREATE_COURSE.getType());
        notice.setSendId(noticeDTO.getSendId());
        notice.setContent("【新增课程】 " + noticeDTO.getContent());
        return notice;
    }
}
