package com.jabibim.admin.mybatis.mapper;

import com.jabibim.admin.domain.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TeacherMapper {
    Teacher getTeachers();

    Teacher teacherInfo(String id);

    int update(Teacher teacher);

    int updatePassword(Teacher teacher);

    Teacher getTeacherById(String teacherId);

    int updateProfileImage(@Param("teacherId") String teacherId, @Param("teacherImgName") String teacherImgName);
}
