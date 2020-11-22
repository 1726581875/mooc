package cn.edu.lingnan.mooc.service;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.dao.ManagerDAO;
import cn.edu.lingnan.mooc.dao.MenuTreeDAO;
import cn.edu.lingnan.mooc.dao.RoleDAO;
import cn.edu.lingnan.mooc.model.*;
import cn.edu.lingnan.mooc.util.RedisUtil;
import cn.edu.lingnan.mooc.util.RsaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author xmz
 * @date: 2020/11/22
 */
@Service
public class ManagerService {

    private Logger log = LoggerFactory.getLogger(ManagerService.class);

    @Value("${mooc.rsa.privateKey}")
    private String RSA_PRI_KEY;

    @Autowired
    private ManagerDAO managerDAO;
    @Autowired
    private RoleDAO roleDAO;
    @Autowired
    private MenuTreeDAO menuTreeDAO;


    public RespResult login(LoginParam loginParam,String sessionId){

        // 根据用户输入账号查询数据库
        MoocManager manager = managerDAO.findManagerByAccount(loginParam.getUsername());
        if(manager == null){
            return RespResult.fail("该账号不存在");
        }
        // 对输入密码进行解密，使用私钥解密
        String decryptPassword = "";
        try {
            decryptPassword = RsaUtil.decryptByPrivateKey(RSA_PRI_KEY,loginParam.getPassword());
        } catch (Exception e) {
            log.info("密码解密失败",e);
            return RespResult.failUnKnownError();
        }
        // 对数据库密码进行解密，并对比输入的密码
        // TODO 数据库密码需要加密存储，目前尚未加密，所以这里先不做解密操作
        if (!decryptPassword.equals(manager.getPassword())) {
            return RespResult.fail("密码不正确");
        }

        //查询管理员拥有的角色id
        List<Long> roleIdList = roleDAO.findAllRoleIdByManagerId(manager.getId());
        //查询对应的菜单权限列表
        List<MenuTree> menuPermissionList = menuTreeDAO.findMenuList(roleIdList);

        //获取该用户可以看到的菜单列表
        List<MenuTree> menuList = menuPermissionList.stream()
                .filter(e -> e.getRouter() != null && e.getRouter().equals("")).collect(Collectors.toList());

        // 拼接权限字符串
        String permissionStr = menuPermissionList.stream().map(MenuTree::getPermission).collect(Collectors.joining(","));

        // 登录信息存redis
        String token = UUID.randomUUID().toString();
        UserToken userToken = new UserToken();
        userToken.setUserId(manager.getId());
        userToken.setAccount(manager.getAccount());
        userToken.setToken(token);
        userToken.setPermission(permissionStr);
        userToken.setSessionId(sessionId);
        RedisUtil.set(token,userToken,1800);

        // 重新登录，把之前的token删除，设置当前token。（也防止两个人同时登录）
        String userId = manager.getId().toString();
        String oldToken = RedisUtil.get(userId);
        if(oldToken != null){
            RedisUtil.delete(oldToken);
        }
        RedisUtil.set(userId,token);

        // 构造登录成功返回对象
        LoginSuccessVO loginSuccessVO = new LoginSuccessVO(token,menuList);

        return RespResult.success(loginSuccessVO,"登录成功");
    }


}
