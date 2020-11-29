package cn.edu.lingnan.mooc.common.exception;

/**
 * @author xmz
 * @date: 2020/11/29
 */
public class ParamerterException extends RuntimeException implements MoocException{

    @Override
    public int getCode() {
        return 0;
    }

    @Override
    public String getMsg() {
        return null;
    }

    @Override
    public MoocException setMsg(String errMsg) {
        return null;
    }

    @Override
    public String getDefaultMsg() {
        return null;
    }

    @Override
    public int getDefaultCode() {
        return 0;
    }
}
