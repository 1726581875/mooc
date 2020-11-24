package cn.edu.lingnan.authorize.entity;

import java.util.List;

/**
 * @author xmz
 * @date: 2020/11/23
 */
public class LoginSuccessVO {

    private String token;

    private List<MenuTree> menuList;

    public LoginSuccessVO() {}

    public LoginSuccessVO(String token, List<MenuTree> menuList) {
        this.token = token;
        this.menuList = menuList;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<MenuTree> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<MenuTree> menuList) {
        this.menuList = menuList;
    }

    @Override
    public String toString() {
        return "LoginSuccessVO{" +
                "token='" + token + '\'' +
                ", menuList=" + menuList +
                '}';
    }
}
