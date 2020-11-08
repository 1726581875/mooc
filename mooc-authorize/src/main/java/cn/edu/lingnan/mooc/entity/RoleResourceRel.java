package cn.edu.lingnan.mooc.entity;

import javax.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import lombok.Data;
import java.util.Date;

@Data
@DynamicUpdate
@DynamicInsert
@Entity(name = "role_resource_rel")
public class RoleResourceRel{
    // ID   
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    // 角色ID   
    private Integer roleId;
    // 资源ID   
    private Integer resourceId;
    // 创建时间   
    private Date createTime;
    // 修改时间   
    private Date updateTime;
    
}