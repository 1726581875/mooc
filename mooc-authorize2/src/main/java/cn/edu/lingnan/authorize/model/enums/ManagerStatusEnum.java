package cn.edu.lingnan.authorize.model.enums;

/**
 * @author xiaomingzhang
 * @date 2021/9/14
 */
public enum ManagerStatusEnum {
    //用户状态| 0未审批（教师角色才需要审批），1正常，2禁用，3已删除
    DRAFT(0,"未审核"),
    NORMAL(1,"正常"),
    DISABLE(2,"禁用"),
    DELETED(3,"已删除"),
    UN_PASS(4,"审核不通过")
    ;
    /**
     * 状态码
     */
    private Integer status;
    /**
     * 状态说明
     */
    private String text;

    private ManagerStatusEnum(Integer status,String text){
        this.status = status;
        this.text = text;
    }

    public Integer getStatus() {
        return status;
    }

    public String getText() {
        return text;
    }

    public static String getText(Integer status){
        for (ManagerStatusEnum statusEnum : ManagerStatusEnum.values()){
            if(statusEnum.getStatus().equals(status)){
                return statusEnum.getText();
            }
        }
        return "未知状态";
    }


}
