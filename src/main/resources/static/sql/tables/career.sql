CREATE TABLE career
(
    career_id               varchar(36),
    created_at              datetime,
    updated_at              datetime,
    deleted_at              datetime,
    career_name             varchar(500),
    career_info             varchar(500),
    career_file_path        varchar(200),
    career_file_origin_name varchar(500),
    display_status          tinyint(1),
    teacher_id              varchar(36),
    academy_id              varchar(36)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;