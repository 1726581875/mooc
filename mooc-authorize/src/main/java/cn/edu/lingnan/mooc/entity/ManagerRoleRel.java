package cn.edu.lingnan.mooc.entity;

import javax.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import lombok.Data;
import java.util.Date;

@Data
@DynamicUpdate
@DynamicInsert
@Entity(name = "manager_role_rel")
public class ManagerRoleRel{
    // ID   
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    // 管理员ID   
    private Integer managerId;
    // 角色ID   
    private Integer roleId;
    // 创建时间   
    private Date createTime;
    // 修改时间   
    private Date updateTime;
    
}