package cn.edu.lingnan.mooc.config.handle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.edu.lingnan.mooc.common.model.RespResult;
import cn.edu.lingnan.mooc.model.MenuTree;
import cn.edu.lingnan.mooc.model.MoocManager;
import cn.edu.lingnan.mooc.model.UserToken;
import cn.edu.lingnan.mooc.repository.MoocManagerRepository;
import cn.edu.lingnan.mooc.service.MoocUserDetailService;
import cn.edu.lingnan.mooc.util.RedisUtil;
import cn.edu.lingnan.mooc.vo.LoginSuccessVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class MoocAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    //spring在启动时会自动注入ObjectMapper
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MoocUserDetailService moocUserDetailService;
    @Resource
    private MoocManagerRepository moocManagerRepository;

    //登录成功之后调用
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        //Authentication 类封装了用户认证信息

        // 获取用户，获取权限
        String account = authentication.getName();
        Optional<MoocManager> optional = moocUserDetailService.findManagerByAccount(account);
        MoocManager moocManager = optional.get();
        List<MenuTree> menuTreeList = moocUserDetailService.getMenuByManagerId(moocManager.getId());

        List<MenuTree> permissionList = new ArrayList<>();
        List<MenuTree> menuList = new ArrayList<>();
        // 获取到用户可见的菜单权限、权限列表
        menuTreeList.forEach(item -> {
            if (item.getRouter() != null) {
                menuList.add(item);
            } else {
                permissionList.add(item);
            }
        });

        String permissionStr = menuTreeList.stream().map(MenuTree::getPermission).collect(Collectors.joining(","));

        String token = UUID.randomUUID().toString();

        // 构造UserToken对象，存redis
        UserToken userToken = new UserToken();
        userToken.setAccount(authentication.getName());
        userToken.setSessionId(request.getSession().getId());
        userToken.setToken(token);
        userToken.setUserId(moocManager.getId());
        userToken.setPermission(permissionStr);
        // token存redis，30分钟有效期
        RedisUtil.set(token, userToken, 1800);


        // 构造登录成功返回给前端的对象(token、菜单权限)
        LoginSuccessVO loginSuccessVO = new LoginSuccessVO();
        loginSuccessVO.setToken(token);
        loginSuccessVO.setMenuList(menuList);
        RespResult respResult = RespResult.success(loginSuccessVO, "登录成功");

        logger.info("登录成功");
        //设置contentType
        response.setContentType("application/json;charset=UTF-8");
        //把信息转成json字符串，写回给浏览器
        response.getWriter().write(objectMapper.writeValueAsString(respResult));

    }

}
