package cn.edu.lingnan.core.authentication.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xmz
 * @date: 2020/11/16
 */
@Data
public class UserToken implements Serializable {

    private String token;

    private Long userId;

    private String account;

    private String sessionId;

    private String permission;
    /**
     * 类型 1为管理员，2为普通用户或教师
     */
    private Integer type;


}
