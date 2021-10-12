package cn.edu.lingnan.mooc.portal.authorize.controller;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.common.model.LoginUser;
import cn.edu.lingnan.mooc.common.util.RedisUtil;
import cn.edu.lingnan.mooc.portal.authorize.model.param.LoginParam;
import cn.edu.lingnan.mooc.portal.authorize.model.param.RegisterParam;
import cn.edu.lingnan.mooc.portal.authorize.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author xmz
 * @date: 2021/02/08
 * 前台系统的登录相关
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class LoginController {

    @Autowired
    private LoginService receptionLoginService;


    /**
     * 前台用户登录方法
     * @param loginParam
     * @param bindingResult
     * @param request
     * @return
     */
    @PostMapping("/login")
    public RespResult login(@RequestBody @Valid LoginParam loginParam, BindingResult bindingResult, HttpServletRequest request){
        // 入参校验
        if(bindingResult.hasErrors()){
            return RespResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        //验证码和sessionId关联
        String sessionId = request.getSession().getId();
        String verificationCode = RedisUtil.get(sessionId);
        if(verificationCode == null || !verificationCode.equalsIgnoreCase(loginParam.getCode())){
            log.warn("登录验证不通过，验证码不正确，sessionId={},code={},输入的验证码inputCode={}"
                    ,sessionId,verificationCode,loginParam.getCode());
            return RespResult.fail("验证码不正确");
        }
        // 调用service方法登录
        RespResult respResult = receptionLoginService.login(loginParam, request);
        return respResult;
    }

    /**
     * 注册方法
     * @param registerParam
     * @return
     */
    @PostMapping("/register")
    public RespResult register(@RequestBody RegisterParam registerParam){
         receptionLoginService.register(registerParam);
         return RespResult.success();
    }


    @GetMapping("/loginOut")
    public RespResult loginOut(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if(token == null){
            return RespResult.success("登出成功");
        }
        LoginUser loginUser = RedisUtil.get(token, LoginUser.class);
        if(token == null || loginUser == null){
            return RespResult.fail("token失效");
        }
        receptionLoginService.delRedisTokenOnline(loginUser.getAccount(), loginUser.getType());
        return RespResult.success("登出成功");
    }

    /**
     * 判断用户是否是登录状态
     * @param request
     * @return
     */
    @GetMapping("/isLogin")
    public RespResult isLogin(HttpServletRequest request){

        String token = request.getHeader("Authorization");
        if(token == null){
            return RespResult.success(false);
        }
        LoginUser userToken = RedisUtil.get(token, LoginUser.class);
        if(token == null || userToken == null){
            return RespResult.success(false);
        }
        return RespResult.success(true);
    }


}
