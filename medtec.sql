-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3306
-- Generation Time: Nov 12, 2023 at 11:17 AM
-- Server version: 8.0.23
-- PHP Version: 8.1.13

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `medtec`
--

-- --------------------------------------------------------

--
-- Table structure for table `discount`
--

DROP TABLE IF EXISTS `discount`;
CREATE TABLE IF NOT EXISTS `discount` (
  `itemcode` char(6) NOT NULL,
  `discount` double(3,1) DEFAULT NULL,
  PRIMARY KEY (`itemcode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `discount`
--

INSERT INTO `discount` (`itemcode`, `discount`) VALUES
('ES001', 10.0),
('ES002', 7.5),
('ES003', 15.0),
('ES004', 5.0),
('MLS001', 0.0),
('MLS002', 10.0),
('MT001', 5.0),
('MT002', 2.5),
('MT003', 0.0),
('MT004', 2.0);

-- --------------------------------------------------------

--
-- Table structure for table `prices`
--

DROP TABLE IF EXISTS `prices`;
CREATE TABLE IF NOT EXISTS `prices` (
  `itemcode` char(6) NOT NULL,
  `price` double(9,2) DEFAULT NULL,
  PRIMARY KEY (`itemcode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `prices`
--

INSERT INTO `prices` (`itemcode`, `price`) VALUES
('ES001', 4000.00),
('ES002', 2500.00),
('ES003', 3400.00),
('ES004', 1500.00),
('MLS001', 750.00),
('MLS002', 4500.00),
('MT001', 2500.00),
('MT002', 1200.00),
('MT003', 350.00),
('MT004', 990.00);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
