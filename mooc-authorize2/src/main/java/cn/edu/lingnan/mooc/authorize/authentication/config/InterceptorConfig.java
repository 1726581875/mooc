package cn.edu.lingnan.mooc.authorize.authentication.config;

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
public class InterceptorConfig implements WebMvcConfigurer {


    @Autowired
    private CheckPermissionInterceptor checkPermissionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //不需要拦截的路径
        List<String> excludePathList = new ArrayList<>();
        // 统一放行spring的/error
        excludePathList.add("/error");
        // 1、authorize
        excludePathList.add("/mooc/admin/login");
        excludePathList.add("/mooc/admin/code/image");
        excludePathList.add("/user/login");
        excludePathList.add("/user/isLogin");
        excludePathList.add("/user/register");
        // 2、code
        excludePathList.add("/file/**");
        excludePathList.add("/logoImage/**");
        //登录页logo
        excludePathList.add("/admin/logo/loginLogo");
        // 登录日志导出接口
        excludePathList.add("/admin/loginLogs/export");
        excludePathList.add("/admin/ipBlacklists/export");
        excludePathList.add("/admin/ipBlacklists/import");
        // 3、statistics
        excludePathList.add("/download/*");
        excludePathList.add("/file/");
        //查询排名前10的课程
        excludePathList.add("/courses/listTop10");
        //查询课程
        excludePathList.add("/courses/search");


        registry.addInterceptor(checkPermissionInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePathList);
    }


}
