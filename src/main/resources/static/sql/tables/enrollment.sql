CREATE TABLE enrollment
(
    enrollment_id     varchar(36),
    created_at        datetime,
    updated_at        datetime,
    deleted_at        datetime,
    enrollment_status varchar(20) DEFAULT 'INIT',
    academy_id        varchar(36),
    student_id        varchar(36),
    course_id         varchar(36),
    CONSTRAINT chk_enrollment_status CHECK ((enrollment_status IN ('INIT', 'COMPLETED', 'CANCELED')))
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;