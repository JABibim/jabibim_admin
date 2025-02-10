CREATE TABLE review
(
    review_id            varchar(36),
    created_at           datetime,
    updated_at           datetime,
    deleted_at           datetime,
    review_re_ref        int,
    review_re_lev        int,
    review_subject       varchar(150),
    review_password      varchar(100),
    review_content       text,
    review_rating        tinyint,
    review_readcount     int,
    review_exposure_stat tinyint(1),
    academy_id           varchar(36),
    course_id            varchar(36),
    teacher_id           varchar(36),
    student_id           varchar(36)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;