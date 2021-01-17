package cn.edu.lingnan.authorize.aspect;

import cn.edu.lingnan.authorize.dao.LoginLogDAO;
import cn.edu.lingnan.authorize.entity.LoginLog;
import cn.edu.lingnan.authorize.entity.LoginParam;
import cn.edu.lingnan.authorize.entity.UserToken;
import cn.edu.lingnan.authorize.util.HttpServletUtil;
import cn.edu.lingnan.authorize.util.RedisUtil;
import cn.edu.lingnan.mooc.common.model.RespResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author xmz
 * @date: 2020/11/23
 */
@Aspect
@Component
public class LoginLogAspect {

    @Autowired
    private LoginLogDAO loginLogDAO;

    private static final Logger log = LoggerFactory.getLogger(LoginLogAspect.class);

    @Around("execution(* cn.edu.lingnan.authorize.controller.LoginController.login(..))")
    public Object handleLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1、获取登录参数
        Object[] args = joinPoint.getArgs();
        LoginParam loginParams = (LoginParam)args[0];
        HttpServletRequest request = (HttpServletRequest)args[2];

        // 2、执行登录方法
        RespResult respResult = (RespResult)joinPoint.proceed();

        // 3、记录登录日志
        String succeed = respResult.isSuccess() ? "成功" : "失败";
        String ip = HttpServletUtil.getIpAddress(request);
        LoginLog loginLog = new LoginLog();
        loginLog.setLogName("登录日志");
        loginLog.setSystemType("windows");
        loginLog.setMessage(respResult.getMsg());
        loginLog.setSucceed(succeed);
        loginLog.setAccount(loginParams.getUsername());
        loginLog.setCreateTime(new Date());
        loginLog.setIp(ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip);
        loginLogDAO.insertLoginLog(loginLog);

        return respResult;
    }


    @Around("execution(* cn.edu.lingnan.authorize.controller.LoginController.loginOut(..))")
    public Object handleLoginOut(ProceedingJoinPoint joinPoint) throws Throwable {
        // 1、获取登出参数
        Object[] args = joinPoint.getArgs();
        HttpServletRequest request = (HttpServletRequest)args[0];
        String token = request.getHeader("Authorization");
        String account = "获取账号异常";
        if(token != null){
            UserToken userToken = RedisUtil.get(token, UserToken.class);
            if(userToken != null){
                account = userToken.getAccount();
            }
        }

        // 2、执行登出方法
        RespResult respResult = (RespResult)joinPoint.proceed();

        // 3、记录登出日志
        String succeed = respResult.isSuccess() ? "成功" : "失败";
        String ip = HttpServletUtil.getIpAddress(request);
        LoginLog loginLog = new LoginLog();
        loginLog.setLogName("登出日志");
        loginLog.setSystemType("windows");
        loginLog.setMessage(respResult.getMsg());
        loginLog.setSucceed(succeed);
        loginLog.setAccount(account);
        loginLog.setCreateTime(new Date());
        loginLog.setIp(ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip);
        loginLogDAO.insertLoginLog(loginLog);

        return respResult;
    }



}
