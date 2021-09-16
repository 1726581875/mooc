package cn.edu.lingnan.mooc.portal.authorize.service;

import cn.edu.lingnan.mooc.common.exception.MoocException;
import cn.edu.lingnan.mooc.common.exception.enums.ExceptionEnum;
import cn.edu.lingnan.mooc.common.exception.enums.UserTypeEnum;
import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.common.model.UserToken;
import cn.edu.lingnan.mooc.common.util.RedisUtil;
import cn.edu.lingnan.mooc.portal.authorize.constant.UserConstant;
import cn.edu.lingnan.mooc.portal.authorize.model.entity.MoocUser;
import cn.edu.lingnan.mooc.portal.authorize.model.entity.OnlineUser;
import cn.edu.lingnan.mooc.portal.authorize.model.enums.UserStatusEnum;
import cn.edu.lingnan.mooc.portal.authorize.model.param.LoginParam;
import cn.edu.lingnan.mooc.portal.authorize.model.param.RegisterParam;
import cn.edu.lingnan.mooc.portal.authorize.util.HttpServletUtil;
import cn.edu.lingnan.mooc.portal.authorize.util.RsaUtil;
import cn.edu.lingnan.mooc.portal.dao.MoocUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

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
    private MoocUserRepository moocUserRepository;

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
        userToken.setType(UserTypeEnum.USER);
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
        Optional<MoocUser> userOptional = moocUserRepository.findOne(Example.of(new MoocUser().setAccount(account)));
        if (!userOptional.isPresent()) {
            throw new MoocException("账号不存在");
        }
        return userOptional.get();
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
        String decryptPassword = getDecryptString(loginParam.getPassword());
        if (!BCrypt.checkpw(decryptPassword, user.getPassword())) {
            throw new MoocException("密码不正确");
        }
    }

    private String getDecryptString(String str){
        try {
            return RsaUtil.decryptByPrivateKey(RSA_PRI_KEY, str);
        } catch (Exception e) {
            log.error("解密失败, str={}, key={}",str,RSA_PRI_KEY, e);
            throw new MoocException(ExceptionEnum.KNOWN_ERROR);
        }
    }


    /**
     * 用户注册的方法
     *
     * @param registerParam
     * @return
     */
    public void register(RegisterParam registerParam) {

        Optional<MoocUser> userOptional = moocUserRepository.findOne(Example.of(new MoocUser().setAccount(registerParam.getAccount())));
        if (userOptional.isPresent()) {
            throw new MoocException("账号已存在");
        }
        String confirmPassword = getDecryptString(registerParam.getConfirmPassword());
        String password = getDecryptString(registerParam.getPassword());
        //密码是否与确认密码相等
        if (!confirmPassword.equals(password)) {
            throw new MoocException("两次输入密码不一致");
        }
        //插入用户信息
        MoocUser moocUser = this.createUser(registerParam, password);
        moocUserRepository.save(moocUser);
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

    /**
     * 登出
     * @param account
     */
    public void delRedisTokenOnline(String account){
        // 删除用户在线息
        RedisUtil.delete(UserConstant.ONLINE_USER_PREFIX + account);
        String token = RedisUtil.get(account);
        if(token != null){
            // 删除用户token, 登录信息
            RedisUtil.delete(token);
        }
        // 删除账户、token关联
        RedisUtil.delete(account);
    }


}
