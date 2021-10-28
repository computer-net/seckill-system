-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        8.0.23 - MySQL Community Server - GPL
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  11.2.0.6213
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- 导出 seckill 的数据库结构
CREATE DATABASE IF NOT EXISTS `seckill` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `seckill`;

-- 导出  表 seckill.item 结构
CREATE TABLE IF NOT EXISTS `item` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `price` double unsigned DEFAULT NULL,
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `sales` int unsigned DEFAULT NULL,
  `imgUrl` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  seckill.item 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `item` DISABLE KEYS */;
INSERT INTO `item` (`id`, `title`, `price`, `description`, `sales`, `imgUrl`) VALUES
	(6, '芬达', 3.5, '碳酸饮料', 14, 'https://files.toodaylab.com/spu/spu_69_20150324114506.jpg'),
	(7, '加多宝', 3.5, '饮料', 24, 'https://e.ecimg.tw/items/DBAB6JA9007KS7L/000001_1476932373.jpg');
/*!40000 ALTER TABLE `item` ENABLE KEYS */;

-- 导出  表 seckill.item_stock 结构
CREATE TABLE IF NOT EXISTS `item_stock` (
  `id` int NOT NULL AUTO_INCREMENT,
  `stock` int DEFAULT NULL,
  `item_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  seckill.item_stock 的数据：~2 rows (大约)
/*!40000 ALTER TABLE `item_stock` DISABLE KEYS */;
INSERT INTO `item_stock` (`id`, `stock`, `item_id`) VALUES
	(6, 9, 6),
	(7, 8, 7);
/*!40000 ALTER TABLE `item_stock` ENABLE KEYS */;

-- 导出  表 seckill.order_info 结构
CREATE TABLE IF NOT EXISTS `order_info` (
  `id` varchar(32) NOT NULL,
  `user_id` int DEFAULT NULL,
  `item_id` int DEFAULT NULL,
  `item_price` double DEFAULT NULL,
  `amount` int DEFAULT NULL,
  `order_price` double DEFAULT NULL,
  `promo_id` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  seckill.order_info 的数据：~6 rows (大约)
/*!40000 ALTER TABLE `order_info` DISABLE KEYS */;
INSERT INTO `order_info` (`id`, `user_id`, `item_id`, `item_price`, `amount`, `order_price`, `promo_id`) VALUES
	('2021102800000000', 9, 7, NULL, 1, NULL, 0),
	('2021102800000100', 9, 7, NULL, 1, NULL, 0),
	('2021102800000200', 9, 7, 3.5, 1, 3.5, 0),
	('2021102800000300', 9, 7, 3.5, 1, 3.5, 0),
	('2021102800000400', 9, 6, 3.5, 1, 3.5, 0),
	('2021102800000500', 9, 6, 1.1, 1, 1.1, 1);
/*!40000 ALTER TABLE `order_info` ENABLE KEYS */;

-- 导出  表 seckill.promo 结构
CREATE TABLE IF NOT EXISTS `promo` (
  `id` int NOT NULL AUTO_INCREMENT,
  `promo_name` varchar(50) DEFAULT NULL,
  `start_date` datetime DEFAULT '0000-00-00 00:00:00',
  `item_id` int DEFAULT NULL,
  `promoItem_price` double DEFAULT NULL,
  `end_date` datetime DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  seckill.promo 的数据：~1 rows (大约)
/*!40000 ALTER TABLE `promo` DISABLE KEYS */;
INSERT INTO `promo` (`id`, `promo_name`, `start_date`, `item_id`, `promoItem_price`, `end_date`) VALUES
	(1, '芬达抢购活动', '2021-10-28 23:59:59', 6, 1.1, '2021-11-22 11:11:11');
/*!40000 ALTER TABLE `promo` ENABLE KEYS */;

-- 导出  表 seckill.sequence_info 结构
CREATE TABLE IF NOT EXISTS `sequence_info` (
  `name` varchar(32) NOT NULL,
  `current_value` int DEFAULT '0',
  `step` int DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  seckill.sequence_info 的数据：~1 rows (大约)
/*!40000 ALTER TABLE `sequence_info` DISABLE KEYS */;
INSERT INTO `sequence_info` (`name`, `current_value`, `step`) VALUES
	('order_info', 6, 1);
/*!40000 ALTER TABLE `sequence_info` ENABLE KEYS */;

-- 导出  表 seckill.user_info 结构
CREATE TABLE IF NOT EXISTS `user_info` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(64) DEFAULT NULL,
  `gender` tinyint DEFAULT NULL,
  `age` int DEFAULT NULL,
  `telephone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `register_mode` varchar(64) DEFAULT NULL,
  `third_party_id` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `telephone_unique_index` (`telephone`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  seckill.user_info 的数据：~3 rows (大约)
/*!40000 ALTER TABLE `user_info` DISABLE KEYS */;
INSERT INTO `user_info` (`id`, `name`, `gender`, `age`, `telephone`, `register_mode`, `third_party_id`) VALUES
	(1, 'rocketeerli', 1, 30, '1317897', '1', '1'),
	(5, 'rocketeerli', 1, 24, '4113441', 'byphone', NULL),
	(9, 'login', 2, 55, '143414234', 'byphone', NULL);
/*!40000 ALTER TABLE `user_info` ENABLE KEYS */;

-- 导出  表 seckill.user_password 结构
CREATE TABLE IF NOT EXISTS `user_password` (
  `id` int NOT NULL AUTO_INCREMENT,
  `encrpt_password` varchar(128) DEFAULT NULL,
  `user_id` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  seckill.user_password 的数据：~3 rows (大约)
/*!40000 ALTER TABLE `user_password` DISABLE KEYS */;
INSERT INTO `user_password` (`id`, `encrpt_password`, `user_id`) VALUES
	(1, 'fhcak', 1),
	(3, 'Sh7H+1OEDwB3yo2nkCC4lA==', 5),
	(4, '4QrcOUm6Wau+VuBX8g+IPg==', 9);
/*!40000 ALTER TABLE `user_password` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
