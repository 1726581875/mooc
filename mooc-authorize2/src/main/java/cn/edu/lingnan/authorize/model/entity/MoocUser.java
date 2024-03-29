package cn.edu.lingnan.authorize.model.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.Id;

import java.util.Date;

/**
 * @author xmz
 * @date: 2021/02/09
 */
@Data
@Accessors(chain = true)
public class MoocUser {

    private Long id;
    // 用户头像
    private String userImage;
    // 用户昵称
    private String name;
    // 登录账号
    private String account;
    // 登录密码
    private String password;
    // 类型，教师/普通用户
    private String userType;
    // 用户状态| 0未审批（教师角色需要审批），1正常，2禁用，3已删除
    private Integer status;
    /**
     * 个人座右铭、格言
     */
    private String motto;
}
