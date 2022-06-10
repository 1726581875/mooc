package cn.edu.lingnan.mooc.message.jms.consumer;

import cn.edu.lingnan.mooc.common.model.LoginLog;
import cn.edu.lingnan.mooc.message.jms.LogMQConfig;
import cn.edu.lingnan.mooc.message.mapper.LoginLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author xiaomingzhang
 * @date 2022/1/12
 */
@Slf4j
@Component
public class LogConsumer {

    @Resource
    private LoginLogMapper loginLogMapper;

    @RabbitListener(queues = LogMQConfig.LOGIN_LOG_QUEUE)
    public void loginLogConsumer(LoginLog loginLog) {
        loginLogMapper.insert(loginLog);
    }


    @RabbitListener(queues = "xmz-canal-queue")
    public void canalConsumer(String str) {
        log.info("接收到binlog消息==>{}", str);
    }


}
