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
    business_regis_num  varchar(500)
);

CREATE TABLE board
(
    board_id            varchar(36),
    created_at          datetime,
    updated_at          datetime,
    deleted_at          datetime,
    board_re_ref        int,
    board_re_lev        int,
    board_re_seq        int,
    board_notice        tinyint(1),
    board_subject       varchar(150),
    board_email         varchar(100),
    board_password      varchar(100),
    board_content       text,
    board_file_name     varchar(500),
    board_file_origin   varchar(500),
    board_readcount     int,
    board_exposure_stat tinyint(1),
    board_type_id       varchar(36),
    academy_id          varchar(36),
    course_id           varchar(36),
    teacher_id          varchar(36),
    class_id            varchar(36)
);


CREATE TABLE board_type
(
    board_type_id   varchar(36),
    created_at      datetime,
    updated_at      datetime,
    deleted_at      datetime,
    board_type_name varchar(16),
    academy_id      varchar(36)
);


CREATE TABLE career
(
    career_id               varchar(36),
    created_at              datetime,
    updated_at              datetime,
    deleted_at              datetime,
    career_name             varchar(500),
    career_info             varchar(500),
    career_file_path        varchar(200),
    career_file_origin_name varchar(500),
    display_status          tinyint(1),
    teacher_id              varchar(36),
    academy_id              varchar(36)
);


--
-- Table structure for table cart
--

CREATE TABLE cart
(
    cart_id    varchar(36),
    created_at datetime,
    updated_at datetime,
    deleted_at datetime,
    student_id varchar(36),
    course_id  varchar(36),
    academy_id varchar(36)
);

CREATE TABLE class
(
    class_id      varchar(36),
    created_at    datetime,
    updated_at    datetime,
    deleted_at    datetime,
    class_name    varchar(100),
    class_content text,
    class_seq     smallint,
    class_type    varchar(20),
    academy_id    varchar(36),
    teacher_id    varchar(36),
    course_id     varchar(36)
);


CREATE TABLE class_file
(
    class_file_id          varchar(36),
    created_at             datetime,
    updated_at             datetime,
    deleted_at             datetime,
    class_file_name        varchar(500),
    class_file_origin_name varchar(500),
    class_file_type        varchar(50),
    class_file_size        int,
    class_file_path        varchar(200),
    academy_id             varchar(36),
    teacher_id             varchar(36),
    course_id              varchar(36),
    class_id               varchar(36)
);

CREATE TABLE class_memo
(
    class_id   varchar(36),
    student_id varchar(36),
    created_at datetime,
    updated_at datetime,
    deleted_at datetime,
    memo       text
);

CREATE TABLE comments
(
    comments_id      varchar(36),
    comments_email   varchar(30),
    comments_content text,
    board_id         varchar(36)
);


CREATE TABLE course
(
    course_id         varchar(36),
    created_at        datetime,
    updated_at        datetime,
    deleted_at        datetime,
    course_name       varchar(100),
    course_subject    varchar(100),
    course_info       varchar(500),
    course_tag        varchar(500),
    course_diff       varchar(100),
    course_price      int,
    course_activation tinyint(1),
    course_img_name   varchar(500),
    course_img_origin varchar(1024),
    academy_id        varchar(36),
    teacher_id        varchar(36)
);

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
    CONSTRAINT chk_enrollment_status CHECK ((enrollment_status in
                                             ('INIT', 'COMPLETED', 'CANCELED')))
);


CREATE TABLE grade
(
    grade_id      varchar(36),
    created_at    datetime,
    updated_at    datetime,
    deleted_at    datetime,
    grade_name    varchar(20),
    discount_rate tinyint,
    academy_id    varchar(36)
);

CREATE TABLE login_history
(
    login_history_id varchar(36),
    created_at       datetime,
    updated_at       datetime,
    deleted_at       datetime,
    ip_info          varchar(40),
    os_info          varchar(40),
    browser_info     varchar(40),
    login_success    tinyint(1),
    academy_id       varchar(36),
    student_id       varchar(36)
);

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
);


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
);

