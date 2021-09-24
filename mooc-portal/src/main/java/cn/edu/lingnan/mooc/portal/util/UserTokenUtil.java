package cn.edu.lingnan.mooc.portal.util;

import cn.edu.lingnan.mooc.common.enums.UserTypeEnum;
import cn.edu.lingnan.mooc.common.model.LoginUser;
import cn.edu.lingnan.mooc.common.util.RedisUtil;

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
        LoginUser userToken = new LoginUser();
        userToken.setUserId(0L);
        userToken.setAccount("admin");
        userToken.setToken(token);
        userToken.setPermission(null);
        userToken.setSessionId(null);
        userToken.setType(UserTypeEnum.MANAGER);
        //设置60秒
        RedisUtil.set(token,userToken,60);
        return token;
    }
}
