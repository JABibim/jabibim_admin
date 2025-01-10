package com.jabibim.admin.service;


import com.jabibim.admin.domain.Student;

import java.util.List;

public interface StudentService {

    int getStudentCount(String academyId, boolean isAdmin, String state, String startDate, String endDate, String studentGrade, String search_field, String search_word);

    List<Student> getStudentList(int page, int limit, String academyId, boolean isAdmin, String state, String startDate, String endDate, String studentGrade, String search_field, String search_word);

    int getStudentAdCount(String academyId, boolean isAdmin, String search_field, String search_word);

    List<Student> getStudentAdList(int page, int limit, String academyId, boolean isAdmin, String search_field, String search_word);
}
