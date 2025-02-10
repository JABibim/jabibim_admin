CREATE TABLE chat_message
(
    chat_message_id varchar(36),
    chat_room_id    varchar(36),
    sender_id       varchar(50),
    chat_message    text,
    sent_at         timestamp,
    is_read         tinyint(1)  
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;