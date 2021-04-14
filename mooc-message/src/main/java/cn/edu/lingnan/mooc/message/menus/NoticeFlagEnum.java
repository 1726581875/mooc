package cn.edu.lingnan.mooc.message.menus;

/**
 * @author xmz
 * @date: 2021/04/09
 */
public enum NoticeFlagEnum {

    /**
     * 消息类型,1新增课程，2课程提问，3评论回复，4、系统通知
     */
    CREATE_COURSE(1),

    QUESTION(2),
    REPLY(3),
    SYSTEM(4);

    private Integer flag;

    private NoticeFlagEnum(Integer flag){
        this.flag = flag;
    }

    public Integer getFlag() {
        return flag;
    }
}
