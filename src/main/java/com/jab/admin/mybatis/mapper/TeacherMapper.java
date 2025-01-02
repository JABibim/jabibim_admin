package com.jab.admin.mybatis.mapper;

import com.jab.admin.domain.Teacher;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TeacherMapper {
    Teacher getTeachers();
}
