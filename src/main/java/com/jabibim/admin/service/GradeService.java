package com.jabibim.admin.service;

import com.jabibim.admin.domain.Grade;

public interface GradeService {
    
    //등급 추가용
    public void addGrade(Grade grade, String academyId, String gradeId);

    //등급 업데이트용
    void modifyGrade(Grade grade, String academyId);

    void getUpdatableGradeList(Grade grade, String academyId);
}
