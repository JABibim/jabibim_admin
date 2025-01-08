package com.jabibim.admin.controller;

import com.jabibim.admin.security.dto.AccountDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    private final RequestCache requestCache = new HttpSessionRequestCache();

    @GetMapping(value = "/login")
    public String loginForm(Authentication authentication, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (authentication != null && authentication.isAuthenticated()) {
            // 이전 경로 가져오기
            SavedRequest savedRequest = requestCache.getRequest(request, response);

            if (savedRequest != null) {
                String targetUrl = savedRequest.getRedirectUrl();
                response.sendRedirect(targetUrl); // 이전 경로로 리다이렉트
            } else {
                response.sendRedirect("/dashboard"); // 이전 경로가 없으면 기본 경로로 리다이렉트
            }
            return null;
        }

        return "member/login";
    }

    @GetMapping(value = "/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // 인증 객체가 필요
        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();

        if (authentication != null) {
            new SecurityContextLogoutHandler()
                    .logout(request, response, authentication);
        }

        return "redirect:/login";
    }

    @GetMapping("/denied")
    public String accessDenied(
            @RequestParam(value = "exception", required = false) String exception,
            @AuthenticationPrincipal AccountDto accountDto,
            Model model
    ) {
        model.addAttribute("username", accountDto.getUsername());
        model.addAttribute("exception", exception);

        return "error/403";
    }
}
