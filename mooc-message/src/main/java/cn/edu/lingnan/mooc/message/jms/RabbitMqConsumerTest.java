package cn.edu.lingnan.mooc.message.jms;

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


    @RabbitListener(queues = RabbitConfig.HELLO_WORLD_QUEUE, concurrency = "3")
    public void helloWorldConsumer(String msg){
        log.info("接收到消息 ==>[{}]", msg);
    }

}
