package cn.edu.lingnan.authorize.entity;

import java.util.Objects;

/**
 * @author xmz
 * @date: 2020/11/08
 */
public class MenuTree {

    private Long id;

    private String label;

    private Long parentId;
    
    private String permission;

    private String router;

    private Integer leaf;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getRouter() {
        return router;
    }

    public void setRouter(String router) {
        this.router = router;
    }

    public Integer getLeaf() {
        return leaf;
    }

    public void setLeaf(Integer leaf) {
        this.leaf = leaf;
    }

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

    @Override
    public String toString() {
        return "MenuTree{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", parentId=" + parentId +
                ", permission='" + permission + '\'' +
                ", router='" + router + '\'' +
                ", leaf=" + leaf +
                '}';
    }
}
