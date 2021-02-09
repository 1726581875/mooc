package cn.edu.lingnan.authorize.model;

import lombok.Data;
import lombok.ToString;

import java.util.Objects;

/**
 * @author xmz
 * @date: 2020/11/08
 */
@Data
@ToString
public class MenuTree {

    private Long id;

    private String label;

    private String menuKey;

    private String icon;

    private Long parentId;
    
    private String permission;

    private String router;

    private Integer leaf;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuTree menuTree = (MenuTree) o;
        return Objects.equals(id, menuTree.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
