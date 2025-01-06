-- 데이터베이스 덤핑 기능으로 만든 파일이므로 실행되지 않는다면 말해주길 바람.


-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: jab
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `academy`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `academy` (
  `academy_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `academy_name` varchar(150) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `academy_address` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `academy_detail_addr` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `academy_postalcode` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `academy_owner` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `academy_contect` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `business_regis_num` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `academy`
--

LOCK TABLES `academy` WRITE;
/*!40000 ALTER TABLE `academy` DISABLE KEYS */;
INSERT INTO `academy` VALUES ('f236923c-4746-4b5a-8377-e7c5b53799c2','2025-01-03 16:14:00',NULL,NULL,'코드 크래프트','서울 종로구 북촌로 31-4','222','03055','사장님','010-1234-5678','1234-5678-0000');
/*!40000 ALTER TABLE `academy` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `board`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board` (
  `board_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `board_re_ref` int DEFAULT NULL,
  `board_re_lev` int DEFAULT NULL,
  `board_re_seq` int DEFAULT NULL,
  `board_notice` tinyint(1) DEFAULT NULL,
  `board_subject` varchar(150) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `board_email` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `board_password` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `board_content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci,
  `board_file_name` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `board_file_origin` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `board_readcount` int DEFAULT NULL,
  `board_exposure_stat` tinyint(1) DEFAULT NULL,
  `board_type_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `academy_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `board_type`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `board_type` (
  `board_type_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `board_type_name` varchar(16) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `academy_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `board_type` WRITE;
/*!40000 ALTER TABLE `board_type` DISABLE KEYS */;
INSERT INTO `board_type` VALUES ('e638034f-5b96-46a7-ad26-b7e9b5a011dc','2025-01-03 16:49:07',NULL,NULL,'자유게시판','f236923c-4746-4b5a-8377-e7c5b53799c2'),('4ed58d29-ff7b-40fe-a77d-bef58a3d63a3','2025-01-03 16:49:07',NULL,NULL,'공지사항','f236923c-4746-4b5a-8377-e7c5b53799c2');
/*!40000 ALTER TABLE `board_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `career`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `career` (
  `career_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `career_name` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `career_info` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `career_file_name` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `career_file_origin` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `display_status` tinyint(1) DEFAULT NULL,
  `teacher_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `academy_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `class`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `class` (
  `class_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `class_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `class_content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci,
  `class_seq` smallint DEFAULT NULL,
  `class_type` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `academy_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `teacher_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `course_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `class` WRITE;
/*!40000 ALTER TABLE `class` DISABLE KEYS */;
INSERT INTO `class` VALUES ('b367ac49-69ed-4024-9f0d-1fa53f2bc105','2025-01-03 16:47:52',NULL,NULL,'테스트 클래스2','테스트 클래스 2 입니다.',2,'text','f236923c-4746-4b5a-8377-e7c5b53799c2','be37dd3d-b1c6-4aad-a378-abf2ed133423','bbb2ceeb-8203-4616-a899-9eacac9e5e0a'),('39269e22-b8b7-4a30-bf32-aa04aa3ceb99','2025-01-03 16:47:54',NULL,NULL,'테스트 클래스1','테스트 클래스 1 입니다.',1,'text','f236923c-4746-4b5a-8377-e7c5b53799c2','be37dd3d-b1c6-4aad-a378-abf2ed133423','bbb2ceeb-8203-4616-a899-9eacac9e5e0a');
/*!40000 ALTER TABLE `class` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `class_file`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `class_file` (
  `class_file_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `class_file_name` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `class_file_origin` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `class_file_type` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `class_file_size` int DEFAULT NULL,
  `class_file_info` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `academy_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `teacher_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `course_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `class_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `class_memo`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `class_memo` (
  `class_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `student_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `memo` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `comments`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comments` (
  `comments_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `comments_email` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `comments_content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci,
  `board_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `coupon`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `coupon` (
  `coupon_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `coupon_name` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `coupon_type` varchar(10) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `coupon_amount` int DEFAULT NULL,
  `expiration` int DEFAULT NULL,
  `academy_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `coupon_box`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `coupon_box` (
  `academy_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `student_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `coupon_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `coupon_status` tinyint(1) DEFAULT NULL,
  `coupon_expire_date` date DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `course`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `course` (
  `course_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `course_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `course_subject` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `course_info` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `course_tag` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `course_diff` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `course_price` int DEFAULT NULL,
  `course_activation` tinyint(1) DEFAULT NULL,
  `course_img_name` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `course_img_origin` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `academy_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `teacher_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `course` WRITE;
/*!40000 ALTER TABLE `course` DISABLE KEYS */;
INSERT INTO `course` VALUES ('bbb2ceeb-8203-4616-a899-9eacac9e5e0a','2025-01-03 16:43:47',NULL,NULL,'테스트 코스 1','테스트 과목','테스트 코스 1 입니다.','test','easy',1000,1,NULL,NULL,'f236923c-4746-4b5a-8377-e7c5b53799c2','be37dd3d-b1c6-4aad-a378-abf2ed133423'),('105b2075-2a7b-48c2-85eb-47a07f0af714','2025-01-03 16:44:20',NULL,NULL,'테스트 코스 2','테스트 과목2','테스트 코스 2 입니다.','test2','normal',2000,1,NULL,NULL,'f236923c-4746-4b5a-8377-e7c5b53799c2','be37dd3d-b1c6-4aad-a378-abf2ed133423');
/*!40000 ALTER TABLE `course` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `enrollment`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `enrollment` (
  `enrollment_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `academy_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `student_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `course_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `grade`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grade` (
  `grade_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `grade_name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `discount_rate` tinyint DEFAULT NULL,
  `academy_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;


LOCK TABLES `grade` WRITE;
/*!40000 ALTER TABLE `grade` DISABLE KEYS */;
INSERT INTO `grade` VALUES ('d8b6602c-fcbb-437f-8874-3f4648f8d22d','2025-01-03 16:49:07',NULL,NULL,'BRONZE',5,'f236923c-4746-4b5a-8377-e7c5b53799c2'),('46c5e40f-655a-47f8-9570-61618d4e7ac2','2025-01-03 16:49:07',NULL,NULL,'SILVER',8,'f236923c-4746-4b5a-8377-e7c5b53799c2'),('417b1cfe-4815-4131-bc0d-766ffcce0f7d','2025-01-03 16:49:07',NULL,NULL,'GOLD',10,'f236923c-4746-4b5a-8377-e7c5b53799c2');
/*!40000 ALTER TABLE `grade` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `login_history`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `login_history` (
  `login_history_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `ip_info` varchar(40) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `os_info` varchar(40) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `browser_info` varchar(40) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `login_success` tinyint(1) DEFAULT NULL,
  `academy_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `student_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `orders`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `orders_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `student_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `course_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `order_price` int DEFAULT NULL,
  `payment_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `academy_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `payment`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payment` (
  `payment_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `orders_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `payment_amount` int DEFAULT NULL,
  `student_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `payment_status` tinyint(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `persistent_logins`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `persistent_logins` (
  `series` varchar(64) NOT NULL,
  `username` varchar(64) NOT NULL,
  `token` varchar(64) NOT NULL,
  `last_used` timestamp NOT NULL,
  PRIMARY KEY (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `privacy_term`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `privacy_term` (
  `privacy_term_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `privacy_term_subject` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `privacy_term_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `privacy_term_content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci,
  `privacy_term_effective_date` date DEFAULT NULL,
  `privacy_term_expiration_date` date DEFAULT NULL,
  `privacy_term_status` tinyint(1) DEFAULT NULL,
  `academy_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `qna`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `qna` (
  `qna_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `qna_re_ref` int DEFAULT NULL,
  `qna_re_lev` int DEFAULT NULL,
  `qna_re_seq` int DEFAULT NULL,
  `qna_subject` varchar(150) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `qna_password` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `qna_content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci,
  `qna_readcount` int DEFAULT NULL,
  `qna_exposure_stat` tinyint(1) DEFAULT NULL,
  `qna_file_name` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `qna_file_origin` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `teacher_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `student_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `course_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `class_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `review`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `review` (
  `review_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `review_re_ref` int DEFAULT NULL,
  `review_re_lev` int DEFAULT NULL,
  `review_subject` varchar(150) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `review_password` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `review_content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci,
  `review_rating` tinyint DEFAULT NULL,
  `review_readcount` int DEFAULT NULL,
  `review_exposure_stat` tinyint(1) DEFAULT NULL,
  `academy_id` varchar(36) DEFAULT NULL,
  `course_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `teacher_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `student_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `review` WRITE;
/*!40000 ALTER TABLE `review` DISABLE KEYS */;
INSERT INTO `review` VALUES ('db976c8e-00e6-4387-a6a8-a0d5df644236','2025-01-03 16:49:07',NULL,NULL,1,0,'수강평 테스트1','1234','수강평 테스트 내용1234',10,0,1,'f236923c-4746-4b5a-8377-e7c5b53799c2','f236923c-4746-4b5a-8377-e7c5b53799c2','be37dd3d-b1c6-4aad-a378-abf2ed133423','9c0df0de-5d41-414e-a021-94c84aaeeeae'),('ecdbe2a9-3213-466c-b71f-2ec0c4617fb5','2025-01-03 16:49:07',NULL,NULL,2,0,'수강평 테스트2','1234','수강평 테스트 내용1234',20,0,1,'f236923c-4746-4b5a-8377-e7c5b53799c2','f236923c-4746-4b5a-8377-e7c5b53799c2','be37dd3d-b1c6-4aad-a378-abf2ed133423','7feeac19-89e9-41e4-8298-3f5fa7df9e5d');
/*!40000 ALTER TABLE `review` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Table structure for table `service_term`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service_term` (
  `service_term_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `service_term_subject` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `service_term_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `service_term_content` text CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci,
  `service_term_effective_date` datetime DEFAULT NULL,
  `service_term_expiration_date` datetime DEFAULT NULL,
  `service_term_status` tinyint(1) DEFAULT NULL,
  `academy_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `student`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `student` (
  `student_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `student_name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `student_email` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `student_phone` varchar(30) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `student_password` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `student_address` json DEFAULT NULL,
  `verification` tinyint(1) DEFAULT NULL,
  `student_img_name` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `student_img_origin` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `ads_agreed` tinyint(1) DEFAULT NULL,
  `auth_role` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `grade_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `academy_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `student` WRITE;
/*!40000 ALTER TABLE `student` DISABLE KEYS */;
INSERT INTO `student` VALUES ('7feeac19-89e9-41e4-8298-3f5fa7df9e5d','2025-01-02 00:00:00',NULL,NULL,'테스트','test@test.com',NULL,'$2a$10$k1NYCnPEH7ijGofDRPZFjOnuacXU6YHP02wvE5spuiH7WOxIqFMXS',NULL,NULL,NULL,NULL,NULL,'student',NULL,'f236923c-4746-4b5a-8377-e7c5b53799c2'),('9c0df0de-5d41-414e-a021-94c84aaeeeae','2025-01-03 16:32:18',NULL,NULL,'김학생','student2@example.com',NULL,'1234',NULL,0,NULL,NULL,1,'student',NULL,'f236923c-4746-4b5a-8377-e7c5b53799c2');
/*!40000 ALTER TABLE `student` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `study_history`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `study_history` (
  `study_history_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `study_time` int DEFAULT NULL,
  `is_class_file_down` tinyint(1) DEFAULT NULL,
  `academy_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `student_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `class_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `teacher`
--

/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `teacher` (
  `teacher_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `teacher_name` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `teacher_phone` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `teacher_email` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `teacher_password` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `teacher_job` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `teacher_img_name` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `teacher_img_origin` varchar(500) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `auth_role` varchar(20) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `academy_id` varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

LOCK TABLES `teacher` WRITE;
/*!40000 ALTER TABLE `teacher` DISABLE KEYS */;
INSERT INTO `teacher` VALUES ('be37dd3d-b1c6-4aad-a378-abf2ed133423','2025-01-03 16:25:14',NULL,NULL,'김실험','010-2222-1111','1234@sample.com','1234',NULL,NULL,NULL,'teacher','f236923c-4746-4b5a-8377-e7c5b53799c2');
/*!40000 ALTER TABLE `teacher` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Dumping routines for database 'jab'
--
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;


/*
 주의!
 대용량 더미 데이터 생성용 프로시저
 학생, 리뷰 테이블에 각각 10000개 행 추가하므로
 필요할 때만 사용

 DELIMITER ;; 부터 복사해서 사용하면 된다.

 비슷한 구조로 다른 테이블 삽입문을 추가할 수 있다.



DELIMITER ;;
CREATE DEFINER=`jab`@`localhost` PROCEDURE `dummy`()
BEGIN

    DECLARE i INT DEFAULT 1;



    WHILE i <= 10000 DO

        IF i <= 1000 THEN

            INSERT INTO student

            VALUES (UUID(), CURRENT_TIMESTAMP, NULL, NULL, CONCAT('학생', i), CONCAT('google', i, '@test.com'), NULL, '$2a$10$k1NYCnPEH7ijGofDRPZFjOnuacXU6YHP02wvE5spuiH7WOxIqFMXS', NULL, 1, NULL, NULL, 0, 'student', 'd8b6602c-fcbb-437f-8874-3f4648f8d22d', 'f236923c-4746-4b5a-8377-e7c5b53799c2');

    		INSERT INTO review

    		VALUES (UUID(), CURRENT_TIMESTAMP, NULL, NULL, i+2, 0, CONCAT('수강평 테스트',i) ,'1234',concat('수강평 테스트 내용',i),10,i,1,'f236923c-4746-4b5a-8377-e7c5b53799c2','f236923c-4746-4b5a-8377-e7c5b53799c2','be37dd3d-b1c6-4aad-a378-abf2ed133423','9c0df0de-5d41-414e-a021-94c84aaeeeae');



        ELSEIF i <= 3000 THEN

            INSERT INTO student

            VALUES (UUID(), CURRENT_TIMESTAMP, NULL, NULL, CONCAT('샘플', i), CONCAT('test', i, '@sample.com'), NULL, '$2a$10$k1NYCnPEH7ijGofDRPZFjOnuacXU6YHP02wvE5spuiH7WOxIqFMXS', NULL, 0, NULL, NULL, 1, 'student', '46c5e40f-655a-47f8-9570-61618d4e7ac2', 'f236923c-4746-4b5a-8377-e7c5b53799c2');



        	INSERT INTO review

    		VALUES (UUID(), CURRENT_TIMESTAMP, NULL, NULL, i+2, 0, CONCAT('수강평 테스트',i) ,'1234',concat('수강평 테스트 내용',i),15,i,1,'f236923c-4746-4b5a-8377-e7c5b53799c2','f236923c-4746-4b5a-8377-e7c5b53799c2','be37dd3d-b1c6-4aad-a378-abf2ed133423','9c0df0de-5d41-414e-a021-94c84aaeeeae');

        ELSEIF i <= 5000 THEN

            INSERT INTO student

            VALUES (UUID(), CURRENT_TIMESTAMP, NULL, NULL, CONCAT('이학생', i), CONCAT('sample', i, '@test.com'), NULL, '$2a$10$k1NYCnPEH7ijGofDRPZFjOnuacXU6YHP02wvE5spuiH7WOxIqFMXS', NULL, 1, NULL, NULL, 1, 'student', 'd8b6602c-fcbb-437f-8874-3f4648f8d22d', 'f236923c-4746-4b5a-8377-e7c5b53799c2');



        	INSERT INTO review

    		VALUES (UUID(), CURRENT_TIMESTAMP, NULL, NULL, i+2, 0, CONCAT('수강평 테스트',i) ,'1234',concat('수강평 테스트 내용',i),20,i,1,'f236923c-4746-4b5a-8377-e7c5b53799c2','f236923c-4746-4b5a-8377-e7c5b53799c2','be37dd3d-b1c6-4aad-a378-abf2ed133423','9c0df0de-5d41-414e-a021-94c84aaeeeae');

        ELSEIF i <= 6000 THEN

            INSERT INTO student

            VALUES (UUID(), CURRENT_TIMESTAMP, NULL, NULL, CONCAT('김학생', i), CONCAT('let', i, '@google.com'), NULL, '$2a$10$k1NYCnPEH7ijGofDRPZFjOnuacXU6YHP02wvE5spuiH7WOxIqFMXS', NULL, 1, NULL, NULL, 0, 'student', '417b1cfe-4815-4131-bc0d-766ffcce0f7d', 'f236923c-4746-4b5a-8377-e7c5b53799c2');



        	INSERT INTO review

    		VALUES (UUID(), CURRENT_TIMESTAMP, NULL, NULL, i+2, 0, CONCAT('수강평 테스트',i) ,'1234',concat('수강평 테스트 내용',i),30,i,1,'f236923c-4746-4b5a-8377-e7c5b53799c2','f236923c-4746-4b5a-8377-e7c5b53799c2','be37dd3d-b1c6-4aad-a378-abf2ed133423','9c0df0de-5d41-414e-a021-94c84aaeeeae');

        ELSEIF i <= 8000 THEN

            INSERT INTO student

            VALUES (UUID(), CURRENT_TIMESTAMP, NULL, NULL, CONCAT('박학생', i), CONCAT('sample', i, '@test.com'), NULL, '$2a$10$k1NYCnPEH7ijGofDRPZFjOnuacXU6YHP02wvE5spuiH7WOxIqFMXS', NULL, 1, NULL, NULL, 0, 'student', '417b1cfe-4815-4131-bc0d-766ffcce0f7d', 'f236923c-4746-4b5a-8377-e7c5b53799c2');



        	INSERT INTO review

    		VALUES (UUID(), CURRENT_TIMESTAMP, NULL, NULL, i+2, 0, CONCAT('수강평 테스트',i) ,'1234',concat('수강평 테스트 내용',i),35,i,1,'f236923c-4746-4b5a-8377-e7c5b53799c2','f236923c-4746-4b5a-8377-e7c5b53799c2','be37dd3d-b1c6-4aad-a378-abf2ed133423','9c0df0de-5d41-414e-a021-94c84aaeeeae');

        ELSE

            INSERT INTO student

            VALUES (UUID(), CURRENT_TIMESTAMP, NULL, NULL, CONCAT('시연', i), CONCAT('testy', i, '@daum.net'), NULL, '$2a$10$k1NYCnPEH7ijGofDRPZFjOnuacXU6YHP02wvE5spuiH7WOxIqFMXS', NULL, 0, NULL, NULL, 1, 'student', '417b1cfe-4815-4131-bc0d-766ffcce0f7d', 'f236923c-4746-4b5a-8377-e7c5b53799c2');



        	INSERT INTO review

    		VALUES (UUID(), CURRENT_TIMESTAMP, NULL, NULL, i+2, 0, CONCAT('수강평 테스트',i) ,'1234',concat('수강평 테스트 내용',i),45,i,1,'f236923c-4746-4b5a-8377-e7c5b53799c2','f236923c-4746-4b5a-8377-e7c5b53799c2','be37dd3d-b1c6-4aad-a378-abf2ed133423','9c0df0de-5d41-414e-a021-94c84aaeeeae');

        END IF;



        SET i = i + 1;

    END WHILE;



END ;;
DELIMITER ;



 */
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-01-05  0:06:48
