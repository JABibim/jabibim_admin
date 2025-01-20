package com.jabibim.admin.controller;

import com.google.gson.Gson;
import com.jabibim.admin.domain.Course;
import com.jabibim.admin.dto.common.ApiResponse;
import com.jabibim.admin.dto.content.classes.response.SelectCourseClassDetailListResDto;
import com.jabibim.admin.dto.content.classes.response.SelectCourseClassListResDto;
import com.jabibim.admin.dto.content.course.request.InsertCourseReqDto;
import com.jabibim.admin.dto.content.course.request.SelectCourseListReqDto;
import com.jabibim.admin.dto.content.course.response.SelectCourseListResDto;
import com.jabibim.admin.func.PaginationResult;
import com.jabibim.admin.security.dto.AccountDto;
import com.jabibim.admin.service.ContentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

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

    @GetMapping(value = "/class")
    public ModelAndView classListPage(
            Authentication authentication,
            ModelAndView modelAndView,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "") String courseId,
            @RequestParam(defaultValue = "") String searchKeyword
    ) {
        AccountDto account = (AccountDto) authentication.getPrincipal();
        boolean isAdmin = account.getRoles().contains("ROLE_ADMIN");
        String academyId = account.getAcademyId();

        List<SelectCourseClassListResDto> courseClassList = contentService.getCourseClassList(isAdmin, academyId);

        List<SelectCourseClassDetailListResDto> courseClassDetailList = contentService.getCourseClassDetailList(page, limit, isAdmin, academyId, courseId, searchKeyword);
        int courseClassDetailListCount = contentService.getCourseClassDetailCount(isAdmin, academyId, courseId, searchKeyword);

        PaginationResult result = new PaginationResult(page, limit, courseClassDetailListCount);

        modelAndView.setViewName("content/class/classList");

        modelAndView.addObject("courseClassList", courseClassList);
        modelAndView.addObject("courseClassListCount", courseClassList.size());
        modelAndView.addObject("page", page);
        modelAndView.addObject("maxpage", result.getMaxpage());
        modelAndView.addObject("startpage", result.getStartpage());
        modelAndView.addObject("endpage", result.getEndpage());
        modelAndView.addObject("courseClassDetailListCount", courseClassDetailListCount);
        modelAndView.addObject("courseClassDetailList", courseClassDetailList);
        modelAndView.addObject("limit", limit);
        modelAndView.addObject("selectedCourseId", courseId);

        return modelAndView;
    }

    @GetMapping(value = "/class/addClass")
    public ModelAndView addClassPage(
            Authentication authentication
    ) {
        AccountDto account = (AccountDto) authentication.getPrincipal();
        boolean isAdmin = account.getRoles().contains("ROLE_ADMIN");
        String academyId = account.getAcademyId();

        List<SelectCourseClassListResDto> courseClassList = contentService.getCourseClassList(isAdmin, academyId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("content/class/classAdd");
        modelAndView.addObject("courseClassList", courseClassList);

        return modelAndView;
    }

    @GetMapping(value = "/addCourse")
    public String addCoursePage() {
        return "content/course/addCourse";
    }

    @GetMapping("/detail/{courseId}")
    public ModelAndView courseDetailPage(
            @PathVariable String courseId,
            ModelAndView modelAndView
    ) {
        Course course = contentService.getCourseById(courseId);

        modelAndView.setViewName("content/course/courseDetail");
        modelAndView.addObject("course", course);

        return modelAndView;
    }

    @PostMapping(value = "/addCourseProcess")
    @Transactional
    @ResponseBody
    public ResponseEntity<ApiResponse<HashMap<String, Object>>> addCourseProcess(
            Authentication authentication
            , @RequestPart("courseData") InsertCourseReqDto insertCourseReqDto
            , @RequestPart("courseImage") MultipartFile courseImage
    ) {
        try {
            AccountDto account = (AccountDto) authentication.getPrincipal();
            String teacherId = account.getId();
            String academyId = account.getAcademyId();

            contentService.addCourse(teacherId, academyId, insertCourseReqDto, courseImage);

            HashMap<String, Object> result = new HashMap<>();
            result.put("redirectUrl", "/content");
            ApiResponse<HashMap<String, Object>> response = new ApiResponse<>(true, result, "과정 추가에 성공했습니다.");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<HashMap<String, Object>> response = new ApiResponse<>(false, null, "과정 추가에 실패했습니다: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping(value = "/getCourseList")
    @Transactional(readOnly = true)
    @ResponseBody
    public ResponseEntity<ApiResponse<HashMap<String, Object>>> getCourseList(
            Authentication authentication,
            @RequestParam String search
    ) {
        try {
            Gson gson = new Gson();
            SelectCourseListReqDto selectCourseListReqDto = gson.fromJson(search, SelectCourseListReqDto.class);
            AccountDto account = (AccountDto) authentication.getPrincipal();
            boolean isAdmin = account.getRoles().contains("ROLE_ADMIN");
            String academyId = account.getAcademyId();

            int courseListCount = contentService.getCourseListCount(isAdmin, academyId, selectCourseListReqDto);
            int page = selectCourseListReqDto.getPage();
            int limit = 10;
            List<SelectCourseListResDto> courseList = contentService.getCourseList(isAdmin, academyId, page, limit, selectCourseListReqDto);
            PaginationResult paginationResult = new PaginationResult(page, limit, courseListCount);

            HashMap<String, Object> result = new HashMap<>();
            result.put("courseListCount", courseListCount);
            result.put("courseList", courseList);
            result.put("page", page);
            result.put("maxPage", paginationResult.getMaxpage());
            result.put("startPage", paginationResult.getStartpage());
            result.put("endPage", paginationResult.getEndpage());
            result.put("limit", limit);

            ApiResponse<HashMap<String, Object>> response = new ApiResponse<>(true, result, "과정 목록을 불러오는데 성공했습니다.");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<HashMap<String, Object>> response =
                    new ApiResponse<>(false, null, "과정 목록을 불러오는데 실패했습니다: " + e.getMessage());

            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping(value = "/updateCourseActivation")
    @ResponseBody
    public ResponseEntity<ApiResponse<HashMap<String, Object>>> updateCourseActivation(
            @RequestParam(required = true) String courseId,
            @RequestParam(required = true) boolean isActive
    ) {
        try {
            contentService.updateCourseActivation(courseId, isActive);
            return ResponseEntity.ok(new ApiResponse<>(true, null, "과정의 활성화 여부 업데이트가 정상적으로 처리되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, null, "과정의 활성화 여부를 업데이트 하는 도중 오류가 발생했습니다.: " + e.getMessage()));
        }
    }

    @PostMapping(value = "/modifyCourse")
    public String modifyCourse(
            Authentication authentication
            , @RequestPart("course_id") String courseId
            , @RequestPart("course_name") String courseName
            , @RequestPart("course_subject") String courseSubject
            , @RequestPart("is_profile_changed") String isProfileChanged
            , @RequestPart("course_image") MultipartFile courseImage
            , @RequestPart("course_intro") String courseInfo
            , @RequestPart("course_price") String coursePrice
            , @RequestPart("course_tag") String courseTag
            , @RequestPart("course_diff") String courseDiff
    ) {
        AccountDto account = (AccountDto) authentication.getPrincipal();
        String teacherId = account.getId();
        String academyId = account.getAcademyId();
        contentService.updateCourse(teacherId, academyId, courseId, courseName, courseSubject, isProfileChanged, courseImage, courseInfo, coursePrice, courseTag, courseDiff);

        return "redirect:/content";
    }

    @PostMapping(value = "/deleteCourse")
    @ResponseBody
    public ResponseEntity<ApiResponse<HashMap<String, Object>>> deleteCourse(
            @RequestParam(required = true) String courseId
    ) {
        try {
            contentService.deleteCourse(courseId);
            return ResponseEntity.ok(new ApiResponse<>(true, null, "과정 삭제가 정상적으로 처리되었습니다."));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, null, "과정 삭제 도중 오류가 발생했습니다.: " + e.getMessage()));
        }
    }

    @PostMapping(value = "/class/addClassProcess")
    @ResponseBody
    public ResponseEntity<ApiResponse<HashMap<String, Object>>> addClassProcess(
            Authentication authentication
            , @RequestParam String courseId
            , @RequestParam String classSubject
            , @RequestParam String classContent
            , @RequestParam String classType
    ) {
        try {
            AccountDto account = (AccountDto) authentication.getPrincipal();
            String academyId = account.getAcademyId();
            String teacherId = account.getId();

            String newClassId = contentService.addNewClassInfo(academyId, teacherId, courseId, classSubject, classContent, classType);
            HashMap<String, Object> result = new HashMap<>();
            result.put("classId", newClassId);

            ApiResponse<HashMap<String, Object>> response = new ApiResponse<>(true, result, "강의 데이터 추가를 성공했습니다.");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<HashMap<String, Object>> response = new ApiResponse<>(false, null, "강의 추가에 실패했습니다: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping(value = "/class/addClassFilesProcess")
    @ResponseBody
    public ResponseEntity<ApiResponse<HashMap<String, Object>>> addClassFilesProcess(
            Authentication authentication,
            @RequestParam("courseId") String courseId,
            @RequestParam("classId") String classId,
            @RequestParam("classType") String classType,
            @RequestParam("file") MultipartFile file
    ) {
        HashMap<String, Object> result = new HashMap<>();
        try {
            AccountDto account = (AccountDto) authentication.getPrincipal();
            String academyId = account.getAcademyId();
            String teacherId = account.getId();

            contentService.addNewClassFileInfo(academyId, teacherId, courseId, classId, classType, file);

            result.put("status", "success");

            ApiResponse<HashMap<String, Object>> response = new ApiResponse<>(true, result, "강의 데이터 추가를 성공했습니다.");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            result.put("status", "fail");

            ApiResponse<HashMap<String, Object>> response = new ApiResponse<>(false, result, "강의 추가에 실패했습니다: " + e.getMessage());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}