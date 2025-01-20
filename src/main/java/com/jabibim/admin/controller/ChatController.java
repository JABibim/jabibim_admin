package com.jabibim.admin.controller;

import com.jabibim.admin.domain.Teacher;
import com.jabibim.admin.security.dto.AccountDto;
import com.jabibim.admin.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping(value = "/chat")
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }


    @GetMapping(value="/teacherList")
    public ResponseEntity<List<Teacher>> getTeacherList(Authentication auth) {

        AccountDto account = (AccountDto)auth.getPrincipal();
        String academyId = account.getAcademyId();
        List<Teacher> teacherList = chatService.getChatableTeacher(academyId);


        return ResponseEntity.ok(teacherList);
    }
}
