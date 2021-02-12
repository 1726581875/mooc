package cn.edu.lingnan.authorize.service;

import cn.edu.lingnan.authorize.authentication.util.UserUtil;
import cn.edu.lingnan.authorize.dao.ManagerDAO;
import cn.edu.lingnan.authorize.dao.UserDAO;
import cn.edu.lingnan.authorize.model.MoocManager;
import cn.edu.lingnan.authorize.model.MoocUser;
import cn.edu.lingnan.authorize.param.PasswordParam;
import cn.edu.lingnan.authorize.util.RsaUtil;
import cn.edu.lingnan.mooc.common.model.RespResult;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author xmz
 * @date: 2020/11/25
 */
@Service
public class ManagerService {

    private static final Logger log = LoggerFactory.getLogger(ManagerService.class);
    @Autowired
    private ManagerDAO managerDAO;
    @Resource
    private UserDAO userDAO;
    @Value("${mooc.rsa.privateKey}")
    private String RSA_PRI_KEY;

    /**
     * 管理员（分管本人）修改密码
     * @param passwordParam
     * @return
     */
    public RespResult updatePassword(PasswordParam passwordParam){
        //获取id，根据id查询管理员
        Integer managerId = UserUtil.getUserId();
        MoocManager manager = managerDAO.findById(managerId);
        if(manager == null){
            log.error("该用户不存在,managerId={}",managerId);
          return RespResult.fail("修改密码失败，该用户不存在");
        }
        //获取解密后的密码原文（密码使用rsa算法加密）
        String oldPassword = null;
        String newPasswprd = null;
        String comfirmPassword = null;
        try {
             oldPassword = RsaUtil.decryptByPrivateKey(RSA_PRI_KEY, passwordParam.getOldPassword());
             newPasswprd = RsaUtil.decryptByPrivateKey(RSA_PRI_KEY, passwordParam.getNewPassword());
             comfirmPassword = RsaUtil.decryptByPrivateKey(RSA_PRI_KEY, passwordParam.getConfirmPassword());
        } catch (Exception e) {
            log.info("修改密码，解密rsa失败",e);
           return RespResult.failUnKnownError();
        }
        //检查输入的旧密码是否正确
        if(!BCrypt.checkpw(oldPassword, manager.getPassword())){
            RespResult.fail("旧密码不正确");
        }
        //对比新密码与确认密码
        if(!newPasswprd.equals(comfirmPassword)){
            return RespResult.fail("新密码与确认密码不对应");
        }

        //设置新密码，密码加密
        manager.setPassword(BCrypt.hashpw(newPasswprd,BCrypt.gensalt()));
        managerDAO.save(manager);

        return RespResult.success();
    }

    /**
     * 超管或有管理用户、管理员管理权限的管理员，重新设置用户密码。
     * 用户忘记密码联系管理员，管理员进行的操作
     * @param userId 用户id
     * @param userType 1是管理员，2是用户
     * @return
     */
    public RespResult randomNewPassword(Integer userId,Integer userType){

        String newPassword = UUID.randomUUID().toString();
        log.info("随机新密码：{}" ,newPassword);
        if(userType == 1){
            MoocManager manager = managerDAO.findById(userId);
            manager.setPassword(BCrypt.hashpw(newPassword,BCrypt.gensalt()));
            managerDAO.save(manager);
        }else {
            MoocUser user = userDAO.findUserById(userId);
            user.setPassword(BCrypt.hashpw(newPassword,BCrypt.gensalt()));
            userDAO.save(user);
        }
        //使用rsa算法加密返回给前端
        String encryptPassword = null;
        try {
             encryptPassword = RsaUtil.encryptByPrivateKey(RSA_PRI_KEY, newPassword);
        } catch (Exception e) {
            log.error("rsa算法加密重置密码失败",e);
            return RespResult.failUnKnownError();
        }
        return RespResult.success(encryptPassword,"重置密码成功");
    }



}
