package cn.edu.lingnan.mooc.authorize.dao;

import cn.edu.lingnan.mooc.authorize.model.entity.LoginLog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.util.Date;

/**
 * @author xmz
 * @date: 2020/11/23
 */
@SpringBootTest
public class LoginLogDAOTest {

    @Autowired
    private LoginLogDAO loginLogDAO;

    @Autowired
    private InterceptorRegistry interceptorRegistry;

    @Test
    public void insertLoginLogTest(){
        LoginLog loginLog = new LoginLog();
        loginLog.setAccount("xmz");
        loginLog.setCreateTime(new Date());
        loginLog.setId(1L);
        loginLog.setIp("127.0.0.1");
        loginLog.setSucceed("成功");
        loginLog.setMessage("登录成功");
        loginLog.setSystemType("1111");
        loginLog.setLogName("登录日志");
        loginLogDAO.insertLoginLog(loginLog);
    }

    public void testGetInterceptor(){
    }


}