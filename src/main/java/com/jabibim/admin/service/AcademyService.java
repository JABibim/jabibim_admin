package com.jabibim.admin.service;

public interface AcademyService {

    boolean isCodeValid(String code);  //Code 유효성 검사

    String getAcademyIdByCode(String code);
}
