package cn.edu.lingnan.authorize.service;

import cn.edu.lingnan.authorize.constant.UserConstant;
import cn.edu.lingnan.authorize.dao.ManagerDAO;
import cn.edu.lingnan.authorize.dao.RoleDAO;
import cn.edu.lingnan.authorize.dao.UserDAO;
import cn.edu.lingnan.authorize.model.entity.MenuTree;
import cn.edu.lingnan.authorize.model.entity.MoocManager;
import cn.edu.lingnan.authorize.model.entity.MoocUser;
import cn.edu.lingnan.authorize.model.entity.OnlineUser;
import cn.edu.lingnan.authorize.model.enums.AuthorizeExceptionEnum;
import cn.edu.lingnan.authorize.model.enums.ManagerStatusEnum;
import cn.edu.lingnan.authorize.model.vo.LoginSuccessVO;
import cn.edu.lingnan.authorize.model.param.LoginParam;
import cn.edu.lingnan.authorize.util.HttpServletUtil;
import cn.edu.lingnan.authorize.util.RedisUtil;
import cn.edu.lingnan.mooc.common.exception.MoocException;
import cn.edu.lingnan.mooc.common.exception.enums.ExceptionEnum;
import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.authorize.dao.MenuTreeDAO;
import cn.edu.lingnan.authorize.util.RsaUtil;
import cn.edu.lingnan.mooc.common.model.UserToken;
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

    public LoginSuccessVO login(LoginParam loginParam, HttpServletRequest request){

        // check verification code
        checkVerificationCode(request.getSession().getId(), loginParam.getCode());

        // get manager
        MoocManager manager = getMoocManager(loginParam.getUsername(),loginParam.getType());
        if(manager == null){
            throw new MoocException(AuthorizeExceptionEnum.ACCOUNT_NOT_EXIST);
        }
        // check password
        checkPassword(manager.getPassword(), loginParam.getPassword());

        checkStatus(manager.getStatus());

        //
        LoginSuccessVO loginSuccessVO = buildLoginSuccessVO(manager, request);

        return loginSuccessVO;
    }


    private LoginSuccessVO buildLoginSuccessVO(MoocManager manager, HttpServletRequest request) {
        if(manager.getAccount().startsWith("teacher-")) {
            // 教师权限绑定教师角色 id为1
            List<MenuTree> menuList = menuTreeDAO.findMenuList(Arrays.asList(1L));
            List<MenuTree> teacherMenuList = new ArrayList<>(menuList.stream().collect(Collectors.toSet()));
            // 拼接权限字符串
            String permissionStr = teacherMenuList.stream().map(MenuTree::getPermission).collect(Collectors.joining(","));

            String token = UUID.randomUUID().toString();
            //教师类型
            Integer teacherType = 2;
            setRedisTokenOnline(manager,teacherType, permissionStr, request, token);

            // 构造登录成功返回对象,教师type=2
            LoginSuccessVO loginSuccessVO = new LoginSuccessVO(token,teacherType, manager.getId().intValue(), menuTreeService.getTeacherMenuTree());

            //更新最近登录时间
            userDAO.updateLoginTime(manager.getId().intValue(),new Date());
            return loginSuccessVO;
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
        return loginSuccessVO;
    }



    private void checkStatus(Integer status) {
        // 判断状态，1正常，2禁用，3已删除
        if(ManagerStatusEnum.NORMAL.equals(status)){
            throw new MoocException(10001,"你的账号不可用，请联系管理员！！");
        }
    }

    /**
     *
     * @param password 数据库密码，BCrypt加密
     * @param encryptPassword 输入密码，RSA加密
     */
    private void checkPassword(String password, String encryptPassword) {
        String decryptPassword = getDecryptString(encryptPassword);
        if (!BCrypt.checkpw(decryptPassword, password)) {
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

    private MoocManager getMoocManager(String account, UserToken.UserType type){

        // 教师
        if(UserToken.UserType.TEACHER.equals(type)){
            MoocUser moocUser = userDAO.findUserByAccount(account);
            if(moocUser == null){
               return null;
            }
            if(!"教师".equals(moocUser.getUserType())){
                throw new MoocException("您不能登录本系统");
            }
            return createTeacherManager(moocUser);
        } else {
            // 超级管理员
            if(superAdminUsername.equals(account)){
                return this.createSuperManager();
            }
            //普通管理员
            return managerDAO.findManagerByAccount(account);
        }
    }




    private void checkVerificationCode(String sessionId, String inputCode){
        String verificationCode = RedisUtil.get(sessionId);
        if(verificationCode == null || !verificationCode.equalsIgnoreCase(inputCode)){
            throw new MoocException(AuthorizeExceptionEnum.INCORRECT_VERIFICATION_CODE);
        }
    }


    /**
     * 构造一个超管信息
     * @return
     */
    private MoocManager createSuperManager(){
        MoocManager manager = new MoocManager();
        manager.setAccount(superAdminUsername);
        manager.setId(0L);
        manager.setStatus(ManagerStatusEnum.NORMAL.getStatus());
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
        manager.setId(user.getId());
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
