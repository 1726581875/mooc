package cn.edu.lingnan.core.authentication.util;

import cn.edu.lingnan.core.authentication.entity.UserToken;

import java.util.UUID;

/**
 * @author xmz
 * @date: 2021/04/12
 */
public class UserTokenUtil {

    /**
     * 创建一个临时token
     * @return
     */
    public static String createToken(){
        String token = UUID.randomUUID().toString();
        UserToken userToken = new UserToken();
        userToken.setUserId(0L);
        userToken.setAccount("admin");
        userToken.setToken(token);
        userToken.setPermission(null);
        userToken.setSessionId(null);
        userToken.setType(1);
        //设置60秒
        RedisUtil.set(token,userToken,60);
        return token;
    }
}
