package cn.edu.lingnan.mooc.message.jms;
import cn.edu.lingnan.mooc.common.model.NoticeDTO;
import cn.edu.lingnan.mooc.message.model.entity.Notice;
import cn.edu.lingnan.mooc.message.service.SendNoticeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    /**
     * 消息消费者，负责接收处理消息
     */
    @RabbitListener(queues = RabbitConfig.MESSAGE_QUEUE_NAME, concurrency = "3")
    public void messageConsumer(NoticeDTO noticeDTO) {
        log.info("messageConsumer accept message : {}", noticeDTO);
        Notice notice = null;
        try {
            sendNoticeService.saveAndSendNotice(noticeDTO);
        } catch (Exception e) {
            log.error("==== 消费发生错误 ==== message={} ", noticeDTO, e);
        }
    }



}
