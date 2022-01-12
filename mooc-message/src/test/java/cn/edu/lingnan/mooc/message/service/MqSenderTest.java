package cn.edu.lingnan.mooc.message.service;

import cn.edu.lingnan.mooc.common.model.NoticeDTO;
import cn.edu.lingnan.mooc.message.constant.RabbitMqConstant;
import cn.edu.lingnan.mooc.message.handler.NoticeHandlerEnum;
import cn.edu.lingnan.mooc.message.jms.RabbitConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class MqSenderTest {


    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private RabbitMqConstant rabbitMqConstant;

    @Test
    public void sendMessageTest() {
        for (int i = 0; i < 10; i++)
        amqpTemplate.convertAndSend(RabbitConfig.HELLO_WORLD_QUEUE,"test hello word");
        //System.out.println("22222222222222222");
    }

    @Test
    public void sendNewCourseMessageTest() {
        NoticeDTO noticeDTO = new NoticeDTO();
        noticeDTO.setCourseId(123456L);
        noticeDTO.setContent("发送课程消息测试");
        noticeDTO.setType(NoticeHandlerEnum.NEW_COURSE_notice.getType());
        amqpTemplate.convertAndSend(RabbitConfig.MESSAGE_QUEUE_NAME,noticeDTO);
    }


    @Test
    public void sendToDeadLetterQueue() {
        for (int i = 0; i < 10; i++) {
            log.info("发送消息" + i);
            amqpTemplate.convertAndSend(RabbitConfig.JAVABOY_EXCHANGE_NAME,
                    RabbitConfig.JAVABOY_ROUTING_KEY, "test hello word" + i);
        }
    }



}
