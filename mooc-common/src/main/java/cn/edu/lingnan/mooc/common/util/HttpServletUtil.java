package cn.edu.lingnan.mooc.common.util;

import cn.edu.lingnan.mooc.common.exception.MoocException;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author xmz
 * @date: 2020/11/23
 */
public class HttpServletUtil {

    /**
     * 获取用户ip地址
     * @return
     */
    public static String getIpAddress() {
        HttpServletRequest request = getRequest();
        if(request == null){
            throw new MoocException("获取不到当前请求");
        }
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

        if(("0:0:0:0:0:0:0:1".equals(ip))){
            return "127.0.0.1";
        }

        return ip;
    }



    public static HttpServletRequest getRequest() {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(attributes)) {
            return null;
        }
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) attributes;
        return requestAttributes.getRequest();
    }

}
