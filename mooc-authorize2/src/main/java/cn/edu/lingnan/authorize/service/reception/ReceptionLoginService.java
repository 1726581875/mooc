package cn.edu.lingnan.authorize.service.reception;

import cn.edu.lingnan.authorize.constant.UserConstant;
import cn.edu.lingnan.authorize.dao.ManagerDAO;
import cn.edu.lingnan.authorize.dao.UserDAO;
import cn.edu.lingnan.authorize.model.MoocUser;
import cn.edu.lingnan.authorize.param.LoginParam;
import cn.edu.lingnan.authorize.model.MoocManager;
import cn.edu.lingnan.authorize.model.OnlineUser;
import cn.edu.lingnan.authorize.model.UserToken;
import cn.edu.lingnan.authorize.param.RegisterParam;
import cn.edu.lingnan.authorize.util.HttpServletUtil;
import cn.edu.lingnan.authorize.util.RedisUtil;
import cn.edu.lingnan.authorize.util.RsaUtil;
import cn.edu.lingnan.mooc.common.model.RespResult;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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
     * @param loginParam
     * @param request
     * @return
     */
    public RespResult login(LoginParam loginParam, HttpServletRequest request){

        MoocUser user =userDAO.findUserByAccount(loginParam.getUsername());
        if (user == null) {
            return RespResult.fail("账号不存在");
        }
        //密码进行解密，rsa算法使用私钥解密
        String decryptPassword = "";
        try {
            decryptPassword = RsaUtil.decryptByPrivateKey(RSA_PRI_KEY,loginParam.getPassword());
        } catch (Exception e) {
            log.info("密码解密失败",e);
            return RespResult.failUnKnownError();
        }
        // 数据库密码对比输入的密码
        if(!BCrypt.checkpw(decryptPassword,user.getPassword())){
            return RespResult.fail("密码不正确");
        }

        //生成token,设置redis
        String token = UUID.randomUUID().toString();
        UserToken userToken = new UserToken();
        userToken.setToken(token);
        userToken.setAccount(user.getAccount());
        userToken.setUserId(Long.valueOf(user.getId()));
        userToken.setType(1);
        userToken.setSessionId(request.getSession().getId());
        RedisUtil.set(token,userToken, LOGIN_EXPIRE_TIME);
        //设置账号与token关系
        RedisUtil.set(userToken.getAccount(),token, LOGIN_EXPIRE_TIME);
        // 记录在线用户
        OnlineUser onlineUser = new OnlineUser();
        onlineUser.setAccount("user-" + user.getAccount());
        onlineUser.setLoginTime(new Date());
        onlineUser.setIp(HttpServletUtil.getIpAddress(request)
                .equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : HttpServletUtil.getIpAddress(request));
        onlineUser.setName(user.getName());
        RedisUtil.set(UserConstant.ONLINE_USER_PREFIX + "user-" + userToken.getAccount(), onlineUser, LOGIN_EXPIRE_TIME);

        return RespResult.success(userToken);
    }


    /**
     * 用户注册的方法
     * @param registerParam
     * @return
     */
    public RespResult register(RegisterParam registerParam){

        MoocUser user =userDAO.findUserByAccount(registerParam.getAccount());
        if (user != null) {
            return RespResult.fail("账号已存在");
        }
        String confirmPassword = null;
        String password = null;
        //密码进行解密，rsa算法使用私钥解密
        try {
            confirmPassword = RsaUtil.decryptByPrivateKey(RSA_PRI_KEY,registerParam.getConfirmPassword());
            password = RsaUtil.decryptByPrivateKey(RSA_PRI_KEY,registerParam.getPassword());
        } catch (Exception e) {
            log.info("密码解密失败",e);
            return RespResult.failUnKnownError();
        }
        //密码是否与确认密码相等
        if(!confirmPassword.equals(password)){
            return RespResult.fail("两次输入密码不一致");
        }

        //插入用户信息
        MoocUser moocUser = this.createUser(registerParam, password);
        userDAO.save(moocUser);
        return RespResult.success();
    }


    private MoocUser createUser(RegisterParam registerParam,String password){
        MoocUser moocUser = new MoocUser();
        moocUser.setAccount(registerParam.getAccount());
        Random random = new Random();
        if("教师".equals(registerParam.getUserType())){
            moocUser.setName("教师" + random.nextInt(123456));
        }else {

            moocUser.setName("用户" + random.nextInt(123456));
        }

        //如果是教师角色，需要插入用户状态为0，表示未审批
        if("教师".equals(registerParam.getUserType())){
            moocUser.setStatus(0);
        }else {
            moocUser.setStatus(1);
        }
        moocUser.setUserImage("/file/default.png");
        moocUser.setUserType(registerParam.getUserType());
        moocUser.setPassword(BCrypt.hashpw(password,BCrypt.gensalt()));
        return moocUser;
    }


}
