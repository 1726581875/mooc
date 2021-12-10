package cn.edu.lingnan.mooc.core.job;

import cn.edu.lingnan.mooc.common.constant.CommonConstant;
import cn.edu.lingnan.mooc.core.client.AuthorizeClient;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * @author xiaomingzhang
 * @date 2021/12/10
 */
@Slf4j
@Component
public class TestJob {

    @Autowired
    private AuthorizeClient authorizeClient;

    @Scheduled(cron = "0/10 * * * * ?")
    public void test() {
        MDC.put(CommonConstant.TRACE_ID, UUID.randomUUID().toString());
        try {
            log.info("开始执行任务");
            todo();
            log.info("结束执行任务");
        } catch (Exception e) {
            log.error("", e);
        } finally {
            MDC.remove(CommonConstant.TRACE_ID);
        }

    }

    private void todo() {
        log.info("做一些事情");
        String hello = authorizeClient.getHello();
        log.info("result: {}", hello);
    }

}
