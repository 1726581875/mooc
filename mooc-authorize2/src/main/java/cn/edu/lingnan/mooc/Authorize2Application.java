package cn.edu.lingnan.mooc;

import cn.edu.lingnan.mooc.model.Role;
import cn.edu.lingnan.mooc.util.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author xmz
 * @date: 2020/11/22
 */
@SpringBootApplication
public class Authorize2Application implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(Authorize2Application.class);

    public static void main(String args[]) {
        SpringApplication.run(Authorize2Application.class, args);
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("工程启动了...");
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public SpringContextHolder initSpringContextHolder(){
        return new SpringContextHolder();
    }
}
