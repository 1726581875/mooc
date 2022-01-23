package cn.edu.lingnan.mooc.message.config;

import cn.edu.lingnan.mooc.common.interceptor.CheckPermissionInterceptor;
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
public class MessageInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private CheckPermissionInterceptor CheckPermissionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        List<String> excludePathList = new ArrayList<>();

        //放行/error
        excludePathList.add("/error");

        registry.addInterceptor(CheckPermissionInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePathList);
    }

}