CREATE TABLE persistent_logins
(
    series    varchar(64) NOT NULL,
    username  varchar(64) NOT NULL,
    token     varchar(64) NOT NULL,
    last_used timestamp   NOT NULL,
    PRIMARY KEY (series)
);

CREATE TABLE privacy_term
(
    privacy_term_id              varchar(36),
    created_at                   datetime,
    updated_at                   datetime,
    deleted_at                   datetime,
    privacy_term_subject         varchar(100),
    privacy_term_name            varchar(100),
    privacy_term_content         text,
    privacy_term_effective_date  date,
    privacy_term_expiration_date date,
    privacy_term_status          tinyint(1),
    academy_id                   varchar(36)
);

CREATE TABLE qna
(
    qna_id            varchar(36),
    created_at        datetime,
    updated_at        datetime,
    deleted_at        datetime,
    qna_re_ref        int,
    qna_re_lev        int,
    qna_re_seq        int,
    qna_subject       varchar(150),
    qna_password      varchar(36),
    qna_content       text,
    qna_readcount     int,
    qna_exposure_stat tinyint(1),
    qna_answer_status tinyint(1),
    qna_file_name     varchar(500),
    qna_file_origin   varchar(500),
    academy_id        varchar(36),
    teacher_id        varchar(36),
    student_id        varchar(36),
    course_id         varchar(36),
    class_id          varchar(36)
);

CREATE TABLE review
(
    review_id            varchar(36),
    created_at           datetime,
    updated_at           datetime,
    deleted_at           datetime,
    review_re_ref        int,
    review_re_lev        int,
    review_subject       varchar(150),
    review_password      varchar(100),
    review_content       text,
    review_rating        tinyint,
    review_readcount     int,
    review_exposure_stat tinyint(1),
    review_reply_stat    tinyint(1),
    academy_id           varchar(36),
    course_id            varchar(36),
    teacher_id           varchar(36),
    student_id           varchar(36)
);

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
);

CREATE TABLE student
(
    student_id         varchar(36),
    created_at         datetime,
    updated_at         datetime,
    deleted_at         datetime,
    student_name       varchar(20),
    student_email      varchar(50),
    student_phone      varchar(30),
    student_password   varchar(100),
    student_address    json,
    verification       tinyint(1),
    student_img_name   varchar(500),
    student_img_origin varchar(500),
    ads_agreed         tinyint(1),
    auth_role          varchar(20),
    grade_id           varchar(36),
    academy_id         varchar(36)
);


CREATE TABLE study_history
(
    study_history_id   varchar(36),
    created_at         datetime,
    updated_at         datetime,
    deleted_at         datetime,
    study_time         int,
    is_class_file_down tinyint(1),
    academy_id         varchar(36),
    student_id         varchar(36),
    class_id           varchar(36)
);


CREATE TABLE teacher
(
    teacher_id         varchar(36),        
    created_at         datetime,
    updated_at         datetime,
    deleted_at         datetime,
    teacher_name       varchar(20), 
    teacher_given_name  varchar(20),  
    teacher_family_name  varchar(20), 
    teacher_phone      varchar(20), 
    teacher_email      varchar(50), 
    teacher_email_verified         tinyint(1),  
    teacher_password   varchar(100), 
    teacher_job        varchar(36),  
    teacher_img_name   varchar(500),
    teacher_img_origin varchar(500),
    oauth_picture    varchar(500),
    auth_role          varchar(20),  
    provider           varchar(20),
    provider_id        varchar(50),  
    code               varchar(20),
    academy_id         varchar(36)
);

CREATE TABLE oauth2_authorized_client (
  client_registration_id varchar(100) NOT NULL,
  principal_name varchar(200) NOT NULL,
  access_token_type varchar(100) NOT NULL,
  access_token_value blob NOT NULL,
  access_token_issued_at timestamp NOT NULL,
  access_token_expires_at timestamp NOT NULL,
  access_token_scopes varchar(1000) DEFAULT NULL,
  refresh_token_value blob DEFAULT NULL,
  refresh_token_issued_at timestamp DEFAULT NULL,
  created_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
  PRIMARY KEY (client_registration_id, principal_name)
);
