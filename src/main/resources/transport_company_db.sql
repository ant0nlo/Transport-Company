-- MySQL dump 10.13  Distrib 9.0.1, for macos15.1 (arm64)
--
-- Host: localhost    Database: transport_company_db
-- ------------------------------------------------------
-- Server version	9.0.1

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
-- Current Database: `transport_company_db`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `transport_company_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `transport_company_db`;

--
-- Table structure for table `client`
--

DROP TABLE IF EXISTS `client`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `client` (
  `clientId` bigint NOT NULL AUTO_INCREMENT,
  `contact_info` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `company_id` bigint NOT NULL,
  `is_deleted` bit(1) DEFAULT NULL,
  PRIMARY KEY (`clientId`),
  KEY `FKbdkhriogfnems0qpgiasmf3x8` (`company_id`),
  CONSTRAINT `FKbdkhriogfnems0qpgiasmf3x8` FOREIGN KEY (`company_id`) REFERENCES `company` (`companyId`)
) ENGINE=InnoDB AUTO_INCREMENT=177 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `client`
--

LOCK TABLES `client` WRITE;
/*!40000 ALTER TABLE `client` DISABLE KEYS */;
INSERT INTO `client` VALUES (171,'Stefan@example.com','Stefan',383,_binary '\0'),(172,'ivan@example.com','Ivan Ivanov',384,_binary '\0'),(173,'maria@example.com','Maria Petrova',385,_binary '\0'),(174,'stefan@example.com','Stefan Georgiev',386,_binary '\0'),(175,'nikolay@example.com','Nikolay Nikolov',387,_binary '\0'),(176,'viktor@example.com','Viktor Ivanov',388,_binary '\0');
/*!40000 ALTER TABLE `client` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `company`
--

DROP TABLE IF EXISTS `company`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `company` (
  `companyId` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `name` varchar(100) NOT NULL,
  `revenue` decimal(15,2) DEFAULT NULL,
  `is_deleted` bit(1) NOT NULL,
  PRIMARY KEY (`companyId`)
) ENGINE=InnoDB AUTO_INCREMENT=389 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `company`
--

LOCK TABLES `company` WRITE;
/*!40000 ALTER TABLE `company` DISABLE KEYS */;
INSERT INTO `company` VALUES (383,'Sofia, Bulgaria','EHL',10000.00,_binary '\0'),(384,'Sofia, Bulgaria','AlphaTrans',20000.00,_binary '\0'),(385,'Plovdiv, Bulgaria','UPS',15000.00,_binary '\0'),(386,'Varna, Bulgaria','XPO Logistics',25000.00,_binary '\0'),(387,'Burgas, Bulgaria','Express Line',10000.00,_binary '\0'),(388,'Ruse, Bulgaria','YRC Worldwide',30000.00,_binary '\0');
/*!40000 ALTER TABLE `company` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `employeeId` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `salary` double NOT NULL,
  `company_id` bigint NOT NULL,
  `is_deleted` bit(1) DEFAULT NULL,
  PRIMARY KEY (`employeeId`),
  KEY `FK5v50ed2bjh60n1gc7ifuxmgf4` (`company_id`),
  CONSTRAINT `FK5v50ed2bjh60n1gc7ifuxmgf4` FOREIGN KEY (`company_id`) REFERENCES `company` (`companyId`)
) ENGINE=InnoDB AUTO_INCREMENT=131 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (124,'G. Petrov',2000,383,_binary '\0'),(125,'Dimitar Dimitrov',3000,384,_binary '\0'),(126,'Georgi Stoyanov',2500,385,_binary '\0'),(127,'Kiril Kirilov',2800,385,_binary '\0'),(128,'Petar Petrov',3000,386,_binary '\0'),(129,'Hristo Hristov',2500,387,_binary '\0'),(130,'Vasil Vasilev',4000,388,_binary '\0');
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_qualification`
--

