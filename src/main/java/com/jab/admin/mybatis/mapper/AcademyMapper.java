package com.jab.admin.mybatis.mapper;

import com.jab.admin.domain.Academy;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AcademyMapper {
    public List<Academy> getAcademyList();
}
