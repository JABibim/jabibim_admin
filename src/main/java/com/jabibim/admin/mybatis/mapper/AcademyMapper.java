package com.jabibim.admin.mybatis.mapper;

import com.jabibim.admin.domain.Academy;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface AcademyMapper {
    public List<Academy> getAcademyList();

    int getAcademyCountByBusinessRegisNum(String businessRegisNum);

    void addAcademy(Academy academy);

    Academy getAcademyById(String academyId);

    void initGradeInfo(HashMap<String, Object> map);

    void initBoardInfo(String academyId);
}
