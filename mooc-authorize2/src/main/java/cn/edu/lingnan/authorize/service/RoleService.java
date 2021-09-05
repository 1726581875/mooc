package cn.edu.lingnan.authorize.service;

import cn.edu.lingnan.authorize.dao.jpa.RoleMenuRelRepository;
import cn.edu.lingnan.authorize.dao.jpa.RoleRepository;
import cn.edu.lingnan.authorize.model.entity.MenuTree;
import cn.edu.lingnan.authorize.model.entity.Role;
import cn.edu.lingnan.authorize.model.entity.RoleMenuRel;
import cn.edu.lingnan.authorize.model.param.RoLeParam;
import cn.edu.lingnan.authorize.model.vo.RoleVO;
import cn.edu.lingnan.authorize.util.CopyUtil;
import cn.edu.lingnan.mooc.common.model.PageVO;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
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
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AdminMenuTreeService menuTreeService;
    @Autowired
    private RoleMenuRelRepository roleMenuRelRepository;

    /**
     * 根据Id查找角色
     * @param id
     * @return 如果找不到返回null
     */
    public RoleVO findById(Long id){
        // 查询角色基本信息
        Optional<Role> optional = roleRepository.findById(id);
        if(!optional.isPresent()){
            return null;
        }
        Role role = optional.get();
        //赋值给VO对象
        RoleVO roleVO = CopyUtil.copy(role, RoleVO.class);
        //查询该角色对应的权限Id
        List<MenuTree> menuList = menuTreeService.findMenuPermByRoleIds(Lists.newArrayList(id));
       // 取到IdList
        List<Long> menuIdLIst = menuList.stream().map(e -> e.getId()).collect(Collectors.toList());
        List<Long> leafNodeIdList = menuList.stream().filter(menu -> menu.getLeaf() == 1)
                .map(menu -> menu.getId()).collect(Collectors.toList());
        roleVO.setMenuIdList(menuIdLIst);
        roleVO.setLeafNodeIdList(leafNodeIdList);
        return roleVO;
    }

    public List<Role> findAll(){
        return roleRepository.findAll();
    }

    /**
     * 根据匹配条件查询所有
     * @param matchObject
     * @return
     */
    public List<Role> findAllByCondition(Role matchObject){
        return roleRepository.findAll(Example.of(matchObject));
    }

    /**
     * 条件分页查询
     * @param matchObject 匹配对象
     * @param pageIndex 第几页
     * @param pageSize 每页大小
     * @return
     */
    public PageVO<RoleVO> findPage(Role matchObject, Integer pageIndex, Integer pageSize){
        // 1、构造条件
         // 1.1 设置匹配策略，name属性模糊查询
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", match -> match.contains());
         // 1.2 构造匹配条件Example对象
        Example<Role> example = Example.of(matchObject,matcher);

        // 2、 构造分页参数 ,第几页,每页大小
        Pageable pageable = PageRequest.of(pageIndex - 1, pageSize);
        // 3、 传入条件、分页参数，调用方法
        Page<Role> rolePage = roleRepository.findAll(example, pageable);
        //获取page对象里的list,并复制到 转换为RoleVO对象list
        List<RoleVO> roleList = CopyUtil.copyList(rolePage.getContent(),RoleVO.class);
        /* 4. 封装到自定义分页结果 */
        PageVO<RoleVO> pageVO = new PageVO<>();
        pageVO.setContent(roleList);
        pageVO.setPageIndex(pageIndex);
        pageVO.setPageSize(pageSize);
        pageVO.setPageCount(rolePage.getTotalPages());
        return pageVO;
    }

    /**
     * 插入数据
     * @param role
     * @return 返回成功数
     */
    public Integer insert(Role role){
        if (role == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        Role newRole = roleRepository.save(role);
        return newRole == null ? 0 : 1;
    }

    /**
     * 插入或更新数据
     * 说明:如果参数带id表示是更新，否则就是插入
     * @param roLeParam
     * @return 返回成功数
     */
    @Transactional
    public Integer insertOrUpdate(RoLeParam roLeParam){
        if (roLeParam == null) {
            throw new IllegalArgumentException("插入表的对象不能为null");
        }
        // 保存菜单权限
        List<Long> paramMenuIdList = roLeParam.getMenuIdList();
        Role role = CopyUtil.copy(roLeParam, Role.class);
        // id不为空，表示更新操作
        if(roLeParam.getId() != null){
            if(paramMenuIdList != null){
                //查询该角色对应的menuId
                List<RoleMenuRel> localRoleMenuRelList = roleMenuRelRepository.findAll(Example.of(new RoleMenuRel(roLeParam.getId(), null)));

                // 获取将要删除的RoleMenuRel对象
                List<RoleMenuRel> withDeleteRoleMenu = localRoleMenuRelList.stream()
                        .filter(roleMenuRel -> !paramMenuIdList.contains(roleMenuRel.getId())).collect(Collectors.toList());
               // 构建要插入或更新的RoleMenuRel对象
                List<RoleMenuRel> roleMenuRelList = paramMenuIdList.stream()
                        .map(menuId -> new RoleMenuRel(role.getId(), menuId)).collect(Collectors.toList());

                roleMenuRelRepository.saveAll(roleMenuRelList);
                roleMenuRelRepository.deleteInBatch(withDeleteRoleMenu);
            }

            return this.update(role);
        }
        Role newRole = roleRepository.save(role);

        if(paramMenuIdList != null){
            List<RoleMenuRel> roleMenuRelList = paramMenuIdList.stream()
                    .map(menuId -> new RoleMenuRel(role.getId(), menuId)).collect(Collectors.toList());
            roleMenuRelRepository.saveAll(roleMenuRelList);
        }

        return newRole == null ? 0 : 1;
    }


    /**
     *  选择性更新
     * @param role
     * @return 返回成功条数
     */
    public Integer update(Role role){
        // 入参校验
        if(role == null || role.getId() == null){
            throw new IllegalArgumentException("更新的对象不能为null");
        }
        // 是否存在
        Optional<Role> optional = roleRepository.findById(role.getId());
        if(!optional.isPresent()){
            throw new RuntimeException("找不到id为"+ role.getId() +"的Role");
        }
        Role dbRole = optional.get();
        //把不为null的属性拷贝到dbRole
        CopyUtil.notNullCopy(role, dbRole);
        //执行保存操作
        Role updateRole = roleRepository.save(dbRole);
        return updateRole == null ? 0 : 1;
    }

    /**
     * 删除角色
     * @param id
     * @return
     */

    @Transactional
    public Integer deleteById(Long id){
        // 删除角色
        roleRepository.deleteById(id);
        // 删除角色权限
        roleMenuRelRepository.deleteAllByRoleIdIn(Lists.newArrayList(id));
        return findById(id) == null ? 1 : 0;
    }

    /**
     * 批量删除
     * @param roleIdList  id list
     * @return 删除条数
     */
    public Integer deleteAllByIds(List<Long> roleIdList){
        //批量删除角色
        List<Role> delRoleList = roleRepository.findAllById(roleIdList);
        roleRepository.deleteInBatch(delRoleList);
        //批量删除角色权限
        roleMenuRelRepository.deleteAllByRoleIdIn(roleIdList);
        return delRoleList.size();
    }

    public Map<Long,List<Role>> findRoleByManagerIdList(List<Long> managerIdList){
        if(managerIdList == null && managerIdList.isEmpty()){
            log.error("查询角色列表传入的id不能为null");
            throw new IllegalArgumentException("传入的id不能为null");
        }
        // TODO 有时间要去优化
        Map<Long,List<Role>> roleMap = new HashMap<>(managerIdList.size());
        managerIdList.forEach(managerId -> roleMap.put(managerId,roleRepository.findAllRoleInManagerId(managerId)));
        //List<Role> roleList = roleRepository.findAllRoleInManagerIdList(managerIdList);
        //Map<Integer, List<Role>> roleMap = roleList.stream().collect(Collectors.groupingBy(Role::getId));
        return roleMap;
    }

}
