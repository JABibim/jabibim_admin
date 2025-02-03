CREATE TABLE orders
(
    orders_id          varchar(36),
    created_at         datetime,
    updated_at         datetime,
    deleted_at         datetime,
    orders_number      varchar(100),
    total_price        int,
    orders_address     varchar(300),
    orders_detail_addr varchar(300),
    orders_postcode    varchar(20),
    orders_status      tinyint,
    student_id         varchar(36),
    course_id          varchar(36),
    payment_id         varchar(36),
    academy_id         varchar(36)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;