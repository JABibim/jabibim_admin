# 필수 => 학원 / 강사 / 학생


# LOMBOK 사용 시 주요 컬럼을 제외하면 비교적 자유롭게 수정할 수 있을 것 같으므로 가감없이 의견 내주길 바람
# 컬럼 추가할 것 있으면 이유와 함께 제시해주었으면.
# 명확한 용도 같은 것을 생각해서 제안해주었으면 함.
# 뺄 컬럼도 자유롭게 제안해주었으면 함.
# 캐싱, 인덱스 지정, 트리거, 서브쿼리랑 조인이랑 성능 차이가 존재하니까 그런 것도 고려해서 쿼리문 생각하는게 백엔드 성장성 있다고 생각.
# 수업 Before select 필요한 값을 가져오는 마이바티스 기능.
# 다양한 방법으로 DB 문법을 적극적으로 사용해보도록 하자.
# PL/SQL 같은 것도 사용할 방법이 있는지 생각해보자.
# 백엔드 스킬을 늘릴 수 있도록 하자!
# 프론트에 집중하기보다 비즈니스 로직에 집중하고 DB에 집중하자!
# 각 잡고 스터디하는 것도 괜찮고
# 일일 토막상식 느낌으로 간단하게 주고 받는 형식도 괜찮고

# 다음주까지 스프링 및 타임리프 전한 마무리
# 이후 추가 기능 붙이면서 스터디

# 학원
# 비즈니스 로직적으로 쓰이지는 않지만 고객이 될 학원에 대한 정보 관리를 위한 테이블
# 여러 학원을 대상으로 한 서비스라고 설계해보자
CREATE TABLE academy
(
    academy_id          varchar(36),
    created_at          datetime,
    updated_at          datetime,
    deleted_at          datetime,
    academy_name        varchar(150), # 학원 이름
    academy_address     varchar(500), # 학원 실 주소지
    academy_detail_addr varchar(500), # 학원 실 주소지 상세 주소
    academy_postalcode  varchar(10),  # 학원 실 주소지 우편번호
    academy_owner       varchar(20),  # 학원 대표 이름
    academy_contect     varchar(50),  # 학원 대표 연락처
    business_regis_num  varchar(500)  # 학원 사업자 등록 번호
);

INSERT INTO academy
VALUES ('f236923c-4746-4b5a-8377-e7c5b53799c2', '2025-01-03 16:14:00', NULL, NULL, '코드 크래프트', '서울 종로구 북촌로 31-4', '222',
        '03055', '사장님', '010-1234-5678', '1234-5678-0000');

# 등급 생성
CREATE TABLE grade
(
    grade_id      varchar(36),
    created_at    datetime,
    updated_at    datetime,
    deleted_at    datetime,
    grade_name    varchar(20),
    discount_rate tinyint,    # 할인율 (정수로 표기)
    academy_id    varchar(36) # 학원별 등급 관리를 위해 구분
);

INSERT INTO grade
VALUES ('d8b6602c-fcbb-437f-8874-3f4648f8d22d', '2025-01-03 16:49:07', NULL, NULL, 'BRONZE', 5,
        'f236923c-4746-4b5a-8377-e7c5b53799c2'),
       ('46c5e40f-655a-47f8-9570-61618d4e7ac2', '2025-01-03 16:49:07', NULL, NULL, 'SILVER', 8,
        'f236923c-4746-4b5a-8377-e7c5b53799c2'),
       ('417b1cfe-4815-4131-bc0d-766ffcce0f7d', '2025-01-03 16:49:07', NULL, NULL, 'GOLD', 10,
        'f236923c-4746-4b5a-8377-e7c5b53799c2');



