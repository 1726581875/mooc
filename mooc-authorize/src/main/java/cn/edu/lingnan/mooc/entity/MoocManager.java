package cn.edu.lingnan.mooc.entity;

import javax.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import lombok.Data;
import java.util.Date;

@Data
@DynamicUpdate
@DynamicInsert
@Entity(name = "mooc_manager")
public class MoocManager{
    // 主键   
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    // 名字   
    private String name;
    // 登录账号   
    private String account;
    // 密码   
    private String password;
    // 用户状态| 1正常，2禁用，3已删除   
    private Integer status;
    // 创建时间   
    private Date createTime;
    // 修改时间   
    private Date updateTime;
    
}