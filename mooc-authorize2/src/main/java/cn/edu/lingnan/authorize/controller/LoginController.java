package cn.edu.lingnan.authorize.controller;

import cn.edu.lingnan.authorize.constant.UserConstant;
import cn.edu.lingnan.authorize.entity.UserToken;
import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.authorize.entity.LoginParam;
import cn.edu.lingnan.authorize.service.AuthorizeService;
import cn.edu.lingnan.authorize.util.RedisUtil;
import cn.edu.lingnan.authorize.util.VerificationCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author xmz
 * @date: 2020/11/22
 */
@RestController
@RequestMapping("/mooc/admin")
public class LoginController {

    private final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private AuthorizeService authorizeService;

    @PostMapping("/login")
    public RespResult login(@RequestBody @Valid LoginParam loginParam,BindingResult bindingResult, HttpServletRequest request){

        // 入参校验
        if(bindingResult.hasErrors()){
            return RespResult.fail(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        // 防止传入不合法参数，防止sql注入
       /* String authorize = request.getHeader("Authorize");
        System.out.println(authorize);*/
        // 校验验证码是否正确
        String sessionId = request.getSession().getId();
        String verificationCode = RedisUtil.get(sessionId);
        if(verificationCode == null || !verificationCode.equalsIgnoreCase(loginParam.getCode())){
           return RespResult.fail("验证码不正确");
        }
        // 调用service方法登录
        RespResult respResult = authorizeService.login(loginParam, request);


        return respResult;
    }

    @GetMapping("/code/image")
    public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        VerificationCode code = new VerificationCode();
        BufferedImage image = code.getImage();
        String sessionId = request.getSession().getId();
        String text = code.getText();
        log.info("获取验证码，sessionId={},code={}",sessionId,text);
        // 验证码放到redis
        RedisUtil.set(sessionId,text,300);
        VerificationCode.output(image,response.getOutputStream());
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


}
