package cn.edu.lingnan.mooc.portal.authorize.model.entity;

import cn.edu.lingnan.mooc.common.enums.UserTypeEnum;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * @author xmz
 * @date: 2021/09/16
 */
@Data
@DynamicInsert
@DynamicUpdate
@Accessors(chain=true)
@Entity(name = "mooc_user")
public class MoocUser {
    /**
     * ID
     */
    @Id
  	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    /**
     * 用户头像
     */
    private String userImage;
    /**
     * 用户昵称
     */
    private String name;
    /**
     * 登录账号
     */
    private String account;
    /**
     * 登录密码
     */
    private String password;
    /**
     * 类型，教师/普通用户
     */
    private UserTypeEnum userType;
    /**
     * 用户格言/座右铭
     */
    private String motto;
    /**
     * 用户状态| 1正常，2禁用，3已删除
     */
    private Integer status;
    /**
     * 最近登录时间
     */
    private Date loginTime;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;

}