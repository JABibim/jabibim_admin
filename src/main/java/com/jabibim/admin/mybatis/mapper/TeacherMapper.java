package com.jabibim.admin.mybatis.mapper;

import com.jabibim.admin.domain.Teacher;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;

@Mapper  //쿼리 쓰는곳
public interface TeacherMapper {

    int getTeacherCount(HashMap<String, Object> params);

    List<Teacher> getTeacherList(HashMap<String, Object> map);

    Teacher teacherInfo(String id);

    int update(Teacher teacher);

    int updatePassword(Teacher teacher);

    Teacher getTeacherById(String teacherId);

    int updateProfileImage(@Param("teacherId") String teacherId, @Param("teacherImgName") String teacherImgName);

}
