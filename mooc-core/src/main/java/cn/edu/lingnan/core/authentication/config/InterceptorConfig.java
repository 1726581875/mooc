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
        excludePathList.add("/admin/moocUsers/*");
        //查询评论回复
        excludePathList.add("/comment/listCommentDetail");

        //放行/error
        excludePathList.add("/error");

        registry.addInterceptor(CheckPermissionInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(excludePathList);
    }

}
