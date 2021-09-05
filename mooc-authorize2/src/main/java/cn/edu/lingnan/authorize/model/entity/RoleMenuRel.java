package cn.edu.lingnan.authorize.model.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @author xmz
 * @date: 2020/11/08
 */
@Data
@Entity
public class RoleMenuRel {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private Long roleId;

    private Long menuId;

    public RoleMenuRel(){}

    public RoleMenuRel(Long roleId, Long menuId){
        this.roleId = roleId;
        this.menuId = menuId;
    }
    public RoleMenuRel(Long id, Long roleId, Long menuId){
        this.id = id;
        this.roleId = roleId;
        this.menuId = menuId;
    }
}
