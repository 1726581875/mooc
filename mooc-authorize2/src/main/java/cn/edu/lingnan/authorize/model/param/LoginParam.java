package cn.edu.lingnan.authorize.model.param;
import cn.edu.lingnan.mooc.common.exception.enums.UserTypeEnum;
import cn.edu.lingnan.mooc.common.model.UserToken;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author xmz
 * @date: 2020/11/22
 */
@Data
public class LoginParam {
    /**
     * 用户名
     */
    @NotBlank(message = "账号为空")
    private String username;
    /**
     * 密码
     */
    @NotBlank(message = "密码为空")
    private String password;
    /**
     * 验证码
     */
    @NotBlank(message = "验证码为空")
    private String code;

    /**
     * 类型 管理员，教师
     */
    private UserTypeEnum type;

}
