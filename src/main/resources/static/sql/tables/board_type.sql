CREATE TABLE board_type
(
    board_type_id   varchar(36),
    created_at      datetime,
    updated_at      datetime,
    deleted_at      datetime,
    board_type_name varchar(16),
    academy_id      varchar(36)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;