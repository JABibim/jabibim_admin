CREATE TABLE class_file
(
    class_file_id          varchar(36),
    created_at             datetime,
    updated_at             datetime,
    deleted_at             datetime,
    class_file_name        varchar(500),
    class_file_origin_name varchar(500),
    class_file_type        varchar(50),
    class_file_size        int,
    class_file_path        varchar(200),
    academy_id             varchar(36),
    teacher_id             varchar(36),
    course_id              varchar(36),
    class_id               varchar(36)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
