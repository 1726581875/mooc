package cn.edu.lingnan.core.config;

import cn.edu.lingnan.core.constant.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xmz
 * @date: 2020/11/15
 */
@Configuration
public class WebConfig implements WebMvcConfigurer{


    @Value("${mooc.logo.path:D:\\data\\logo}")
    private String LOGO_BASE_PATH;

    @Value("${mooc.file.path:D:\\data}")
    private String FILE_BASE_PATH;


/*    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }*/


    /**
     * 跨域配置
     * @return
     */
    @Bean
    public CorsFilter corsFilter() {
        List<String> allowOrigins = new ArrayList<>();
        allowOrigins.add("http://localhost:8080");
        //allowOrigins.add("http://localhost:8081");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(allowOrigins);
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    /**
     * 配置图片映射地址
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //logo地址映射
        registry.addResourceHandler(Constant.LOGO_MAPPING_PATH + "**").addResourceLocations("file:" + LOGO_BASE_PATH + "/");
        //文件地址映射
        registry.addResourceHandler(  "/file/**").addResourceLocations("file:" + FILE_BASE_PATH + "/");
    }


}
