package cn.edu.lingnan.mooc.message.controller;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.message.websock.MyWebSocket;
import cn.edu.lingnan.mooc.message.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    /**
     * 统计没有未读消息数
     * @return
     */
    @GetMapping("/count")
    public RespResult getUnReadMessageCount(){
        return RespResult.success(noticeService.getUnReadNoticeNum());
    }



}
