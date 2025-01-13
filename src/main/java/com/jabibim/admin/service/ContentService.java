package com.jabibim.admin.service;

import com.jabibim.admin.domain.Course;
import com.jabibim.admin.dto.content.course.AddCourseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ContentService {
    List<Course> getCourseList(boolean isAdmin, String search);

    void addCourse(String teacherId, String academyId, AddCourseDto addCourseDto, MultipartFile courseImage);
}