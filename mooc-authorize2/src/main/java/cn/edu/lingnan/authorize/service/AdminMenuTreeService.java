package cn.edu.lingnan.authorize.service;

import cn.edu.lingnan.authorize.dao.jpa.MenuTreeRepository;
import cn.edu.lingnan.authorize.model.dto.MenuTreeDTO;
import cn.edu.lingnan.authorize.model.entity.MenuTree;
import cn.edu.lingnan.authorize.model.vo.MenuTreeVO;
import cn.edu.lingnan.mooc.common.util.CopyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author xmz
 * @date: 2020/11/08
 */
@Slf4j
@Service
public class AdminMenuTreeService {

    @Autowired
    private MenuTreeRepository menuTreeRepository;

    public MenuTreeVO getMenuTree(){
        // 查询出全部菜单列表
        List<MenuTree> menuList = menuTreeRepository.findAll();

        if(CollectionUtils.isEmpty(menuList)){
          log.warn("查询菜单列表异常,菜单列表为null");

        }
        // 遍历设置菜单树第一层节点
        List<MenuTreeDTO> menuTreeDTOList = new ArrayList<>();
        menuList.forEach(menu -> {
            if(menu.getParentId().equals(0L)){
                menuTreeDTOList.add(CopyUtil.copy(menu, MenuTreeDTO.class));
            }
        });
        // 递归设置其他节点
        this.setTreeNode(menuList,menuTreeDTOList);

        return new MenuTreeVO(menuTreeDTOList);
    }

    /**
     * 递归设置菜单树
     * @param menuList
     * @param menuTreeDTOList
     */
    private void setTreeNode(List<MenuTree> menuList, List<MenuTreeDTO> menuTreeDTOList){
        //构造菜单权限树
        menuTreeDTOList.forEach(menuDTO -> {
            List<MenuTreeDTO> childrenList = new ArrayList<>();
            menuList.forEach(menu -> {
                if(menuDTO.getId().equals(menu.getParentId())){
                    childrenList.add(CopyUtil.copy(menu,menuDTO.getClass()));
                }
            });
            if(!childrenList.isEmpty()) {
                menuDTO.setChildren(childrenList);
                //递归
                this.setTreeNode(menuList,childrenList);
            }
        });
    }


    /**
     * 根据角色Id列表查询菜单权限
     * @param roleIdList
     * @return
     */
    public List<MenuTree> findMenuPermByRoleIds(List<Long> roleIdList){
        
        return menuTreeRepository.findMenuPermByRoleIds(roleIdList);
    }

}
