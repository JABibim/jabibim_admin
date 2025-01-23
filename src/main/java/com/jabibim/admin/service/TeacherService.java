package com.jabibim.admin.service;

import com.jabibim.admin.domain.Teacher;
import com.jabibim.admin.domain.TeacherCareer;
import com.jabibim.admin.dto.TeacherProfileDTO;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.util.List;

public interface TeacherService {

    int getTeacherCount(String state, String search_field, String search_word);

    List<Teacher> getTeacherList(int page, int limit, String academyId, boolean isAdmin, String state, String search_field, String search_word);

    TeacherProfileDTO teacherInfo(String id);

    int update(Teacher teacher);

    Teacher getTeacherById(String teacherId);

    int updatePassword(Teacher teacher);

    String saveProfileImage(MultipartFile file, String uploadDir) throws IOException;

    int updateProfileImage(String teacherId, String teacherImgName);

    List<TeacherCareer> getcareerList(boolean isAdmin, String academyId);

    void resetAllCareers();

    int updateCareerActive(String asisCareerId, String tobeCareerId);

    void insertCareer(String academyId, String teacherId, String careerName, MultipartFile careerImage);

    String getUploadPathByCareerId(String careerId);
}
