CREATE TABLE calendar_event
(
    calendar_event_id   VARCHAR(36),
    created_at          DATETIME,
    updated_at          DATETIME,
    deleted_at          DATETIME,
    google_event_id     VARCHAR(255),
    event_creator_email VARCHAR(255),
    event_title         VARCHAR(100),
    event_description   TEXT,
    event_start         DATETIME,
    event_end           DATETIME,
    event_location      VARCHAR(255),
    calendar_id         VARCHAR(255),
    academy_id          VARCHAR(36),
    teacher_id          VARCHAR(36)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;