package cn.edu.lingnan.authorize.controller;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.authorize.model.param.LoginParam;
import cn.edu.lingnan.authorize.service.AuthorizeService;
import cn.edu.lingnan.authorize.util.RedisUtil;
import cn.edu.lingnan.authorize.util.VerificationCode;
import cn.edu.lingnan.mooc.common.model.UserToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RestController
@RequestMapping("/mooc/admin")
public class LoginController {

    private final Logger log = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private AuthorizeService authorizeService;

    @PostMapping("/login")
    public RespResult login(@RequestBody @Valid LoginParam loginParam,HttpServletRequest request){
        return authorizeService.login(loginParam, request);
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
        String token = request.getHeader("Authorization");
        if(token == null){
            return RespResult.success("登出成功");
        }
        UserToken userToken = RedisUtil.get(token, UserToken.class);
        if(token == null || userToken == null){
            return RespResult.fail("token失效");
        }
        authorizeService.delRedisTokenOnline(userToken.getAccount());
        return RespResult.success("登出成功");
    }


}
