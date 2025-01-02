package com.jab.admin.service;

import com.jab.admin.domain.Academy;
import com.jab.admin.mybatis.mapper.AcademyMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingServiceImpl implements SettingService {
    @Value("${admin.settingPassword}")
    private String settingPassword;

    private final AcademyMapper academyDao;

    public SettingServiceImpl(AcademyMapper academyDao) {
        this.academyDao = academyDao;
    }

    @Override
    public boolean checkAdminPass(String password) {
        return settingPassword.equals(password);
    }

    @Override
    public List<Academy> getAcademyList() {
        return academyDao.getAcademyList();
    }
}
