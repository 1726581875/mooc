package cn.edu.lingnan.authorize.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author xmz
 * @date: 2020/11/25
 */
@Data
public class OnlineUser {

    /**
     * 用户昵称
     */
    private String name;

    /**
     * 用户账户
     */
    private String account;

    /**
     * IP
     */
    private String ip;

    /**
     * 登录时间
     */
    private Date loginTime;


}
