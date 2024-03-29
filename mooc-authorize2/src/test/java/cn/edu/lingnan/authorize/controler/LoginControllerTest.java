package cn.edu.lingnan.authorize.controler;

import cn.edu.lingnan.authorize.model.param.LoginParam;
import cn.edu.lingnan.authorize.BaseMvcTest;
import cn.edu.lingnan.authorize.dao.ManagerDAO;
import cn.edu.lingnan.authorize.service.impl.AuthorizeServiceImpl;
import cn.edu.lingnan.authorize.util.RsaUtil;
import cn.edu.lingnan.mooc.common.model.RespResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;


/**
 * @author xmz
 * @date: 2020/11/26
 */
public class LoginControllerTest extends BaseMvcTest {

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @Value("${mooc.rsa.publicKey}")
    private String pubKey;

    @Autowired
    AuthorizeServiceImpl authorizeService;

    @Autowired
    ManagerDAO managerDAO;

    @Test
    public void loginTest() throws Exception {
        MockHttpSession mockHttpSession = new MockHttpSession();
        System.out.println(mockHttpSession.getId());
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        request.setSession(mockHttpSession);
        LoginParam loginParam = new LoginParam();
        loginParam.setUsername("zhangsan");
        System.out.println(pubKey);
        loginParam.setPassword(RsaUtil.encryptByPublicKey(pubKey,"root"));
        RespResult loginResult = authorizeService.login(loginParam, request);
        System.out.println(loginResult);
    }



}
