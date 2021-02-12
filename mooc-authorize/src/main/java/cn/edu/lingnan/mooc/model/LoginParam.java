package cn.edu.lingnan.mooc.model;

import lombok.Data;
import lombok.ToString;

/**
 * @author xmz
 * @date: 2020/11/22
 */
@Data
@ToString
public class LoginParam {
    //用户名
    private String username;
    //密码
    private String password;
    //验证，码
    private String verifyCode;


}
