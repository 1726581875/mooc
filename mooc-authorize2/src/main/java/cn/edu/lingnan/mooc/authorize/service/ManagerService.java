package cn.edu.lingnan.mooc.authorize.service;

import cn.edu.lingnan.mooc.authorize.dao.ManagerDAO;
import cn.edu.lingnan.mooc.authorize.dao.UserDAO;
import cn.edu.lingnan.mooc.authorize.dao.jpa.ManagerRepository;
import cn.edu.lingnan.mooc.authorize.model.entity.ManagerRoleRel;
import cn.edu.lingnan.mooc.authorize.model.entity.MoocManager;
import cn.edu.lingnan.mooc.authorize.model.entity.MoocUser;
import cn.edu.lingnan.mooc.authorize.model.entity.Role;
import cn.edu.lingnan.mooc.authorize.model.param.ManagerParam;
import cn.edu.lingnan.mooc.authorize.model.param.PasswordParam;
import cn.edu.lingnan.mooc.authorize.model.vo.ManagerVO;
import cn.edu.lingnan.mooc.authorize.util.RsaUtil;
import cn.edu.lingnan.mooc.common.exception.MoocException;
import cn.edu.lingnan.mooc.common.model.PageVO;
import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.common.util.CopyUtil;
import cn.edu.lingnan.mooc.common.util.UserUtil;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
    @Resource
    private ManagerRepository managerRepository;

    /**
     * 管理员（分管本人）修改密码
     * @param passwordParam
     * @return
     */
    public RespResult updatePassword(PasswordParam passwordParam){
        //获取id，根据id查询管理员
        Long managerId = UserUtil.getUserId();
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
    public RespResult randomNewPassword(Long userId,Integer userType){

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

    /**
     * 分页查询
     * @param queryStr
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public PageVO<MoocManager> findPage(String queryStr, Integer pageIndex, Integer pageSize) {
        return managerDAO.findManagePage(queryStr, pageIndex, pageSize);
    }


    public Integer update(MoocManager moocManager) {
        return managerDAO.save(moocManager);
    }

    public Integer insertOrUpdate(ManagerParam managerParam) {

        Assert.notNull(managerParam, "managerParam对象不能为null");

        MoocManager manager = CopyUtil.copy(managerParam, MoocManager.class);
        // 处理密码
        if(!StringUtils.isEmpty(manager.getPassword())) {
            // 解密传过来的密码（密码是加密传输的，需要先解密）
            String password = null;
            try {
                password = RsaUtil.decryptByPrivateKey(RSA_PRI_KEY, manager.getPassword());
            } catch (Exception e) {
                log.error("密码解密失败",e);
                return 0;
            }
            // BCrypt重新加密
            manager.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        }

        /* 2、如果id不为空，表示更新操作 */
        if(managerParam.getId() != null){
            List<Role> roleList = managerParam.getRoleList();
            //先删除，后插入
            managerDAO.deleteManagerRoleRefByManagerId(manager.getId());
            if(!CollectionUtils.isEmpty(roleList)){
                List<ManagerRoleRel> managerRoleRelList = roleList.stream().map(role -> new ManagerRoleRel(manager.getId(), role.getId()))
                        .collect(Collectors.toList());
                managerDAO.batchInsertManagerRoleRel(managerRoleRelList);
            }
            return this.update(manager);
        }
        // todo 不确定插入失败会返回什么值
        /* 2、如果传的id为null, 表示需要插入新用户 */
        long managerId = managerDAO.insert(manager);
        /* 3、更新管理员角色关系 */
        // 获取前端传过来的角色list
        List<Role> roleList = managerParam.getRoleList();
        if(!CollectionUtils.isEmpty(roleList)){
            // 构造入库管理员角色关联关系
            List<ManagerRoleRel> managerRoleRelList = roleList.stream().map(role ->
                    new ManagerRoleRel(managerId, role.getId())
            ).collect(Collectors.toList());
            //入库
            if(!CollectionUtils.isEmpty(managerRoleRelList)) {
                managerDAO.batchInsertManagerRoleRel(managerRoleRelList);
            }
        }

        return 1;
    }

    public void updateManagerStatus(@NotNull Long managerId, @NotNull Integer status){
        Optional<MoocManager> managerOptional = managerRepository.findById(managerId);
        if(!managerOptional.isPresent()){
            throw new MoocException("管理员没找到");
        }
        MoocManager manager = managerOptional.get();
        manager.setStatus(status);
        managerRepository.save(manager);
    }


    public Integer deleteById(Integer id) {
        return null;
    }

    public void deleteAllByIds(List<Integer> moocManagerIdList) {
    }
}
