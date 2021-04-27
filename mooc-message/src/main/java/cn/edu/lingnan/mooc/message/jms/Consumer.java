package cn.edu.lingnan.mooc.message.jms;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author xiaomingzhang
 * @date 2021/04/27
 * 消息消费者
 */
@Slf4j
@Component
public class Consumer {


    /**
     * 消息消费者，负责接收消息
     */
    @RabbitHandler
    @RabbitListener(queues = "${constant.mq.messageQueueName:mooc.mq.messageQueue}")
    public void messageConsumer(String message){
        log.info("messageConsumer accept message : {}",message);
    }



}
