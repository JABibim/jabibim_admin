CREATE TABLE class_memo
(
    class_id   varchar(36),
    student_id varchar(36),
    created_at datetime,
    updated_at datetime,
    deleted_at datetime,
    memo       text
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;