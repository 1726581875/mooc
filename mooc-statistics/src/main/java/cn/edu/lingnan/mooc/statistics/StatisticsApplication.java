package cn.edu.lingnan.mooc.statistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xmz
 * @date: 2020/12/30
 */
@SpringBootApplication
@RestController
public class StatisticsApplication {

    public static void main(String[] args) {
        SpringApplication.run(StatisticsApplication.class);
    }

    @GetMapping("/hello")
    public String sayHello(){
        return "hello statistics !";
    }

}