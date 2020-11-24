package cn.edu.lingnan.mooc.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xmz
 * @date: 2020/11/16
 */
@Data
public class UserToken implements Serializable {

    private String token;

    private Integer userId;

    private String account;

    private String sessionId;

    private String permission;

}
