package com.jabibim.admin.service;

import com.jabibim.admin.domain.Student;
import com.jabibim.admin.dto.DeleteGradeDTO;
import com.jabibim.admin.mybatis.mapper.StudentMapper;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentMapper dao;

    public StudentServiceImpl(StudentMapper dao) {
        this.dao = dao;
    }

    @Override
    public int getStudentCount(String academyId, boolean isAdmin, String state, String startDate, String endDate, String studentGrade, String search_field, String search_word) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("academyId", academyId);
        params.put("isAdmin", isAdmin);
        params.put("state", state);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("studentGrade", studentGrade);
        params.put("search_field", search_field);
        params.put("search_word", search_word);
        return dao.getStudentCount(params);
    }

    @Override
    public List<Student> getStudentList(int page, int limit, String academyId, boolean isAdmin, String state
                                        ,String startDate, String endDate, String studentGrade, String search_field
                                        ,String search_word) {
        int startrow = (page - 1) * limit + 1;
        int endrow = startrow + limit - 1;

        HashMap<String, Object> params = new HashMap<>();
        params.put("start", startrow);
        params.put("end", endrow);
        params.put("academyId", academyId);
        params.put("isAdmin", isAdmin);
        params.put("state", state);
        params.put("startDate", startDate);
        params.put("endDate", endDate);
        params.put("studentGrade", studentGrade);
        params.put("search_field", search_field);
        params.put("search_word", search_word);

        return dao.getStudentList(params);
    }

    @Override
    public int getStudentAdCount(String academyId, boolean isAdmin, String search_field, String search_word) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("academyId", academyId);
        params.put("isAdmin", isAdmin);
        params.put("search_field", search_field);
        params.put("search_word", search_word);
        return dao.getStudentAdCount(params);
    }

    @Override
    public List<Student> getStudentAdList(int page, int limit, String academyId, boolean isAdmin, String search_field, String search_word) {
        int startrow = (page - 1) * limit + 1;
        int endrow = startrow + limit - 1;

        HashMap<String, Object> params = new HashMap<>();
        params.put("start", startrow);
        params.put("end", endrow);
        params.put("academyId", academyId);
        params.put("isAdmin", isAdmin);
        params.put("search_field", search_field);
        params.put("search_word", search_word);

        return dao.getStudentAdList(params);
    }

    @Override
    public void replaceGrade(DeleteGradeDTO deleteGradeDTO) {
        dao.replaceGrade(deleteGradeDTO);
    }


}
