package cn.edu.lingnan.mooc.message.authentication.interceptor;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.message.authentication.annotation.Check;
import cn.edu.lingnan.mooc.message.authentication.entity.UserToken;
import cn.edu.lingnan.mooc.message.authentication.util.RedisUtil;
import cn.edu.lingnan.mooc.message.authentication.util.UserUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * @author xmz
 * @date: 2020/11/15
 */
@Component
public class CheckPermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //todo 为了方便开发
        //构造超管用户
/*        UserToken superMan = new UserToken();
        superMan.setUserId(0L);
        superMan.setAccount("admin");
        UserUtil.setUserToken(superMan);
        //所有请求都放行
        if(true){
            return true;
        }*/


        // 获取ip地址
        String ipAddress = getIpAddress(request);
        //判断是否在Redis里
        if(RedisUtil.isExist("IpBlacklist:" + ipAddress)){
            //返回403
            responseMsg(response,HttpStatus.FORBIDDEN.value(),"你的IP已经被禁止");
            //禁止通行
            return false;
        }

        // 1、获取请求头携带的token
        String token = request.getHeader("Authorization");
        if(token == null){
            responseMsg(response,HttpStatus.UNAUTHORIZED.value(),"你还没有登录");
            return false;
        }
        // 2、去redis获取用户信息，如果获取不到，说明没有登录或者token过期
        UserToken userToken = RedisUtil.get(token, UserToken.class);
        if(userToken == null){
            responseMsg(response,HttpStatus.UNAUTHORIZED.value(),"无效token");
            return false;
        }
        //设置用户信息
        UserUtil.setUserToken(userToken);
        // 3、获取到用户权限
        String userTokenPermission = userToken.getPermission();
        // 4、权限校验，获取controller方法上的@Check注解判断是否有权限
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 获取请求的方法
            Method method = handlerMethod.getMethod();
            //判断方法名上是@Check注解
            boolean hasCheck = method.isAnnotationPresent(Check.class);
            // 如果该controller方法没有加上@Check注解，直接放行任何人都可以访问
            if (!hasCheck) {
                return true;
            }
            Check checkAnnotation = method.getAnnotation(Check.class);
            //获取注解的值
            String permission = checkAnnotation.value();
            // 判断是否是超管,如果没有权限
            boolean isSuperManager = userToken.getUserId().equals(0L) ? true : false;
            if(!isSuperManager &&!userTokenPermission.contains(permission)){
                responseMsg(response,HttpStatus.FORBIDDEN.value(),"权限不足");
                return false;
            }

        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ///最后要释放ThreadLocal,防止内存泄露
        UserUtil.remove();
    }


    private void responseMsg(HttpServletResponse response,Integer status, String message) throws IOException {
        response.setStatus(status);
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(
                RespResult.build().setStatus(status).setMsg(message)));
    }


    public static String getIpAddress(HttpServletRequest request) {

        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

}
