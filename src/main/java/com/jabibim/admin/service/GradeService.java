package com.jabibim.admin.service;

import com.jabibim.admin.domain.Grade;
import com.jabibim.admin.dto.DeleteGradeDTO;
import com.jabibim.admin.dto.GetStudentGradesDTO;
import java.util.List;

public interface GradeService {
    
    //등급 조회용
    List<GetStudentGradesDTO> getStudentGrades(String academyId);
    
    //등급 추가용
    public void addGrade(Grade grade, String academyId, String gradeId);

    //등급 업데이트용
    void modifyGrade(Grade grade, String academyId);

    //등급 삭제시, 변경 가능한 등급 조회용
    List<Grade> getUpdatableGradeList(Grade grade);
    
    //등급 삭제용
    void deleteGrade(DeleteGradeDTO deleteGradeDTO);

}
