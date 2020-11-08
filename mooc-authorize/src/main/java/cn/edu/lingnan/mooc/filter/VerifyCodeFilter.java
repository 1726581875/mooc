package cn.edu.lingnan.mooc.filter;

import cn.edu.lingnan.mooc.config.handle.MoocAuthenticationFailureHandler;
import cn.edu.lingnan.mooc.exception.VerificationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
/**
 * @author xmz
 * @date: 2020/10/25
 * 验证码校验过滤器，配置在Security config里
 * UsernamePasswordAuthenticationFilter(校验登陆用户名和密码的过滤器)之前
 */
public class VerifyCodeFilter extends OncePerRequestFilter {


    private static final Logger log = LoggerFactory.getLogger(VerifyCodeFilter.class);

    private MoocAuthenticationFailureHandler failureHandler;

    public VerifyCodeFilter(MoocAuthenticationFailureHandler failureHandler){
        this.failureHandler = failureHandler;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, AuthenticationException {

        System.out.println(request.getSession().getId());
        //如果是登陆请求才校验图片验证码
        if(request.getRequestURI().equals("/mooc/admin/login")
                && request.getMethod().toUpperCase().equals("POST")) {
            //获取到session里存的验证码
            String sessionVerifyCode = (String) request.getSession().getAttribute("verifyCode");
            //获取前端输入的验证码
            String inputVerifyCode = ServletRequestUtils.getStringParameter(request, "code").trim();
            log.info("sessionVerifyCode={} , webVerifyCode={}", sessionVerifyCode, inputVerifyCode);

            //验证码是否为空
            if(inputVerifyCode == null || inputVerifyCode == "") {
                log.info("输入验证码不能为空！");
                //failureHandler.onAuthenticationFailure(request,response,new VerificationException("输入验证码不能为空"));
            }
            //如果验证码验证码不匹配
//            if(!sessionVerifyCode.toLowerCase().equals(inputVerifyCode.toLowerCase())) {
//               log.info("验证码不正确！");
//                //failureHandler.onAuthenticationFailure(request,response,new VerificationException("验证码不正确"));
//            }
        }
        filterChain.doFilter(request,response);
    }

}
