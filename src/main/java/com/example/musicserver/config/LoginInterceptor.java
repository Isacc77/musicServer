package com.example.musicserver.config;

import com.example.musicserver.tools.MyConstant;
import com.example.musicserver.tools.ResponseBodyMessage;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {

        HttpSession httpSession = request.getSession(false);

        if (httpSession != null &&
                httpSession.getAttribute(MyConstant.USERINFO_SESSION_KEY) != null) {
            return true;
        }
        System.out.println("Please, login your account.");
        return false;
    }
}
