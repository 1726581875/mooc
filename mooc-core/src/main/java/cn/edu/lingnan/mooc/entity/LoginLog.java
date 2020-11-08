package cn.edu.lingnan.mooc.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import javax.persistence.Id;
import java.util.Date;

@DynamicInsert
@DynamicUpdate
@Entity(name = "login_log")
public class LoginLog{
    // 主键   
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    // 日志名称   
    private String logname;
    // 管理员账号   
    private String username;
    // 创建时间   
    private Date createTime;
    // 是否执行成功   
    private String succeed;
    // 具体消息   
    private String message;
    // 登录ip   
    private String ip;
    //    
    private String userId;
    // 系统类型   
    private String systemType;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogname() {
        return logname;
    }

    public void setLogname(String logname) {
        this.logname = logname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSucceed() {
        return succeed;
    }

    public void setSucceed(String succeed) {
        this.succeed = succeed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", logname=").append(logname);
        sb.append(", username=").append(username);
        sb.append(", createTime=").append(createTime);
        sb.append(", succeed=").append(succeed);
        sb.append(", message=").append(message);
        sb.append(", ip=").append(ip);
        sb.append(", userId=").append(userId);
        sb.append(", systemType=").append(systemType);
        sb.append("]");
        return sb.toString();
    }

}