# 강사 => auth 또는 캘린더 기능을 위한 추가 컬럼 예정
# 프로필 이미지는 ACADEMY_ID 로 폴더 만들고 TEACHER / STUDENT / COURSE 등 각각 따로 폴더 만들어서 관리
# S3 버킷? -> 식별자만 주고 이미지 파일 넣으면 된다?
# S3 요청 건수별로 가격 -> 비용을 고려해가지고 대안을 찾아보자.
# DB 에서 캐싱도 고려
# 공통적으로 path 는 가리고 파일 이름만 노출될 수 있도록 해본다.
# JOB 테이블 제거하고 직원 관리 페이지에서 직급은 자유롭게 수정할 수 있다.
# 본인인증은 불필요한 기능이라 판단되어 삭제 예정

CREATE TABLE teacher
(
    teacher_id         varchar(36),
    created_at         datetime,
    updated_at         datetime,
    deleted_at         datetime,
    teacher_name       varchar(20),  # 강사 본명
    teacher_phone      varchar(20),  # 연락처
    teacher_email      varchar(50),  # 이메일
    teacher_password   varchar(100), # 비밀번호
    teacher_job        varchar(36),  # 선임강사, 평강사 등 직원 관리 탭에서 학원이 알아서 등록할 수 있다.
    teacher_img_name   varchar(500), # DB에 들어갈 강사 프로필 이미지 이름
    teacher_img_origin varchar(500), # 웹에서 표시될 강사 프로필 이미지 이름
    auth_role          varchar(20),  # 권한 ADMIN OR MANAGER OR LECTURER
    academy_id         varchar(36)
);


INSERT INTO teacher
VALUES ('be37dd3d-b1c6-4aad-a378-abf2ed133423', '2025-01-03 16:25:14', NULL, NULL, '김실험', '010-2222-1111',
        '1234@sample.com', '1234', NULL, NULL, NULL, 'teacher', 'f236923c-4746-4b5a-8377-e7c5b53799c2');



# 이력 생성 / 이력? 강사 약력 또는 강사 소개라고 표현?
# 마찬가지로 path 에 대한 정보는 가리고 파일 이름만 사용할 수 있도록 노력한다.
# S3 또는 DB에 저장 고민
CREATE TABLE career
(
    career_id               varchar(36),
    created_at              datetime,
    updated_at              datetime,
    deleted_at              datetime,
    career_name             varchar(500), # 목록에 표시될 소개 이미지 이름
    career_info             varchar(500), # 소개 파일에 대한 간단한 설명(일단 냅둘게요)
    career_file_origin_name varchar(500), # 파일 실제 이름
    career_file_path        varchar(500), # 파일의 s3 경로
    display_status          tinyint(1),   # 해당 이미지 공개 여부
    teacher_id              varchar(36),  # 해당 선생 구분 위해
    academy_id              varchar(36)   # 학원별 구분 위해
);


# 학생
CREATE TABLE student
(
    student_id         varchar(36),
    created_at         datetime,
    updated_at         datetime,
    deleted_at         datetime,
    student_name       varchar(20),  # 실명
    student_email      varchar(50),  # 이메일
    student_phone      varchar(30),  # 핸드폰 번호
    student_password   varchar(100), # 비밀번호
    student_address    json,         # 배송지와 같은 정보를 JSON으로 관리, 최대 3개 배송지 설정, 대표 배송지 설정등 기능
    verification       tinyint(1),   # 0,1 나누기 / 본인 인증 여부 => 이메일 또는 휴대전화 등 수단은 여러 개 가능
    student_img_name   varchar(500), # DB에 들어갈 프로필 이미지 이름
    student_img_origin varchar(500), # 웹에서 표시될 프로필 이미지 이름
    ads_agreed         tinyint(1),   # 광고성 정보 동의 여부
    auth_role          varchar(20),  # 인가 제한, 학생은 아마도 STUDENT 고정?
    grade_id           varchar(36),  # 등급 할인등을 위해
    academy_id         varchar(36)   # 학원 별 관리 위해 - 외래키 인덱스(자동)
);

