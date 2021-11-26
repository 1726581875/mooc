package cn.edu.lingnan.mooc.core.vo;

import lombok.Data;

/**
 * @author: xiaomingzhang
 * @time: 2020/12/3
 */
@Data
public class LogoVO {

    /**
     * 系统名
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

}
