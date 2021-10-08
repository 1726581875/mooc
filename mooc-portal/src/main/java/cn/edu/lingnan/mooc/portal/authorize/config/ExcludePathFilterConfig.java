package cn.edu.lingnan.mooc.portal.authorize.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author xiaomingzhang
 * @date 2021/09/13
 */
@Slf4j
@Component
public class ExcludePathFilterConfig {

    private static AntPathMatcher antPathMatcher;

    @Value("${security.filter.excludeUrls:null}")
    private String excludeUrls;

    public List<String> getAnonConfig() {
        List<String> excludePathList = new ArrayList<>();
        String[] defaultExcludeUrls = new String[]{

        };
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
        // 课程收藏列表
        excludePathList.add("/courses/collection/list");

        excludePathList.addAll(Arrays.asList(defaultExcludeUrls));
        if(excludeUrls == null || "null".equals(excludeUrls)){
            log.debug("excludePaths:{}", excludeUrls);
            excludePathList.addAll(Arrays.asList(excludeUrls.split(",")));
        }

       return excludePathList;
    }

    public boolean isExcludePath(String url) {
        log.debug("接受到的请求:{}", url);
        if (antPathMatcher == null) {
            antPathMatcher = new AntPathMatcher();
        }
        for (String pattern : getAnonConfig()) {
            if (antPathMatcher.match(pattern, url)) {
                return true;
            }
        }
        return false;
    }
}
