package cn.edu.lingnan.mooc.message.websock;

import cn.edu.lingnan.mooc.message.authentication.util.SpringContextHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaomingzhang
 * @date 2021/04/08
 */
@Slf4j
@Component
@ServerEndpoint(value="/webSocket/{type}/{userId}")
public class MyWebSocket {

    private Integer userId;
    /**
     * 类型1为管理员，类型2为教师、学生
     */
    private int type;
    /**
     * 管理员前缀，管理员和教师属于不同的表，通过前缀区分
     */
    public final static String MANAGER_PRE = "manager-";

    private Session session;

    private static int onlineCount = 0;

    /**
     * 用于对象和字符串转换
     */
    private final static ObjectMapper objectMapper = SpringContextHolder.getBean(ObjectMapper.class);

    /**
     * 存储在线用户
     */
    private static ConcurrentHashMap<String, MyWebSocket> webSocketMap = new ConcurrentHashMap<>();

    /**
     * 建立连接会调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam("userId") Integer userId,@PathParam("type") Integer type, Session session){
        this.session = session;
        this.userId = userId;
        this.type = type;
        //如果是管理员，加上前缀区分
        if(this.type == 1) {
            webSocketMap.put(MANAGER_PRE + userId, this);
            //发送一条提示消息
            MessageDTO message = MessageDTO.createTips("欢迎登陆慕课后台管理系统~~~");
            this.sendMessageToManager(message,userId);
        }else {
            webSocketMap.put(String.valueOf(userId), this);
        }

        log.info("[websocket消息] 有新的用户登录，当前websocket连接数：{}",webSocketMap.size());
        log.info("[websocket消息] 连接用户为{} id===>{}",type == 1 ? "管理员" : "普通用户",userId);
    }
    /**
     * 关闭连接时调用
     */
    @OnClose
    public void onClose(){
        webSocketMap.remove(this);
        log.info("[websocket消息] 有用户连接断开，当前websocket连接数：{}",webSocketMap.size());
    }

    /**
     * 收到消息时
     */
    @OnMessage
    public void onMessage(String message){
        log.info("[websocket消息] 收到客户端发来的消息：{}",message);
    }

    /**
     *  向客户端广播发送信息
     */

    public void sendMessage(MessageDTO message){

        for (MyWebSocket webSocket : webSocketMap.values()) {
            try {
                webSocket.session.getBasicRemote().sendText(objectMapper.writeValueAsString(message));
            } catch (Exception e) {
                log.error("[websocket消息] 广播消息，message={},发生异常",message,e);
            }
            log.info("[websocket消息] 广播消息，message={}",message);
        }


    }


    /**
     * 向某些在线用户发通知
     * @param message
     * @param userIds
     */
    public void sendMessageToUser(MessageDTO message, List<Integer> userIds){
        userIds.forEach(userId ->{
            if(webSocketMap.get(String.valueOf(userId)) != null) {
                try {
                    webSocketMap.get(String.valueOf(userId)).session.getBasicRemote().sendText(objectMapper.writeValueAsString(message));
                    log.info("[websocket消息] 向用户{} 推送信息：message={}",userId,message);
                } catch (Exception exception) {
                    log.error("[websocket消息] 向用户{} 推送信息：message={} 异常：",userId,message,exception);
                }
            }
        });

    }

    /**
     * 向某些在线管理员发通知
     * @param message
     * @param userIds
     */
    public void sendMessageToManager(MessageDTO message, List<Integer> userIds){
        userIds.forEach(userId ->{
            if(webSocketMap.get(MANAGER_PRE + userId) != null) {
                try {
                    webSocketMap.get(MANAGER_PRE + userId).session.getBasicRemote().sendText(objectMapper.writeValueAsString(message));
                    log.info("[websocket消息] 向管理员{} 推送信息：message={}",userId,message);
                } catch (Exception exception) {
                    log.error("[websocket消息] 向管理员{} 推送信息：message={} 异常：",userId,message,exception);
                }
            }
        });

    }



    /**
     * 向某个用户发通知
     * @param message
     * @param toUserId
     */
    public void sendMessageToUser(MessageDTO message, Integer toUserId){
        if(webSocketMap!=null && webSocketMap.get(String.valueOf(toUserId)) != null){
            log.info("[websocket消息] 向{}发消息，message={}",toUserId,message);
            try {
                webSocketMap.get(String.valueOf(toUserId)).session.getBasicRemote().sendText(objectMapper.writeValueAsString(message));
            } catch (Exception e) {
                log.error("[websocket消息] 向{}发消息，message={},发生异常",toUserId,message,e);
            }
        }
    }

    /**
     * 向某个管理员发通知
     * @param message
     * @param toUserId
     */
    public void sendMessageToManager(MessageDTO message, Integer toUserId){
        if(webSocketMap!=null && webSocketMap.get(MANAGER_PRE + userId) != null){
            log.info("[websocket消息] 向{}发消息，message={}",MANAGER_PRE + userId,message);
            try {
                webSocketMap.get(MANAGER_PRE + userId).session.getBasicRemote().sendText(objectMapper.writeValueAsString(message));
            } catch (Exception e) {
                log.error("[websocket消息] 向{}发消息，message={},发生异常",MANAGER_PRE + userId,message,e);
            }
        }
    }

}
