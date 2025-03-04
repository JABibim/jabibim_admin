package com.jabibim.admin.mybatis.mapper;

import com.jabibim.admin.domain.Teacher;
import com.jabibim.admin.domain.TeacherCareer;
import com.jabibim.admin.dto.TeacherProfileDTO;
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

    TeacherProfileDTO teacherInfo(String id);

    int update(Teacher teacher);

    int updatePassword(Teacher teacher);

    Teacher getTeacherById(String teacherId);

    int updateProfileImage(@Param("teacherId") String teacherId, @Param("teacherImgName") String teacherImgName);

    List<TeacherCareer> getcareerList(HashMap<String, Object> params);

    void resetAllCareers();

    int updateCareerActive(String asisCareerId, String tobeCareerId);

    void insertCareer(HashMap<String, Object> map);

    String getUploadPathByCareerId(String careerId);

    void insertOauthTeacher(Teacher teacher);

    void updateOauthTeacher(Teacher existData);

    Teacher getTeacherByProviderId(String provider);

    void updateTeacherAcademy(String teacherId, String academyId, String code);

    String getTeacherIdByEmail(String teacheremail);

    String getAcademyIdByEmail(String email);
}
