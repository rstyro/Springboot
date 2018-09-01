/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : db1

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-09-01 17:08:20
*/
CREATE DATABASE `db1` ;
USE `db1`;
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL,
  `nick_name` varchar(50) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `pic_path` varchar(200) DEFAULT '/images/logo.png',
  `status` enum('unlock','lock') DEFAULT 'unlock',
  `sessionId` varchar(50) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin1', '管理员1', 'd033e22ae348aeb5660fc2140aec35850c4da997', 'http://www.lrshuai.top/upload/user/20170612/05976238.png', 'unlock', 'D7B7F5997D87AED1390270A5260BD8FF', '2017-08-18 13:57:32');
INSERT INTO `sys_user` VALUES ('2', 'tyro1', 'tyro1', '481c63e8b904bb8399f1fc1dfdb77cb40842eb6f', '/upload/show/user/82197046.png', 'unlock', null, '2017-09-12 14:03:39');
INSERT INTO `sys_user` VALUES ('3', 'asdf1', 'asdf1', '3da541559918a808c2402bba5012f6c60b27661c', '/upload/show/user/85610497.png', 'unlock', null, '2017-09-13 14:49:10');
INSERT INTO `sys_user` VALUES ('4', 'bbbb', 'bbb', 'bbbbb', '/images/logo.png', 'unlock', 'bbbbb', '2018-09-01 15:33:40');
