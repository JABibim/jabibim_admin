package com.jabibim.admin.service;

import com.jabibim.admin.domain.Academy;
import com.jabibim.admin.domain.Teacher;
import com.jabibim.admin.dto.setting.AddAcademyDto;
import com.jabibim.admin.dto.setting.AddTeacherDto;

import java.util.List;

public interface SettingService {
    boolean checkAdminPass(String password);

    List<Academy> getAcademyList();

    int getAcademyCountByBusinessRegisNum(String businessRegisNum);

    Academy addAcademy(AddAcademyDto addAcademyDto);

    List<Teacher> getTeacherList(String academyId);

    void addTeacher(AddTeacherDto addTeacherDto);
}
