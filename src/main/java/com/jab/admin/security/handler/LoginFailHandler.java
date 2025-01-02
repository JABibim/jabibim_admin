package com.jab.admin.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LoginFailHandler implements AuthenticationFailureHandler {
    private static final Logger logger = LoggerFactory.getLogger(LoginFailHandler.class);

    @Override
    public void onAuthenticationFailure(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception
    ) throws IOException, ServletException {
        logger.info("📍 로그인 실패 (LoginFailHandler.java) ");
        logger.info(exception.getMessage());

        HttpSession session = request.getSession();
        session.setAttribute("loginFail", "입력하신 이메일과 비밀번호를 확인해 주세요.");

        String url = request.getContextPath() + "/member/login";

        response.sendRedirect(url);
    }
}
