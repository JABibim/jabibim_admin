CREATE TABLE persistent_logins
(
    series    varchar(64) NOT NULL,
    username  varchar(64) NOT NULL,
    token     varchar(64) NOT NULL,
    last_used TIMESTAMP   NOT NULL,
    PRIMARY KEY (series)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;