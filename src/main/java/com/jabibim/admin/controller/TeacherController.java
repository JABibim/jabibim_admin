package com.jabibim.admin.controller;

import com.jabibim.admin.domain.PaginationResult;
import com.jabibim.admin.domain.Teacher;
import com.jabibim.admin.domain.TeacherCareer;
import com.jabibim.admin.service.TeacherService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(value = "/teacher")
public class TeacherController {
    private static final Logger logger = LoggerFactory.getLogger(TeacherController.class);

    private TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }


    @GetMapping(value = "")
    public String teacher(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "option1") String state,
            @RequestParam(defaultValue = "0") String search_field,  //0:전체, 1:이메일 2:이름
            @RequestParam(defaultValue = "") String search_word,
            Model model,
            HttpSession session) {

        logger.info("---------->>>>> page: {}, limit: {}, state: {}, search_field: {}, search_word: {}",
                page, limit, state, search_field, search_word);

        session.setAttribute("referer", "list");
        String academyId = (String) session.getAttribute("aid");
        boolean isAdmin = academyId.equals("ADMIN");

        // 총 선생 수를 받아옴
        int listcount = teacherService.getTeacherCount(state, search_field, search_word);

        // 선생 리스트를 받아옴
        List<Teacher> list = teacherService.getTeacherList(page, limit, academyId, isAdmin, state, search_field, search_word);

        // Pagination 객체 생성
        PaginationResult result = new PaginationResult(page, limit, listcount);


        // 모델에 데이터 추가
        model.addAttribute("page", page);
        model.addAttribute("maxpage", result.getMaxpage());
        model.addAttribute("startpage", result.getStartpage());
        model.addAttribute("endpage", result.getEndpage());
        model.addAttribute("listcount", listcount);
        model.addAttribute("teacherlist", list);
        model.addAttribute("limit", limit);
        model.addAttribute("state", state);
        model.addAttribute("search_field", search_field);
        model.addAttribute("search_word", search_word);
        model.addAttribute("startnumber",(page - 1) * limit + 1 );

        return "teachers/teacher";
    }


    @GetMapping(value ="/profile")
    public String teacherProfile(
            Model model, HttpSession session, HttpServletRequest request) {

        session.setAttribute("referer", "list");
        String academyId = (String) session.getAttribute("aid");
        boolean isAdmin = academyId.equals("ADMIN");

        List<TeacherCareer> list = teacherService.getcareerList(isAdmin, academyId);

        model.addAttribute("contextPath", request.getContextPath());
        model.addAttribute("academyId", academyId);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("careerList",list);
        return "teachers/teacherprofile";
    }

    // 약력 상태를 업데이트하는 메서드
    @PostMapping("/updateCareerActive")
    @ResponseBody
    public ResponseEntity<String> updateCareerActive(
            @RequestParam("careerName") String careerName,
            @RequestParam("displayStatus") int displayStatus) {

        try {
            System.out.println("------updateCareerActiveController 실행---------");
            System.out.println("받은 careerName: " + careerName);
            System.out.println("받은 displayStatus: " + displayStatus);

            // 선택된 항목을 1로 설정하고, 나머지는 0으로 초기화하는 로직
            if (displayStatus == 1) {
                teacherService.resetAllCareers(); // 모든 항목을 0으로 초기화
            }

            // 선택된 항목만 활성화 상태(1)로 설정
            int result = teacherService.updateCareerActive(careerName, displayStatus);

            if (result > 0) {
                return ResponseEntity.ok("약력 상태가 성공적으로 업데이트되었습니다.");
            } else {
                return ResponseEntity.status(400).body("약력 상태 업데이트 실패.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("서버 오류가 발생했습니다.");
        }
    }



    @GetMapping(value ="/write")
    public String teacherWrite(Model model, HttpServletRequest request) {
        model.addAttribute("contextPath", request.getContextPath());
        return "teachers/profile_write";
    }
    
    
    //어디에 만들어야 제일 좋을지 모르겠는데, teacher 계정들을 피해 만드는거니
    //그냥 TeacherController 에 만듦
    @GetMapping(value = "/prepareDashboard")
    public String prepareDashboard(HttpSession session, Model model) {

        //Optional 로 일단 teacherId 추출하기
        Optional<String> teacherIdOpt = Optional.ofNullable((String) session.getAttribute("id"));

        //teacherId 가 없으면 로그인 페이지로 리다이렉트
        if (teacherIdOpt.isEmpty()) {
            return "redirect:/login";
        }

        String teacherId = teacherIdOpt.get();

        // teacherService 로부터 Teacher 객체를 Optional 로 감싸서 처리
        Optional<Teacher> teacherInfoOpt = Optional.ofNullable(teacherService.getTeacherById(teacherId));

        //teacherInfo 가 없으면 default 이미지 쓰기
        String teacherImgName = teacherInfoOpt
                .filter(teacher -> !"ADMIN".equals(teacherId))
                .map(Teacher::getTeacherImgName)
                .orElse("");

        model.addAttribute("teacherImgName", teacherImgName);

        return "redirect:/dashboard";
    }

}
