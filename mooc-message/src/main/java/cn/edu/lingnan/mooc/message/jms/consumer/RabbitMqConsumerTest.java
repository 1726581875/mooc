package cn.edu.lingnan.mooc.message.jms.consumer;

import cn.edu.lingnan.mooc.message.jms.MessageMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author xiaomingzhang
 * @date 2022/1/10
 */
@Slf4j
@Component
public class RabbitMqConsumerTest {


    @RabbitListener(queues = MessageMQConfig.HELLO_WORLD_QUEUE, concurrency = "3")
    public void helloWorldConsumer(String msg){
        log.info("接收到消息 ==>[{}]", msg);
    }

}
