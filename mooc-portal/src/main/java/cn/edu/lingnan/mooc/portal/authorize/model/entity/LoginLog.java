package cn.edu.lingnan.mooc.portal.authorize.model.entity;
import java.util.Date;

public class LoginLog {
    // 主键
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogName() {
        return logName;
    }

    public void setLogName(String logName) {
        this.logName = logName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    @Override
    public String toString() {
        return "LoginLog{" +
                "id=" + id +
                ", logName='" + logName + '\'' +
                ", account='" + account + '\'' +
                ", createTime=" + createTime +
                ", succeed='" + succeed + '\'' +
                ", message='" + message + '\'' +
                ", ip='" + ip + '\'' +
                ", systemType='" + systemType + '\'' +
                '}';
    }
}