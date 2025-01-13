package com.jabibim.admin.controller;

import com.google.gson.Gson;
import com.jabibim.admin.domain.Course;
import com.jabibim.admin.dto.common.ApiResponse;
import com.jabibim.admin.dto.content.course.AddCourseDto;
import com.jabibim.admin.dto.content.course.GetCourseListDto;
import com.jabibim.admin.security.dto.AccountDto;
import com.jabibim.admin.service.ContentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value = "/content")
public class ContentController {
    private final ContentService contentService;

    public ContentController(ContentService contentService) {
        this.contentService = contentService;
    }

    @GetMapping(value = "")
    public String courseListPage() {
        return "content/course/courseList";
    }

    @GetMapping(value = "/addCourse")
    public String addCoursePage() {
        return "content/course/addCourse";
    }

    @PostMapping(value = "addCourseProcess")
    @Transactional
    @ResponseBody
    public String addCourseProcess(
            Authentication authentication,
            @RequestPart("courseData") AddCourseDto addCourseDto,
            @RequestPart("courseImage") MultipartFile courseImage
    ) {
        AccountDto account = (AccountDto) authentication.getPrincipal();
        String teacherId = account.getId();
        String academyId = account.getAcademyId();

        contentService.addCourse(teacherId, academyId, addCourseDto, courseImage);

        return "redirect:/content";
    }

    // TODO 🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨 일단 보류 🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨
//    @GetMapping(value = "/getCourseList")
//    @Transactional(readOnly = true)
//    @ResponseBody
//    public ResponseEntity<ApiResponse<HashMap<String, Object>>> getCourseList(
//            Authentication authentication,
//            @RequestParam String search
//    ) {
//        try {
//            Gson gson = new Gson();
//            AccountDto account = (AccountDto) authentication.getPrincipal();
//            boolean isAdmin = account.getRoles().contains("ROLE_ADMIN");
//            List<Course> courseList = contentService.getCourseList(isAdmin, search);
////            GetCourseListDto getCourseListDto = gson.fromJson(search, GetCourseListDto.class);
//
//        } catch (Exception e) {
//            ApiResponse<HashMap<String, Object>> response =
//                    new ApiResponse<>(false, null, "과정 목록을 불러오는데 실패했습니다: " + e.getMessage());
//
//
//            return ResponseEntity.badRequest().body(response);
//        }
//
//        return null;
////        try {
////            HashMap<String, Object> result = new HashMap<>();
////            result.put("courseList", settingService.getCourseList());
////            result.put("courseListCount", settingService.getCourseListCount());
////            ApiResponse<HashMap<String, Object>> response = new ApiResponse<>(true, result, "과정 목록을 불러오는데 성공했습니다.");
////
////            return ResponseEntity.ok(response);
//    }
    // TODO 🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨 일단 보류 🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨🚨
}