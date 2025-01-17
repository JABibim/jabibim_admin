package com.jabibim.admin.mybatis.mapper;

import com.jabibim.admin.domain.Student;
import com.jabibim.admin.dto.DeleteGradeDTO;
import com.jabibim.admin.dto.GetStudentGradesDTO;
import com.jabibim.admin.dto.StudentUserVO;
import org.apache.ibatis.annotations.Mapper;
import java.util.HashMap;
import java.util.List;

@Mapper  //쿼리 쓰는곳
public interface StudentMapper {

    int getStudentCount(HashMap<String, Object> params);

    List<Student> getStudentList(HashMap<String, Object> params);

    List<GetStudentGradesDTO> getStudentGrades(String academyId);

    int getStudentAdCount(HashMap<String, Object> params);

    List<Student> getStudentAdList(HashMap<String, Object> params);

    int replaceGrade(DeleteGradeDTO deleteGradeDTO);

    StudentUserVO getStudentByEmail(String email, String academyId);
}
