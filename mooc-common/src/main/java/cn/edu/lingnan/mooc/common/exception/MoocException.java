package cn.edu.lingnan.mooc.common.exception;

/**
 * @author xmz
 * @date: 2020/11/29
 */
public interface MoocException {
     int getCode();
     String getMsg();
     MoocException setMsg(String errMsg);

    default String getDefaultMsg(){
        return "未知异常";
    }
    default int getDefaultCode(){
        return -1;
    }

}
