package cn.edu.lingnan.authorize.authentication.config;

import cn.edu.lingnan.authorize.authentication.interceptor.CheckPermissionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author xmz
 * @date: 2021/02/07
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private cn.edu.lingnan.authorize.authentication.interceptor.CheckPermissionInterceptor CheckPermissionInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(CheckPermissionInterceptor);
    }

}