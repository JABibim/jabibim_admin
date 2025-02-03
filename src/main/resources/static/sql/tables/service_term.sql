CREATE TABLE service_term
(
    service_term_id              varchar(36),
    created_at                   datetime,
    updated_at                   datetime,
    deleted_at                   datetime,
    service_term_subject         varchar(100),
    service_term_name            varchar(100),
    service_term_content         text,
    service_term_effective_date  datetime,
    service_term_expiration_date datetime,
    service_term_status          tinyint(1),
    academy_id                   varchar(36)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;