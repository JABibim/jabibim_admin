package com.jabibim.admin.controller;

import com.jabibim.admin.security.dto.AccountDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    @GetMapping(value = "/login")
    public String loginForm() {
        return "member/login";
    }

    @GetMapping(value = "/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        // 인증 객체가 필요
        Authentication authentication = SecurityContextHolder.getContextHolderStrategy().getContext().getAuthentication();

        if ( authentication != null ) {
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
