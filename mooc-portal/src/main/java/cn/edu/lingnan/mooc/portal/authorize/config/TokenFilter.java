package cn.edu.lingnan.mooc.portal.authorize.config;

import cn.edu.lingnan.mooc.common.exception.MoocException;
import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.common.model.UserToken;
import cn.edu.lingnan.mooc.common.util.RedisUtil;
import cn.edu.lingnan.mooc.common.util.UserUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xiaomingzhang
 * @date 2021/9/13
 */
@Slf4j
@Component
public class TokenFilter implements Filter {

    @Autowired
    private ExcludePathFilterConfig config;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String accessUrl = request.getRequestURI();
        if (!config.isExcludePath(accessUrl)) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String token = request.getHeader("Authorization");
            // 获取和校验登录信息
            UserToken loginInfo = getLoginInfo(token);
            // 设置用户信息
            UserUtil.setUserToken(loginInfo);
            filterChain.doFilter(request, response);
        }catch (MoocException e) {
            responseMsg(response, e.getCode(), e.getMsg());
        } catch (Exception e){
            log.error("发生错误", e);
        }finally {
            UserUtil.remove();
        }

    }

    private UserToken getLoginInfo(String token){
        if (token == null) {
            throw new MoocException(HttpStatus.UNAUTHORIZED.value(), "你还没有登录");
        }
        UserToken userToken = RedisUtil.get(token, UserToken.class);
        if (userToken == null) {
            throw new MoocException(HttpStatus.UNAUTHORIZED.value(), "无效token");
        }
        return userToken;
    }


    private void responseMsg(HttpServletResponse response,Integer status, String message) throws IOException {
        response.setStatus(status);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(
                RespResult.build().setStatus(status).setMsg(message)));
    }

}
