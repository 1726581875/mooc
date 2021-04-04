package cn.edu.lingnan.core.authentication.config;

import cn.edu.lingnan.core.authentication.interceptor.CheckPermissionInterceptor;
import cn.edu.lingnan.core.constant.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.*;
import java.util.stream.Collectors;

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
        excludePathList.add("/file/**");
        excludePathList.add("/logoImage/**");
        excludePathList.add("/admin/categorys/all");
        excludePathList.add("/admin/courses/getByTag");

        registry.addInterceptor(CheckPermissionInterceptor).addPathPatterns("/**").excludePathPatterns();
    }

}
