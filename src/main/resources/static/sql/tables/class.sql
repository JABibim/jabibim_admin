CREATE TABLE class
(
    class_id      varchar(36),
    created_at    datetime,
    updated_at    datetime,
    deleted_at    datetime,
    class_name    varchar(100),
    class_content text,
    class_seq     smallint,
    class_type    varchar(20),
    academy_id    varchar(36),
    teacher_id    varchar(36),
    course_id     varchar(36)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;