package cn.edu.lingnan.mooc.portal.authorize.model.param;

import cn.edu.lingnan.mooc.common.enums.UserTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author xmz
 * @date: 2021/02/09
 */
@Data
public class RegisterParam {
    /**
     * 用户名
     */
    @NotBlank(message = "账号为空")
    private String account;
    /**
     * 密码
     */
    @NotBlank(message = "密码为空")
    private String password;
    /**
     * 确认密码
     */
    @NotBlank(message = "确认密码为空")
    private String confirmPassword;
    /**
     * 用户类型，教师/普通用户
     */
    private UserTypeEnum userType;
}
