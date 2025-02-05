CREATE TABLE academy
(
    academy_id          varchar(36),
    created_at          datetime,
    updated_at          datetime,
    deleted_at          datetime,
    academy_name        varchar(150),
    academy_address     varchar(500),
    academy_detail_addr varchar(500),
    academy_postalcode  varchar(10),
    academy_owner       varchar(20),
    academy_contect     varchar(50),
    business_regis_num  varchar(500),
    registered_at       datetime,
    code                varchar(20)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;