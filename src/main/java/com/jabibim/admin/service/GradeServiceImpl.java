package com.jabibim.admin.service;

import com.jabibim.admin.domain.Grade;
import com.jabibim.admin.dto.DeleteGradeDTO;
import com.jabibim.admin.dto.GetStudentGradesDTO;
import com.jabibim.admin.mybatis.mapper.GradeMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GradeServiceImpl implements GradeService{

    private final GradeMapper dao;

    public GradeServiceImpl(GradeMapper dao) {
        this.dao = dao;
    }

    @Override
    public List<GetStudentGradesDTO> getStudentGrades(String academyId) {
        return dao.getStudentGrades(academyId);
    }

    @Override
    public void addGrade(Grade grade, String academyId, String gradeId) {
        grade.setGradeId(gradeId);
        grade.setAcademyId(academyId);
        dao.addGrade(grade);
    }

    @Override
    public void modifyGrade(Grade grade, String academyId) {
        grade.setAcademyId(academyId);
        dao.modifyGrade(grade);
    }

    @Override
    public List<Grade> getUpdatableGradeList(Grade grade) {
        return dao.getUpdatableGradeList(grade);
    }

    @Override
    public void deleteGrade(DeleteGradeDTO deleteGradeDTO) {
        dao.deleteGrade(deleteGradeDTO);
    }



}
