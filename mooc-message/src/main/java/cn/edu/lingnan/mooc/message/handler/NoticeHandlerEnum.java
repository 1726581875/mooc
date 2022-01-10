package cn.edu.lingnan.mooc.message.handler;

/**
 * @author xiaomingzhang
 * @date 2022/1/10
 */
public enum NoticeHandlerEnum {
    NEW_COURSE_notice(1, "newCourseNoticeHandler"),
    QUESTION_NOTICE(2,"questionNoticeHandler"),
    REPLY_NOTICE(3,"replyNoticeHandler"),
    OFFLINE_NOTICE(6, "offlineNoticeHandler")
    ;

    private Integer type;

    private String name;

    NoticeHandlerEnum(Integer type, String name) {
        this.type = type;
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static String getBeanNameByType(Integer type) {

        for (NoticeHandlerEnum handlerEnum : values()){
            if(handlerEnum.getType().equals(type)){
                return handlerEnum.getName();
            }
        }
        return null;
    }
}
