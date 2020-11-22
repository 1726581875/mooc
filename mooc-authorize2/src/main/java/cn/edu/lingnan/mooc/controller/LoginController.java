package cn.edu.lingnan.mooc.controller;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.model.LoginParam;
import cn.edu.lingnan.mooc.model.MoocManager;
import cn.edu.lingnan.mooc.service.ManagerService;
import cn.edu.lingnan.mooc.util.RedisUtil;
import cn.edu.lingnan.mooc.util.VerificationCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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

    private Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private ManagerService managerService;

    @PostMapping("/login")
    private RespResult login(@Valid LoginParam loginParam, HttpServletRequest request, BindingResult bindingResult){
        log.info("loginParam={}",loginParam);
        // 入参校验
        if(bindingResult.hasErrors()){
            return RespResult.fail(-1,"参数不合法");
        }
        // 防止传入不合法参数，防止sql注入

        // 校验验证码是否正确
        String sessionId = request.getSession().getId();
        String verificationCode = RedisUtil.get(sessionId);
        if(verificationCode == null || !verificationCode.equalsIgnoreCase(loginParam.getCode())){
           return RespResult.fail("验证码不正确");
        }
        // 调用service方法登录
        RespResult respResult = managerService.login(loginParam, sessionId);

        return respResult;
    }

    @GetMapping("/code/image")
    public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        VerificationCode code = new VerificationCode();
        BufferedImage image = code.getImage();
        String sessionId = request.getSession().getId();
        String text = code.getText();
        log.info("sessionId={},code={}",sessionId,text);
        // 验证码放到redis
        RedisUtil.set(sessionId,text);
        VerificationCode.output(image,response.getOutputStream());
    }


}
