package cn.edu.lingnan.mooc.controller;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.model.LoginParam;
import cn.edu.lingnan.mooc.util.RsaUtil;
import cn.edu.lingnan.mooc.util.VerificationCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author xmz
 * @date: 2020/10/25
 */
@Slf4j
@RestController
public class LoginController {

    @Value("mooc.rsa.privateKey")
    private String privateKey;

    @GetMapping("/login/msg")
    public RespResult Login(){
        return RespResult.fail(-1 , "你还没有登录");
    }

    @GetMapping("/mooc/admin/code/image")
    public void createCode(HttpServletRequest request, HttpServletResponse response) throws IOException {

        VerificationCode code = new VerificationCode();
        BufferedImage image = code.getImage();
        String text = code.getText();
        HttpSession session = request.getSession(true);
        session.setAttribute("verifyCode", text);
        VerificationCode.output(image,response.getOutputStream());

    }


    public RespResult login(@RequestBody LoginParam loginParam){

        String username = loginParam.getUsername();
        String password = "";
        try {
            password = RsaUtil.decryptByPrivateKey(privateKey,loginParam.getPassword());
        } catch (Exception e) {
            log.error("密码解密失败",e);
            RespResult.fail("登录失败");
        }

        return RespResult.success("登录成功");
    }

}
