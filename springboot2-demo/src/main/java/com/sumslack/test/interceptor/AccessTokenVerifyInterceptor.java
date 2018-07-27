package com.sumslack.test.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.google.common.base.Optional;
import com.sumslack.test.controller.LoginController;
import com.sumslack.test.request.User;
@Component
public class AccessTokenVerifyInterceptor extends HandlerInterceptorAdapter{
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        boolean flag = false;
        // token
        String accessToken = request.getParameter("token");
        if (!StringUtils.isEmpty(accessToken)) {
        	
        	Optional<User> user = LoginController.userCache.get(accessToken);
            if(user.isPresent()) {
            	request.setAttribute("user", user);
            	return true;
            }
        }
        if (!flag) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().print("AccessToken ERROR");
        }
 
        return flag;
    }
}
