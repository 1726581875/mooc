package cn.edu.lingnan.mooc.common.model;

import cn.edu.lingnan.mooc.common.exception.enums.UserTypeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xmz
 * @date: 2020/11/16
 */
@Data
public class UserToken implements Serializable {

    private static final long serialVersionUID = 5021055089779459121L;

    /**
     * 前端Http请求头携带的token key
     */
    public static final String HTTP_TOKEN_HEAD = "Authorization";

    private String token;

    private Long userId;
    /**
     * 教师通过前缀teacher-区分
     */
    private String account;
    /**
     * 类型 为管理员，教师,普通用户
     */
    private UserTypeEnum type;

    private String permission;

}
