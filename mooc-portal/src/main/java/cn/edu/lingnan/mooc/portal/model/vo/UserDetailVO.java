package cn.edu.lingnan.mooc.portal.model.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author xmz
 * @date: 2021/02/08
 */
@Data
@ToString
public class UserDetailVO {

    private Long id;
    /**
     * 用户头像
     */
    private String userImage;
    /**
     * 用户昵称
     */
    private String name;
    /**
     * 登录账号
     */
    private String account;
    /**
     * 个人座右铭、格言
     */
    private String motto;
    /**
     * 类型，教师/普通用户
     */
    private String userType;

}
