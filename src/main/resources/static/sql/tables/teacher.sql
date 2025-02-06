CREATE TABLE teacher
(
    teacher_id             varchar(36),
    created_at             datetime,
    updated_at             datetime,
    deleted_at             datetime,
    teacher_name           varchar(20),
    teacher_given_name     varchar(20),
    teacher_family_name    varchar(20),
    teacher_phone          varchar(20),
    teacher_email          varchar(50),
    teacher_email_verified tinyint(1),
    teacher_password       varchar(100),
    teacher_job            varchar(36),
    teacher_img_name       varchar(500),
    teacher_img_origin     varchar(500),
    oauth_picture          varchar(500),
    auth_role              varchar(20),
    provider               varchar(20),
    provider_id            varchar(50),
    code                   varchar(20),
    academy_id             varchar(36)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;