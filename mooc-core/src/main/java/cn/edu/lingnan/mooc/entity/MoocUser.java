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
@Entity(name = "mooc_user")
public class MoocUser{
    // ID   
    @Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    // 用户头像   
    private String userIamge;
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
    // 创建时间   
    private Date createTime;
    // 修改时间   
    private Date updateTime;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserIamge() {
        return userIamge;
    }

    public void setUserIamge(String userIamge) {
        this.userIamge = userIamge;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userIamge=").append(userIamge);
        sb.append(", name=").append(name);
        sb.append(", account=").append(account);
        sb.append(", password=").append(password);
        sb.append(", userType=").append(userType);
        sb.append(", status=").append(status);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append("]");
        return sb.toString();
    }

}