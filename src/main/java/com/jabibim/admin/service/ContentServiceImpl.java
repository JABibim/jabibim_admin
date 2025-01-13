package com.jabibim.admin.service;

import com.jabibim.admin.domain.Course;
import com.jabibim.admin.dto.content.course.AddCourseDto;
import com.jabibim.admin.func.UUIDGenerator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {
    @Override
    public List<Course> getCourseList(boolean isAdmin, String search) {
        return null;
    }

    @Override
    public void addCourse(String teacherId, String academyId, AddCourseDto addCourseDto, MultipartFile courseImage) {
        System.out.println("==========================과정 등록시 필요한 데이터 목록!=============================");
        System.out.println("==> teacherId = " + teacherId);
        System.out.println("==> academyId = " + academyId);
        System.out.println("==> addCourseDto = " + addCourseDto);
        System.out.println("==> courseImage = " + courseImage);
        System.out.println("==========================과정 등록시 필요한 데이터 목록!=============================");
    }
}