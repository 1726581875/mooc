package cn.edu.lingnan.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @Author xmz
 * @Date: 2020/10/06
 */
@SpringBootApplication
@EnableEurekaClient
public class CoreApplication {

    
    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class,args);
    }

}
