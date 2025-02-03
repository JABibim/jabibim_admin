CREATE TABLE student
(
    student_id         varchar(36),
    created_at         datetime,
    updated_at         datetime,
    deleted_at         datetime,
    student_name       varchar(20),
    student_email      varchar(50),
    student_phone      varchar(30),
    student_password   varchar(100),
    student_address    json,
    verification       tinyint(1),
    student_img_name   varchar(500),
    student_img_origin varchar(500),
    ads_agreed         tinyint(1),
    auth_role          varchar(20),
    grade_id           varchar(36),
    academy_id         varchar(36)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
