package cn.edu.lingnan.authorize.service;

import cn.edu.lingnan.authorize.constant.UserConstant;
import cn.edu.lingnan.authorize.dao.ManagerDAO;
import cn.edu.lingnan.authorize.dao.RoleDAO;
import cn.edu.lingnan.authorize.dao.UserDAO;
import cn.edu.lingnan.authorize.model.*;
import cn.edu.lingnan.authorize.param.LoginParam;
import cn.edu.lingnan.authorize.util.HttpServletUtil;
import cn.edu.lingnan.authorize.util.RedisUtil;
import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.authorize.dao.MenuTreeDAO;
import cn.edu.lingnan.authorize.util.RsaUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xmz
 * @date: 2020/11/22
 */
@Service
public class AuthorizeService {

    private Logger log = LoggerFactory.getLogger(AuthorizeService.class);

    @Value("${mooc.rsa.privateKey}")
    private String RSA_PRI_KEY;
    @Value("${mooc.login.expire.time:1800}")
    private Integer LOGIN_EXPIRE_TIME;

    @Autowired
    private ManagerDAO managerDAO;
    @Autowired
    private RoleDAO roleDAO;
    @Autowired
    private MenuTreeDAO menuTreeDAO;
    @Autowired
    private MenuTreeService menuTreeService;
    @Autowired
    private UserDAO userDAO;

    @Value("${mooc.superAdmin.username:admin}")
    private String superAdminUsername;

    @Value("${mooc.superAdmin.password:root}")
    private String superAdminPassword;

    public RespResult login(LoginParam loginParam, HttpServletRequest request){

        MoocManager manager = new MoocManager();
        // 如果是超管
        if(superAdminUsername.equals(loginParam.getUsername())){
            manager = this.createSuperManager();
        }else {
            // 否则就是分管，根据输入账号查询数据库
            manager = managerDAO.findManagerByAccount(loginParam.getUsername());
            //todo 管理员和教师分别在不同的两张表，如果教师账号和管理员账号相同，则教师登录不了

            // 如果管理员表没有(则可能是教师，查询用户表)
            if (manager == null) {
                MoocUser moocUser = userDAO.findUserByAccount(loginParam.getUsername());
                if(moocUser == null){
                    return RespResult.fail("账号不存在");
                }
                //类型是教师才授权
                if("教师".equals(moocUser.getUserType())){
                    manager = createTeacherManager(moocUser);
                }else {
                    return RespResult.fail("账号不存在");
                }
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

        //判断状态，1正常，2禁用，3已删除
        Integer status = manager.getStatus();
        if(status == 2){
            return RespResult.fail(10001,"你的账号已经被禁用，请联系管理员！！");
        }else if(status == 3){
            return RespResult.fail(10001,"你的账号已经被删除，请联系管理员！！");
        }else if(status == 0){
            return RespResult.fail(10001,"你的账号已经还没审核通过！！");
        }


        //todo 写法冗余，需要去优化
        //如果是教师
        if(manager.getAccount().startsWith("teacher-")) {
            List<Long> teacherRoleId = new ArrayList();
            teacherRoleId.add(1L);
            List<MenuTree> menuList = menuTreeDAO.findMenuList(teacherRoleId);
            List<MenuTree> teacherMenuList = new ArrayList<>(menuList.stream().collect(Collectors.toSet()));
            // 拼接权限字符串
            String permissionStr = teacherMenuList.stream().map(MenuTree::getPermission).collect(Collectors.joining(","));
            //生成token,设置redis
            String token = UUID.randomUUID().toString();
            //教师类型
            Integer teacherType = 2;
            setRedisTokenOnline(manager,teacherType, permissionStr,request,token);

            // 构造登录成功返回对象,教师type=2
            LoginSuccessVO loginSuccessVO = new LoginSuccessVO(token,teacherType, manager.getId().intValue(), menuTreeService.getTeacherMenuTree());

            //更新最近登录时间
            userDAO.updateLoginTime(manager.getId().intValue(),new Date());
            return RespResult.success(loginSuccessVO,"登录成功");
        }

       // 拼接权限字符串
        String permissionStr = menuTreeService.getPermission(manager.getId())
                .stream().map(MenuTree::getPermission).collect(Collectors.joining(","));

        //生成token,设置redis
        String token = UUID.randomUUID().toString();
        //管理员类型
        Integer managerType = 1;
        setRedisTokenOnline(manager,managerType, permissionStr,request,token);

        // 构造登录成功返回对象,管理员type=1
        LoginSuccessVO loginSuccessVO = new LoginSuccessVO(token,1,manager.getId().intValue(),menuTreeService.getMenuTree(manager.getId()));

        //更新最近登录时间
        managerDAO.updateLoginTime(manager.getId().intValue(),new Date());
        return RespResult.success(loginSuccessVO,"登录成功");
    }

    /**
     * 构造一个超管信息
     * @return
     */
    private MoocManager createSuperManager(){
        MoocManager manager = new MoocManager();
        manager.setAccount(superAdminUsername);
        manager.setId(0L);
        manager.setStatus(1);
        manager.setName(superAdminUsername);
        manager.setPassword(BCrypt.hashpw(superAdminPassword,BCrypt.gensalt()));
        return manager;
    }

    /**
     * 创建一个教师管理员
     * 为了做区分，教师账号以teacher-前缀开头
     * @param user
     * @return
     */
    private MoocManager createTeacherManager(MoocUser user){
        MoocManager manager = new MoocManager();
        manager.setAccount("teacher-" + user.getAccount());
        manager.setId(Long.valueOf(user.getId()));
        manager.setStatus(user.getStatus());
        manager.setName(user.getName());
        manager.setPassword(user.getPassword());
        return manager;
    }


    /**
     *登录成功设置redis
     * 1、设置用户token和登录信息
     * 2、把前该账号旧token删除
     * redis存储的k、v
     * token-userDetail
     * account-token
     *
     * @param manager
     * @param permissionStr
     * @param request
     * @param token
     */
    private void setRedisTokenOnline(MoocManager manager, Integer type, String permissionStr, HttpServletRequest request, String token){
        // 登录信息存redis
        UserToken userToken = new UserToken();
        userToken.setUserId(manager.getId());
        userToken.setAccount(manager.getAccount());
        userToken.setToken(token);
        userToken.setPermission(permissionStr);
        userToken.setSessionId(request.getSession().getId());
        userToken.setType(type);
        //设置用户UserToken（用户登录基本信息）
        RedisUtil.set(token,userToken,LOGIN_EXPIRE_TIME);

        // 重新登录，把之前的token删除，设置当前token。（防止两个人同时登录）
        String account = manager.getAccount();
        String oldToken = RedisUtil.get(account);
        if(oldToken != null){
            RedisUtil.delete(oldToken);
        }
        // 账户和token关联
        RedisUtil.set(account,token,LOGIN_EXPIRE_TIME);

        // 非超管记录在线信息
        if(!manager.getId().equals(0L)) {
            // 记录在线用户
            OnlineUser onlineUser = new OnlineUser();
            onlineUser.setAccount(manager.getAccount());
            onlineUser.setLoginTime(new Date());
            onlineUser.setIp(HttpServletUtil.getIpAddress(request)
                    .equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : HttpServletUtil.getIpAddress(request));
            onlineUser.setName(manager.getName());
            RedisUtil.set(UserConstant.ONLINE_USER_PREFIX + account, onlineUser, LOGIN_EXPIRE_TIME);
        }

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
