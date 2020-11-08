package cn.edu.lingnan.mooc.config.handle;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
	@Autowired
	private ObjectMapper objectMapper; //spring在启动时会自动注入ObjectMapper

	
	//登录成功之后调用
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		    //Authentication 类封装了用户认证信息
		    logger.info("登录成功");
			//设置contentType
			response.setContentType("application/json;charset=UTF-8");
			//把信息转成json字符串，写回给浏览器
			response.getWriter().write(objectMapper.writeValueAsString(authentication));

		}

}
