package com.jabibim.admin.domain;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage implements Serializable {
    private String chatMessageId;
    private String chatRoomId;
    private String senderId;
    private String senderName;
    private String chatMessage;
    private LocalDateTime sentAt;
}
