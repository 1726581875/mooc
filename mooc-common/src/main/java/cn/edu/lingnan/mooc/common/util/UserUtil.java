package cn.edu.lingnan.mooc.common.util;


import cn.edu.lingnan.mooc.common.enums.ExceptionEnum;
import cn.edu.lingnan.mooc.common.exception.MoocException;
import cn.edu.lingnan.mooc.common.model.LoginUser;

/**
 * @author xmz
 * @date: 2021/02/07
 */
public class UserUtil {

    private static ThreadLocal<LoginUser> user = new ThreadLocal<>();


    public static void setUserToken(LoginUser userToken){
        user.set(userToken);
    }


    /**
     * 获取用户信息
     * @return
     */
    public static LoginUser getLoginUser(){
        LoginUser loginUser = user.get();
        if(loginUser == null){
            throw new MoocException(ExceptionEnum.UNAUTHORIZED_ERROR);
        }
        return loginUser;
    }

    /**
     * 获取用户id
     * @return
     */
    public static Long getUserId() {
        LoginUser userToken = getLoginUser();
        return userToken.getUserId();
    }

    /**
     * 使用完记得remove,防止内存泄露
     */
    public static void remove(){
        user.remove();
    }

}
