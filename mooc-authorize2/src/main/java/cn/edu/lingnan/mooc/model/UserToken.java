package cn.edu.lingnan.mooc.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xmz
 * @date: 2020/11/16
 */
public class UserToken implements Serializable {

    private String token;

    private Long userId;

    private String account;

    private String sessionId;

    private String permission;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return "UserToken{" +
                "token='" + token + '\'' +
                ", userId=" + userId +
                ", account='" + account + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", permission='" + permission + '\'' +
                '}';
    }
}
