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

    /**
     * 获取用户id
     * todo 因为很多地方都用了Integer类型，所以干脆做个适配
     * @return
     */
    public static Integer getUserId(){
        return user.get().getUserId().intValue();
    }

    public static boolean isTeacher(){
        UserToken userToken = user.get();
        //防止空子针报错，直接返回false
        if(userToken == null){
            return false;
        }
        if(userToken.getAccount().startsWith("teacher-")){
            return true;
        }

        return false;
    }


    /**
     * 使用完记得remove,防止内存泄露
     */
    public static void remove(){
        user.remove();
    }

}
