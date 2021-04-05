package cn.edu.lingnan.mooc.statistics.authentication.util;

import cn.edu.lingnan.mooc.statistics.authentication.entity.UserToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author xmz
 * @date: 2021/02/07
 */
public class UserUtil {

    private static ThreadLocal<UserToken> user = new ThreadLocal<>();

    private static final Logger log = LoggerFactory.getLogger(UserUtil.class);

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
        UserToken userToken = user.get();
        if(userToken == null){
            log.error("==== 用户还没有登录，拿不到用户id ====");
            return null;
        }
        return userToken.getUserId().intValue();
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
