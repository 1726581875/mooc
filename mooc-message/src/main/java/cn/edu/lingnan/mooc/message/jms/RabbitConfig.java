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

    @Resource
    private RabbitMqConstant rabbitMqConstant;


    @Bean
    public Queue setGroupInfoQueue(){
        return new Queue(rabbitMqConstant.getMessageQueueName());

    }


}
