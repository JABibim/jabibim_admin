package com.jabibim.admin.controller;

import com.jabibim.admin.domain.Grade;
import com.jabibim.admin.domain.PaginationResult;
import com.jabibim.admin.domain.Student;
import com.jabibim.admin.dto.DeleteGradeDTO;
import com.jabibim.admin.dto.GetStudentGradesDTO;
import com.jabibim.admin.func.UUIDGenerator;
import com.jabibim.admin.security.dto.AccountDto;
import com.jabibim.admin.service.GradeService;
import com.jabibim.admin.service.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/student")
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    private final StudentService studentService;
    private final GradeService gradeService;

    public StudentController(StudentService studentService, GradeService gradeService) {
        this.studentService = studentService;
        this.gradeService = gradeService;
    }

    @GetMapping("")
    public String student(
            Authentication authentication,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "option1") String state,
            @RequestParam(defaultValue = "null") String startDate, // startDate 초기화
            @RequestParam(defaultValue = "null") String endDate,   // endDate 초기화
            @RequestParam(defaultValue = "0") String studentGrade,  //등급 0:전체 1:브론즈 2:실버 3:골드
            @RequestParam(defaultValue = "0") String search_field,  // 0:전체, 1:이름, 2:이메일
            @RequestParam(defaultValue = "") String search_word,
            Model model,
            HttpSession session, HttpServletRequest request) {

        System.out.println("authentication==========:" + authentication.getPrincipal().toString());

        logger.info("---------->>>>> page: {}, limit: {}, state: {}, startDate: {}, endDate: {}, studentGrade:{}, search_field: {}, search_word: {}",
                page, limit, state, startDate, endDate, studentGrade, search_field, search_word);

        session.setAttribute("referer", "list");
        String academyId =  (String) session.getAttribute("aid");
        boolean isAdmin = academyId.equals("ADMIN");
        // 총 학생 수를 받아옴
        int listcount = studentService.getStudentCount(academyId, isAdmin, state, startDate, endDate, studentGrade, search_field, search_word);

        // 학생 리스트를 받아옴
        List<Student> list = studentService.getStudentList(page, limit, academyId, isAdmin, state, startDate, endDate, studentGrade, search_field, search_word);

        // Pagination 객체 생성
        PaginationResult result = new PaginationResult(page, limit, listcount);


        // 모델에 데이터 추가
        model.addAttribute("page", page);
        model.addAttribute("maxpage", result.getMaxpage());
        model.addAttribute("startpage", result.getStartpage());
        model.addAttribute("endpage", result.getEndpage());
        model.addAttribute("listcount", listcount);
        model.addAttribute("studentlist", list);
        model.addAttribute("limit", limit);
        model.addAttribute("state", state);
        model.addAttribute("studentGrade", studentGrade);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("search_field", search_field);
        model.addAttribute("search_word", search_word);
        model.addAttribute("startnumber", (page - 1) * limit + 1);

        return "students/student";
    }

    @GetMapping("/ad")
    public String studentad(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "0") String search_field,  //0:전체, 1:이름
            @RequestParam(defaultValue = "") String search_word,
            Model model,
            HttpSession session) {

        logger.info("---------->>>>> page: {}, limit: {}, search_field: {}, search_word: {}",
                page, limit, search_field, search_word);

        session.setAttribute("referer", "list");
        String academyId = (String) session.getAttribute("aid");
        boolean isAdmin = academyId.equals("ADMIN");

        //총 학생마케팅동의여부 수 받아옴
        int listcount = studentService.getStudentAdCount(academyId, isAdmin, search_field, search_word);

        //총 리스트 받아옴
        List<Student> list = studentService.getStudentAdList(page, limit, academyId, isAdmin, search_field, search_word);

        // Pagination 객체 생성
        PaginationResult result = new PaginationResult(page, limit, listcount);
        
        // 모델에 데이터 추가
        model.addAttribute("page", page);
        model.addAttribute("maxpage", result.getMaxpage());
        model.addAttribute("startpage", result.getStartpage());
        model.addAttribute("endpage", result.getEndpage());
        model.addAttribute("listcount", listcount);
        model.addAttribute("studentAdlist", list);
        model.addAttribute("limit", limit);
        model.addAttribute("search_field", search_field);
        model.addAttribute("search_word", search_word);
        model.addAttribute("startnumber", (page - 1) * limit + 1);

        return "students/studentad";
    }

    //등급관리 클릭시 나오는 페이지, DB 에서 등급 리스트 불러와 뿌려줘야함
    @GetMapping("/grade")
    public String studentGrade(Model model, Authentication auth) {
        AccountDto account = (AccountDto) auth.getPrincipal();
        String academyId = account.getAcademyId();

        List<GetStudentGradesDTO> grades = gradeService.getStudentGrades(academyId);
        model.addAttribute("gradeList", grades);
        return "students/studentGrade";
    }

    @PostMapping(value = "/grade/addGrade")
    @ResponseBody
    public ResponseEntity<?> addGrade(@RequestBody Grade grade, Authentication auth) {
        try {
            //필요한 정보 선언
            AccountDto account = (AccountDto) auth.getPrincipal();
            String academyId = account.getAcademyId();
            String gradeId = UUIDGenerator.getUUID();

            gradeService.addGrade(grade, academyId, gradeId);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "등급이 성공적으로 추가되었습니다."
            ));
        } catch (Exception e) {
            logger.error("등급 추가중 에러 발생 : ", e);
            return ResponseEntity.status(500).body(Map.of(
                    "status", "fail",
                    "message", "등급 추가 실패 : " + e.getMessage()
            ));
        }
    }

    @PostMapping(value = "/grade/modifyGradeInfo")
    @ResponseBody
    public ResponseEntity<?> modifyGradeInfo(@RequestBody Grade grade, Authentication auth) {
        try {
            AccountDto account = (AccountDto) auth.getPrincipal();
            String academyId = account.getAcademyId();

            gradeService.modifyGrade(grade, academyId);
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "등급이 성공적으로 수정되었습니다."
            ));
        } catch (Exception e) {
            logger.error("등급 수정중 에러 발생 : ", e);
            return ResponseEntity.status(500).body(Map.of(
                    "status", "fail",
                    "message", "등급 수정 실패 : " + e.getMessage()
            ));
        }
    }

    //등급 삭제시, 삭제되는 등급의 대체제 등급들 리스트롤 보여줘야함
    @GetMapping(value = "/grade/getUpdatableGradeList")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getUpdatableGradeList(@RequestParam String academyId,
                                                                     @RequestParam String gradeId) {
        try {
            Grade grade = new Grade();
            grade.setAcademyId(academyId);
            grade.setGradeId(gradeId);

            List<Grade> gradeList = gradeService.getUpdatableGradeList(grade);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "등급이 성공적으로 조회되었습니다.",
                    "gradeList", gradeList
            ));
        } catch (Exception e) {
            logger.error("등급 조회 중 에러 발생 : " + e);
            return ResponseEntity.ok(Map.of(
                    "status", "fail",
                    "message", "등급 조회에 실패하였습니다."
            ));
        }
    }

    @PostMapping(value = "/grade/deleteGrade")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteGrade(@RequestParam("academyId") String academyId,
                                                           @RequestParam("gradeId") String gradeId,
                                                           @RequestParam("newGradeId") String newGradeId) {
        try {
            DeleteGradeDTO deleteGradeDTO = new DeleteGradeDTO(academyId, gradeId, newGradeId);
            gradeService.deleteGrade(deleteGradeDTO);
            studentService.replaceGrade(deleteGradeDTO);

            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "등급의 삭제/조정이 완료되었습니다."
            ));
        } catch (Exception e) {
            logger.error("등급 삭제/조정 수행중 에러 발생 : ", e);
            return ResponseEntity.ok(Map.of(
                    "status", "fail",
                    "message", "등급 삭제/조정에 실패하였습니다."
            ));
        }
    }


}
