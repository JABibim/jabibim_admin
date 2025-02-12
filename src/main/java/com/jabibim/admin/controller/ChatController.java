package com.jabibim.admin.controller;

import com.jabibim.admin.domain.ChatMessage;
import com.jabibim.admin.domain.Teacher;
import com.jabibim.admin.security.dto.AccountDto;
import com.jabibim.admin.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/chat")
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * 학원 내에서 채팅 가능한 선생님 목록 조회
     */
    @GetMapping(value = "/teacherList")
    public ResponseEntity<List<Map<String, Object>>> getTeacherList(Authentication auth) {
        AccountDto account = (AccountDto) auth.getPrincipal();
        String academyId = account.getAcademyId();
        String loggedInTeacherId = account.getId(); // 현재 로그인한 선생의 teacherId

        List<Teacher> teacherList = chatService.getChatableTeacher(academyId);

        // 현재 로그인한 선생님 제외
        List<Teacher> filteredTeacherList = teacherList.stream()
                .filter(teacher -> !teacher.getTeacherId().equals(loggedInTeacherId))
                .collect(Collectors.toList());

        // 선생님 목록 + 최신 메시지를 포함한 응답 데이터 생성
        List<Map<String, Object>> responseList = filteredTeacherList.stream().map(teacher -> {
                    Map<String, Object> teacherData = new HashMap<>();
                    teacherData.put("teacherId", teacher.getTeacherId());
                    teacherData.put("teacherName", teacher.getTeacherName());

            // 각 선생님과의 최근 메시지 가져오기
            String chatRoomId = chatService.getOrCreateChatRoom(loggedInTeacherId, teacher.getTeacherId());
            ChatMessage lastMessage = chatService.findLastMessage(chatRoomId);

            // 최근 메시지 추가 (없으면 기본 메시지 설정)
            if (lastMessage != null) {
                String trimmedMessage = lastMessage.getChatMessage().length() > 10
                        ? lastMessage.getChatMessage().substring(0, 10) + "..."
                        : lastMessage.getChatMessage();
                teacherData.put("lastMessage", trimmedMessage);
            } else {
                teacherData.put("lastMessage", "메시지가 없습니다.");
            }

            return teacherData;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(responseList);
    }

    /**
     * 채팅방 생성 또는 기존 채팅방 조회
     */
    @GetMapping(value = "/openChat")
    public ResponseEntity<Map<String, Object>> openChat(@RequestParam String teacherId, Authentication auth) {
        AccountDto account = (AccountDto) auth.getPrincipal();
        String loggedInTeacherId = account.getId();
        String roomId = chatService.getOrCreateChatRoom(loggedInTeacherId, teacherId);

        //채팅방 열 시, 메시지를 읽음 처리
        chatService.markMessagesAsRead(roomId, loggedInTeacherId);

        Map<String, Object> response = new HashMap<>();
        response.put("chatRoomId", roomId);
        response.put("senderId", loggedInTeacherId);
        return ResponseEntity.ok(response);
    }

    /**
     * WebSocket 을 통해 메시지 전송
     */
    @MessageMapping("/sendMessage")    // 클라이언트가 메시지를 보내는 경로
    @SendTo("/topic/chatRoom")              // 메시지를 브로드캐스트할 경로
    public ChatMessage sendMessage(@Payload Map<String, Object> payload, Authentication auth) {

        AccountDto accountDto = (AccountDto) auth.getPrincipal();
        String chatRoomId = (String) payload.get("chatRoomId");
        String senderId = (String) payload.get("senderId");
        String chatMessage = (String) payload.get("chatMessage");
        String senderName = accountDto.getName();
        LocalDateTime sentAt = LocalDateTime.now();

        if (chatRoomId == null || chatRoomId.isEmpty()) {
            throw new IllegalArgumentException("채팅방 ID가 없습니다!");
        }

        if (senderId == null || senderId.isEmpty()) {
            AccountDto account = (AccountDto) auth.getPrincipal();
            senderId = account.getId();
        }

        ChatMessage message = new ChatMessage();
        message.setChatRoomId(chatRoomId);
        message.setSenderId(senderId);
        message.setChatMessage(chatMessage);
        message.setSenderName(senderName);
        message.setSentAt(sentAt);

        chatService.sendMessage(message);
        return message;
    }

    @GetMapping("/recent")
    public ResponseEntity<?> findRecentChat(@RequestParam String id) {

        List<ChatMessage> chatHistory = chatService.findRecentChat(id);

        Map<String, Object> response = new HashMap<String, Object>();
        response.put("list", chatHistory);

        return ResponseEntity.ok(response);
    }

    //채팅방을 열면 자동으로 메시지를 읽음으로 처리
    @PostMapping("/markAsRead")
    public ResponseEntity<Void> markMessageAsRead(@RequestParam String chatRoomId, Authentication auth) {
        AccountDto account = (AccountDto) auth.getPrincipal();
        chatService.markMessagesAsRead(chatRoomId, account.getId());
        return ResponseEntity.ok().build();
    }

    //안읽은 메시지 조회용 API
    @GetMapping("/unreadMessagesByChatRoom")
    public ResponseEntity<Map<String, Integer>> getUnreadMessagesByChatRoom(Authentication auth) {
        AccountDto account = (AccountDto) auth.getPrincipal();
        String loggedInTeacherId = account.getId();

        Map<String, Integer> unreadCounts = chatService.getUnreadMessagesByChatRoom(loggedInTeacherId);
        System.out.println("unreadCounts 는요!!!!!!!!!!!!!!!!!!!!" + unreadCounts);

        return ResponseEntity.ok(unreadCounts);
    }

    @GetMapping("/getChatRoomIds")
    public ResponseEntity<Map<String, String>> getChatRoomIds(Authentication auth) {
        AccountDto account = (AccountDto) auth.getPrincipal();
        String loggedInTeacherId = account.getId();

        List<Teacher> teacherList = chatService.getChatableTeacher(account.getAcademyId());

        Map<String, String> chatRoomIdMap = new HashMap<>();
        for (Teacher teacher : teacherList) {
            if (!teacher.getTeacherId().equals(loggedInTeacherId)) {
                String chatRoomId = chatService.getOrCreateChatRoom(loggedInTeacherId, teacher.getTeacherId());
                chatRoomIdMap.put(teacher.getTeacherId(), chatRoomId);
            }
        }

        return ResponseEntity.ok(chatRoomIdMap);
    }
}
