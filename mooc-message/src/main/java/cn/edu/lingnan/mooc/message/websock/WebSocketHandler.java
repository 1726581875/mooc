package cn.edu.lingnan.mooc.message.websock;

import cn.edu.lingnan.mooc.common.enums.UserTypeEnum;
import cn.edu.lingnan.mooc.message.authentication.util.SpringContextHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiaomingzhang
 * @date 2021/04/08
 */
@Slf4j
@Component
@ServerEndpoint(value = "/webSocket/{type}/{userId}")
public class WebSocketHandler {

    private Long userId;
    /**
     * 1为管理员，2为教师/学生
     */
    private UserTypeEnum type;
    /**
     * 管理员前缀，管理员和教师属于不同的表，可能id会存在相同，所以通过前缀区分
     */
    public final static String MANAGER_PRE = "manager_";

    private Session session;

    /**
     * 用于对象和字符串转换
     */
    private final static ObjectMapper objectMapper = SpringContextHolder.getBean(ObjectMapper.class);

    /**
     * 存储在线用户
     */
    private static ConcurrentHashMap<String, WebSocketHandler> webSocketMap = new ConcurrentHashMap<>();

    private static Map<String, UserSession> userSessionMap = new ConcurrentHashMap<>();


    /**
     * 建立连接会调用的方法
     */
    @OnOpen
    public void onOpen(@PathParam("userId") Long userId, @PathParam("type") UserTypeEnum type, Session session) {

        userSessionMap.put(type + ":" + userId, new UserSession(userId, type, session));
        log.info("[websocket消息] 有新的用户登录，当前websocket连接数：{}", webSocketMap.size());
        log.info("[websocket消息] 连接用户为{} id===>{}", type, userId);
    }

    /**
     * 关闭连接时调用
     */
    @OnClose
    public void onClose() {
        webSocketMap.remove(this);
        log.info("[websocket消息] 有用户连接断开，当前websocket连接数：{}", webSocketMap.size());
    }

    /**
     * 收到消息时
     */
    @OnMessage
    public void onMessage(String message) {
        log.info("[websocket消息] 收到客户端发来的消息：{}", message);
    }

    /**
     * 向客户端广播发送信息
     */

    public void sendMessage(MessageDTO message) {

        for (WebSocketHandler webSocket : webSocketMap.values()) {
            try {
                webSocket.session.getBasicRemote().sendText(objectMapper.writeValueAsString(message));
            } catch (Exception e) {
                log.error("[websocket消息] 广播消息，message={},发生异常", message, e);
            }
            log.info("[websocket消息] 广播消息，message={}", message);
        }


    }


    /**
     * 向某些在线用户发通知
     *
     * @param message
     * @param userIds
     */
    public void sendMessageToUser(MessageDTO message, List<Long> userIds) {
        userIds.forEach(userId -> {
            if (webSocketMap.get(String.valueOf(userId)) != null) {
                try {
                    webSocketMap.get(String.valueOf(userId)).session.getBasicRemote().sendText(objectMapper.writeValueAsString(message));
                    log.info("[websocket消息] 向用户{} 推送信息：message={}", userId, message);
                } catch (Exception exception) {
                    log.error("[websocket消息] 向用户{} 推送信息：message={} 异常：", userId, message, exception);
                }
            }
        });

    }

    /**
     * 向某些在线管理员发通知
     *
     * @param message
     * @param userIds
     */
    public void sendMessageToManager(MessageDTO message, List<Long> userIds) {
        userIds.forEach(userId -> {
            if (webSocketMap.get(getManagerKey(userId)) != null) {
                try {
                    webSocketMap.get(MANAGER_PRE + userId).session.getBasicRemote().sendText(objectMapper.writeValueAsString(message));
                    log.info("[websocket消息] 向管理员{} 推送信息：message={}", userId, message);
                } catch (Exception exception) {
                    log.error("[websocket消息] 向管理员{} 推送信息：message={} 异常：", userId, message, exception);
                }
            }
        });

    }

    private String getManagerKey(Long userId) {
        return getKey(UserTypeEnum.MANAGER, userId);
    }

    private String getKey(UserTypeEnum type, Long userId){
        return type + ":" + userId;
    }


    /**
     * 向某个用户发通知
     *
     * @param message
     * @param toUserId
     */
    public void sendMessageToUser(MessageDTO message, Integer toUserId) {
        if (webSocketMap != null && webSocketMap.get(String.valueOf(toUserId)) != null) {
            log.info("[websocket消息] 向{}发消息，message={}", toUserId, message);
            try {
                webSocketMap.get(String.valueOf(toUserId)).session.getBasicRemote().sendText(objectMapper.writeValueAsString(message));
            } catch (Exception e) {
                log.error("[websocket消息] 向{}发消息，message={},发生异常", toUserId, message, e);
            }
        }
    }

    /**
     * 向某个管理员发通知
     *
     * @param message
     * @param toUserId
     */
    public void sendMessageToManager(MessageDTO message, Long toUserId) {
        if (webSocketMap != null && webSocketMap.get(MANAGER_PRE + userId) != null) {
            log.info("[websocket消息] 向{}发消息，message={}", MANAGER_PRE + userId, message);
            try {
                webSocketMap.get(MANAGER_PRE + userId).session.getBasicRemote().sendText(objectMapper.writeValueAsString(message));
            } catch (Exception e) {
                log.error("[websocket消息] 向{}发消息，message={},发生异常", MANAGER_PRE + userId, message, e);
            }
        }
    }


    @Data
    class UserSession {

        private Long userId;

        private UserTypeEnum type;

        private Session session;

        public UserSession(){}

        public UserSession(Long userId, UserTypeEnum type, Session session) {
            this.userId = userId;
            this.type = type;
            this.session = session;
        }

    }

}
