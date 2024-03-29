package cn.edu.lingnan.mooc.portal.client.hystrix;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.portal.client.NoticeServiceClient;
import org.springframework.stereotype.Component;

@Component
public class NoticeHystrix implements NoticeServiceClient {

    @Override
    public RespResult sendCreateCourseNotice(String token, Integer senderId, Integer courseId, String content) {
        return RespResult.fail("发送新增课程消息失败");
    }

    @Override
    public RespResult sendQuestionNotice(String token, Integer senderId, Integer acceptId, Integer courseId, Integer commentId, String content) {
        return RespResult.fail("发送提问消息失败");
    }

    @Override
    public RespResult sendReplyNotice(String token, Integer senderId, Integer acceptId, Integer courseId, Integer commentId, Integer replyId, String content) {
        return RespResult.fail("发送回复消息失败");
    }
}
