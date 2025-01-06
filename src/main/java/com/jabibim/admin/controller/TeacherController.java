package com.jabibim.admin.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/teacher")
public class TeacherController {
    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

    @GetMapping(value ="")
    public String teacher() {
        return "teachers/teacher";
    }


    @GetMapping(value ="/profile")
    public String teacherProfile(Model model, HttpServletRequest request) {
        model.addAttribute("contextPath", request.getContextPath());
        return "teachers/teacherprofile";
    }

    @GetMapping(value ="/write")
    public String teacherWrite(Model model, HttpServletRequest request) {
        model.addAttribute("contextPath", request.getContextPath());
        return "teachers/profile_write";
    }

}
