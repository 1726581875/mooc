package cn.edu.lingnan.mooc.message.jms;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
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


    @Bean
    public Queue xmzCanalQueue() {
        return new Queue("xmz-canal-queue");
    }


    @Bean
    DirectExchange xmzCanalExchange() {
        return new DirectExchange("xmz-canal-exchange", true, false);
    }


    /**
     * 绑定普通队列和与之对应的交换机
     * @return
     */
    @Bean
    Binding xmzCanalBinding() {
        return BindingBuilder.bind(xmzCanalQueue())
                .to(xmzCanalExchange())
                .with("xmz-canal-queue");
    }

}
