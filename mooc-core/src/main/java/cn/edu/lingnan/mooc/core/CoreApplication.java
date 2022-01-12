package cn.edu.lingnan.mooc.core;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.common.util.UserUtil;
import cn.edu.lingnan.mooc.core.client.AuthorizeClient;
import cn.edu.lingnan.mooc.core.client.DocmanClient;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author xmz
 * @Date: 2020/10/06
 */
//Feign
@EnableFeignClients
//Hystrix
@EnableCircuitBreaker
@SpringBootApplication
@ComponentScan({"cn.edu.lingnan.mooc.core"
        //,"cn.edu.lingnan.mooc.authorize"
        ,"cn.edu.lingnan.mooc.common"})
@EnableEurekaClient
// 开启异步
@EnableAsync
// 开启定时任务
@EnableScheduling
@RestController
@Slf4j
public class CoreApplication {

    @Resource
    private AuthorizeClient authorizeClient;
    @Autowired
    private DocmanClient docmanClient;

    @Autowired
    private AmqpTemplate amqpTemplate;


    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class,args);
    }

    @GetMapping("/hello")
    public RespResult sayHello(){
/*        String managerList = docmanClient.getManagerList();
        log.info(managerList);
        return RespResult.success(authorizeClient.getHello());*/

        log.info("userId=" + UserUtil.getUserId());
       // 新开线程，拷贝MDC上下文
        Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
        new Thread(() -> {
            MDC.setContextMap(copyOfContextMap);
            log.info("=== begin ===");
            log.info("thread-userId=" + UserUtil.getUserId());
            log.info("=== end ===");
        }).start();

        amqpTemplate.convertAndSend("mooc.mq.hello.world", "Hello 我来自core模块");

        return RespResult.success();
    }



}
