package cn.edu.lingnan.mooc.message.authentication.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
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
    private cn.edu.lingnan.mooc.message.authentication.interceptor.CheckPermissionInterceptor CheckPermissionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> excludePathList = new ArrayList<>();

        registry.addInterceptor(CheckPermissionInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePathList);
    }

}
