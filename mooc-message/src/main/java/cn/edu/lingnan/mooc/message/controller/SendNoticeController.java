package cn.edu.lingnan.mooc.message.controller;
import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.message.service.SendNoticeService;
import cn.edu.lingnan.mooc.message.websock.MessageFactory;
import cn.edu.lingnan.mooc.message.service.NoticeService;
import cn.edu.lingnan.mooc.message.websock.MyWebSocket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/send")
public class SendNoticeController {

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private SendNoticeService sendNoticeService;

    @Autowired
    private MyWebSocket myWebSocket;

    @Autowired
    private MessageFactory messageFactory;


    public void sendSystemNotice(){

    }

    @PostMapping("/createCourseNotice")
    public RespResult sendCreateCourseNotice(Integer senderId, Integer courseId, String content){

        boolean success = sendNoticeService.sendCreateCourseNotice(senderId, courseId, content);

        return success ? RespResult.success("消息发送成功") : RespResult.fail("消息发送失败");
    }


}
