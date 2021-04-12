package cn.edu.lingnan.core.client.hystrix;

import cn.edu.lingnan.core.client.NoticeServiceClient;
import cn.edu.lingnan.mooc.common.model.RespResult;
import org.springframework.stereotype.Component;

@Component
public class NoticeHystrix implements NoticeServiceClient {

    @Override
    public RespResult sendCreateCourseNotice(String token, Integer senderId, Integer courseId, String content) {
        return RespResult.fail("发送新增课程消息失败");
    }
}
