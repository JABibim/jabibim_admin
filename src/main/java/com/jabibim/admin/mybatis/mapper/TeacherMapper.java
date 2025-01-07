package com.jabibim.admin.mybatis.mapper;

import com.jabibim.admin.domain.Teacher;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TeacherMapper {
    Teacher getTeacherByEmail(String email);
}
