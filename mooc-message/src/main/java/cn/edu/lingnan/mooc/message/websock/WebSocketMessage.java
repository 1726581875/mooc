package cn.edu.lingnan.mooc.message.websock;

import lombok.Data;

/**
 * @author xmz
 * @date: 2021/04/09
 */
@Data
public class WebSocketMessage {
    /**
     * 消息类型，类型1简单提醒，2弹框踢除下线
     */
    private Integer type;

    private String content;


}
