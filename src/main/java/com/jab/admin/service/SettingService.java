package com.jab.admin.service;

import com.jab.admin.domain.Academy;

import java.util.List;

public interface SettingService {
    boolean checkAdminPass(String password);

    List<Academy> getAcademyList();
}
