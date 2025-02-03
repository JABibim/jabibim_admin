CREATE TABLE privacy_term
(
    privacy_term_id              varchar(36),
    created_at                   datetime,
    updated_at                   datetime,
    deleted_at                   datetime,
    privacy_term_subject         varchar(100),
    privacy_term_name            varchar(100),
    privacy_term_content         text,
    privacy_term_effective_date  DATE,
    privacy_term_expiration_date DATE,
    privacy_term_status          tinyint(1),
    academy_id                   varchar(36)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;