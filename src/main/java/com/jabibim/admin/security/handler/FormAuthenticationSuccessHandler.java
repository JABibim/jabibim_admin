package com.jabibim.admin.security.handler;

import com.jabibim.admin.security.util.FnUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
        setDefaultTargetUrl("/dashboard");

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        // TODO 추후 IP가 필요한 경우 사용 가능 ( 시간은 추가해야함 )
        // principal = AccountDto(id=ADMIN, username=abcd1234@naver.com, password=$2a$10$6kGaYEczpa1PDdp1Wc2NjuWo/dcxHHYPieHjO8wS3M8A.T7tsoybG, roles=TEACHER)
        Object principal = authentication.getPrincipal();
        // ipAddr = 0:0:0:0:0:0:0:1
        String ipAddr = FnUtil.getIpAddr(request);

        if (savedRequest != null) {
            String targetUrl = savedRequest.getRedirectUrl();

            redirectStrategy.sendRedirect(request, response, targetUrl);
        } else {
            redirectStrategy.sendRedirect(request, response, getDefaultTargetUrl());
        }
    }
}
