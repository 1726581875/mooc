package cn.edu.lingnan.mooc.message.jms;
import cn.edu.lingnan.mooc.message.constant.RabbitMqConstant;
import cn.edu.lingnan.mooc.message.model.entity.Notice;
import cn.edu.lingnan.mooc.message.service.SendNoticeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author xiaomingzhang
 * @date 2021/04/27
 * 消息消费者
 */
@Slf4j
@Component
public class MessageConsumer {

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SendNoticeService sendNoticeService;

    @Resource
    private RabbitMqConstant rabbitMqConstant;

    /**
     * 消息消费者，负责接收消息
     */
    @RabbitHandler
    @RabbitListener(queues = "${constant.mq.messageQueueName:mooc.mq.messageQueue}")
    public void messageConsumer(String message) {
        log.info("messageConsumer accept message : {}", message);
        Notice notice = null;
        try {
            notice = objectMapper.readValue(message, Notice.class);
            sendNoticeService.saveAndSendNotice(notice);
        } catch (JsonProcessingException e) {
            log.error("==== 转换消息发生错误 ==== message={} ", message, e);
        } catch (Exception e) {
            log.error("==== 消费发生错误 ==== message={} ", message, e);
        }
    }



}
