package com.jabibim.admin.mybatis.mapper;

import com.jabibim.admin.domain.Teacher;
import com.jabibim.admin.domain.TeacherCareer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.HashMap;

@Mapper  //쿼리 쓰는곳
public interface TeacherMapper {
    Teacher getTeacherByEmail(String email);

    List<Teacher> getTeacherListByAcademyId(String academyId);

    void addTeacher(Teacher teacher);

    int getTeacherCount(HashMap<String, Object> params);

    List<Teacher> getTeacherList(HashMap<String, Object> map);

    Teacher teacherInfo(String id);

    int update(Teacher teacher);

    int updatePassword(Teacher teacher);

    Teacher getTeacherById(String teacherId);

    int updateProfileImage(@Param("teacherId") String teacherId, @Param("teacherImgName") String teacherImgName);

    List<TeacherCareer> getcareerList(HashMap<String, Object> params);

    void resetAllCareers();

    int updateCareerActive(String careerName, int displayStatus);
}
