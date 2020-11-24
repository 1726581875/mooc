package cn.edu.lingnan.authorize.service;

import cn.edu.lingnan.authorize.dao.ManagerDAO;
import cn.edu.lingnan.authorize.dao.RoleDAO;
import cn.edu.lingnan.authorize.entity.*;
import cn.edu.lingnan.authorize.util.RedisUtil;
import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.authorize.dao.MenuTreeDAO;
import cn.edu.lingnan.mooc.entity.*;
import cn.edu.lingnan.authorize.util.RsaUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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

    @Value("${mooc.superAdmin.username:admin}")
    private String superAdminUsername;

    @Value("${mooc.superAdmin.password:root}")
    private String superAdminPassword;

    public RespResult login(LoginParam loginParam, String sessionId){

        MoocManager manager = new MoocManager();
        // 如果是超管
        if(superAdminUsername.equals(loginParam.getUsername())){
            manager.setAccount(superAdminUsername);
            manager.setId(0L);
            manager.setStatus(1);
            manager.setName(superAdminUsername);
            manager.setPassword(BCrypt.hashpw(superAdminPassword,BCrypt.gensalt()));
        }else {
            // 否则就是分管，根据输入账号查询数据库
            manager = managerDAO.findManagerByAccount(loginParam.getUsername());
            if (manager == null) {
                return RespResult.fail("账号不存在");
            }
        }

        // 密码进行解密，rsa算法使用私钥解密
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

        //获取菜单权限列表
        List<MenuTree> menuPermissionList = getMenuPermissionList(manager.getId());

        //获取该用户可以看到的菜单列表,set去重
        Set<MenuTree> menuList = menuPermissionList.stream()
                .filter(e -> e.getRouter() != null && !e.getRouter().equals("")).collect(Collectors.toSet());
        // 去重，拼接权限字符串
        String permissionStr = menuPermissionList.stream().collect(Collectors.toSet())
                .stream().map(MenuTree::getPermission).collect(Collectors.joining(","));

        //生成token,设置redis
        String token = UUID.randomUUID().toString();
        setRedisToken(manager,permissionStr,sessionId,token);

        // 构造登录成功返回对象
        LoginSuccessVO loginSuccessVO = new LoginSuccessVO(token,new ArrayList<>(menuList));

        return RespResult.success(loginSuccessVO,"登录成功");
    }

    /**
     *登录成功设置redis
     * 1、设置用户token和登录信息
     * 2、把前该账号旧token删除
     * @param manager
     * @param permissionStr
     * @param sessionId
     * @param token
     */
    private void setRedisToken(MoocManager manager,String permissionStr,String sessionId,String token){
        // 登录信息存redis
        UserToken userToken = new UserToken();
        userToken.setUserId(manager.getId());
        userToken.setAccount(manager.getAccount());
        userToken.setToken(token);
        userToken.setPermission(permissionStr);
        userToken.setSessionId(sessionId);
        RedisUtil.set(token,userToken,1800);

        // 重新登录，把之前的token删除，设置当前token。（也防止两个人同时登录）
        String account = manager.getAccount();
        String oldToken = RedisUtil.get(account);
        if(oldToken != null){
            RedisUtil.delete(oldToken);
        }
        RedisUtil.set(account,token);

    }

    private List<MenuTree> getMenuPermissionList(Long managerId) {

        List<MenuTree> menuPermissionList = new ArrayList<>(16);
        // 如果是超管，拥有所有权限
        if (managerId.equals(0L)) {
            menuPermissionList = menuTreeDAO.findAllMenuList();
        } else {
            //查询管理员拥有的角色id
            List<Long> roleIdList = roleDAO.findAllRoleIdByManagerId(managerId);
            //查询对应的菜单权限列表
            menuPermissionList = menuTreeDAO.findMenuList(roleIdList);
        }
        //去重
        return menuPermissionList.stream().collect(Collectors.toSet()).stream().collect(Collectors.toList());
    }



    public void insertManager(MoocManager manager){
        managerDAO.save(manager);

    }


}
