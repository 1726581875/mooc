package cn.edu.lingnan.mooc.message.menus;

/**
 * @author xmz
 * @date: 2021/04/09
 */
public enum NoticeTypeEnum {


    /**
     * 这条消息属于管理员
     */
    MANAGER(1),
    /**
     * 这条消息属于用户
     */
    USER(2);

    private Integer type;

    private NoticeTypeEnum(Integer type){
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