INSERT INTO student
VALUES ('7feeac19-89e9-41e4-8298-3f5fa7df9e5d', '2025-01-02 00:00:00', NULL, NULL, '테스트', 'test@test.com', NULL,
        '$2a$10$k1NYCnPEH7ijGofDRPZFjOnuacXU6YHP02wvE5spuiH7WOxIqFMXS', NULL, NULL, NULL, NULL, NULL, 'student', NULL,
        'f236923c-4746-4b5a-8377-e7c5b53799c2'),
       ('9c0df0de-5d41-414e-a021-94c84aaeeeae', '2025-01-03 16:32:18', NULL, NULL, '김학생', 'student2@example.com', NULL,
        '1234', NULL, 0, NULL, NULL, 1, 'student', NULL, 'f236923c-4746-4b5a-8377-e7c5b53799c2');



# AUTH2 관련  타입 테이블 / 구글? 네이버? 카카오? 에 대한 테이블 추가 필요


# 로그인 이력 생성
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

# 프로모션 쿠폰 테이블 -> 구상 단계, 의견 자유롭게
CREATE TABLE coupon
(
    coupon_id     varchar(36),
    created_at    datetime,
    updated_at    datetime,
    deleted_at    datetime,
    coupon_name   varchar(50),
    coupon_type   varchar(10), # 정액 할인 / 비율 할인
    coupon_amount int,
    expiration    int,         # 유효 기간에 대한 정보 정수로
    academy_id    varchar(36)
);

# 학생 쿠폰함 => 학생 ID + 쿠폰 ID 로 복합키
CREATE TABLE coupon_box
(
    academy_id         varchar(36),
    student_id         varchar(36),
    coupon_id          varchar(36),
    created_at         datetime,
    updated_at         datetime,
    deleted_at         datetime,
    coupon_status      tinyint(1), # 쿠폰 사용 여부
    coupon_expire_date datetime    # 만료일
);


# 과정 생성
CREATE TABLE course
(
    course_id         varchar(36),
    created_at        datetime,
    updated_at        datetime,
    deleted_at        datetime,
    course_name       varchar(100),  # 과정 이름
    course_subject    varchar(100),  # 과정 과목
    course_info       varchar(500),  # 과정에 대한 한 줄 정보
    course_tag        varchar(500),  # 과정 검색 및 특징 표현을 위한 태그인데 안쓰이면 삭제
    course_diff       varchar(100),  # 과정 수준에 대한 정보
    course_price      int,           # 과정 가격 정보
    course_activation tinyint(1),    # 활성화 여부
    course_img_name   varchar(500),  # DB에 표시될 이미지 이름
    course_img_origin varchar(1024), # 웹에 표시될 이미지 이름
    academy_id        varchar(36),
    teacher_id        varchar(36)    # 개설자
);


INSERT INTO course
VALUES ('bbb2ceeb-8203-4616-a899-9eacac9e5e0a', '2025-01-03 16:43:47', NULL, NULL, '테스트 코스 1', '테스트 과목',
        '테스트 코스 1 입니다.', 'test', 'easy', 1000, 1, NULL, NULL, 'f236923c-4746-4b5a-8377-e7c5b53799c2',
        'be37dd3d-b1c6-4aad-a378-abf2ed133423'),
       ('105b2075-2a7b-48c2-85eb-47a07f0af714', '2025-01-03 16:44:20', NULL, NULL, '테스트 코스 2', '테스트 과목2',
        '테스트 코스 2 입니다.', 'test2', 'normal', 2000, 1, NULL, NULL, 'f236923c-4746-4b5a-8377-e7c5b53799c2',
        'be37dd3d-b1c6-4aad-a378-abf2ed133423');


# 강의 생성 => 텍스트 강의 인지 비디오 강의인지 여부
CREATE TABLE class
(
    class_id      varchar(36),
    created_at    datetime,
    updated_at    datetime,
    deleted_at    datetime,
    class_name    varchar(100), # 강의 이름
    class_content text,         # 강의 내용
    class_seq     smallint,     # 강의 번호
    class_type    varchar(20),  # TEXT OR VIDEO OR ..? IS_VIDEO 0,1 로? 아니면 확장자 그 자체
    academy_id    varchar(36),
    teacher_id    varchar(36),
    course_id     varchar(36)
);


