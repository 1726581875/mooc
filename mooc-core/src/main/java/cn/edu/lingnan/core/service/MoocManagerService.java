package cn.edu.lingnan.core.service;

import cn.edu.lingnan.core.entity.MoocManager;
import cn.edu.lingnan.core.enums.UserEnum;
import cn.edu.lingnan.mooc.common.model.PageVO;
import cn.edu.lingnan.core.entity.ManagerRoleRel;
import cn.edu.lingnan.core.entity.Role;
import cn.edu.lingnan.core.param.ManagerParam;
import cn.edu.lingnan.core.repository.ManagerRepository;
import cn.edu.lingnan.core.repository.ManagerRoleRelRepository;
import cn.edu.lingnan.core.repository.RoleRepository;
import cn.edu.lingnan.core.util.CopyUtil;
import cn.edu.lingnan.core.util.RsaUtil;
import cn.edu.lingnan.core.vo.MoocManagerVO;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author xmz
 * @date: 2020/10/23
 */
@Service
@Slf4j
public class MoocManagerService {

    @Resource
    private ManagerRepository moocManagerRepository;
    @Resource
    private ManagerRoleRelRepository managerRoleRelRepository;
    @Resource
    private RoleService roleService;
    @Resource
    private RoleRepository roleRepository;
    @Value("${mooc.rsa.privateKey}")
    private String PRI_KEY;

    /**
     * 根据Id查找
     * @param id
     * @return 如果找不到返回null
     */
    public MoocManager findById(Integer id){
        Optional<MoocManager> optional = moocManagerRepository.findById(id);
        if(!optional.isPresent()){
            return null;
        }
        return optional.get();
    }

    public List<MoocManager> findAll(){
        return moocManagerRepository.findAll();
    }

    /**
     * 根据匹配条件查询所有
     * @param matchObject
     * @return
     */
    public List<MoocManager> findAllByCondition(MoocManager matchObject){
        return moocManagerRepository.findAll(Example.of(matchObject));
    }

    /**
     * 条件分页查询
     * @param matchStr 匹配对象
     * @param pageIndex 第几页
     * @param pageSize 每页大小
     * @return
     */
    public PageVO<MoocManagerVO> findPage(String matchStr, Integer pageIndex, Integer pageSize){

        //先按照account查
        MoocManager matchObject = new MoocManager();
        if(!StringUtils.isEmpty(matchStr)) {
            matchObject.setAccount(matchStr);
        }
        Example<MoocManager> example = Example.of(matchObject);
        // 2、 构造分页参数 ,第几页,每页大小
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize, Sort.Direction.DESC,"createTime");
        // 3、 传入条件、分页参数，调用方法
        Page<MoocManager> moocManagerPage = moocManagerRepository.findAll(example, pageable);
        //如果按照账号查获取不到数据，按照名字模糊匹配
        if(matchObject.getAccount() != null && moocManagerPage.isEmpty()){
            // 模糊匹配
            ExampleMatcher  matcher = ExampleMatcher.matching().withMatcher("name", match -> match.contains());
            MoocManager nameMatcher = new MoocManager();
            nameMatcher.setName(matchObject.getAccount());
            moocManagerPage = moocManagerRepository.findAll(Example.of(nameMatcher,matcher), pageable);
        }

        //获取page对象里的list
        List<MoocManager> moocManagerList = moocManagerPage.getContent();
        // 将要返回给前端的VO对象
        List<MoocManagerVO> moocManagerVOList = new ArrayList<>(moocManagerList.size());
        // 用户对应的获取角色列表
        Map<Integer, List<Role>> roleMap = roleService.findRoleByManagerIdList(moocManagerList.stream().map(e -> e.getId()).collect(Collectors.toList()));

        moocManagerList.forEach(manager -> {
            MoocManagerVO managerVO = new MoocManagerVO();
            managerVO.setId(manager.getId());
            managerVO.setName(manager.getName());
            managerVO.setAccount(manager.getAccount());
            managerVO.setCreateTime(manager.getCreateTime());
            managerVO.setStatus(UserEnum.isEnable(manager.getStatus()));
            managerVO.setRoleList(roleMap.get(manager.getId()));
            moocManagerVOList.add(managerVO);
        });

