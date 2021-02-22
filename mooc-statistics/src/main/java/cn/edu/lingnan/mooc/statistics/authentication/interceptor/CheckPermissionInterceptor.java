package cn.edu.lingnan.mooc.statistics.authentication.interceptor;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.statistics.authentication.annotation.Check;
import cn.edu.lingnan.mooc.statistics.authentication.entity.UserToken;
import cn.edu.lingnan.mooc.statistics.authentication.util.RedisUtil;
import cn.edu.lingnan.mooc.statistics.authentication.util.UserUtil;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author xmz
 * @date: 2020/11/15
 */
@Component
public class CheckPermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private ObjectMapper objectMapper;

    private static Set<String> whiteUrlSet = new HashSet<>();

    static {
        whiteUrlSet = whiteUrl(
                );
    }

    private static Set<String> whiteUrl(String ...urls){
        return new HashSet<>(Arrays.stream(urls).collect(Collectors.toList()));
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //todo 为了方便开发
        //构造超管用户
/*        UserToken superMan = new UserToken();
        superMan.setUserId(0L);
        superMan.setAccount("admin");
        UserUtil.setUserToken(superMan);*/
        //所有请求都放行
/*        if(true){
            return true;
        }*/


        // TODO 获取ip地址
        String ipAddress = getIpAddress(request);

       //TODO URI,不知是否存在安全问题，待完善
        String requestURI = request.getRequestURI();
        //判断url是否在白名单url里，若存在则不需要token验证
        for (String url : whiteUrlSet) {
            if(requestURI.contains(url)){
                return true;
            }
        }

        // 1、获取请求头携带的token
        String token = request.getHeader("Authorization");


        // String token = "smallchili";
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
            String permission = checkAnnotation.value();
            System.out.println("permission=" + permission);

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
