package com.jabibim.admin.service;

import com.jabibim.admin.domain.Teacher;
import com.jabibim.admin.mybatis.mapper.TeacherMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatServiceImpl implements ChatService{
    private final TeacherMapper dao;

    public ChatServiceImpl(TeacherMapper dao) {
        this.dao = dao;
    }

    @Override
    public List<Teacher> getChatableTeacher(String academyId) {
        return dao.getTeacherListByAcademyId(academyId);
    }
}
