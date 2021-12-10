package cn.edu.lingnan.mooc.common.interceptor;

import cn.edu.lingnan.mooc.common.constant.CommonConstant;
import cn.edu.lingnan.mooc.common.model.LoginUser;
import cn.edu.lingnan.mooc.common.util.HttpServletUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author xmz
 * @date 2021/12/09
 * Feign调用传递token
 */
@Configuration
public class FeignRequestInterceptor implements RequestInterceptor {

    private static final Logger log = LoggerFactory.getLogger(FeignRequestInterceptor.class);

    @Override
    public void apply(RequestTemplate requestTemplate) {
        try {
            // 设置token
            HttpServletRequest request = HttpServletUtil.getRequest();
            if (request != null) {
                requestTemplate.header(LoginUser.HTTP_TOKEN_HEAD, request.getHeader(LoginUser.HTTP_TOKEN_HEAD));
            }
            // 链路跟踪，设置traceId，通过Http header 传递给服务提供方，服务提供方需要设置拦截并设置traceId
            String traceId = MDC.get(CommonConstant.TRACE_ID);
            if (!StringUtils.isEmpty(traceId)) {
                requestTemplate.header(CommonConstant.TRACE_ID, traceId);
            }

        } catch (Exception e) {
            log.error("feign调用传递token发生异常", e);
        }
    }

}
