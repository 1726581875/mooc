package cn.edu.lingnan.mooc.core.config;

import cn.edu.lingnan.mooc.common.interceptor.CheckPermissionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.*;

/**
 * @author xmz
 * @date: 2021/02/07
 */
@Configuration
public class CoreInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private CheckPermissionInterceptor CheckPermissionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        List<String> excludePathList = new ArrayList<>();
        excludePathList.add("/file/**");
        excludePathList.add("/logoImage/**");
        excludePathList.add("/admin/categorys/all");
        //用户端，放行
        //根据分类查询课程
        excludePathList.add("/admin/courses/getByTag");
        //分类
        excludePathList.add("/admin/categorys/all");
        //查询课程详情
        excludePathList.add("/courses/*");
        //查询课程评论
        excludePathList.add("/comment/list");
        excludePathList.add("/comment/listAll");
        //查询用户信息
        excludePathList.add("/admin/moocUsers/{id:\\d+}");
        //查询评论回复
        excludePathList.add("/comment/listCommentDetail");
        //放行登录页logo
        excludePathList.add("/admin/logo/loginLogo");
        excludePathList.add("/admin/logo/get");
        //登录日志导出接口
        excludePathList.add("/admin/loginLogs/export");
        excludePathList.add("/admin/ipBlacklists/export");
        excludePathList.add("/admin/ipBlacklists/import");
        //获取人员收藏的课程列表
        excludePathList.add("/courses/collection/list");

        //放行/error
        excludePathList.add("/error");

        registry.addInterceptor(CheckPermissionInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePathList);
    }

}
