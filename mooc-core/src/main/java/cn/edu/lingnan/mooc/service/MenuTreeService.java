package cn.edu.lingnan.mooc.service;

import cn.edu.lingnan.mooc.dto.MenuTreeDTO;
import cn.edu.lingnan.mooc.entity.MenuTree;
import cn.edu.lingnan.mooc.repository.MenuTreeRepository;
import cn.edu.lingnan.mooc.util.CopyUtil;
import cn.edu.lingnan.mooc.vo.MenuTreeVO;
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
public class MenuTreeService {

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
            if(menu.getParentId().equals(0)){
                menuTreeDTOList.add(CopyUtil.copy(menu,MenuTreeDTO.class));
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

        menuTreeDTOList.forEach(menuDTO -> {
            List<MenuTreeDTO> childrenList = new ArrayList<>();
            menuList.forEach(menu -> {
                if(menuDTO.getId() == menu.getParentId()){
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
    public List<MenuTree> findMenuPermByRoleIds(List<Integer> roleIdList){
        
        return menuTreeRepository.findMenuPermByRoleIds(roleIdList);
    }

}
