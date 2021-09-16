package cn.edu.lingnan.authorize.service.impl;

import cn.edu.lingnan.authorize.util.RedisKeyUtil;
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
import cn.edu.lingnan.authorize.model.vo.UserMenuTreeVO;
import cn.edu.lingnan.authorize.service.AuthorizeService;
import cn.edu.lingnan.authorize.service.MenuTreeService;
import cn.edu.lingnan.mooc.common.exception.MoocException;
import cn.edu.lingnan.mooc.common.exception.enums.ExceptionEnum;
import cn.edu.lingnan.authorize.dao.MenuTreeDAO;
import cn.edu.lingnan.authorize.util.RsaUtil;
import cn.edu.lingnan.mooc.common.exception.enums.UserTypeEnum;
import cn.edu.lingnan.mooc.common.model.UserToken;
import cn.edu.lingnan.mooc.common.util.HttpServletUtil;
import cn.edu.lingnan.mooc.common.util.RedisUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xmz
 * @date: 2020/11/22
 */
@Service
public class AuthorizeServiceImpl implements AuthorizeService {

    private Logger log = LoggerFactory.getLogger(AuthorizeServiceImpl.class);

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

    @Override
    public LoginSuccessVO login(LoginParam loginParam){


        // check verification code
        checkVerificationCode(loginParam.getCode());

        // get manager
        MoocManager manager = getMoocManager(loginParam.getUsername(),loginParam.getType());
        if(manager == null){
            throw new MoocException(AuthorizeExceptionEnum.ACCOUNT_NOT_EXIST);
        }
        // check password
        checkPassword(manager.getPassword(), loginParam.getPassword());

        checkStatus(manager.getStatus());


        LoginSuccessVO loginSuccessVO = buildLoginSuccessVO(manager);

        updateLastLoginTime(manager.getId(), manager.getType());

        return loginSuccessVO;
    }

    @Override
    public void loginOut() {
        HttpServletRequest request = HttpServletUtil.getRequest();
        String token = request.getHeader(UserToken.HTTP_TOKEN_HEAD);
        if(token == null){
           return;
        }
        UserToken userToken = RedisUtil.get(token, UserToken.class);
        if(token == null || userToken == null){
           throw new MoocException("token失效");
        }
        clearRedisLoginInfo(userToken.getType(), userToken.getAccount());
    }


    private void updateLastLoginTime(Long managerId, UserTypeEnum userTypeEnum) {
        if(UserTypeEnum.TEACHER.equals(userTypeEnum)) {
            userDAO.updateLoginTime(managerId, new Date());
        }else {
            managerDAO.updateLoginTime(managerId, new Date());
        }
    }


    private LoginSuccessVO buildLoginSuccessVO(MoocManager manager) {

        String token = UUID.randomUUID().toString();

        List<UserMenuTreeVO> menuVOList = null;
        List<MenuTree> permissionList = null;
        // 获取菜单和权限
        if(UserTypeEnum.TEACHER.equals(manager.getType())) {
            // 教师权限绑定教师角色 id为1
            Long roleId = 1L;
            List<MenuTree> menuList = menuTreeDAO.findMenuList(Arrays.asList(roleId));
            permissionList = new ArrayList<>(menuList.stream().collect(Collectors.toSet()));
            menuVOList = menuTreeService.getMenuByRoleId(Arrays.asList(roleId));
        }else {
            permissionList = menuTreeService.getPermission(manager.getId());
            menuVOList = menuTreeService.getMenuTree(manager.getId());

        }

        // 拼接权限字符串
        String permissionStr = getPermissionStr(permissionList);

        // 存储token
        saveLoginUserToken(manager, permissionStr, token);

        return new LoginSuccessVO(token, manager.getType() , manager.getId(), menuVOList);
    }

