package cn.edu.lingnan.authorize.shiro;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author xmz
 * @date: 2020/11/22
 */
@SpringBootApplication
public class AuthorizeShiroApplication implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AuthorizeShiroApplication.class);

    public static void main(String args[]) {
        SpringApplication.run(AuthorizeShiroApplication.class, args);
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("权限工程shiro版启动了...");
    }



}
