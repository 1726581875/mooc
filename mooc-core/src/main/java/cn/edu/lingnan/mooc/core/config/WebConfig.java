package cn.edu.lingnan.mooc.core.config;

import cn.edu.lingnan.mooc.core.constant.Constant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
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
