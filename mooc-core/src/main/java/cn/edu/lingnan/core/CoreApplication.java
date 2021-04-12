package cn.edu.lingnan.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author xmz
 * @Date: 2020/10/06
 */
//Feign
@EnableFeignClients
//Hystrix
@EnableCircuitBreaker
@SpringBootApplication
@EnableEurekaClient
public class CoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class,args);
    }

}
