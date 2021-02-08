package cn.edu.lingnan.authorize.service.reception;

import cn.edu.lingnan.authorize.dao.ManagerDAO;
import cn.edu.lingnan.authorize.entity.LoginParam;
import cn.edu.lingnan.authorize.entity.MoocManager;
import cn.edu.lingnan.authorize.entity.UserToken;
import cn.edu.lingnan.authorize.util.RedisUtil;
import cn.edu.lingnan.authorize.util.RsaUtil;
import cn.edu.lingnan.mooc.common.model.RespResult;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    private ManagerDAO managerDAO;

    /**
     * 前台系统的登录方法
     * @param loginParam
     * @param request
     * @return
     */
    public RespResult login(LoginParam loginParam, HttpServletRequest request){

        MoocManager manager = managerDAO.findManagerByAccount(loginParam.getUsername());
        if (manager == null) {
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
        if(!BCrypt.checkpw(decryptPassword,manager.getPassword())){
            return RespResult.fail("密码不正确");
        }

        //生成token,设置redis
        String token = UUID.randomUUID().toString();
        UserToken userToken = new UserToken();
        userToken.setToken(token);
        userToken.setAccount(manager.getAccount());
        userToken.setUserId(manager.getId());
        userToken.setSessionId(request.getSession().getId());
        RedisUtil.set(token,userToken);

        return RespResult.success(userToken);
    }



}
