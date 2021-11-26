package cn.edu.lingnan.mooc.authorize.controller;

import cn.edu.lingnan.mooc.authorize.service.AuthorizeService;
import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.authorize.model.param.LoginParam;
import cn.edu.lingnan.mooc.authorize.util.VerificationCode;
import cn.edu.lingnan.mooc.common.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
@Slf4j
@RestController
@RequestMapping("/mooc/admin")
public class LoginController {

    @Autowired
    private AuthorizeService authorizeService;

    @PostMapping("/login")
    public RespResult login(@RequestBody @Valid LoginParam loginParam){
        return RespResult.success(authorizeService.login(loginParam));
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
    public RespResult loginOut(){
        authorizeService.loginOut();
        return RespResult.success();
    }


}
