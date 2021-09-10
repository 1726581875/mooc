package cn.edu.lingnan.mooc.portal.authorize.service;

import cn.edu.lingnan.mooc.common.exception.MoocException;
import cn.edu.lingnan.mooc.common.exception.enums.ExceptionEnum;
import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.portal.authorize.constant.UserConstant;
import cn.edu.lingnan.mooc.portal.authorize.model.UserToken;
import cn.edu.lingnan.mooc.portal.authorize.model.dao.UserDAO;
import cn.edu.lingnan.mooc.portal.authorize.model.entity.MoocUser;
import cn.edu.lingnan.mooc.portal.authorize.model.entity.OnlineUser;
import cn.edu.lingnan.mooc.portal.authorize.model.enums.UserStatusEnum;
import cn.edu.lingnan.mooc.portal.authorize.model.param.LoginParam;
import cn.edu.lingnan.mooc.portal.authorize.model.param.RegisterParam;
import cn.edu.lingnan.mooc.portal.authorize.util.HttpServletUtil;
import cn.edu.lingnan.mooc.portal.authorize.util.RedisUtil;
import cn.edu.lingnan.mooc.portal.authorize.util.RsaUtil;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

/**
 * @author xmz
 * @date: 2021/02/08
 * 登录相关
 */
@Slf4j
@Service
public class ReceptionLoginService {

    @Value("${mooc.rsa.privateKey}")
    private String RSA_PRI_KEY;
    @Resource
    private UserDAO userDAO;
    @Value("${mooc.login.expire.time:1800}")
    private Integer LOGIN_EXPIRE_TIME;

    /**
     * 前台系统的登录方法
     *
     * @param loginParam
     * @param request
     * @return
     */
    public RespResult login(LoginParam loginParam, HttpServletRequest request) {

        MoocUser user = getUserByAccount(loginParam.getUsername());

        checkPassword(user, loginParam);

        //判断状态，1正常，2禁用，3已删除
        checkUserStatus(user.getStatus());

        setUserToken(user);

        return RespResult.success();
    }


    private void setUserToken(MoocUser user) {
        String token = UUID.randomUUID().toString();
        UserToken userToken = buildUserToken(user, token);
        // 设置账号与token关系
        RedisUtil.set(userToken.getAccount(), token, LOGIN_EXPIRE_TIME);
        // 设置token
        RedisUtil.set(token, userToken, LOGIN_EXPIRE_TIME);

        // 设置用户在线信息
        OnlineUser onlineUser = buildOnlineUser(user);
        RedisUtil.set(UserConstant.ONLINE_USER_PREFIX + "user-" + userToken.getAccount(), onlineUser, LOGIN_EXPIRE_TIME);
    }


    private UserToken buildUserToken(MoocUser user, String token) {
        UserToken userToken = new UserToken();
        userToken.setToken(token);
        userToken.setAccount(user.getAccount());
        userToken.setUserId(Long.valueOf(user.getId()));
        userToken.setType(2);
        RedisUtil.set(token, userToken, LOGIN_EXPIRE_TIME);
        return userToken;
    }

    private OnlineUser buildOnlineUser(MoocUser user) {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        OnlineUser onlineUser = new OnlineUser();
        onlineUser.setAccount("user-" + user.getAccount());
        onlineUser.setLoginTime(new Date());
        if (Objects.nonNull(attributes)) {
            ServletRequestAttributes requestAttributes = (ServletRequestAttributes) attributes;
            //设置ip
            onlineUser.setIp(HttpServletUtil.getIpAddress(requestAttributes.getRequest()));
        }

        onlineUser.setName(user.getName());
        return onlineUser;
    }


    private MoocUser getUserByAccount(String account) {
        MoocUser user = userDAO.findUserByAccount(account);
        if (user == null) {
            throw new MoocException("账号不存在");
        }
        return user;
    }

    private void checkUserStatus(Integer status) {
        if (!UserStatusEnum.NORMAL.getStatus().equals(status)) {
            throw new MoocException(10001, "账号" + UserStatusEnum.getText(status) + "，请联系管理员！！");
        }
    }

    private void checkPassword(MoocUser user, LoginParam loginParam) {
        if (user == null) {
            throw new MoocException("账号不存在");
        }
        String decryptPassword = "";
        try {
            decryptPassword = RsaUtil.decryptByPrivateKey(RSA_PRI_KEY, loginParam.getPassword());
        } catch (Exception e) {
            log.error("密码解密失败", e);
            throw new MoocException(ExceptionEnum.KNOWN_ERROR);
        }
        if (!BCrypt.checkpw(decryptPassword, user.getPassword())) {
            throw new MoocException("密码不正确");
        }
    }


    /**
     * 用户注册的方法
     *
     * @param registerParam
     * @return
     */
    public RespResult register(RegisterParam registerParam) {

        MoocUser user = userDAO.findUserByAccount(registerParam.getAccount());
        if (user != null) {
            return RespResult.fail("账号已存在");
        }
        String confirmPassword = null;
        String password = null;
        //密码进行解密，rsa算法使用私钥解密
        try {
            confirmPassword = RsaUtil.decryptByPrivateKey(RSA_PRI_KEY, registerParam.getConfirmPassword());
            password = RsaUtil.decryptByPrivateKey(RSA_PRI_KEY, registerParam.getPassword());
        } catch (Exception e) {
            log.info("密码解密失败", e);
            return RespResult.failUnKnownError();
        }
        //密码是否与确认密码相等
        if (!confirmPassword.equals(password)) {
            return RespResult.fail("两次输入密码不一致");
        }

        //插入用户信息
        MoocUser moocUser = this.createUser(registerParam, password);
        userDAO.save(moocUser);
        return RespResult.success();
    }


    private MoocUser createUser(RegisterParam registerParam, String password) {
        MoocUser moocUser = new MoocUser();
        moocUser.setAccount(registerParam.getAccount());
        Random random = new Random();
        if ("教师".equals(registerParam.getUserType())) {
            moocUser.setName("教师" + random.nextInt(123456));
        } else {
            moocUser.setName("用户" + random.nextInt(123456));
        }

        //如果是教师角色，需要插入用户状态为0，表示未审批
        if ("教师".equals(registerParam.getUserType())) {
            moocUser.setStatus(0);
        } else {
            moocUser.setStatus(1);
        }
        moocUser.setUserImage("/file/default.png");
        moocUser.setUserType(registerParam.getUserType());
        moocUser.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        return moocUser;
    }


}
