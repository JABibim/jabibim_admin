package com.jabibim.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/teacher")
public class TeacherController {

    @GetMapping(value = "/test")
    public String test() {


        return null;
    }
}
