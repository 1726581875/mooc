package cn.edu.lingnan.authorize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author xmz
 * @date: 2020/11/22
 */
//Feign
@EnableFeignClients
//Hystrix
@EnableCircuitBreaker
@EnableEurekaClient
@SpringBootApplication(scanBasePackages =
        {"cn.edu.lingnan.mooc.common","cn.edu.lingnan.authorize"})
public class Authorize2Application implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Authorize2Application.class);

    public static void main(String args[]) {
        SpringApplication.run(Authorize2Application.class, args);
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("权限工程启动了...");
    }



}
