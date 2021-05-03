package cn.edu.lingnan.mooc.message.service;

import cn.edu.lingnan.mooc.message.constant.RabbitMqConstant;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class MqSenderTest {


    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private RabbitMqConstant rabbitMqConstant;

    @Test
    public void sendMessageTest(){
        amqpTemplate.convertAndSend(rabbitMqConstant.messageQueueName,"test");
        //System.out.println("22222222222222222");
    }


}
