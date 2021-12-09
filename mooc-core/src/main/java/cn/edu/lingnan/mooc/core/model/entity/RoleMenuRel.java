package cn.edu.lingnan.mooc.core.model.entity;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author xmz
 * @date: 2020/11/08
 */
@Data
//@Entity
public class RoleMenuRel {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private Integer roleId;

    private Integer menuId;

    public RoleMenuRel(){}

    public RoleMenuRel(Integer roleId,Integer menuId){
        this.roleId = roleId;
        this.menuId = menuId;
    }
    public RoleMenuRel(Integer id, Integer roleId,Integer menuId){
        this.id = id;
        this.roleId = roleId;
        this.menuId = menuId;
    }
}
