package com.jabibim.admin.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/student")
public class StudentController {
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    @GetMapping("")
    public String student() {

        logger.info("student");
        return "students/student";
    }

    @GetMapping("/ad")
    public String studentad() {
        return "students/studentad";
    }

    @GetMapping("/grade")   //오류수정필요
    public String studentgrade() {
        return "students/studentgrade";
    }

    @GetMapping("/resignhist")  //오류수정필요
    public String resignhist() {
        return "students/studentresignhist";
    }

}
