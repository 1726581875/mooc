package cn.edu.lingnan.mooc.authorize.shiro.oauth2;

import cn.edu.lingnan.mooc.authorize.shiro.entity.UserToken;
import cn.edu.lingnan.mooc.authorize.shiro.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.Set;

/**
 * @author xmz
 * @date: 2020/11/23
 * 认证
 */
@Component
@Primary
@Slf4j
public class OAuth2Realm extends AuthorizingRealm {

    @Autowired
    DataSource dataSource;

    /**
     * 在redis存储sessionId和对应的token，使sessionId与token绑定
     */
    private static final String WEB_SESSION_ID_TOKEN = "logAuditWeb:sessionId:token:";

    @Value("${do1.token.expireTime:43200}")
    private static final long WEB_SESSION_ID_EXPIRE_TIME = 43200;

    @Override
    public boolean supports(AuthenticationToken token) {

       // return token instanceof OAuth2Token;
        return true;
    }

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        UserToken user = (UserToken) principals.getPrimaryPrincipal();
        Long userId = user.getUserId();
        //用户权限列表
/*        Set<String> permsSet = permissionsRedis.get(userId);*/

        Set<String> permsSet = new HashSet<>();
        permsSet.add("list");
        permsSet.add("test");

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String accessToken = (String) token.getPrincipal();
        UserToken userToken = RedisUtil.get(accessToken,UserToken.class);
        if (null == userToken) {
            throw new RuntimeException("token失效，请重新登录");
        }

      /*
        // 判断token是否与sessionId对应
        Serializable sessionId = SecurityUtils.getSubject().getSession().getId();
        String redisAccessToken = redisUtil.get(WEB_SESSION_ID_TOKEN + sessionId);
        if (StringUtils.isBlank(redisAccessToken) || !redisAccessToken.equals(accessToken)) {
            throw new RestException(-401, "token无效");
        }
      */

/*        long intervalTime = System.currentTimeMillis() - tokenEntity.getUpdateTime().getTime();
        if (intervalTime > 1800000) {
            tokenEntity.setUpdateTime(new Date());
            tokenEntity.setExpireTime(new Date(tokenEntity.getExpireTime().getTime() + intervalTime));
            dsfTokenRedis.saveOrUpdate(tokenEntity);
        }*/
        //查询用户信息


/*        //帐号锁定
        if ("0".equals(user.getStatus())) {
            throw new LockedAccountException("帐号已被锁定,请联系管理员");
        }*/

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(userToken, accessToken, getName());
        return info;
    }
}
