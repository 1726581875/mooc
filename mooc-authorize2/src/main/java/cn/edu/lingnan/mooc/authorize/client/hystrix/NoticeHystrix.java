package cn.edu.lingnan.mooc.authorize.client.hystrix;

import cn.edu.lingnan.mooc.authorize.client.NoticeServiceClient;
import cn.edu.lingnan.mooc.common.model.RespResult;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NoticeHystrix implements NoticeServiceClient {

    @Override
    public RespResult sendCreateCourseNotice(String token, Integer senderId, Integer courseId, String content) {
        return RespResult.fail("发送新增课程消息失败");
    }

    @Override
    public RespResult sendOfflineNotice(String token, List<Integer> userIdList, Boolean isManager) {
        return RespResult.fail("踢除用户下线失败");
    }
}
