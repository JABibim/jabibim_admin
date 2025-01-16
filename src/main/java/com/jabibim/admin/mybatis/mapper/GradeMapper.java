package com.jabibim.admin.mybatis.mapper;

import com.jabibim.admin.domain.Grade;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GradeMapper {

    void addGrade(Grade grade);

    void modifyGrade(Grade grade);

    void getUpdatableGradeList(Grade grade);

}
