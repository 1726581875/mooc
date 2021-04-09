package cn.edu.lingnan.mooc.message.websock;

/**
 * @author xmz
 * @date: 2021/04/09
 */
public enum MessageTypeEnum {

    /**
     * 简单提示消息，页面展示后一两秒自动关闭
     */
    TIPS(1),
    /**
     * 停留消息，页面顶部停留，需要手动关闭
     */
    STAY(2),
    /**
     * 踢出下线消息,弹框
     */
    OUT(3)
    ;
    private Integer type;

    private MessageTypeEnum(Integer type){
        this.type = type;
    }

    public Integer getType() {
        return type;
    }
}
