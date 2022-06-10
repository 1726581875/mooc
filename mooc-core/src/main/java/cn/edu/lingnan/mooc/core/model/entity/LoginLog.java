package cn.edu.lingnan.mooc.core.model.entity;

import javax.persistence.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import lombok.Data;
import java.util.Date;

@Data
@DynamicUpdate
@DynamicInsert
@Entity(name = "login_log")
public class LoginLog{
    // 主键   
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    // 日志名称   
    private String logName;
    // 管理员账号   
    private String account;
    // 创建时间   
    private Date createTime;
    // 是否执行成功   
    private String succeed;
    // 具体消息   
    private String message;
    // 登录ip   
    private String ip;
    // 系统类型   
    private String systemType;
}