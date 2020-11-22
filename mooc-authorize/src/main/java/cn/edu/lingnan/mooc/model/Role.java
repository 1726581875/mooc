package cn.edu.lingnan.mooc.model;

import javax.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import lombok.Data;
import java.util.Date;

@Data
@DynamicUpdate
@DynamicInsert
@Entity(name = "role")
public class Role{
    // 角色ID   
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    // 角色名称   
    private String name;
    // 创建时间   
    private Date createTime;
    // 修改时间   
    private Date updateTime;
    
}