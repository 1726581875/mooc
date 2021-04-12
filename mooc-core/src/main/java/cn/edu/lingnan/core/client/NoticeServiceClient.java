package cn.edu.lingnan.core.client;
import cn.edu.lingnan.core.client.hystrix.NoticeHystrix;
import cn.edu.lingnan.mooc.common.model.RespResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "mooc-message",fallback = NoticeHystrix.class)
public interface NoticeServiceClient {

    /**
     * 发送一条课程审核信息
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


}
