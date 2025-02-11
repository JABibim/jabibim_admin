package com.jabibim.admin.service;

import com.jabibim.admin.domain.ChatMessage;
import com.jabibim.admin.domain.Teacher;
import com.jabibim.admin.func.UUIDGenerator;
import com.jabibim.admin.mybatis.mapper.ChatMapper;
import com.jabibim.admin.mybatis.mapper.TeacherMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ChatServiceImpl implements ChatService{
    private final TeacherMapper teacherMapper;
    private final ChatMapper chatMapper;

    public ChatServiceImpl(TeacherMapper teacherMapper, ChatMapper chatMapper) {
        this.teacherMapper = teacherMapper;
        this.chatMapper = chatMapper;
    }

    @Override
    public List<Teacher> getChatableTeacher(String academyId) {
        return teacherMapper.getTeacherListByAcademyId(academyId);
    }

    @Override
    @Transactional
    public String getOrCreateChatRoom(String loggedInTeacherId, String teacherId) {
        //기존 채팅방 ID 있는지 조회해야함
        String roomId = chatMapper.findRoomId(loggedInTeacherId, teacherId);

        //채팅방 없다면 생성하자
        if(roomId == null) {
            String newRoomId = UUIDGenerator.getUUID();
            chatMapper.insertChatRoom(newRoomId, loggedInTeacherId, teacherId);

            // 채팅방 생성 후, 다시 한 번 DB 에서 조회하여 확인
            roomId = chatMapper.findRoomId(loggedInTeacherId, teacherId);
            if (roomId == null) {
                throw new RuntimeException("채팅방 생성 실패! DB에서 확인하세요. by ChatServiceImpl");
            }
        }

        return roomId;
    }

    @Override
    public void sendMessage(ChatMessage chatMessage) {
        if (chatMessage.getChatRoomId() == null || chatMessage.getSenderId() == null
                || chatMessage.getChatMessage() == null) {
            System.out.println("==== [sendMessage] 메시지 정보 오류 ====");
            System.out.println("chatRoomId: " + chatMessage.getChatRoomId());
            System.out.println("senderId: " + chatMessage.getSenderId());
            System.out.println("chatMessage: " + chatMessage.getChatMessage());
            throw new IllegalArgumentException("메시지 정보가 부족합니다!");
        }

        chatMessage.setChatMessageId(UUIDGenerator.getUUID());
        chatMapper.insertMessage(chatMessage);
    }

    @Override
    public List<ChatMessage> findRecentChat(String chatRoomId) {
        return chatMapper.findRecentChatWithSenderName(chatRoomId);
    }

    @Override
    @Transactional
    public void markMessagesAsRead(String chatRoomId, String userId) {
        chatMapper.markMessagesAsRead(chatRoomId, userId);
    }

    @Override
    public int getUnreadMessageCount(String userId) {
        return chatMapper.countUnreadMessages(userId);
    }


}
