package com.jabibim.admin.service;

import com.jabibim.admin.domain.Qna;

import java.util.List;

public interface QnaService {

    int getListCount(String academyId);

    List<Qna> getQnaList(int page, int limit, String academyId);

}