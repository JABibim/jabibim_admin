CREATE TABLE grade
(
    grade_id      varchar(36),
    created_at    datetime,
    updated_at    datetime,
    deleted_at    datetime,
    grade_name    varchar(20),
    discount_rate tinyint,
    academy_id    varchar(36)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
