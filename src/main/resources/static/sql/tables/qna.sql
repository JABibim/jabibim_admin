CREATE TABLE qna
(
    qna_id            varchar(36),
    created_at        datetime,
    updated_at        datetime,
    deleted_at        datetime,
    qna_re_ref        int,
    qna_re_lev        int,
    qna_re_seq        int,
    qna_subject       varchar(150),
    qna_password      varchar(36),
    qna_content       text,
    qna_readcount     int,
    qna_exposure_stat tinyint(1),
    qna_answer_status tinyint(1),
    qna_file_name     varchar(500),
    qna_file_origin   varchar(500),
    academy_id        varchar(36),
    teacher_id        varchar(36),
    student_id        varchar(36),
    course_id         varchar(36),
    class_id          varchar(36)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
