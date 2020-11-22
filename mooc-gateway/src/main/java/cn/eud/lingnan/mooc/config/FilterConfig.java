package cn.eud.lingnan.mooc.config;

import cn.eud.lingnan.mooc.filter.LoginFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author xmz
 * @date: 2020/11/17
 */
@Configuration
public class FilterConfig {

    @Bean
    public LoginFilter getLoginFilter(){
        return new LoginFilter();
    }

}
