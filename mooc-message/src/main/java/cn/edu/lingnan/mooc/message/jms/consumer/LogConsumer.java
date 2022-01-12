package cn.edu.lingnan.mooc.message.jms.consumer;

import cn.edu.lingnan.mooc.common.model.LoginLog;
import cn.edu.lingnan.mooc.message.jms.LogMQConfig;
import cn.edu.lingnan.mooc.message.mapper.LoginLogMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author xiaomingzhang
 * @date 2022/1/12
 */
@Component
public class LogConsumer {

    @Resource
    private LoginLogMapper loginLogMapper;

    @RabbitListener(queues = LogMQConfig.LOGIN_LOG_QUEUE)
    public void loginLogConsumer(LoginLog loginLog) {
        loginLogMapper.insert(loginLog);
    }


}
