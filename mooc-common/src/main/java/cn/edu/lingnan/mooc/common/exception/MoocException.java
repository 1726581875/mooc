package cn.edu.lingnan.mooc.common.exception;

import cn.edu.lingnan.mooc.common.enums.ExceptionEnum;

/**
 * @author xmz
 * @date: 2020/11/29
 */
public class MoocException extends RuntimeException implements BaseException {

    private int code;

    private String msg;


    public MoocException(int code, String msg){
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public MoocException(BaseException exceptionEnum){
        this(exceptionEnum.getCode(), exceptionEnum.getMsg());
    }

    public MoocException(String msg){
        this(ExceptionEnum.KNOWN_ERROR.getCode(), msg);
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
