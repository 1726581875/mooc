package cn.edu.lingnan.mooc.common.util;


import cn.edu.lingnan.mooc.common.enums.ExceptionEnum;
import cn.edu.lingnan.mooc.common.enums.UserTypeEnum;
import cn.edu.lingnan.mooc.common.exception.MoocException;
import cn.edu.lingnan.mooc.common.model.LoginUser;

/**
 * @author xmz
 * @date: 2021/02/07
 */
public class UserUtil {

    // ThreadLocal新建子线程该上下文内容不会被继承
    //private static ThreadLocal<LoginUser> user = new ThreadLocal<>();
    /**
     * InheritableThreadLocal 里存储的值会在新建子线程的时候被继承
     */
    private static ThreadLocal<LoginUser> user = new InheritableThreadLocal<>();


    public static void setUserToken(LoginUser userToken) {
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

    public static boolean isTeacher(){
        return UserTypeEnum.TEACHER.equals(getLoginUser().getType());
    }

    public static Long getUserId() {
        LoginUser loginUser = getLoginUser();
        return loginUser.getUserId();
    }

    /**
     * 使用完记得remove,防止内存泄露
     */
    public static void remove(){
        user.remove();
    }

}
