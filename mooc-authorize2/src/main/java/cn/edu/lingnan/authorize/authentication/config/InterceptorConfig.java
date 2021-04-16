package cn.edu.lingnan.authorize.authentication.config;

import cn.edu.lingnan.authorize.authentication.interceptor.CheckPermissionInterceptor;
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
    private cn.edu.lingnan.authorize.authentication.interceptor.CheckPermissionInterceptor CheckPermissionInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //不需要拦截的路径
        List<String> excludePathList = new ArrayList<>();
        excludePathList.add("/mooc/admin/login");
        excludePathList.add("/mooc/admin/code/image");
        excludePathList.add("/user/login");
        excludePathList.add("/user/isLogin");
        excludePathList.add("/user/register");
        //放行/error
        excludePathList.add("/error");

        registry.addInterceptor(CheckPermissionInterceptor).addPathPatterns("/**").excludePathPatterns(excludePathList);
    }

}