INSERT INTO class
VALUES ('b367ac49-69ed-4024-9f0d-1fa53f2bc105', '2025-01-03 16:47:52', NULL, NULL, '테스트 클래스2', '테스트 클래스 2 입니다.', 2,
        'text', 'f236923c-4746-4b5a-8377-e7c5b53799c2', 'be37dd3d-b1c6-4aad-a378-abf2ed133423',
        'bbb2ceeb-8203-4616-a899-9eacac9e5e0a'),
       ('39269e22-b8b7-4a30-bf32-aa04aa3ceb99', '2025-01-03 16:47:54', NULL, NULL, '테스트 클래스1', '테스트 클래스 1 입니다.', 1,
        'text', 'f236923c-4746-4b5a-8377-e7c5b53799c2', 'be37dd3d-b1c6-4aad-a378-abf2ed133423',
        'bbb2ceeb-8203-4616-a899-9eacac9e5e0a');



# 강의 파일 생성 => 한 개의 강의에서 복수의 파일을 올릴 수 있으니 테이블 따로 관리
# 강의 파일 디렉토리 구성 방법에 대해 설정, S3 에 대한 path 가 외부로 노출되지 않도록 academy_id, teacher_id, course_id 합성해서 디렉토리 구성하고
# 외부로는 파일 이름만 노출될 수 있는 방법 구상

CREATE TABLE class_file
(
    class_file_id          varchar(36),
    created_at             datetime,
    updated_at             datetime,
    deleted_at             datetime,
    class_file_name        varchar(500), # DB에 저장될 이름
    class_file_origin_name varchar(500), # 웹에 표시될 이름
    class_file_type        varchar(50),  # 파일 확장자
    class_file_size        int,          # 필요한지 ==> 추후 학원별로 s3 storage 얼마나 사용하고 있는지 확인하는데 사용 가능할 듯...
    class_file_path        varchar(200), # 파일의 s3 경로
    academy_id             varchar(36),
    teacher_id             varchar(36),  # 작성자 밝히기위해
    course_id              varchar(36),  # 과정별 파일 목록 출력 위해? 필요한지?
    class_id               varchar(36)
);

# 학습 메모용 테이블
CREATE TABLE class_memo
(
    class_id   varchar(36),
    student_id varchar(36),
    created_at datetime,
    updated_at datetime,
    deleted_at datetime,
    memo       text
);



# 수강 테이블 생성 => 학생 마이페이지에서 수강 목록을 위한 테이블
# 컬럼 자유롭게 추가

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
    CONSTRAINT chk_enrollment_status CHECK (enrollment_status IN ('INIT', 'COMPLETED', 'CANCELED'))
    #수강상태  INIT:수강전 COMPLETED:수강완료 CANCELED:수강취소
);




# 장바구니 테이블
# 과정 과 학생을 다대다 로 이어서 장바구니를 보여준다.
CREATE TABLE cart (
                        cart_id varchar(36) ,
                        created_at datetime ,
                        updated_at datetime ,
                        deleted_at datetime ,
                        student_id varchar(36) ,
                        course_id varchar(36) ,
                        academy_id varchar(36)
) ;


# 학생 주문 목록 테이블 생성
# 다른 사례 같은거 참고하면서
#	=> 하나의 주문에 하나의 과정만 가능한지 아니면 여러 개의 과정이 하나의 주문에 담길 수 있는지?
# ORDER 가 예약어라 사용할 수 없으므로 적절한 단어 있으면 수정
# 주문 정보에서 주소, 우편번호 전화번호 등 개인정보 관리 어떻게 할지
# 쿠폰이나 프로모션 할인 정보는 주문 테이블에 담는게 맞나?
-- jab.orders definition

