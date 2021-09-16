package cn.edu.lingnan.authorize.util;

import cn.edu.lingnan.mooc.common.exception.enums.UserTypeEnum;

/**
 * @author xmz
 * @date: 2020/11/25
 */
public class RedisKeyUtil {

    /**
     * 在线人员前缀
     */
    public final static String ONLINE_USER_PREFIX = "online:user:";
    /**
     * 用户token前缀
     */
    public final static String USER_TOKEN_PREFIX = "user:token:";
    /**
     * 管理员token前缀
     */
    public final static String MANAGER_TOKEN_PREFIX = "manager:token:";

    /**
     * redis账号key前缀
     */
    public final static String MANAGER_ACCOUNT_PREFIX = "manager:account:";

    /**
     * 前缀:类型:账号/token
     */
    public final static String KEY_TEMPLATE = "%s%s:%s";


    public static String getRedisAccountKey(UserTypeEnum type, String account) {
        return String.format(KEY_TEMPLATE, MANAGER_ACCOUNT_PREFIX, type, account);
    }

    public static String getRedisOnlineKey(UserTypeEnum type, String account){
        return String.format(KEY_TEMPLATE, ONLINE_USER_PREFIX, type, account);
    }

    public static String getRedisTokenKey(UserTypeEnum type, String token){
        return String.format(KEY_TEMPLATE, MANAGER_TOKEN_PREFIX, type, token);
    }

}
