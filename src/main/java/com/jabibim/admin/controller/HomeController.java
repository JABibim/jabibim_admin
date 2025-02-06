package com.jabibim.admin.controller;

import com.jabibim.admin.dto.OAuth.CustomOAuth2User;
import com.jabibim.admin.service.AcademyService;
import com.jabibim.admin.service.TeacherService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    private final AcademyService academyService;
    private final TeacherService teacherService;

    public HomeController(AcademyService academyService, TeacherService teacherService) {
        this.academyService = academyService;
        this.teacherService = teacherService;
    }

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

    @GetMapping("/authenticationCode")
    public String authenticationCode(HttpServletRequest request) {
        return "member/authenticationCode";
    }

    @PostMapping("/authenticationCode")
    public String verifyAuthenticationCode(@RequestParam String code, Model model, HttpSession session) {
        boolean isValid = academyService.isCodeValid(code); // 인증 코드 유효성 체크

        if (isValid) {
            String academyId = academyService.getAcademyIdByCode(code); // 해당 코드의 academy_id 가져오기
            session.setAttribute("aid", academyId);
            String teacheremail = (String) session.getAttribute("email"); // 로그인한 teacherId 가져오기
            String teacherId = teacherService.getTeacherIdByEmail(teacheremail);
            System.out.println("getAcademyIdByCode-------------------:" + academyId);
            System.out.println("teacheremail---------------------:" + teacheremail);
            System.out.println("teacherId---------------------------:" + teacherId);

            if (teacherId != null)
                teacherService.updateTeacherAcademy(teacherId, academyId, code); // Teacher 테이블 업데이트
            return "redirect:/dashboard";  // 인증 성공 시 대시보드로 이동

        }

        model.addAttribute("error", "잘못된 인증 코드입니다."); // 실패 시 에러 메시지
        return "member/authenticationCode";
    }

}
