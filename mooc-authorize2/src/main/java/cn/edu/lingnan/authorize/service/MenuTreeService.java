package cn.edu.lingnan.authorize.service;

import cn.edu.lingnan.authorize.dao.MenuTreeDAO;
import cn.edu.lingnan.authorize.dao.RoleDAO;
import cn.edu.lingnan.authorize.model.vo.UserMenuTreeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xmz
 * @date: 2020/11/28
 */
@Service
public class MenuTreeService {

    @Autowired
    private MenuTreeDAO menuTreeDAO;
    @Autowired
    private RoleDAO roleDAO;

    /**
     * 获取教师菜单树
     * @return
     */
    public List<UserMenuTreeVO> getTeacherMenuTree(){

        List<Long> teacherRoleId = new ArrayList();
        //固定角色id=1为教师角色
        teacherRoleId.add(1L);
        List<cn.edu.lingnan.authorize.model.entity.MenuTree> menuList = menuTreeDAO.findMenuList(teacherRoleId);
        List<cn.edu.lingnan.authorize.model.entity.MenuTree> teacherMenuList = new ArrayList<>(menuList.stream().collect(Collectors.toSet()));
        List<UserMenuTreeVO> menuTreeDTOList = teacherMenuList.stream().filter(menu -> menu.getParentId().equals(0L))
                .map(this::convertMenuTreeToDTO).collect(Collectors.toList());

        // 递归设置子菜单
        getTree(menuTreeDTOList,teacherMenuList);
        return menuTreeDTOList;
    }

    /**
     * 获取菜单树
     * @param managerId
     * @return
     */
    public List<UserMenuTreeVO> getMenuTree(Long managerId){

        // 获取对应管理员菜单权限
        List<cn.edu.lingnan.authorize.model.entity.MenuTree> menuList = getAllPermissionMenuTree(managerId);
        // 获取一级菜单,parentId为Long类型，0表示根节点
        List<UserMenuTreeVO> menuTreeDTOList = menuList.stream().filter(menu -> menu.getParentId().equals(0L))
                .map(this::convertMenuTreeToDTO).collect(Collectors.toList());

        // 递归设置子菜单
        getTree(menuTreeDTOList,menuList);
        return menuTreeDTOList;
    }

    /**
     * 递归设置子菜单
     * @param menuTreeDTOList 需要设置的一级菜单
     * @param menuList 所有菜单list
     */
    private void getTree(List<UserMenuTreeVO> menuTreeDTOList, List<cn.edu.lingnan.authorize.model.entity.MenuTree> menuList){
        menuTreeDTOList.forEach(menuDTO -> {
            List<UserMenuTreeVO> childMenuList = new ArrayList<>();
            menuList.forEach(menu -> {
                //如果是它的子菜单并且是菜单（leaf=0），则加入list
                if(menu.getParentId().equals(menuDTO.getId()) && !StringUtils.isEmpty(menu.getMenuKey())){
                    childMenuList.add(convertMenuTreeToDTO(menu));
                }
                if (!childMenuList.isEmpty()) {
                    menuDTO.setSubs(childMenuList);
                    getTree(childMenuList, menuList);
                }
            });

        });

    }

    private UserMenuTreeVO convertMenuTreeToDTO(cn.edu.lingnan.authorize.model.entity.MenuTree menu){
        UserMenuTreeVO menuTreeDTO = new UserMenuTreeVO();
        menuTreeDTO.setIcon(menu.getIcon());
        menuTreeDTO.setIndex(menu.getMenuKey());
        menuTreeDTO.setTitle(menu.getLabel());
        menuTreeDTO.setId(menu.getId());
        return menuTreeDTO;
    }


    /**
     *  获取权限
     * @param managerId
     * @return
     */
    public List<cn.edu.lingnan.authorize.model.entity.MenuTree> getPermission(Long managerId){

        List<cn.edu.lingnan.authorize.model.entity.MenuTree> menuList = getAllPermissionMenuTree(managerId);
        //leaf=1 ,表示是叶子节点，表示是具体权限
        return menuList.stream().filter(menu -> menu.getLeaf().equals(1)).collect(Collectors.toList());
    }

    /**
     * 获取管理员所有权限菜单数据（未解析）
     * @param managerId
     * @return
     */
    public List<cn.edu.lingnan.authorize.model.entity.MenuTree> getAllPermissionMenuTree(Long managerId){
        //如果是超管
        if(managerId.equals(0L)){
            return menuTreeDAO.findAllMenuList();
        }
        //获取管理员所有角色列表
        List<Long> roleIdList = roleDAO.findAllRoleIdByManagerId(managerId);
        if(CollectionUtils.isEmpty(roleIdList)){
            return new ArrayList<>();
        }
        List<cn.edu.lingnan.authorize.model.entity.MenuTree> menuList = menuTreeDAO.findMenuList(roleIdList);
        //去重，注意对象去重，MenuTree需要重写equals 和 hashCode方法
        return new ArrayList<>(menuList.stream().collect(Collectors.toSet()));
    }




}
