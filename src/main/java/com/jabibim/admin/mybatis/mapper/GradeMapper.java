package com.jabibim.admin.mybatis.mapper;

import com.jabibim.admin.domain.Grade;
import com.jabibim.admin.dto.DeleteGradeDTO;
import com.jabibim.admin.dto.GetStudentGradesDTO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface GradeMapper {

    List<GetStudentGradesDTO> getStudentGrades(String academyId);

    void addGrade(Grade grade);

    void modifyGrade(Grade grade);

    List<Grade> getUpdatableGradeList(Grade grade);

    void deleteGrade(DeleteGradeDTO deleteGradeDTO);

}
