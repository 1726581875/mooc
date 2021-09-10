package cn.edu.lingnan.mooc.portal.authorize.model.entity;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Data
@DynamicInsert
@DynamicUpdate
@Entity(name = "role")
public class Role {
    // 角色ID   
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    // 角色名称   
    private String name;
    // 创建时间   
    private Date createTime;
    // 修改时间   
    private Date updateTime;

    public Role(){}

    public Role(Long id, String name){
        this.id = id;
        this.name = name;
    }

}