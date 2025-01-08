package com.jabibim.admin.service;

import com.jabibim.admin.domain.Teacher;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.List;

public interface TeacherService {

    int getTeacherCount(String state, String search_field, String search_word);

    List<Teacher> getTeacherList(int page, int limit, String academyId, boolean isAdmin, String state, String search_field, String search_word);

    Teacher teacherInfo(String id);

    int update(Teacher teacher);

    Teacher getTeacherById(String teacherId);

    int updatePassword(Teacher teacher);

    String saveProfileImage(MultipartFile file, String uploadDir) throws IOException;

    int updateProfileImage(String teacherId, String teacherImgName);

}
