package cn.edu.lingnan.authorize.service;

import cn.edu.lingnan.authorize.model.param.LoginParam;
import cn.edu.lingnan.authorize.model.vo.LoginSuccessVO;
import cn.edu.lingnan.mooc.common.enums.UserTypeEnum;

/**
 * @author xiaomingzhang
 * @date 2021/9/16
 */
public interface AuthorizeService {

    /**
     * 登录方法
     * @param loginParam
     * @return
     */
    LoginSuccessVO login(LoginParam loginParam);

    /**
     * 登出方法
     * @return
     */
    void loginOut();

    /**
     * 清除用户登录信息
     * @param type
     * @param account
     */
    void clearRedisLoginInfo(UserTypeEnum type, String account);

}
