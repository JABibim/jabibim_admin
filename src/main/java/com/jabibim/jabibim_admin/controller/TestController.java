package com.jabibim.jabibim_admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
    @GetMapping("/test")
    public String test() {
        System.out.println("==> TestController.test()");

        return "test1/sample1";
    }
}
