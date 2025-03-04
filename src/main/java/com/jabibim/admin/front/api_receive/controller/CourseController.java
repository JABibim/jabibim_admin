package com.jabibim.admin.front.api_receive.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jabibim.admin.dto.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jabibim.admin.front.security.custom.JwtCustomUserDetails;
import com.jabibim.admin.service.CourseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final Logger logger = LoggerFactory.getLogger(CourseController.class);

    @GetMapping("/list")
    public ResponseEntity<?> getCourseInfoList(Authentication auth) {
        logger.info("getCourseInfoList 호출");

        if (auth == null) {
            logger.error("인증 실패");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "인증 실패"));
        }

        JwtCustomUserDetails userDetails = (JwtCustomUserDetails) auth.getPrincipal();

        StudentUserVO user = userDetails.getUser();

        String academyId = user.getAcademyId();

        List<CourseInfoVO> courseInfoList = courseService.getCourseInfoList(academyId);

        Map<String, Object> response = new HashMap<>();

        response.put("courseList", courseInfoList);
        response.put("message", "강의 목록을 성공적으로 조회했습니다.");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getCourseDetail(@RequestParam String id, Authentication auth) {
        logger.info("getCourseDetail 호출");
        logger.info("courseId: {}", id);

        if (auth == null) {
            logger.error("인증 실패");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "인증 실패"));
        }

        JwtCustomUserDetails userDetails = (JwtCustomUserDetails) auth.getPrincipal();
        StudentUserVO user = userDetails.getUser();

        String academyId = user.getAcademyId();

        CourseDetailVO courseDetail = courseService.getCourseDetail(id, academyId);

        Map<String, Object> response = new HashMap<>();
        response.put("courseDetail", courseDetail);
        response.put("message", "강의 상세 정보를 성공적으로 조회했습니다.");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/purchased")
    public ResponseEntity<?> getPurchasedCourseList(Authentication auth) {
        logger.info("getPurchasedCourseList 호출");

        if (auth == null) {
            logger.error("인증 실패");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "인증 실패"));
        }

        JwtCustomUserDetails userDetails = (JwtCustomUserDetails) auth.getPrincipal();
        StudentUserVO user = userDetails.getUser();
        String academyId = user.getAcademyId();
        String studentId = user.getStudentId();

        List<PurchasedCourseVO> purchasedCourseList = courseService.getPurchasedCourseList(studentId, academyId);

        Map<String, Object> response = new HashMap<>();
        response.put("purchasedCourseList", purchasedCourseList);
        response.put("message", "구매한 강의 목록을 성공적으로 조회했습니다.");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/classDetail")
    public ResponseEntity<?> getClassDetail(@RequestParam String id, Authentication auth) {
        logger.info("classFileDetail 호출");
        logger.info("classId: {}", id);

        if (auth == null) {
            logger.error("인증 실패");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "인증 실패"));
        }

        JwtCustomUserDetails userDetails = (JwtCustomUserDetails) auth.getPrincipal();
        StudentUserVO user = userDetails.getUser();

        String academyId = user.getAcademyId();

        ClassDetailVO classDetail = courseService.getClassDetail(id, academyId);

        Map<String, Object> response = new HashMap<>();
        response.put("classDetail", classDetail);
        response.put("message", "강의(파일)의 상세 정보를 성공적으로 조회했습니다.");

        return ResponseEntity.ok(response);
    }
}
