package cn.edu.lingnan.mooc.common.util;


import cn.edu.lingnan.mooc.common.model.UserToken;

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
     * 获取用户id
     * todo 因为很多地方都用了Integer类型，所以干脆做个适配
     * @return
     */
    public static Integer getUserId(){
        return user.get().getUserId().intValue();
    }

    /**
     * 使用完记得remove,防止内存泄露
     */
    public static void remove(){
        user.remove();
    }

}