# 장바구니에서 선택된 상품만 주문 테이블로 등록
# 한 행당 상품은 하나
# 주문번호로 묶는다.
CREATE TABLE orders (
                          orders_id varchar(36) ,
                          created_at datetime ,
                          updated_at datetime ,
                          deleted_at datetime ,
                          order_number varchar(100) ,
                          total_price int ,                    # 주문 금액
                          order_address varchar(300) ,
                          order_detail_addr varchar(300) ,
                          order_postcode varchar(20) ,
                          order_status tinyint ,
                          student_id varchar(36) ,              # 주문자
                          course_id varchar(36) ,               # 주문 상품
                          academy_id varchar(36)                # 학원
);



-- jab.payment definition

# 결제 이력 테이블
# 주문의 각 상품 별로 삽입
CREATE TABLE payment (
                           payment_id varchar(36) ,
                           created_at datetime ,
                           updated_at datetime ,
                           deleted_at datetime ,
                           payment_amount int ,                   # 실결제 금액
                           payment_method varchar(100) ,
                           payment_status varchar(30) ,          #'INIT' / 'PENDING' / 'PAID' / 'FAILED' / 'CANCELED'
                           student_id varchar(36) ,              # 결제자 정보
                           orders_id varchar(36) ,               # 상품 이름, 가격, 등등에 대한 정보
                           course_id varchar(100) ,
                           academy_id varchar(100)
) ;


# 학습 이력 생성 => 학습 시간 기록 어떻게 할지 모르겠다
CREATE TABLE study_history
(
    study_history_id   varchar(36),
    created_at         datetime,
    updated_at         datetime,
    deleted_at         datetime,
    study_time         int, # 강의 페이지 나가기 TIMESTAMP - 강의 페이지 입장 TIMESTAMP => 정수로 관리?
    is_class_file_down tinyint(1),
    academy_id         varchar(36),
    student_id         varchar(36),
    class_id           varchar(36)
);


# QNA 게시판 생성 => 답변의 답변을 허용할 것인지 아니면 1개의 qna 에 1개의 답변만 허가할 것인지
CREATE TABLE qna
(
    qna_id            varchar(36),
    created_at        datetime,
    updated_at        datetime,
    deleted_at        datetime,
    qna_re_ref        int,          # 원본글이 될 번호
    qna_re_lev        int,          # 답변 글인지 여부 확인 및 답변의 답변 등을 위한 정보
    qna_re_seq        int,          # 원본글에 대한 답변 등의 출력 순서를 위한 컬럼
    qna_subject       varchar(150), # QNA 제목
    qna_password      varchar(36),  # 비밀번호
    qna_content       text,
    qna_readcount     int,          # 조회수
    qna_exposure_stat tinyint(1),   # QNA 노출 여부
    qna_answer_status tinyint(1),   # QNA 답변 상태
    qna_file_name     varchar(500), # 첨부파일 db 이름
    qna_file_origin   varchar(500), # 첨부파일 웹 이름
    academy_id        varchar(36),  # 학원 ID
    teacher_id        varchar(36),  # 담당자 또는 작성자
    student_id        varchar(36),  # 작성자
    course_id         varchar(36),  # 과정별 QNA 출력 위해
    class_id          varchar(36)   # 무슨 강의에 대한 질문인지 알기 위해
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
    academy_id           varchar(36),
    course_id            varchar(36),
    teacher_id           varchar(36),
    student_id           varchar(36)
);

INSERT INTO review
VALUES ('db976c8e-00e6-4387-a6a8-a0d5df644236', '2025-01-03 16:49:07', NULL, NULL, 1, 0, '수강평 테스트1', '1234',
        '수강평 테스트 내용1234', 10, 0, 1, 'f236923c-4746-4b5a-8377-e7c5b53799c2', 'f236923c-4746-4b5a-8377-e7c5b53799c2',
        'be37dd3d-b1c6-4aad-a378-abf2ed133423', '9c0df0de-5d41-414e-a021-94c84aaeeeae'),
       ('ecdbe2a9-3213-466c-b71f-2ec0c4617fb5', '2025-01-03 16:49:07', NULL, NULL, 2, 0, '수강평 테스트2', '1234',
        '수강평 테스트 내용1234', 20, 0, 1, 'f236923c-4746-4b5a-8377-e7c5b53799c2', 'f236923c-4746-4b5a-8377-e7c5b53799c2',
        'be37dd3d-b1c6-4aad-a378-abf2ed133423', '7feeac19-89e9-41e4-8298-3f5fa7df9e5d');



