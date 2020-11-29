package cn.edu.lingnan.core.enums;

/**
 * @author xmz
 * @date: 2020/11/10
 */
public enum UserEnum {
    NORMAL(1,true),
    DISABLE(2,false),
    DELETED(3,false),
    ;
    private Integer status;

    private boolean enable;

    private UserEnum(Integer status,boolean enable){
        this.status = status;
        this.enable = enable;
    }

    public Integer getStatus() {
        return status;
    }

    public boolean isEnable() {
        return enable;
    }


    public static boolean isEnable(Integer status){
        for (UserEnum userEnum : UserEnum.values()){
            if(userEnum.getStatus().equals(status)){
                return userEnum.isEnable();
            }
        }
        return false;
    }

}
