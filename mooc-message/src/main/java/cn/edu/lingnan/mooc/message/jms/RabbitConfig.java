package cn.edu.lingnan.mooc.message.jms;

import cn.edu.lingnan.mooc.message.constant.RabbitMqConstant;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author xiaomingzhang
 * @date 2021/04/27
 * mq配置类
 */
@Configuration
public class RabbitConfig {

    /**
     * 消息接收队列
     */
    public static final String MESSAGE_QUEUE_NAME = "mooc.mq.messageQueue";

    public static final String HELLO_WORLD_QUEUE = "mooc.mq.hello.world";



    @Bean
    public Queue createMessageQueue(){
        return new Queue(MESSAGE_QUEUE_NAME);
    }


    @Bean
    public Queue createHelloWorldQueue(){
        return new Queue(HELLO_WORLD_QUEUE);
    }


}
