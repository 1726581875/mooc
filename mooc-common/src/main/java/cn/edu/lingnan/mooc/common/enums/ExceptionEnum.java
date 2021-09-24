package cn.edu.lingnan.mooc.common.enums;

import cn.edu.lingnan.mooc.common.exception.BaseException;

/**
 * @author xiaomingzhang
 * @date 2021/9/10
 */
public enum ExceptionEnum implements BaseException {
    KNOWN_ERROR(-1, "未知异常"),
    UNAUTHORIZED_ERROR(401, "未授权异常");
    ;
    private int code;

    private String msg;

    private ExceptionEnum(int code, String msg){
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
