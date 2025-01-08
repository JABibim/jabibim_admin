package com.jabibim.admin.mybatis.mapper;

import com.jabibim.admin.domain.Teacher;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TeacherMapper {
    Teacher getTeacherByEmail(String email);
    List<Teacher> getTeacherList(String academyId);
    void addTeacher(Teacher teacher);
}
