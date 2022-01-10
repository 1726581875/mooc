package cn.edu.lingnan.mooc.common.filter;

import cn.edu.lingnan.mooc.common.constant.CommonConstant;
import cn.edu.lingnan.mooc.common.exception.MoocException;
import cn.edu.lingnan.mooc.common.util.HttpServletUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author xiaomingzhang
 * @date 2021/12/13
 * 链路跟踪，MDC过滤器，拦截获取请求传来的traceId
 */
@Order(1)
@Component
public class MDCFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(MDCFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        try {
            // 请求头获取traceId，如果存在则设置到MDC上下文中
            String traceId = httpRequest.getHeader(CommonConstant.TRACE_ID);
            if (StringUtils.isNotEmpty(traceId)) {
                MDC.put(CommonConstant.TRACE_ID, traceId);
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (MoocException e) {
            HttpServletUtil.responseMsg(httpResponse, e.getCode(), e.getMsg());
        } catch (Exception e) {
            log.error("", e);
        } finally {
            MDC.remove(CommonConstant.TRACE_ID);
        }

    }
}
