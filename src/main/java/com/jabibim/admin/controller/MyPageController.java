package com.jabibim.admin.controller;


import com.jabibim.admin.domain.Teacher;
import com.jabibim.admin.service.TeacherService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value="/mypage")
public class MyPageController {

    private static final Logger logger = LoggerFactory.getLogger(MyPageController.class);

    private TeacherService teacherService;

    public MyPageController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping(value = "/detail")
    public ModelAndView teacherInfo(String id,
                                    ModelAndView mv,
                                    HttpServletRequest request) {

        System.out.println("id-=================" + id);


        Teacher t = teacherService.teacherInfo(id);

        if( t != null) {
            mv.setViewName("member/teacherProfile");
            mv.addObject("teacherInfo", t);
        } else {
            mv.addObject("url", request.getRequestURL());
            mv.addObject("message", "해당 정보가 없습니다.");
            mv.setViewName("error/404");
        }
        return mv;
    }

    @PostMapping(value="/updateProcess")
    public String updateProcess(Teacher teacher, Model model, HttpServletRequest request,
                                RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession();
        String id = (String)session.getAttribute("id");

        System.out.println("아이디는==============" + id );

        int result = teacherService.update(teacher);


        System.out.println("result================" + result);

        if (result == 1) {
           redirectAttributes.addFlashAttribute("result", "updateSuccess");
           return "redirect:/mypage/detail";
        } else {
            model.addAttribute("url", request.getRequestURL());
            //model.addAttribute("message", "정보수정 실패");
            return "error/common";
        }
    }


}
