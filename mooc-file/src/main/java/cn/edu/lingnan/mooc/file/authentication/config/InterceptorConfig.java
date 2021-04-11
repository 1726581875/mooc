package cn.edu.lingnan.mooc.file.authentication.config;

import cn.edu.lingnan.mooc.file.authentication.interceptor.CheckPermissionInterceptor;
import cn.edu.lingnan.mooc.file.constant.FileConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xmz
 * @date: 2021/02/07
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private CheckPermissionInterceptor CheckPermissionInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludePathList = new ArrayList<>();
        excludePathList.add("/download/*");
        excludePathList.add("/file/**");
        //放行/error
        excludePathList.add("/error");
        registry.addInterceptor(CheckPermissionInterceptor).addPathPatterns("/**").excludePathPatterns(excludePathList);
    }

    /**
     * 跨域配置
     * @return
     */
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

}
