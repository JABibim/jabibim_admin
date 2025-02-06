CREATE TABLE payment
(
    payment_id     varchar(36),
    created_at     datetime,
    updated_at     datetime,
    deleted_at     datetime,
    payment_amount int,
    payment_method varchar(100),
    payment_status varchar(30),
    paid_at        varchar(100),
    receipt_url    varchar(100),
    student_id     varchar(36),
    orders_id      varchar(36),
    course_id      varchar(100),
    academy_id     varchar(100)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
