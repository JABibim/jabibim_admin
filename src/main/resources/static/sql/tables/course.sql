CREATE TABLE course
(
    course_id              varchar(36),
    created_at             datetime,
    updated_at             datetime,
    deleted_at             datetime,
    course_name            varchar(100),
    course_subject         varchar(100),
    course_info            varchar(500),
    course_tag             varchar(500),
    course_diff            varchar(100),
    course_price           int,
    course_activation      tinyint(1),
    course_img_name        varchar(500),
    course_img_origin_name varchar(1024),
    course_profile_path    varchar(1024),
    academy_id             varchar(36),
    teacher_id             varchar(36)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;