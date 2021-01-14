package cn.edu.lingnan.authorize.shiro.oauth2;

import cn.edu.lingnan.mooc.common.model.RespResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 自定义过滤器
 */
public class AuthFilter extends AuthenticatingFilter {


    /**
     * 获取token
     *
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        //获取请求头携带的token
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String token = httpServletRequest.getHeader("Authorization");
        System.out.println("创建token...");
        return token == null ? null : new UserToken(token);
    }

    /**
     * 对跨域提供支持
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        System.out.println("请求前...对跨域支持..");
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个option请求，这里我们给option请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     * 验证token
     * 当访问拒绝时是否已经处理了；
     * 如果返回true表示需要继续处理；
     * 如果返回false表示该拦截器实例已经处理完成了，将直接返回即可。
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletResponse httpResponse = (HttpServletResponse)servletResponse;
        httpResponse.setContentType("application/json;charset=utf-8");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");
        System.out.println("验证token..");
        //完成token登入
        //1.检查请求头中是否含有token
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        String token = httpServletRequest.getHeader("Authorization");
        if (null == token || "".equals(token)) {
            responseTokenError(servletResponse, servletRequest,"token为空，你无权访问");
            return false;
        }
        //2. 如果有，对进行进行token验证
        return executeLogin(servletRequest, servletResponse);
    }

    /**
     * 执行认证
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
        System.out.println("进入验证");

        HttpServletResponse httpResponse = (HttpServletResponse)response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        httpResponse.setContentType("application/json;charset=utf-8");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Origin", httpRequest.getHeader("Origin"));

        String token = httpRequest.getHeader("Authorization");
        if (null == token || "".equals(token)) {
            responseTokenError(response, request,"token为空，你无权访问");
            return false;
        }

        return true;
    }

    /**
     * 无需转发，直接返回Response信息 Token认证错误
     */
    private void responseTokenError(ServletResponse response, ServletRequest request, String msg){
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        httpResponse.setContentType("application/json;charset=utf-8");
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");

        String origin = httpRequest.getHeader("Origin");
        httpResponse.setHeader("Access-Control-Allow-Origin",origin);
        try (PrintWriter out = httpResponse.getWriter()) {
            ObjectMapper objectMapper = new ObjectMapper();
            String data = objectMapper.writeValueAsString(new RespResult(401, msg, null));
            out.write(data);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}