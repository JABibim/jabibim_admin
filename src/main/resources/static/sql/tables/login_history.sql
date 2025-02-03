CREATE TABLE login_history
(
    login_history_id varchar(36),
    created_at       datetime,
    updated_at       datetime,
    deleted_at       datetime,
    ip_info          varchar(40),
    os_info          varchar(40),
    browser_info     varchar(40),
    login_success    tinyint(1),
    academy_id       varchar(36),
    student_id       varchar(36)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;