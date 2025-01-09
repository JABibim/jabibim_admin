package com.jabibim.admin.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping(value = "/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping(value = "/dashboard")
    public String dashBoard() {
        return "dashboard";
    }

    @GetMapping("/message")
    public String message(HttpServletRequest request) {
        return "message/message_modal";
    }
}
