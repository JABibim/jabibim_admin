package com.jabibim.admin.service;

import com.jabibim.admin.domain.ChatMessage;
import com.jabibim.admin.domain.Teacher;
import java.util.List;

public interface ChatService {

    List<Teacher> getChatableTeacher(String academyId);

    String getOrCreateChatRoom(String loggedInTeacherId, String teacherId);

    //메시지 저장용 메서드
    void sendMessage(ChatMessage chatMessage);

    List<ChatMessage> findRecentChat(String chatRoomId);

    void markMessagesAsRead(String chatRoomId, String userId);

    int getUnreadMessageCount(String userId);

    ChatMessage findLastMessage(String chatRoomId);
}