    private String getPermissionStr(List<MenuTree> menuList) {
        if(CollectionUtils.isEmpty(menuList)) {
            return "";
        }
        return menuList.stream().map(MenuTree::getPermission).collect(Collectors.joining(","));
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

    private MoocManager getMoocManager(String account, UserTypeEnum type){

        // 教师
        if(UserTypeEnum.TEACHER.equals(type)){
            MoocUser moocUser = userDAO.findUserByAccount(account);
            if(moocUser == null){
               return null;
            }
            final String teacher = "教师";
            if(!teacher.equals(moocUser.getUserType())){
                throw new MoocException("您不能登录本系统");
            }
            return createTeacherManager(moocUser);
        } else {
            // 超级管理员
            if(superAdminUsername.equals(account)){
                return this.createSuperManager();
            }
            //普通管理员
            MoocManager manager = managerDAO.findManagerByAccount(account);
            if(manager != null) {
                manager.setType(UserTypeEnum.MANAGER);
            }
            return manager;
        }
    }




    private void checkVerificationCode(String inputCode){
        HttpServletRequest request = HttpServletUtil.getRequest();
        String verificationCode = RedisUtil.get(request.getSession().getId());
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
        manager.setType(UserTypeEnum.MANAGER);
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
        manager.setAccount(user.getAccount());
        manager.setId(user.getId());
        manager.setStatus(user.getStatus());
        manager.setName(user.getName());
        manager.setPassword(user.getPassword());
        manager.setType(UserTypeEnum.TEACHER);
        return manager;
    }


    /**
     * 登录成功设置token
     * 1、删除该账号旧token
     * 2、设置用户token和登录信息
     * 3、记录在线信息
     *
     * @param manager
     * @param permissionStr
     * @param token
     */
    private void saveLoginUserToken(MoocManager manager, String permissionStr, String token){

        String accountKey = RedisKeyUtil.getRedisAccountKey(manager.getType(), manager.getAccount());
        // 如果旧token存在，则先删除（防止一个账号两个地方同时登录）
        if(RedisUtil.isExist(accountKey)){
            String oldToken = RedisUtil.get(accountKey);
            RedisUtil.delete(oldToken);
        }

        // 设置UserToken（用户登录基本信息）
        String tokenKey = RedisKeyUtil.getRedisTokenKey(manager.getType(), token);
        UserToken userToken = buildUserToken(manager, token, permissionStr);
        RedisUtil.set(tokenKey, userToken, LOGIN_EXPIRE_TIME);

        // 账户和token关联关系
        RedisUtil.set(accountKey, token, LOGIN_EXPIRE_TIME);

        // 非超管记录在线信息
        if(!manager.getId().equals(0L)) {
            String redisOnlineKey = RedisKeyUtil.getRedisOnlineKey(manager.getType(), manager.getAccount());
            OnlineUser onlineUser = buildOnlineUser(manager);
            RedisUtil.set(redisOnlineKey, onlineUser, LOGIN_EXPIRE_TIME);
        }

    }


    private UserToken buildUserToken(MoocManager manager, String token, String permissionStr){
        UserToken userToken = new UserToken();
        userToken.setUserId(manager.getId());
        userToken.setAccount(manager.getAccount());
        userToken.setToken(token);
        userToken.setPermission(permissionStr);
        userToken.setType(manager.getType());
        return userToken;
    }


    private OnlineUser buildOnlineUser(MoocManager manager) {
        OnlineUser onlineUser = new OnlineUser();
        onlineUser.setAccount(manager.getAccount());
        onlineUser.setLoginTime(new Date());
        //设置ip
        onlineUser.setIp(HttpServletUtil.getIpAddress());
        onlineUser.setName(manager.getName());
        onlineUser.setType(manager.getType());
        return onlineUser;
    }


    @Override
    public void clearRedisLoginInfo(UserTypeEnum type, String account) {
        // 删除用户在线息
        String redisOnlineKey = RedisKeyUtil.getRedisOnlineKey(type, account);
        RedisUtil.delete(redisOnlineKey);
        // 获取 账号 对应token
        String redisAccountKey = RedisKeyUtil.getRedisAccountKey(type, account);
        String token = RedisUtil.get(redisAccountKey);
        if(token != null){
            RedisUtil.delete(token);
        }
        // 删除账户、token关联
        RedisUtil.delete(redisAccountKey);
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



}
