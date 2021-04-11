package cn.edu.lingnan.mooc.message.menus;

/**
 * @author xmz
 * @date: 2021/04/09
 */
public enum NoticeStatusEnum {

    /**
     * 未读
     */
    UNREAD(0),
    /**
     * 已读
     */
    READ(1),
    /**
     * 已删除
     */
    DELETED(2),
    /**
     * 真正删除
     */
    TRUE_DELETED(3)
    ;
    private Integer status;

    private NoticeStatusEnum(Integer status){
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}
