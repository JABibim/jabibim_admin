CREATE TABLE study_history
(
    study_history_id   varchar(36),
    created_at         datetime,
    updated_at         datetime,
    deleted_at         datetime,
    study_time         int,
    is_class_file_down tinyint(1),
    academy_id         varchar(36),
    student_id         varchar(36),
    class_id           varchar(36)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
