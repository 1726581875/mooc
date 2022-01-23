package cn.edu.lingnan.mooc.message.jms;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xiaomingzhang
 * @date 2022/1/12
 */
@Configuration
public class LogMQConfig {

    public static final String LOGIN_LOG_QUEUE = "mooc.mq.loginLog";


    @Bean
    public Queue createLoginLog() {
        return new Queue(LOGIN_LOG_QUEUE);
    }

}
