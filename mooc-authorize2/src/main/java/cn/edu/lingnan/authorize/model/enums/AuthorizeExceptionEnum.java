package cn.edu.lingnan.authorize.model.enums;

import cn.edu.lingnan.mooc.common.exception.BaseException;

/**
 * @author xiaomingzhang
 * @date 2021/9/14
 */
public enum AuthorizeExceptionEnum implements BaseException {
    INCORRECT_VERIFICATION_CODE(100001, "验证码不正确"),
    ACCOUNT_NOT_EXIST(100002, "账号不存在")
    ;
    private int code;

    private String msg;

    private AuthorizeExceptionEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }


    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
