CREATE TABLE comments
(
    comments_id      varchar(36),
    comments_email   varchar(30),
    comments_content text,
    board_id         varchar(36)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;