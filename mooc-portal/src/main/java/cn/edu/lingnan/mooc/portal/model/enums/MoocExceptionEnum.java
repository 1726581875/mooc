package cn.edu.lingnan.mooc.portal.model.enums;

import cn.edu.lingnan.mooc.common.exception.BaseException;

/**
 * @author xiaomingzhang
 * @date 2021/9/14
 */
public enum MoocExceptionEnum implements BaseException {
    COURSE_NOT_EXIST(100001, "课程不存在")
    ;
    private int code;

    private String msg;

    private MoocExceptionEnum(int code, String msg){
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
