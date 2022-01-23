package cn.edu.lingnan.mooc.message.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaomingzhang
 * @date 2022/1/10
 * 消息处理工厂类
 */
@Slf4j
@Component
public class NoticeHandlerFactory {

    @Autowired
    private Map<String, BaseNoticeHandler> noticeHandlerMap = new ConcurrentHashMap<>(16);

    public BaseNoticeHandler getNoticeHandler(Integer type) {

        String beanName = NoticeHandlerEnum.getBeanNameByType(type);

        if (beanName == null) {
            log.warn("无法找到 type={} 的消息处理类beanName",type);
            return null;
        }

        BaseNoticeHandler noticeHandler = noticeHandlerMap.get(beanName);
        if (noticeHandler == null) {
            log.warn("无法找到 type={},beanName={} 的消息处理类beanName={}", type, beanName);
        }

        return noticeHandler;

    }


}