        /* 4. 封装到自定义分页结果 */
        PageVO<MoocManagerVO> pageVO = new PageVO<>();
        pageVO.setContent(moocManagerVOList);
        pageVO.setPageIndex(pageIndex);
        pageVO.setPageSize(pageSize);
        pageVO.setPageCount(moocManagerPage.getTotalPages());
        return pageVO;
    }

    /**
     * 插入数据
     * @param moocManager
     * @return 返回成功数
     */
    public Integer insert(MoocManager moocManager){
        if (moocManager == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        MoocManager newMoocManager = moocManagerRepository.save(moocManager);
        return newMoocManager == null ? 0 : 1;
    }

    /**
     * 插入或更新数据
     * 说明:如果参数带id表示是更新，否则就是插入
     * @param managerParam
     * @return 返回成功数
     */
    @Transactional
    public Integer insertOrUpdate(ManagerParam managerParam){
        if (managerParam == null) {
            throw new IllegalArgumentException("managerParam对象不能为null");
        }
        /*  获取到将更新/插入的对象*/
        MoocManager manager = CopyUtil.copy(managerParam, MoocManager.class);
        // 处理密码
        if(!StringUtils.isEmpty(manager.getPassword())) {
            // 解密传过来的密码（密码是加密传输的，需要先解密）
            String password = null;
            try {
                password = RsaUtil.decryptByPrivateKey(PRI_KEY, manager.getPassword());
            } catch (Exception e) {
                log.error("密码解密失败",e);
                return 0;
            }
            // BCrypt重新加密
            manager.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        }

        /* 2、如果id不为空，表示更新操作 */
        if(managerParam.getId() != null){

            // 更新角色关联信息
            List<Role> roleList = managerParam.getRoleList();
            // TODO 需要去优化，目前不改动其他逻辑先这样
            // 删除全部关系
            managerRoleRelRepository.deleteAllByManagerId(manager.getId());
            // 重新插入
            if(!CollectionUtils.isEmpty(roleList)){
                managerRoleRelRepository.saveAll(roleList.stream().map(role->
                        new ManagerRoleRel(manager.getId(),role.getId())
                ).collect(Collectors.toList()));
            }

          return this.update(manager);
        }
        /* 2、如果传的id为null, 表示需要插入新用户 */
        MoocManager newMoocManager = moocManagerRepository.save(manager);
        if( newMoocManager == null){
            return 0;
        }


        /* 3、更新管理员角色关系 */
        // 获取前端传过来的角色list
        List<Role> roleList = managerParam.getRoleList();
        if(!CollectionUtils.isEmpty(roleList)){
            // 构造入库管理员角色关联关系
            List<ManagerRoleRel> managerRoleRelList = roleList.stream().map(role ->
                    new ManagerRoleRel(newMoocManager.getId(), role.getId())
            ).collect(Collectors.toList());
            //入库
            if(!CollectionUtils.isEmpty(managerRoleRelList)) {
                managerRoleRelRepository.saveAll(managerRoleRelList);
            }
        }

        return 1;
    }


    /**
     *  选择性更新
     * @param moocManager
     * @return 返回成功条数
     */
    public Integer update(MoocManager moocManager){
        // 入参校验
        if(moocManager == null || moocManager.getId() == null){
            throw new IllegalArgumentException("更新的对象不能为null");
        }
        // 是否存在
        Optional<MoocManager> optional = moocManagerRepository.findById(moocManager.getId());
        if(!optional.isPresent()){
            throw new RuntimeException("找不到id为"+ moocManager.getId() +"的MoocManager");
        }
        MoocManager dbMoocManager = optional.get();
        //把不为null的属性拷贝到dbMoocManager
        CopyUtil.notNullCopy(moocManager, dbMoocManager);
        //执行保存操作
        MoocManager updateMoocManager = moocManagerRepository.save(dbMoocManager);




        return updateMoocManager == null ? 0 : 1;
    }


    public Integer deleteById(Integer id){
        moocManagerRepository.deleteById(id);
        return  findById(id) == null ? 1 : 0;
    }

    /**
     * 批量删除
     * @param moocManagerIdList  id list
     * @return 删除条数
     */
    public Integer deleteAllByIds(List<Integer> moocManagerIdList){
        List<MoocManager> delMoocManagerList = moocManagerRepository.findAllById(moocManagerIdList);
        moocManagerRepository.deleteInBatch(delMoocManagerList);
        return delMoocManagerList.size();
    }


}
