package cn.edu.lingnan.mooc.core;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.core.client.AuthorizeClient;
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
public class CoreApplication {

    @Resource
    private AuthorizeClient authorizeClient;

    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class,args);
    }

    @GetMapping("/hello")
    public RespResult sayHello(){
        return RespResult.success(authorizeClient.getHello());
    }



}
