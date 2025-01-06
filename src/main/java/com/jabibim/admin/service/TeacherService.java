package com.jabibim.admin.service;

import com.jabibim.admin.domain.Teacher;

public interface TeacherService {


    Teacher teacherInfo(String id);

    int update(Teacher teacher);
}
