package cn.edu.lingnan.core.authentication.util;

import cn.edu.lingnan.core.authentication.entity.UserToken;

/**
 * @author xmz
 * @date: 2021/02/07
 */
public class UserUtil {

    private static ThreadLocal<UserToken> user = new ThreadLocal<>();


    public static void setUserToken(UserToken userToken){
        user.set(userToken);
    }


    /**
     * 获取用户信息
     * @return
     */
    public static UserToken getUserToken(){
        return user.get();
    }


}
