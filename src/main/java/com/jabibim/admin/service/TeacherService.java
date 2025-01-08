package com.jabibim.admin.service;

import com.jabibim.admin.domain.Teacher;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface TeacherService {


    Teacher teacherInfo(String id);

    int update(Teacher teacher);

    Teacher getTeacherById(String teacherId);

    int updatePassword(Teacher teacher);


    String saveProfileImage(MultipartFile file, String uploadDir) throws IOException;

    int updateProfileImage(String teacherId, String teacherImgName);
}
