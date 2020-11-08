package cn.edu.lingnan.mooc.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author xmz
 * @date: 2020/10/26
 * 自定义校验异常
 */
public class VerificationException extends AuthenticationException {

    public VerificationException(String msg, Throwable t) {
        super(msg, t);
    }

    public VerificationException(String msg) {
        super(msg);
    }
}
