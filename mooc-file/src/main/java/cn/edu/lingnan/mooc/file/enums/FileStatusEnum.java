package cn.edu.lingnan.mooc.file.enums;

/**
 * @author xmz
 * @date: 2020/12/21
 */
public enum FileStatusEnum {

    /**
     * 正常
     */
    NORMAL(1),
    /**
     * 已删除
     */
    DELETED(2)
    ;
    private Integer status;
    private FileStatusEnum(Integer status){
        this.status = status;
    }

    public Integer getStatus(){
        return this.status;
    }

}
