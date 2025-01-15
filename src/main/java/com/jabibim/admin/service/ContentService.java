package com.jabibim.admin.service;

import com.jabibim.admin.domain.Course;
import com.jabibim.admin.dto.content.course.request.InsertCourseReqDto;
import com.jabibim.admin.dto.content.course.request.SelectCourseListReqDto;
import com.jabibim.admin.dto.content.course.response.SelectCourseListResDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ContentService {
    void addCourse(String teacherId, String academyId, InsertCourseReqDto insertCourseReqDto, MultipartFile courseImage);

    List<SelectCourseListResDto> getCourseList(boolean isAdmin, String academyId, int page, int limit, SelectCourseListReqDto selectCourseListReqDto);

    int getCourseListCount(boolean isAdmin, String academyId, SelectCourseListReqDto selectCourseListReqDto);

    void updateCourseActivation(String courseId, boolean isActive);

    Course getCourseById(String courseId);

    void updateCourse(String teacherId, String academyId, String courseId, String courseName, String courseSubject, String isProfileChanged, MultipartFile courseImage, String courseInfo, String coursePrice, String courseTag, String courseDiff);
}