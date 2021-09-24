package cn.edu.lingnan.mooc.message.controller;
import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.message.service.SendNoticeService;
import cn.edu.lingnan.mooc.message.websock.MessageFactory;
import cn.edu.lingnan.mooc.message.service.NoticeService;
import cn.edu.lingnan.mooc.message.websock.WebSocketHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/send")
public class SendNoticeController {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private SendNoticeService sendNoticeService;

    @Autowired
    private WebSocketHandler webSocket;

    @Autowired
    private MessageFactory messageFactory;


    public void sendSystemNotice(){

    }

    @PostMapping("/createCourseNotice")
    public RespResult sendCreateCourseNotice(@RequestParam("token") String token, Long senderId, Long courseId, String content){

        boolean success = sendNoticeService.sendCreateCourseNotice(senderId, courseId, content);

        return success ? RespResult.success("消息发送成功") : RespResult.fail("消息发送失败");
    }


    @PutMapping("/offlineNotice")
    public RespResult sendOfflineNotice(@RequestParam("token") String token,@RequestParam("userIdList") List<Long> userIdList,@RequestParam("isManager") Boolean isManager){
        sendNoticeService.sendOfflineNotice(userIdList,isManager);
        return  RespResult.success("消息发送成功");
    }


    @PutMapping("/questionNotice")
    public RespResult sendQuestionNotice(@RequestParam("token") String token,Long senderId,Long acceptId,Long courseId,Integer commentId, String content){
        sendNoticeService.sendQuestionNotice(senderId, acceptId, courseId, commentId,  content);
        return  RespResult.success("消息发送成功");
    }


    @PutMapping("/replyNotice")
    public RespResult sendReplyNotice(@RequestParam("token") String token,Long senderId,Long acceptId,Long courseId,Integer commentId, Integer replyId, String content){
        sendNoticeService.sendReplyNotice(senderId, acceptId, courseId, commentId,replyId,  content);
        return  RespResult.success("消息发送成功");
    }

}
