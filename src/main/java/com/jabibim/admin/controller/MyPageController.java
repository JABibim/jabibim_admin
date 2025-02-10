package com.jabibim.admin.controller;

import com.jabibim.admin.domain.Teacher;
import com.jabibim.admin.dto.TeacherProfileDTO;
import com.jabibim.admin.func.Files;
import com.jabibim.admin.security.dto.AccountDto;
import com.jabibim.admin.service.S3FileService;
import com.jabibim.admin.service.TeacherService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/mypage")
public class MyPageController {
    private static final Logger logger = LoggerFactory.getLogger(MyPageController.class);
    private final TeacherService teacherService;
    private final S3FileService s3FileService;
    private final PasswordEncoder passwordEncoder;

    public MyPageController(TeacherService teacherService, S3FileService s3FileService, PasswordEncoder passwordEncoder) {
        this.teacherService = teacherService;
        this.s3FileService = s3FileService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(value = "/detail")
    public ModelAndView teacherInfo(@RequestParam("id") String id,
                                    ModelAndView mv,
                                    HttpServletRequest request) {

        TeacherProfileDTO t = teacherService.teacherInfo(id);

        if (t != null) {
            mv.setViewName("member/myProfile");
            mv.addObject("teacherInfo", t);
        } else {
            mv.addObject("url", request.getRequestURL());
            mv.addObject("message", "해당 정보가 없습니다.");
            mv.setViewName("error/404");
        }
        return mv;
    }

    @PostMapping(value = "/updateProcess")
    public String updateProcess(
            Authentication authentication,
            @ModelAttribute Teacher teacher
            , Model model
            , HttpServletRequest request
            , @RequestParam("teacherProfileImage") MultipartFile teacherProfileImage
            , RedirectAttributes redirectAttributes
            , HttpSession session
    ) {
        String teacherId = (String) session.getAttribute("id");
        String academyId = (String) session.getAttribute("aid");
        teacher.setTeacherId(teacherId);

        if (!teacherProfileImage.isEmpty()) {
            if (teacherProfileImage.getOriginalFilename() == null) {
                throw new IllegalArgumentException("파일 이름을 확인해 주세요.");
            }
            String fileName = "profile." + Files.getExtension(teacherProfileImage.getOriginalFilename());
            String dirName = academyId + "/teacher/" + teacherId + "/profile/" + fileName;
            String uploadedPath = s3FileService.uploadFile(teacherProfileImage, dirName);

            teacher.setTeacherProfileOriginName(teacherProfileImage.getOriginalFilename());
            teacher.setTeacherProfilePath(uploadedPath);
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
        System.out.println("teacherId=============================" + teacherId);

        Teacher teacher = teacherService.getTeacherById(teacherId);
        if (teacher == null) {
            model.addAttribute("errorMessage", "해당 사용자를 찾을 수 없습니다.");
            return "error/common.html";
        }

        //현재 비밀번호와 데이터베이스 비밀번호 비교
        if (!passwordEncoder.matches(currentPassword, teacher.getTeacherPassword())) {
            rattr.addAttribute("errorMessage", "현재 비밀번호가 일치하지 않습니다.");
            return "redirect:/mypage/detail?id=" + teacherId;
        }

        //새 비밀번호와 재입력한 비밀번호 비교
        if (!newPassword.equals(renewPassword)) {
            rattr.addAttribute("message", "새 비밀번호와 재입력한 비밀번호가 일치하지 않습니다.");
            return "redirect:/mypage/detail?id=" + teacherId;
        }

        //새 비밀번호 업데이트
        teacher.setTeacherPassword(passwordEncoder.encode(newPassword));
        int updateResult = teacherService.updatePassword(teacher);

        if (updateResult == 1) {
            rattr.addFlashAttribute("result", "비밀번호 변경 성공");
            return "redirect:/mypage/detail?id=" + teacherId;
        } else {
            rattr.addAttribute("url", request.getRequestURL());
            return "error/common";
        }
    }

    @PostMapping(value = "/checkPassword")
    @ResponseBody
    public Map<String, Object> checkPassword(@RequestBody Map<String, String> requestData,
                                             HttpServletRequest request) {

        // 클라이언트에서 전달된 비밀번호를 받아옴
        String password = requestData.get("password");  //입력받은 패스워드 값

        HttpSession session = request.getSession();
        String teacherId = (String) session.getAttribute("id");
        Teacher teacher = teacherService.getTeacherById(teacherId);

        Map<String, Object> response = new HashMap<>();
        if (teacher != null && passwordEncoder.matches(password, teacher.getTeacherPassword())) {
            response.put("valid", true);
        } else {
            response.put("valid", false);
        }
        return response;
    }
}
