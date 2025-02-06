CREATE TABLE calendar
(
    calendar_id        VARCHAR(36),
    created_at         DATETIME,
    updated_at         DATETIME,
    deleted_at         DATETIME,
    google_calendar_id VARCHAR(500),
    access_token       VARCHAR(500),
    refresh_token      VARCHAR(500),
    expires_in         INT,
    academy_id         VARCHAR(36),
    teacher_id         VARCHAR(36)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
