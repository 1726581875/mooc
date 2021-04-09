package cn.edu.lingnan.mooc.message.websock;

import lombok.Data;

/**
 * @author xmz
 * @date: 2021/04/09
 */
public class MessageDTO {
    /**
     * 消息类型，类型1简单提醒，2停留消息，3踢出下线消息
     */
    private Integer type;

    private String content;

    public MessageDTO(){}

    public MessageDTO(Integer type, String content) {
        this.type = type;
        this.content = content;
    }

    public static MessageDTO build(){
        return new MessageDTO();
    }

    public static MessageDTO createTips(String content){
      return MessageDTO.build().setType(MessageTypeEnum.TIPS.getType()).setContent(content);
    }

    public static MessageDTO createStay(String content){
        return MessageDTO.build().setType(MessageTypeEnum.STAY.getType()).setContent(content);
    }

    public static MessageDTO createOut(String content){
        return MessageDTO.build().setType(MessageTypeEnum.OUT.getType()).setContent(content);
    }

    public Integer getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    public MessageDTO setType(Integer type) {
        this.type = type;
        return this;
    }

    public MessageDTO setContent(String content) {
        this.content = content;
        return this;
    }

}
