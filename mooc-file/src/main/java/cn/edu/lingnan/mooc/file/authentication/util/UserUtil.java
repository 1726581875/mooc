package cn.edu.lingnan.mooc.file.authentication.util;


import cn.edu.lingnan.mooc.file.authentication.entity.UserToken;

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


    /**
     * 使用完记得remove,防止内存泄露
     */
    public static void remove(){
        user.remove();
    }

}
