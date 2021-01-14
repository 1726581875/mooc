package cn.edu.lingnan.authorize.shiro.oauth2;

import org.apache.shiro.authc.AuthenticationToken;

public class UserToken  implements AuthenticationToken {

    private String token;

    public UserToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return this.token;
    }

    @Override
    public Object getCredentials() {
        return this.token;
    }
}
