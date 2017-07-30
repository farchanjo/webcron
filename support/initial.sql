CREATE DATABASE IF NOT EXISTS `webcrons` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `webcrons`;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

--
-- Table structure for table `jobs`
--

DROP TABLE IF EXISTS `jobs`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jobs` (
  `id`         BIGINT(20)   NOT NULL AUTO_INCREMENT,
  `async`      INT(11)      NOT NULL,
  `command`    TEXT         NOT NULL,
  `created`    DATETIME     NOT NULL,
  `cron`       VARCHAR(255)          DEFAULT NULL,
  `directory`  VARCHAR(255)          DEFAULT NULL,
  `envs`       LONGBLOB,
  `fixed_rate` INT(11)               DEFAULT NULL,
  `modified`   DATETIME     NOT NULL,
  `name`       VARCHAR(255) NOT NULL,
  `status`     INT(11)      NOT NULL,
  `unit`       INT(11)               DEFAULT NULL,
  `user_id`    BIGINT(20)            DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKra3g6pshf0p0hv5aisuh3weg8` (`user_id`),
  CONSTRAINT `FKra3g6pshf0p0hv5aisuh3weg8` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jobs`
--

LOCK TABLES `jobs` WRITE;
/*!40000 ALTER TABLE `jobs`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `jobs`
  ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id`       BIGINT(20) NOT NULL AUTO_INCREMENT,
  `created`  DATETIME            DEFAULT NULL,
  `email`    VARCHAR(255)        DEFAULT NULL,
  `modified` DATETIME            DEFAULT NULL,
  `name`     VARCHAR(255)        DEFAULT NULL,
  `password` VARCHAR(255)        DEFAULT NULL,
  `role`     INT(11)             DEFAULT NULL,
  `status`   INT(11)             DEFAULT NULL,
  `username` VARCHAR(255)        DEFAULT NULL,
  PRIMARY KEY (`id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users`
  DISABLE KEYS */;
/*!40000 ALTER TABLE `users`
  ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;