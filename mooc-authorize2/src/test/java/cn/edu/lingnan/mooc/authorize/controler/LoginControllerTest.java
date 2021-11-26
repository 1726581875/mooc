package cn.edu.lingnan.mooc.authorize.controler;

import cn.edu.lingnan.mooc.authorize.model.param.LoginParam;
import cn.edu.lingnan.mooc.authorize.BaseMvcTest;
import cn.edu.lingnan.mooc.authorize.dao.ManagerDAO;
import cn.edu.lingnan.mooc.authorize.model.vo.LoginSuccessVO;
import cn.edu.lingnan.mooc.authorize.service.impl.AuthorizeServiceImpl;
import cn.edu.lingnan.mooc.authorize.util.RsaUtil;
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
        LoginSuccessVO login = authorizeService.login(loginParam);
        System.out.println(login);
    }



}
