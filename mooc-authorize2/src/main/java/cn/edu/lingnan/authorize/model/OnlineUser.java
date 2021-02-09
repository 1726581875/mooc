package cn.edu.lingnan.authorize.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date loginTime;


}
