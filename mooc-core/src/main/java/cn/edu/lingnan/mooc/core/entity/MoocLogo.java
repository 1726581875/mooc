package cn.edu.lingnan.mooc.core.entity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * @Author xiaomingzhang
 * @Date 2020/12/2
 */
@Getter
@Setter
@Entity
public class MoocLogo {

    /**
     * id
     */
    @Id
    private Integer id;
    /**
     * 系统标题
     */
    private String systemName;
    /**
     * 系统页面logo地址
     */
    private String systemLogoPath;
    /**
     * 登录页面logo地址
     */
    private String loginLogoPath;
    /**
     * 网站图标地址
     */
    private String faviconPath;

    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 创建时间
     */
    private Date createTime;


}
