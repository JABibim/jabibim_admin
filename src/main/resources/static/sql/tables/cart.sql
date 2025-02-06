CREATE TABLE cart
(
    cart_id    varchar(36),
    created_at datetime,
    updated_at datetime,
    deleted_at datetime,
    student_id varchar(36),
    course_id  varchar(36),
    academy_id varchar(36)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;