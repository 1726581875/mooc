package cn.edu.lingnan.mooc.authorize.shiro.oauth2;

import cn.edu.lingnan.mooc.common.model.RespResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


//@ControllerAdvice
@Slf4j
public class AuthorizeExceptionHandler {

    /**
     * 登录认证异常
     */
    // todo 如果打开，没有权限也是返回没有登录
/*    @ResponseBody
    @ExceptionHandler({UnauthenticatedException.class,AuthenticationException.class })
    public Object noLoginException() {
        return RespResult.fail(403,"没有登录");
    }*/

    /**
     * 权限异常
     */
/*    @ResponseBody
    @ExceptionHandler({UnauthorizedException.class, AuthorizationException.class })
    public Object authorizationException(Exception e) {
        return RespResult.fail(403,"没有权限");
    }*/

}