# 다목적 게시판 테이블 => 공지사항, 자유 게시판 등등 비슷한 포맷으로 사용할 게시판 테이블? 학원별 요구에 따라 게시판 생성할 때 사용
# 댓글 사용? 사용할 거면 comments 테이블 따로 생성, 수업했던 것과 똑같이 진행하면 된다.
CREATE TABLE board
(
    board_id            varchar(36),
    created_at          datetime,
    updated_at          datetime,
    deleted_at          datetime,
    board_re_ref        int,          -- 원본 글인지 여부
    board_re_lev        int,          -- 답변 글인지 여부
    board_re_seq        int,          -- 출력 순서를 위한 컬럼
    board_notice        tinyint(1),   -- 공지사항 최상단에 고정될 알림글인지 여부
    board_subject       varchar(150), -- 글 제목
    board_email         varchar(100), -- 작성자 아이디
    board_password      varchar(100), -- 글 비밀번호
    board_content       text,         -- 글 내용
    board_file_name     varchar(500), -- 파일 첨부 (공지사항 일 때만 가능)
    board_file_origin   varchar(500), -- 파일 첨부 (공지사항 일 때만 가능)
    board_readcount     int,          -- 조회수
    board_exposure_stat tinyint(1),   -- 글 노출 설정
    board_type_id       varchar(36),  -- 게시판 종류
    academy_id          varchar(36),  -- 학원 ID (외래키)
    course_id           varchar(36),  -- 과정 ID (추가된 컬럼)
    teacher_id          varchar(36),  -- 교사 ID (추가된 컬럼)
    class_id            varchar(36)   -- 클래스 ID (추가된 컬럼)
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

INSERT INTO board_type
VALUES ('e638034f-5b96-46a7-ad26-b7e9b5a011dc', '2025-01-03 16:49:07', NULL, NULL, '자유게시판',
        'f236923c-4746-4b5a-8377-e7c5b53799c2'),
       ('4ed58d29-ff7b-40fe-a77d-bef58a3d63a3', '2025-01-03 16:49:07', NULL, NULL, '공지사항',
        'f236923c-4746-4b5a-8377-e7c5b53799c2');


# 댓글용 테이블, 필요 컬럼 추가

CREATE TABLE comments
(
    comments_id      varchar(36),
    comments_email   varchar(30), # 작성자
    comments_content text,        # 글 내용
    board_id         varchar(36)  # 원본 게시글 참조
);


# 로그인 유지 기능을 위한 테이블 참고(수업)
CREATE TABLE persistent_logins
(
    series    varchar(64) NOT NULL, # 기기, 브라우저별 쿠키를 구분할 고유 값
    username  varchar(64) NOT NULL, # 스프링 시큐리티에 입력된 principle??
    token     varchar(64) NOT NULL, # 브라우저가 가지고 있는 쿠키의 값을 검증할 인증값
    last_used timestamp   NOT NULL, # 가장 최신 자동 로그인 시간
    PRIMARY KEY (series)
);


# 개인정보처리 약관

CREATE TABLE privacy_term
(
    privacy_term_id              varchar(36),
    created_at                   datetime,
    updated_at                   datetime,
    deleted_at                   datetime,
    privacy_term_subject         varchar(100), # 개인정보처리 약관 글 제목
    privacy_term_name            varchar(100), # 작성자(관리 책임자 또는 대표)
    privacy_term_content         text,
    privacy_term_effective_date  date,         # 효력 개시일
    privacy_term_expiration_date date,         # 효력 상실일
    privacy_term_status          tinyint(1),   # 어떤 정보를 위한 컬럼?
    academy_id                   varchar(36)
);


# 서비스 약관
CREATE TABLE service_term
(
    service_term_id              varchar(36),
    created_at                   datetime,
    updated_at                   datetime,
    deleted_at                   datetime,
    service_term_subject         varchar(100), # 서비스 약관 글 제목
    service_term_name            varchar(100), # 작성자(관리 책임자 또는 대표)
    service_term_content         text,
    service_term_effective_date  datetime,     # 효력 개시일
    service_term_expiration_date datetime,     # 효력 상실일
    service_term_status          tinyint(1),   # 어떤 정보를 위한 컬럼?
    academy_id                   varchar(36)
);



# 레디스에서 영구적으로 저장해야할 정보 DB


/*
 주의!
 대용량 더미 데이터 생성용 프로시저
 학생, 리뷰 테이블에 각각 10000개 행 추가하므로
 필요할 때만 사용

 DELIMITER ;; 부터 복사해서 사용하면 된다.

 비슷한 구조로 다른 테이블 삽입문을 추가할 수 있다.



DELIMITER ;;
CREATE DEFINER=jab@localhost PROCEDURE jab.dummy()
BEGIN
    DECLARE i INT DEFAULT 1;

    WHILE i <= 10000 DO
        IF i <= 1000 THEN
            INSERT INTO student
            VALUES (UUID(), CURRENT_TIMESTAMP, NULL, NULL, CONCAT('학생', i), CONCAT('google', i, '@test.com'), NULL, '$2a$10$k1NYCnPEH7ijGofDRPZFjOnuacXU6YHP02wvE5spuiH7WOxIqFMXS', NULL, 1, NULL, NULL, 0, 'student', 'd8b6602c-fcbb-437f-8874-3f4648f8d22d', 'f236923c-4746-4b5a-8377-e7c5b53799c2');

    		INSERT INTO review
    		VALUES (UUID(), CURRENT_TIMESTAMP, NULL, NULL, i+2, 0, CONCAT('수강평 테스트',i) ,'1234',concat('수강평 테스트 내용',i),10,i,1,'f236923c-4746-4b5a-8377-e7c5b53799c2','bbb2ceeb-8203-4616-a899-9eacac9e5e0a','be37dd3d-b1c6-4aad-a378-abf2ed133423','9c0df0de-5d41-414e-a021-94c84aaeeeae');

        ELSEIF i <= 3000 THEN
            INSERT INTO student
            VALUES (UUID(), CURRENT_TIMESTAMP, NULL, NULL, CONCAT('샘플', i), CONCAT('test', i, '@sample.com'), NULL, '$2a$10$k1NYCnPEH7ijGofDRPZFjOnuacXU6YHP02wvE5spuiH7WOxIqFMXS', NULL, 0, NULL, NULL, 1, 'student', '46c5e40f-655a-47f8-9570-61618d4e7ac2', 'f236923c-4746-4b5a-8377-e7c5b53799c2');

        	INSERT INTO review
    		VALUES (UUID(), CURRENT_TIMESTAMP, NULL, NULL, i+2, 0, CONCAT('수강평 테스트',i) ,'1234',concat('수강평 테스트 내용',i),15,i,1,'f236923c-4746-4b5a-8377-e7c5b53799c2','bbb2ceeb-8203-4616-a899-9eacac9e5e0a','be37dd3d-b1c6-4aad-a378-abf2ed133423','9c0df0de-5d41-414e-a021-94c84aaeeeae');
        ELSEIF i <= 5000 THEN
            INSERT INTO student
            VALUES (UUID(), CURRENT_TIMESTAMP, NULL, NULL, CONCAT('이학생', i), CONCAT('sample', i, '@test.com'), NULL, '$2a$10$k1NYCnPEH7ijGofDRPZFjOnuacXU6YHP02wvE5spuiH7WOxIqFMXS', NULL, 1, NULL, NULL, 1, 'student', 'd8b6602c-fcbb-437f-8874-3f4648f8d22d', 'f236923c-4746-4b5a-8377-e7c5b53799c2');

        	INSERT INTO review
    		VALUES (UUID(), CURRENT_TIMESTAMP, NULL, NULL, i+2, 0, CONCAT('수강평 테스트',i) ,'1234',concat('수강평 테스트 내용',i),20,i,1,'f236923c-4746-4b5a-8377-e7c5b53799c2','bbb2ceeb-8203-4616-a899-9eacac9e5e0a','be37dd3d-b1c6-4aad-a378-abf2ed133423','9c0df0de-5d41-414e-a021-94c84aaeeeae');
        ELSEIF i <= 6000 THEN
            INSERT INTO student
            VALUES (UUID(), CURRENT_TIMESTAMP, NULL, NULL, CONCAT('김학생', i), CONCAT('let', i, '@google.com'), NULL, '$2a$10$k1NYCnPEH7ijGofDRPZFjOnuacXU6YHP02wvE5spuiH7WOxIqFMXS', NULL, 1, NULL, NULL, 0, 'student', '417b1cfe-4815-4131-bc0d-766ffcce0f7d', 'f236923c-4746-4b5a-8377-e7c5b53799c2');

        	INSERT INTO review
    		VALUES (UUID(), CURRENT_TIMESTAMP, NULL, NULL, i+2, 0, CONCAT('수강평 테스트',i) ,'1234',concat('수강평 테스트 내용',i),30,i,1,'f236923c-4746-4b5a-8377-e7c5b53799c2','bbb2ceeb-8203-4616-a899-9eacac9e5e0a','be37dd3d-b1c6-4aad-a378-abf2ed133423','9c0df0de-5d41-414e-a021-94c84aaeeeae');
        ELSEIF i <= 8000 THEN
            INSERT INTO student
            VALUES (UUID(), CURRENT_TIMESTAMP, NULL, NULL, CONCAT('박학생', i), CONCAT('sample', i, '@test.com'), NULL, '$2a$10$k1NYCnPEH7ijGofDRPZFjOnuacXU6YHP02wvE5spuiH7WOxIqFMXS', NULL, 1, NULL, NULL, 0, 'student', '417b1cfe-4815-4131-bc0d-766ffcce0f7d', 'f236923c-4746-4b5a-8377-e7c5b53799c2');

        	INSERT INTO review
    		VALUES (UUID(), CURRENT_TIMESTAMP, NULL, NULL, i+2, 0, CONCAT('수강평 테스트',i) ,'1234',concat('수강평 테스트 내용',i),35,i,1,'f236923c-4746-4b5a-8377-e7c5b53799c2','bbb2ceeb-8203-4616-a899-9eacac9e5e0a','be37dd3d-b1c6-4aad-a378-abf2ed133423','9c0df0de-5d41-414e-a021-94c84aaeeeae');
        ELSE
            INSERT INTO student
            VALUES (UUID(), CURRENT_TIMESTAMP, NULL, NULL, CONCAT('시연', i), CONCAT('testy', i, '@daum.net'), NULL, '$2a$10$k1NYCnPEH7ijGofDRPZFjOnuacXU6YHP02wvE5spuiH7WOxIqFMXS', NULL, 0, NULL, NULL, 1, 'student', '417b1cfe-4815-4131-bc0d-766ffcce0f7d', 'f236923c-4746-4b5a-8377-e7c5b53799c2');

        	INSERT INTO review
    		VALUES (UUID(), CURRENT_TIMESTAMP, NULL, NULL, i+2, 0, CONCAT('수강평 테스트',i) ,'1234',concat('수강평 테스트 내용',i),45,i,1,'f236923c-4746-4b5a-8377-e7c5b53799c2','bbb2ceeb-8203-4616-a899-9eacac9e5e0a','be37dd3d-b1c6-4aad-a378-abf2ed133423','9c0df0de-5d41-414e-a021-94c84aaeeeae');
        END IF;

        SET i = i + 1;
    END WHILE;

END ;;
DELIMITER ;
 */
