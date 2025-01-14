package com.jabibim.admin.service;

import com.jabibim.admin.domain.Grade;

public interface GradeService {
    
    //등급 추가용
    public void addGrade(Grade grade, String academyId, String gradeId);

}
