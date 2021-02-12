package cn.edu.lingnan.authorize.param;

import lombok.Data;

/**
 * @author xmz
 * @date: 2021/02/11
 */
@Data
public class PasswordParam {

    /**
     * 旧密码
     */
    private String oldPassword;
    /**
     * 新密码
     */
    private String newPassword;
    /**
     * 确认密码
     */
    private String confirmPassword;


}
