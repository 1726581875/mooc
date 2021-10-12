package cn.edu.lingnan.mooc.portal.authorize.model.param;
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

}
