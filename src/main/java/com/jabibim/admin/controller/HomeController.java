package com.jabibim.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping(value = "/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping(value = "/login")
    public String loginForm() {
        return "member/login";
    }

    @GetMapping(value = "/dashboard")
    public String dashBoard() {
        return "dashboard";
    }
}
