package cn.edu.lingnan.mooc.message.handler;

/**
 * @author xiaomingzhang
 * @date 2022/1/10
 */
public enum NoticeHandlerEnum {
    NEW_COURSE_NOTICE(1, NoticeHandlerEnum.NEW_COURSE_NOTICE_NAME),
    QUESTION_NOTICE(2, NoticeHandlerEnum.QUESTION_NOTICE_NAME),
    REPLY_NOTICE(3,NoticeHandlerEnum.REPLY_NOTICE_NAME),
    OFFLINE_NOTICE(6, NoticeHandlerEnum.OFFLINE_NOTICE_NAME)
    ;

    public static final String NEW_COURSE_NOTICE_NAME = "newCourseNoticeHandler";
    public static final String QUESTION_NOTICE_NAME = "questionNoticeHandler";
    public static final String REPLY_NOTICE_NAME = "replyNoticeHandler";
    public static final String OFFLINE_NOTICE_NAME = "offlineNoticeHandler";

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
