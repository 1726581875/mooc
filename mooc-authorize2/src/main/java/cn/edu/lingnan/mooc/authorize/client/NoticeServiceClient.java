package cn.edu.lingnan.mooc.authorize.client;
import cn.edu.lingnan.mooc.authorize.client.hystrix.NoticeHystrix;
import cn.edu.lingnan.mooc.common.model.RespResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "mooc-message",fallback = NoticeHystrix.class)
public interface NoticeServiceClient {

    /**
     * 发送一条课程审核信息
     * @param token
     * @param senderId
     * @param courseId
     * @param content
     * @return
     */
    @PostMapping(value = "/send/createCourseNotice")
    RespResult sendCreateCourseNotice(@RequestParam("token") String token,
                                      @RequestParam("senderId") Integer senderId,
                                      @RequestParam("courseId") Integer courseId,
                                      @RequestParam("content") String content);

    /**
     * 发送一条“踢除下线消息”
     * @param token
     * @param userIdList
     * @param isManager
     * @return
     */
    @PutMapping(value = "/send/offlineNotice")
    RespResult sendOfflineNotice(@RequestParam("token") String token, @RequestParam("userIdList")List<Integer> userIdList,@RequestParam("isManager") Boolean isManager);


}
