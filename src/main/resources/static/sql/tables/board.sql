CREATE TABLE board
(
    board_id            varchar(36),
    created_at          datetime,
    updated_at          datetime,
    deleted_at          datetime,
    board_re_ref        int,
    board_re_lev        int,
    board_re_seq        int,
    board_notice        tinyint(1),
    board_subject       varchar(150),
    board_email         varchar(100),
    board_password      varchar(100),
    board_content       text,
    board_file_name     varchar(500),
    board_file_origin   varchar(500),
    board_readcount     int,
    board_exposure_stat tinyint(1),
    board_type_id       varchar(36),
    academy_id          varchar(36),
    course_id           varchar(36),
    teacher_id          varchar(36),
    class_id            varchar(36)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;