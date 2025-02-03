package com.jabibim.admin.service;


import com.jabibim.admin.domain.Teacher;

import java.util.List;

public interface ChatService {

    List<Teacher> getChatableTeacher(String academyId);
}
