package com.jabibim.admin.service;

import com.jabibim.admin.domain.Teacher;

import java.util.List;

public interface TeacherService {

    int getTeacherCount(String state, String search_field, String search_word);

    List<Teacher> getTeacherList(int page, int limit, String academyId, boolean isAdmin, String state, String search_field, String search_word);
}
