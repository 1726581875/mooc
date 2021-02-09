package cn.edu.lingnan.core.entity;

import javax.persistence.*;

import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import lombok.Data;
import java.util.Date;

@Data
@DynamicUpdate
@DynamicInsert
@Entity(name = "mooc_user")
@Accessors(chain=true)
public class MoocUser{
    // ID   
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    // 用户头像   
    private String userImage;
    // 用户昵称   
    private String name;
    // 登录账号   
    private String account;
    // 登录密码   
    private String password;
    // 类型，教师/普通用户   
    private String userType;
    // 用户状态| 1正常，2禁用，3已删除   
    private Integer status;
    /**
     * 个人座右铭、格言
     */
    private String motto;

    // 最近登录时间   
    private Date loginTime;
    // 创建时间   
    private Date createTime;
    // 修改时间   
    private Date updateTime;
    
}