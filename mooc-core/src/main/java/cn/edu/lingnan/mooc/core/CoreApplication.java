package cn.edu.lingnan.mooc.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

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
@EnableAsync
public class CoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class,args);
    }

}
