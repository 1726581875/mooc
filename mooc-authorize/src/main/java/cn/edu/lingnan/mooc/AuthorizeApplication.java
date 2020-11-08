package cn.edu.lingnan.mooc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xmz
 * @date: 2020/10/25
 */
@SpringBootApplication
@RestController
public class AuthorizeApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizeApplication.class, args);
    }

    @GetMapping("/hello")
    public String hello(){
        return "Hello Word";
    }


}
