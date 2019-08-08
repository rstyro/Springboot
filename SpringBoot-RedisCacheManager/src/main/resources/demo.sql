/*
 Navicat MySQL Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : localhost:3306
 Source Schema         : demo

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 08/08/2019 22:58:42
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for act_acticle
-- ----------------------------
DROP TABLE IF EXISTS `act_acticle`;
CREATE TABLE `act_acticle`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '内容',
  `tag_id` bigint(20) NULL DEFAULT 1,
  `create_by` bigint(20) NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_by` bigint(20) NULL DEFAULT NULL,
  `modify_time` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of act_acticle
-- ----------------------------
INSERT INTO `act_acticle` VALUES (1, 'SpringBoot缓存Cacheable整合', 'SpringBoot缓存Cacheable整合', 0, 1, '2019-08-08 17:03:59', 1, '2019-08-08 17:04:02');
INSERT INTO `act_acticle` VALUES (3, '测试', '阿道夫士大夫ssff', 1, 1, '2019-08-08 17:22:38', 1, '2019-08-08 17:22:38');
INSERT INTO `act_acticle` VALUES (4, 'abc', '阿道夫士大夫ssff', 2, 1, '2019-08-08 17:47:13', 1, '2019-08-08 17:47:13');

SET FOREIGN_KEY_CHECKS = 1;
