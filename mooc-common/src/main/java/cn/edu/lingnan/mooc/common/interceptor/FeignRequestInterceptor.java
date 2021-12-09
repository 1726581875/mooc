package cn.edu.lingnan.mooc.common.interceptor;

import cn.edu.lingnan.mooc.common.model.LoginUser;
import cn.edu.lingnan.mooc.common.util.HttpServletUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

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
            HttpServletRequest request = HttpServletUtil.getRequest();
            requestTemplate.header(LoginUser.HTTP_TOKEN_HEAD, request.getHeader(LoginUser.HTTP_TOKEN_HEAD));
        }catch (Exception e){
            log.error("feign调用传递token发生异常", e);
        }
    }

}
