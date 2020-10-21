package cn.edu.lingnan.mooc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @Author xmz
 * @Date: 2020/10/06
 */
@SpringBootApplication
@EnableEurekaClient
//@EnableJpaAuditing //jpa实体类自己更新时间
public class CoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class,args);
    }

}
