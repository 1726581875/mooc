package cn.edu.lingnan.authorize.controller.reception;

import cn.edu.lingnan.authorize.entity.LoginParam;
import cn.edu.lingnan.authorize.entity.UserToken;
import cn.edu.lingnan.authorize.service.AuthorizeService;
import cn.edu.lingnan.authorize.service.reception.ReceptionLoginService;
import cn.edu.lingnan.authorize.util.RedisUtil;
import cn.edu.lingnan.mooc.common.model.RespResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
public class ReceptionLoginController {

    @Autowired
    private ReceptionLoginService receptionLoginService;
    @Autowired
    private AuthorizeService authorizeService;
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

    @GetMapping("/loginOut")
    public RespResult loginOut(HttpServletRequest request){
        // 1、获取请求头携带的token
        String token = request.getHeader("Authorization");
        if(token == null){
            RespResult.success("登出成功");
        }
        UserToken userToken = RedisUtil.get(token, UserToken.class);
        if(token == null || userToken == null){
            RespResult.fail("token失效");
        }
        // 删除token/在线信息
        authorizeService.delRedisTokenOnline(userToken.getAccount());
        return RespResult.success("登出成功");
    }

    /**
     * 判断用户是否是登录状态
     * @param request
     * @return
     */
    @GetMapping("/isLogin")
    public RespResult isLogin(HttpServletRequest request){

        // 1、获取请求头携带的token
        String token = request.getHeader("Authorization");
        if(token == null){
            return RespResult.success(false);
        }
        UserToken userToken = RedisUtil.get(token, UserToken.class);
        if(token == null || userToken == null){
            return RespResult.success(false);
        }
        return RespResult.success(true);
    }


}
