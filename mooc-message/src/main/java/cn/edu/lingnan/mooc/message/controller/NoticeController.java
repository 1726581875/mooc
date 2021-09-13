package cn.edu.lingnan.mooc.message.controller;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.message.menus.NoticeStatusEnum;
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
    public RespResult getUnReadMessageCount(@RequestParam(value = "status", defaultValue = "0") Integer status){
        return RespResult.success(noticeService.getUnReadNoticeNum(status));
    }



    /**
     * 获取通知List
     * @param status
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public RespResult getMessageList(@RequestParam Integer status,
                                     @RequestParam(value = "pageIndex",defaultValue = "1") Integer pageIndex,
                                     @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize){
       return RespResult.success(noticeService.getMessageList(status,pageIndex,pageSize));
    }


    /**
     * 阅读消息
     * @param noticeIdList
     * @return
     */
    @PostMapping("/read")
    public RespResult readNotice(@RequestBody List<Integer> noticeIdList) {
        noticeService.updateNoticeStatus(noticeIdList, NoticeStatusEnum.READ.getStatus());
        return RespResult.success("阅读消息成功");
    }

    /**
     * 逻辑删除消息
     * @param noticeIdList
     * @return
     */
    @PostMapping("/delete")
    public RespResult deleteNotice(@RequestBody List<Integer> noticeIdList) {
        noticeService.updateNoticeStatus(noticeIdList, NoticeStatusEnum.DELETED.getStatus());
        return RespResult.success("删除消息成功");
    }

    /**
     * 恢复已删除的消息
     * @param noticeIdList
     * @return
     */
    @PostMapping("/recover")
    public RespResult recoverNotice(@RequestBody List<Integer> noticeIdList) {
        noticeService.updateNoticeStatus(noticeIdList, NoticeStatusEnum.READ.getStatus());
        return RespResult.success("恢复消息成功");
    }

    /**
     *  批量更改消息状态
     * @param status
     * @return
     */
    @PostMapping("/updateAll")
    public RespResult updateAll(@RequestParam(value = "status",defaultValue = "0") Integer status) {
        noticeService.updateAllNoticeStatus(status);
        return RespResult.success("操作成功");
    }


    /**
     * 获取通知详情
     * @param id
     * @return
     */
    @GetMapping("/reply/detail")
    public RespResult getReplyMessageDetail(@RequestParam Integer id){
        return RespResult.success(noticeService.getReplyNoticeDetail(id));
    }

}