DROP TABLE IF EXISTS `employee_qualification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_qualification` (
  `employee_id` bigint NOT NULL,
  `qualification` varchar(255) DEFAULT NULL,
  KEY `FKcy593kvg0lbr9vba4wktosnef` (`employee_id`),
  CONSTRAINT `FKcy593kvg0lbr9vba4wktosnef` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employeeId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_qualification`
--

LOCK TABLES `employee_qualification` WRITE;
/*!40000 ALTER TABLE `employee_qualification` DISABLE KEYS */;
INSERT INTO `employee_qualification` VALUES (124,'PASSENGERS_12_PLUS'),(125,'CHEMICALS'),(126,'PASSENGERS_12_PLUS'),(127,'REFRIGERATED_GOODS'),(128,'LIVESTOCK_TRANSPORT'),(129,'PASSENGERS_12_PLUS'),(130,'MILITARY_CARGO');
/*!40000 ALTER TABLE `employee_qualification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `transport`
--

DROP TABLE IF EXISTS `transport`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `transport` (
  `transportId` bigint NOT NULL AUTO_INCREMENT,
  `arrival_date` varchar(255) NOT NULL,
  `cargo_description` varchar(255) DEFAULT NULL,
  `cargo_weight` double DEFAULT NULL,
  `departure_date` varchar(255) NOT NULL,
  `end_location` varchar(255) NOT NULL,
  `is_paid` bit(1) NOT NULL,
  `price` double NOT NULL,
  `start_location` varchar(255) NOT NULL,
  `client_id` bigint NOT NULL,
  `company_id` bigint NOT NULL,
  `employee_id` bigint NOT NULL,
  `vehicle_id` bigint NOT NULL,
  `is_deleted` bit(1) NOT NULL,
  PRIMARY KEY (`transportId`),
  KEY `FKhiv3b16vlt1pth6i2sgkq1bhl` (`client_id`),
  KEY `FK2gggq9es5hg8dw3mo8n8uhqcj` (`company_id`),
  KEY `FK3c47clr7v4ek42jvyoawp16rx` (`employee_id`),
  KEY `FK8xipceubfxar38wptuhjhfjkw` (`vehicle_id`),
  CONSTRAINT `FK2gggq9es5hg8dw3mo8n8uhqcj` FOREIGN KEY (`company_id`) REFERENCES `company` (`companyId`),
  CONSTRAINT `FK3c47clr7v4ek42jvyoawp16rx` FOREIGN KEY (`employee_id`) REFERENCES `employee` (`employeeId`),
  CONSTRAINT `FK8xipceubfxar38wptuhjhfjkw` FOREIGN KEY (`vehicle_id`) REFERENCES `vehicle` (`vehicleId`),
  CONSTRAINT `FKhiv3b16vlt1pth6i2sgkq1bhl` FOREIGN KEY (`client_id`) REFERENCES `client` (`clientId`)
) ENGINE=InnoDB AUTO_INCREMENT=107 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `transport`
--

LOCK TABLES `transport` WRITE;
/*!40000 ALTER TABLE `transport` DISABLE KEYS */;
INSERT INTO `transport` VALUES (101,'2024-12-25','Table',20,'2024-12-24','Varna',_binary '',20000,'Sofia',171,383,124,151,_binary '\0'),(102,'2025-01-07','Chemicals',200,'2025-01-05','Varna',_binary '',25000,'Sofia',172,384,125,152,_binary '\0'),(103,'2025-01-12','Frozen goods',100,'2025-01-10','Burgas',_binary '',12000,'Plovdiv',173,385,127,153,_binary '\0'),(104,'2025-01-16','Livestock',150,'2025-01-15','Ruse',_binary '',18000,'Varna',174,386,128,154,_binary '\0'),(105,'2025-01-22','Passengers',40,'2025-01-20','Sofia',_binary '',15000,'Burgas',175,387,129,155,_binary '\0'),(106,'2025-01-28','Military equipment',200,'2025-01-25','Plovdiv',_binary '',30000,'Ruse',176,388,130,156,_binary '\0');
/*!40000 ALTER TABLE `transport` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vehicle`
--

DROP TABLE IF EXISTS `vehicle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vehicle` (
  `vehicleId` bigint NOT NULL AUTO_INCREMENT,
  `capacity` int NOT NULL,
  `registration_number` varchar(255) NOT NULL,
  `type` varchar(255) NOT NULL,
  `company_id` bigint NOT NULL,
  `required_qualification` varchar(255) DEFAULT NULL,
  `is_deleted` bit(1) NOT NULL,
  PRIMARY KEY (`vehicleId`),
  UNIQUE KEY `UK_6fo0502tpr111m29vqj0bhpa4` (`registration_number`),
  KEY `FK8l9m1j8m30mdmdcbbt1c4trkd` (`company_id`),
  CONSTRAINT `FK8l9m1j8m30mdmdcbbt1c4trkd` FOREIGN KEY (`company_id`) REFERENCES `company` (`companyId`)
) ENGINE=InnoDB AUTO_INCREMENT=157 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vehicle`
--

LOCK TABLES `vehicle` WRITE;
/*!40000 ALTER TABLE `vehicle` DISABLE KEYS */;
INSERT INTO `vehicle` VALUES (151,4,'SF4893AB','VAN',383,NULL,_binary '\0'),(152,5,'SF1234AB','CHEMICAL_TANKER',384,'CHEMICALS',_binary '\0'),(153,10,'SF5678AB','REFRIGERATED_TRUCK',385,'REFRIGERATED_GOODS',_binary '\0'),(154,10,'SF9876CD','LIVESTOCK_CARRIER',386,'LIVESTOCK_TRANSPORT',_binary '\0'),(155,50,'SF1122AA','BUS',387,'PASSENGERS_12_PLUS',_binary '\0'),(156,4,'SF54321EF','MILITARY_VEHICLE',388,'MILITARY_CARGO',_binary '\0');
/*!40000 ALTER TABLE `vehicle` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-01-08 14:17:12
