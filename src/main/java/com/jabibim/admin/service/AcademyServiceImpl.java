package com.jabibim.admin.service;

import com.jabibim.admin.domain.Academy;
import com.jabibim.admin.mybatis.mapper.AcademyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AcademyServiceImpl implements AcademyService {

    private AcademyMapper academyMapper;

    public AcademyServiceImpl(AcademyMapper academyMapper) {
        this.academyMapper = academyMapper;
    }

    @Override
    public boolean isCodeValid(String code) {
        return academyMapper.getAcademyByCode(code) != null;
    }

    @Override
    public String getAcademyIdByCode(String code) {
        return academyMapper.getAcademyIdByCode(code);
    }
}
