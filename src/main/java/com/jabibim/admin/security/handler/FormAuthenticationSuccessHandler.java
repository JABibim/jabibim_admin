package com.jabibim.admin.security.handler;

import com.jabibim.admin.security.dto.AccountDto;
import com.jabibim.admin.security.util.FnUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FormAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final RequestCache requestCache = new HttpSessionRequestCache();
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        System.out.println("==========onAuthenticationSuccess====authentication===: " + authentication.getPrincipal());

        setDefaultTargetUrl("/dashboard");

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        setSession(authentication, request);

        // TODO 추후 IP가 필요한 경우 사용 가능 ( 시간은 추가해야함 )
        Object principal = authentication.getPrincipal();
        String ipAddr = FnUtil.getIpAddr(request);

        if (savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();

            redirectStrategy.sendRedirect(request, response, targetUrl);
        } else {
            redirectStrategy.sendRedirect(request, response, getDefaultTargetUrl());
        }
    }

    private void setSession(Authentication authentication, HttpServletRequest request) {
        HttpSession session = request.getSession();
        AccountDto account = (AccountDto) authentication.getPrincipal();

        session.setAttribute("isAdmin", account.getId().equals("ADMIN"));
        session.setAttribute("id", account.getId());
        session.setAttribute("aid", account.getAcademyId());
        session.setAttribute("role", account.getRoles());
        session.setAttribute("name", account.getName());
        session.setAttribute("email", account.getEmail());
    }
}
