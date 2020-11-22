package cn.edu.lingnan.mooc.annotation;

import lombok.Data;

import java.io.Serializable;

/**
 * @author xmz
 * @date: 2020/11/16
 */
@Data
public class UserToken implements Serializable {

    private String token;

    private String userId;

    private String sessionId;

    private String permission;

}
