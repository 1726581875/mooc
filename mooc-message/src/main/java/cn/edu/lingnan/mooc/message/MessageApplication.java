package cn.edu.lingnan.mooc.message;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author xmz
 * @date: 2020/10/21
 */
//Feign
@EnableFeignClients
//Hystrix
@EnableCircuitBreaker
//Eureka
@EnableEurekaClient
@SpringBootApplication
@ComponentScan({"cn.edu.lingnan.mooc.message"
        ,"cn.edu.lingnan.mooc.common"})
@MapperScan("cn.edu.lingnan.mooc.message.mapper")
public class MessageApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessageApplication.class,args);
    }

}
