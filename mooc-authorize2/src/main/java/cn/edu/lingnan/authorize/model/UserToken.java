package cn.edu.lingnan.authorize.model;

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
    /**
     * 教师通过前缀teacher-区分
     */
    private String account;
    /**
     * 类型 1为管理员，2为普通用户或教师
     */
    private Integer type;

    private String sessionId;

    private String permission;

}
