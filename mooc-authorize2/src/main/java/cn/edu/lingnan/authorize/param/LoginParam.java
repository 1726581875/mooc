package cn.edu.lingnan.authorize.param;


import javax.validation.constraints.NotBlank;

/**
 * @author xmz
 * @date: 2020/11/22
 */
public class LoginParam {
    //用户名
    @NotBlank(message = "账号为空")
    private String username;
    //密码
    @NotBlank(message = "密码为空")
    private String password;
    //验证码
    @NotBlank(message = "验证码为空")
    private String code;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "LoginParam{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
