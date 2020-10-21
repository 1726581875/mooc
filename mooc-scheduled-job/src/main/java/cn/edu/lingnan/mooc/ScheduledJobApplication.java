package cn.edu.lingnan.mooc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author xmz
 * @date: 2020/10/21
 */
@EnableEurekaClient
@EnableScheduling
@SpringBootApplication
public class ScheduledJobApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScheduledJobApplication.class);
    }
}
