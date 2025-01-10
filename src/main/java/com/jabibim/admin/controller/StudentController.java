package com.jabibim.admin.controller;


import com.jabibim.admin.domain.PaginationResult;
import com.jabibim.admin.domain.Student;
import com.jabibim.admin.service.StudentService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(value = "/student")
public class StudentController {
    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("")
    public String student(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "option1") String state,
            @RequestParam(defaultValue = "null") String startDate, // startDate 초기화
            @RequestParam(defaultValue = "null") String endDate,   // endDate 초기화
            @RequestParam(defaultValue = "0") String studentGrade,  //등급 0:전체 1:브론즈 2:실버 3:골드
            @RequestParam(defaultValue = "0") String search_field,  // 0:전체, 1:이름, 2:이메일
            @RequestParam(defaultValue = "") String search_word,
            Model model,
            HttpSession session) {

        logger.info("---------->>>>> page: {}, limit: {}, state: {}, startDate: {}, endDate: {}, studentGrade:{}, search_field: {}, search_word: {}",
                page, limit, state, startDate, endDate, studentGrade,search_field, search_word);

        session.setAttribute("referer", "list");
        String academyId = (String) session.getAttribute("aid");
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
        model.addAttribute("startnumber",(page - 1) * limit + 1 );

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
        model.addAttribute("startnumber",(page - 1) * limit + 1 );

        return "students/studentad";
    }

    @GetMapping("/grade")
    public String studentGrade() {
        return "students/studentGrade";
    }

}
