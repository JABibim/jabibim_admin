package com.jabibim.admin.mybatis.mapper;

import com.jabibim.admin.domain.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface ChatMapper {

    String findRoomId(@Param("teacher1Id") String teacher1Id, @Param("teacher2Id") String teacher2Id);

    void insertChatRoom(@Param("chatRoomId") String chatRoomId,@Param("teacher1Id") String teacher1Id,
                        @Param("teacher2Id") String teacher2Id);

    void insertMessage(@Param("chatMessage") ChatMessage chatMessage);
    
    //ChatRoomId 로 일단 해당 chatRoom 내역 전부 찾기
    List<ChatMessage> findRecentChatWithSenderName(@Param("chatRoomId") String chatRoomId);

    void markMessagesAsRead(@Param("chatRoomId") String chatRoomId, @Param("userId") String userId);

    int countUnreadMessages(@Param("userId") String userId);

    ChatMessage findLastMessage(@Param("chatRoomId")String chatRoomId);
}
