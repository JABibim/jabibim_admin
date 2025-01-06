package com.jabibim.admin.service;

import com.jabibim.admin.domain.Teacher;
import com.jabibim.admin.mybatis.mapper.TeacherMapper;
import org.springframework.stereotype.Service;

@Service
public class TeacherServiceImpl implements TeacherService {

    private TeacherMapper dao;

    public TeacherServiceImpl(TeacherMapper dao) {
        this.dao = dao;
    }

    @Override
    public Teacher teacherInfo(String id) {

        return dao.teacherInfo(id);
    }

    public int update(Teacher teacher) {
        return dao.update(teacher);
    }
}
