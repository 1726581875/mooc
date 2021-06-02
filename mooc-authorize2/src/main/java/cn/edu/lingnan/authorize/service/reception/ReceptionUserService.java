package cn.edu.lingnan.authorize.service.reception;

import cn.edu.lingnan.authorize.authentication.util.UserUtil;
import cn.edu.lingnan.authorize.dao.UserDAO;
import cn.edu.lingnan.authorize.model.entity.MoocUser;
import cn.edu.lingnan.authorize.model.param.PasswordParam;
import cn.edu.lingnan.authorize.util.RsaUtil;
import cn.edu.lingnan.mooc.common.model.RespResult;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author xmz
 * @date: 2021/02/12
 */
@Service
@Slf4j
public class ReceptionUserService {

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
        Integer userId = UserUtil.getUserId();
        MoocUser user = userDAO.findUserById(userId);
        if(user == null){
            log.error("该用户不存在,managerId={}",userId);
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
        if(!BCrypt.checkpw(oldPassword, user.getPassword())){
            RespResult.fail("旧密码不正确");
        }
        //对比新密码与确认密码
        if(!newPasswprd.equals(comfirmPassword)){
            return RespResult.fail("新密码与确认密码不对应");
        }

        //设置新密码，密码加密
        user.setPassword(BCrypt.hashpw(newPasswprd,BCrypt.gensalt()));
        userDAO.save(user);

        return RespResult.success();
    }


}
