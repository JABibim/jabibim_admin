package com.jabibim.admin.controller;

import com.jabibim.admin.domain.Teacher;
import com.jabibim.admin.service.TeacherServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    private final TeacherServiceImpl teacherServiceImpl;

    public HomeController(TeacherServiceImpl teacherServiceImpl) {
        this.teacherServiceImpl = teacherServiceImpl;
    }

    @GetMapping(value = "/")
    public String home() {
        return "redirect:member/login";
    }

    @GetMapping(value = "/dashboard")
    public String dashBoard(HttpSession session, Model model) {
        String teacherId = (String) session.getAttribute("id");
        Teacher teacherInfo = teacherServiceImpl.getTeacherById(teacherId);
        System.out.println("teacherInfo============" + teacherInfo);

        model.addAttribute("teacherImgName", teacherInfo.getTeacherImgName());
        return "dashboard";
    }
}
