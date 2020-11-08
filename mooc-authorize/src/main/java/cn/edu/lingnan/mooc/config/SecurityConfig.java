package cn.edu.lingnan.mooc.config;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.config.handle.MoocAuthenticationFailureHandler;
import cn.edu.lingnan.mooc.config.handle.MoocAuthenticationSuccessHandler;
import cn.edu.lingnan.mooc.filter.VerifyCodeFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

/**
 * @author xmz
 * @date: 2020/10/25
 */
@Slf4j
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    // spring在启动时会自动注入ObjectMapper，用于转换json串
    private ObjectMapper objectMapper;

    /**
     * 注册了一个PasswordEncoder
     * 密码自动加密
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private MoocAuthenticationSuccessHandler successHandler;
    @Autowired
    private MoocAuthenticationFailureHandler failureHandler;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
            // 配置验证码过滤器，在UsernamePasswordAuthenticationFilter之前
            http.addFilterBefore(new VerifyCodeFilter(failureHandler), UsernamePasswordAuthenticationFilter.class)
                    //===跨域配置=== begin
                    .cors().disable()
                    .cors()
                    .and().authorizeRequests()
                     //处理跨域请求中的Preflight请求
                    .requestMatchers(CorsUtils::isPreFlightRequest)
                    .permitAll()
                    .and()
                    //====跨域配置 ===end
                .formLogin()
                .loginPage("/login/msg")
                .loginProcessingUrl("/mooc/admin/login")
                //当登录成功，返回成功json
                .successHandler((request,response,authentication) ->{
                    log.info("登录成功");
                    response.setContentType("application/json;charset=UTF-8");
                    RespResult respResult = RespResult.success("登录成功");
                    response.getWriter().write(objectMapper.writeValueAsString(respResult));
                })
                //当登录失败，返回失败json
                .failureHandler((request,response,exception) ->{
                    log.info("登录失败,原因：{}",exception.getMessage());
                    //response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
                    response.setContentType("application/json;charset=UTF-8");
                    RespResult respResult = RespResult.fail(exception.getMessage());
                    response.getWriter().write(objectMapper.writeValueAsString(respResult));
                })
                .and()
                //对请求的授权，指定下面这些都是授权的配置
                .authorizeRequests()
                //允许登录页
                .antMatchers("/login/msg","/code/image","/mooc/admin/login")
                .permitAll()
                .anyRequest()//任何请求
                .authenticated()//都需要身份验证
                .and()
                .csrf().disable();

    }
}
