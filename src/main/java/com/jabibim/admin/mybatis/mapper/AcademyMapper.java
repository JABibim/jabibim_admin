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

    Academy getAcademyByCode(String code);  // code로 academy를 찾는 메서드 추가

    String getAcademyIdByCode(String code);

    void initGradeInfo(HashMap<String, Object> map);

    void initBoardInfo(String academyId);

}
