package cn.edu.lingnan.mooc.portal.client;
import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.portal.client.hystrix.NoticeHystrix;
import org.springframework.cloud.openfeign.FeignClient;
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

    /**
     * 发送一条提问消息
     * @param token
     * @param senderId
     * @param acceptId
     * @param courseId
     * @param commentId
     * @param content
     * @return
     */
    @PutMapping("/send/questionNotice")
     RespResult sendQuestionNotice(@RequestParam("token") String token,
                                   @RequestParam("senderId") Integer senderId,
                                   @RequestParam("acceptId") Integer acceptId,
                                   @RequestParam("courseId") Integer courseId,
                                   @RequestParam("commentId") Integer commentId,
                                   @RequestParam("content") String content);

    @PutMapping("/send/replyNotice")
    RespResult sendReplyNotice(@RequestParam("token") String token,
                                  @RequestParam("senderId") Integer senderId,
                                  @RequestParam("acceptId") Integer acceptId,
                                  @RequestParam("courseId") Integer courseId,
                                  @RequestParam("commentId") Integer commentId,
                                  @RequestParam("replyId") Integer replyId,
                                  @RequestParam("content") String content);

}
