package cn.edu.lingnan.authorize.model.vo;

import cn.edu.lingnan.authorize.model.dto.MenuTreeDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author xmz
 * @date: 2020/11/08
 * 角色管理-菜单权限管理VO
 */
@Data
public class MenuTreeVO implements Serializable {

    private Integer id = 0;
    private String label = "根节点";
    private List<MenuTreeDTO> children;

    public MenuTreeVO(){}

    public MenuTreeVO(List<MenuTreeDTO> menuTreeDTOList){
        this.children = menuTreeDTOList;
    }
}
