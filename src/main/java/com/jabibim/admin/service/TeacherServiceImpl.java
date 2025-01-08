package com.jabibim.admin.service;

import com.jabibim.admin.domain.Teacher;
import com.jabibim.admin.mybatis.mapper.TeacherMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService {
    private TeacherMapper dao;
    public TeacherServiceImpl(TeacherMapper dao) {
        this.dao = dao;
    }

    @Override
    public int getTeacherCount(String state, String search_field, String search_word) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("state", state);
        params.put("search_field", search_field);
        params.put("search_word", search_word);
        return dao.getTeacherCount(params);
    }

    @Override
    public List<Teacher> getTeacherList(int page, int limit, String academyId, boolean isAdmin, String state, String search_field, String search_word) {
        int startrow = (page - 1) * limit + 1;
        int endrow = startrow + limit - 1;

        HashMap<String, Object> params = new HashMap<>();
        params.put("start", startrow);
        params.put("end", endrow);
        params.put("academyId", academyId);
        params.put("isAdmin", isAdmin);
        params.put("state", state);
        params.put("search_field", search_field);
        params.put("search_word", search_word);

        return dao.getTeacherList(params);
    }
}
