CREATE TABLE chat_room
(
    chat_room_id varchar(36),
    teacher_1_id varchar(36),
    teacher_2_id varchar(36),
    created_at   timestamp
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;