package com.jabibim.admin.controller;


import com.jabibim.admin.domain.Teacher;
import com.jabibim.admin.service.TeacherService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value="/mypage")
public class MyPageController {

    private static final Logger logger = LoggerFactory.getLogger(MyPageController.class);

    private TeacherService teacherService;

    public MyPageController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping(value = "/detail")
    public ModelAndView teacherInfo(@RequestParam("id") String id,
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
    public String updateProcess(@ModelAttribute Teacher teacher, Model model, HttpServletRequest request,
                                @RequestParam("profileImage") MultipartFile profileImage,
                                RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession();
        String teacherId = (String)session.getAttribute("id");
        teacher.setTeacherId(teacherId);
        System.out.println("teacherId is==============" + teacherId);
        System.out.println("여기까지 왔을까???????????????????????");

        if(!profileImage.isEmpty()) {
            try {
                //파일 저장 경로부터 설쟁해야함
                String uploadDir =
                        request.getSession().getServletContext().
                                getRealPath("/resources/static/uploadFiles/profileImageUpload");
                String newFileName = teacherService.saveProfileImage(profileImage, uploadDir);


                //Teacher 객체에 저장된 파일명 설정
                teacher.setTeacherImgName(newFileName);

                teacherService.updateProfileImage(teacherId, newFileName);

            } catch(Exception e) {
                e.printStackTrace();
                model.addAttribute("message", "파일 업로드 중 오류가 발생했습니다.");
                return "error/common";
            }
        }

        int result = teacherService.update(teacher);

        if (result == 1) {
           redirectAttributes.addFlashAttribute("result", "updateSuccess");
           return "redirect:/mypage/detail?id=" + teacherId;
        } else {
            model.addAttribute("url", request.getRequestURL());
            //model.addAttribute("message", "정보수정 실패");
            return "error/common";
        }
    }

    @PostMapping(value = "/changePassword")
    public String changePassword(@RequestParam("password") String currentPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("renewPassword") String renewPassword,
                                 HttpServletRequest request, Model model, RedirectAttributes rattr) {

        HttpSession session = request.getSession();
        String teacherId = (String) session.getAttribute("id");
        System.out.println("teacherId------------------" + teacherId);


        Teacher teacher = teacherService.getTeacherById(teacherId);
        if(teacher == null ) {
            model.addAttribute("errorMessage", "해당 사용자를 찾을 수 없습니다.");
            return "error/common.html";
        }
        System.out.println("teacher=================" + teacher);

        //현재 비밀번호와 데이터베이스 비밀번호 비교
        if(!teacher.getTeacherPassword().equals(currentPassword)) {
            rattr.addAttribute("errorMessage", "현재 비밀번호가 일치하지 않습니다.");
            System.out.println("현재 비밀번호와 데이터베이스 비밀번호가 일치하지 않음!!!!!!!!!!!!");
            return "redirect:/mypage/detail?id=" + teacherId ;

        }
        System.out.println("여기까지옴=============================");

        //새 비밀번호와 재입력한 비밀번호 비교
        if(!newPassword.equals(renewPassword)) {
            rattr.addAttribute("message", "새 비밀번호와 재입력한 비밀번호가 일치하지 않습니다.");
            return "redirect:/mypage/detail?id=" + teacherId ;
        }

        //새 비밀번호 업데이트
        teacher.setTeacherPassword(newPassword);
        int updateResult = teacherService.updatePassword(teacher);

        if(updateResult == 1) {
            rattr.addFlashAttribute("result", "비밀번호 변경 성공");
            return "redirect:/mypage/detail?id=" + teacherId ;
        } else {
            rattr.addAttribute("url", request.getRequestURL());
            return "error/common";
        }

    }

    @PostMapping(value = "/checkPassword")
    @ResponseBody
    public Map<String, Object> checkPassword(@RequestBody Map<String, String> requestData, HttpServletRequest request) {
        System.out.println("여기까지 왔음!!!!!!!!!!!===========================");

        // 클라이언트에서 전달된 비밀번호를 받아옴
        String password = requestData.get("password");
        System.out.println("currentPassword================" + password);

        HttpSession session = request.getSession();
        String teacherId = (String) session.getAttribute("id");
        System.out.println("teacherId====================" + teacherId);

        Teacher teacher = teacherService.getTeacherById(teacherId);
        Map<String, Object> response = new HashMap<>();


        if (teacher != null && teacher.getTeacherPassword().equals(password)) {
            response.put("valid", true);
        } else {
            response.put("valid", false);
        }

        System.out.println("response is!!!!!!!!!!!!!!!!!!!!!" + response);
        return response;
    }





}
