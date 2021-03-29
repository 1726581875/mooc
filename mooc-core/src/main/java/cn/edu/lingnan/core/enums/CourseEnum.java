package cn.edu.lingnan.core.enums;

/**
 * @author xmz
 * @date: 2020/12/03
 */
public enum CourseEnum {
    // 状态|0未审核/1发布/2禁用/3已删除
    DRAFT(0,"未审核"),
    NORMAL(1,"正常"),
    DISABLE(2,"禁用"),
    DELETED(3,"已删除")
    ;
    /**
     * 状态码
     */
    private Integer status;
    /**
     * 状态说明
     */
    private String text;

    private CourseEnum(Integer status,String text){
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
        for (CourseEnum courseEnum : CourseEnum.values()){
            if(courseEnum.getStatus().equals(status)){
                return courseEnum.getText();
            }
        }
        return null;
    }

}
