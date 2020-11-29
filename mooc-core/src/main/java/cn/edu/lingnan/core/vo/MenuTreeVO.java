package cn.edu.lingnan.core.vo;

import cn.edu.lingnan.core.dto.MenuTreeDTO;

import java.io.Serializable;
import java.util.List;

/**
 * @author xmz
 * @date: 2020/11/08
 */
public class MenuTreeVO implements Serializable {

    private Integer id = 0;
    private String label = "根节点";
    private List<MenuTreeDTO> children;

    public MenuTreeVO(){}

    public MenuTreeVO(List<MenuTreeDTO> menuTreeDTOList){
        this.children = menuTreeDTOList;
    }

    public Integer getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public List<MenuTreeDTO> getChildren() {
        return children;
    }

    public void setChildren(List<MenuTreeDTO> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "MenuTreeVO{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", children=" + children +
                '}';
    }
}
