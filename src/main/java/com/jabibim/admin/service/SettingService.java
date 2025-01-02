package com.jabibim.admin.service;

import com.jabibim.admin.domain.Academy;

import java.util.List;

public interface SettingService {
    boolean checkAdminPass(String password);

    List<Academy> getAcademyList();
}
