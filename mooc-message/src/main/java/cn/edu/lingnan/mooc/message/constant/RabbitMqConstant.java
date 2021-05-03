package cn.edu.lingnan.mooc.message.constant;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author xiaomingzhang
 * @date 2021/04/27
 */
@Getter
@Component
public class RabbitMqConstant {

    /**
     * 消息通知队列
     */
    @Value("${constant.mq.messageQueueName:mooc.mq.messageQueue}")
    public String messageQueueName;

}
