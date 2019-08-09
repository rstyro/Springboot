/*
 Navicat MySQL Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : localhost:3306
 Source Schema         : mqtest

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 09/08/2019 21:41:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_message
-- ----------------------------
DROP TABLE IF EXISTS `sys_message`;
CREATE TABLE `sys_message`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `message_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `content` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_message
-- ----------------------------
INSERT INTO `sys_message` VALUES (2, '169c32ac-6b22-4b4f-9cf6-0917ea4f604c', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (3, '6081ca26-b4c3-4aaf-b30a-7ae0d881650c', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (4, '5c291033-d4d2-4a30-916e-40a366c6482c', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (5, '18a54e32-b87b-414f-a84a-0b77030cb425', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (6, '38e2cbaa-ea9a-4c79-a23b-ddb246d31731', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (7, '4bebe7e4-426b-46f3-a2dd-38f1b6817d91', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (8, '8bbd3419-8de4-41c7-9418-b5558f1e2ebb', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (9, '35dae2f1-41e7-4dd1-835c-b0329259a01b', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (10, '7059b08a-539c-4ffc-8cce-23139f764259', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (11, '2e94df1f-6443-4edc-bfb0-ca58af168e93', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (12, 'df0c9f09-c873-461d-9289-dc406f48ab71', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (13, '25ccd31a-b33e-4d18-8361-8947e183f143', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (14, '442dd5a9-af98-421b-99c5-95648a0ec7db', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (15, 'baf35ee2-d440-4733-af7a-910e424597a0', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (16, 'f6e2a703-519e-4854-be42-193096c72e4f', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (17, '5d7a8a2e-e1aa-4b96-949f-67a3c15a5b97', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (18, '399aca07-27c9-4e4b-921c-d1b7829a650c', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (19, 'a2a6c4e2-6415-4df7-a1f8-579b7c4ccf9e', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (20, '64dd6b7d-cec2-4f7d-92f2-309cc7844f91', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (21, 'd3b5f344-2f00-4fec-94d3-b869650c1aa1', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (22, 'c660eae8-2913-4f47-b25b-498073fc1dc3', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (23, 'b413dcc2-b764-4a22-afcf-ad08d7ebf95a', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (24, 'a546f273-7d79-4529-9442-102b4315cfb0', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (25, '3c148644-23d5-4188-ab19-d6b64c993481', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (26, 'e5eefc5e-0509-4647-a20b-598b695db456', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (27, '5f3dd237-dc04-43a4-ba0a-d5690bcb79d1', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (28, '7604d669-1231-40ee-b44c-4a17830196da', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (29, 'a7ec3796-f499-4996-95d4-08b866804893', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (30, 'a5fe3e8f-1b96-4c22-80ef-30bd8bd80bb4', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (31, '319dc15d-f95d-4be1-a7e3-0bbd893850dd', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (32, '7eace2fb-4443-4d85-a304-bef2ebf2735f', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (33, '17120554-1ab5-4d68-beef-2d8da984287f', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (34, '47b68290-829d-4f40-99f8-acc4f2dd4cc5', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (35, '5362bd48-8972-4b61-917f-b7a712ba96ca', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (36, 'dfa7b4a9-44cf-489b-8649-c74f87a63b69', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (37, 'c12457ca-f033-4553-b7d3-5d0b42b7b5b7', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (38, '4016c1ad-ae98-40e9-8cab-73debf5fa82b', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (39, '44b18831-2055-4ac2-b0d0-0d14edf654f5', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (40, 'f5597108-eb8d-4433-86d9-c8e6d5becaa8', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (41, 'd28027a1-dd79-4735-8c7b-1871ddedf975', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (42, '6e27b8a7-ebec-45a1-b913-133716f3413c', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (43, '17eb1be9-3df4-458d-86a9-adcb9b169022', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (44, 'e131ae50-a05f-41f0-a3fc-13896fc0c4c6', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (45, '106111ac-d561-425e-8d71-34c0e2025180', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (46, '5b1d6c84-1b75-48c6-aa97-a9e351974c03', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (47, '764ff51d-38e9-42ca-8b17-c9037d6e21e1', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (48, '2cdcf4a8-a7c8-491c-b7e1-e74589a3fee9', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (49, '941a033e-004a-47e2-bba0-98c54094d3b8', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (50, '99a5577e-6bc4-453c-bbeb-37d87cc2941a', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (51, 'b5565744-5ba6-46ce-827e-2f6761c54c93', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (52, '445c24cf-4dde-4c9e-b71d-7c1d41390da3', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (53, 'eafbcefe-9d1c-4605-b57b-06edd0e1295e', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (54, '06bf52fc-e166-48c2-9b46-823793dabb74', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (55, '6c72c52e-6c20-468f-875b-062243a3a9d1', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (56, '7333f3ac-490d-4340-8edd-bdb86b9caaef', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (57, 'cf102e75-348c-4206-b65e-eef0dfb39709', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (58, '1fabaa06-131f-4a9a-9265-a6499f673060', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (59, '9c23f62e-3aad-4310-915a-1cd8e9a81f96', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (60, 'ceea68fb-59b5-4205-b01e-c521d6437617', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (61, '106cd465-e9b0-4b06-87ad-55e88f65e5d7', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (62, '1dfc2298-b42b-4b91-9e6a-305c1d95b62d', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (63, '8a791eeb-fc17-474e-996c-1f6ea92ecddc', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (64, 'a58549b9-7ba4-4468-a160-4af86ea90068', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (65, 'aeef4f53-bb48-4027-b1f3-72dfc73be001', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (66, 'c7677066-c5d8-4b9f-90ee-87a8ef71d1bc', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (67, '54e9634a-c15b-48f0-9638-117093f57ec1', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (68, '69fd033c-06c3-4773-ba87-c39bacf53c56', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (69, '82e33b64-dead-4d57-b06e-7baac73b4514', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (70, 'a080c422-30d0-49a4-9535-8d4eae527e84', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (71, 'ce0e720c-31bf-4a47-80ef-640ac5b85d53', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (72, '667d5a26-df8a-4925-ba1e-84baf7564129', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (73, '4156cfce-4a4f-4659-a086-8ffc06b43deb', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (74, 'd803ed41-20fc-43b4-a0c5-dd105c0c7271', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (75, '8d1a8765-836b-4b38-8c90-d08b4c5adf68', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (76, '64d9342d-47a5-4ca1-acde-f67b5104d8bb', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (77, '73be218f-c0d3-4da9-9564-5cabd1803f85', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (78, 'cece7ad6-eae8-4e76-ab55-855ab52ffa1e', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (79, '71441b14-0dab-4bed-803a-251026897714', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (80, '53d90915-5076-4f46-9f28-729564e04f2a', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (81, '13e95be0-2a8c-41e7-96ad-a49c4ffb302d', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (82, 'b8143964-58fa-432c-a991-03c218a016e9', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (83, '845640fe-07ba-419b-9528-bc497ecdf222', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (84, '8e696434-57a2-4ec4-a4f7-c03387eeac30', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (85, 'e142ddff-5942-47ae-a51f-1c091af62680', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (86, '0468b648-47a1-447b-ba01-a9781c3ce3e5', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (87, '6d014ca6-b04c-436e-96f7-436b0ea6642c', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (88, '33a5d348-f62d-44ac-b562-7f08c9d776cc', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (89, 'cbacfddc-9265-4770-8faf-d62b87bf3f92', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (90, '1660a43e-31ff-45eb-be6d-29c4a222dd77', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (91, '9f8becdc-1455-4b1d-89fc-fc17aa0709e3', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (92, '46369f44-d4b7-40e9-9114-19b6bb66fe6d', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (93, '7961488e-ac82-4dad-b800-1e997cee7ae2', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (94, '05412acd-c80e-4744-a94b-7e9684da3f49', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (95, '0b8bb2bd-11e4-4822-83a1-26b6e3ee91e2', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (96, 'ebc84ce2-181e-4ae7-9864-eca28683ca68', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (97, 'eb3309d9-90c2-4aac-91bc-7daf1743f77a', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (98, 'fd0f1cae-e4f8-4b9c-a3c5-863b585618ea', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (99, '77d3a49d-9741-4e67-b584-3816786db559', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (100, '1b56b3d8-da91-4e05-ab49-6b11923b18ba', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (101, 'b8a3c9d8-2682-4b68-980d-ed03f08d00e1', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (102, '913924fd-995c-4383-b257-fcfd51757515', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (103, '430c5732-e373-4888-8cf3-81a91ac646e0', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (104, 'c3aa7059-16eb-4580-a9c8-5d64ab6d3991', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (105, 'c9df15a6-158e-4060-a528-205778f0ec91', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (106, 'eb91638a-4203-4736-9d2a-dbd8e07d09b0', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (107, 'd19a2204-7fe9-439e-9804-6f76f0ba6d8f', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (108, '00610353-b5ed-4a9b-9a71-95245ce5edcf', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (109, 'e55f9452-981a-4344-a533-67290aab7a69', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (110, '91991e25-6f1c-42b0-92e4-d81f281eecaf', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (111, 'f04a6415-1152-4565-9cfc-3d10eef7ad7d', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (112, '27b174e4-264b-4a48-9b5a-d1cc227799b2', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (113, '7df5528b-5fb2-4a65-8ce0-7595a953eb17', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (114, '7f1e6966-a20e-469a-9054-42522987863e', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (115, 'b12659c5-630b-46bc-82fd-834a010806d9', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (116, 'ba28d1bb-0498-4406-a4ce-f5d08632f184', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (117, 'caac8d07-6261-4ffe-87cd-99da9a19f507', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (118, 'b3c0abcb-dc8c-425f-980d-003e22ebb76d', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (119, '4d8958ee-a869-41e3-9b05-aab46031862f', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (120, '600c68ea-efb2-45be-aa43-115c92c6160c', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (121, '4fed720b-e019-4141-931e-46b0569c5390', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (122, 'a6e147ff-b59f-4e48-aa90-241f7224edc2', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (123, '4d3c9298-36c7-4b9e-8747-1395523f630a', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (124, 'd249223a-1752-40fe-b1e9-4f025735fa9d', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (125, 'e4fd66e7-2071-4fb9-8be6-437f47047a0e', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (126, '54f892aa-7c7e-4947-bdfa-a8427f61c9ab', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (127, '6185be77-c6ae-4c1c-aaf1-bcfd383f1f5d', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (128, '67c26698-c6c1-41e9-b6e1-bbc245f311f2', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (129, '9c9ce6b2-9f08-4256-b0ae-c5ae468b0010', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (130, 'c0e85870-f821-4bdd-8692-994e6f2f5b8f', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (131, 'f5525801-ca6a-4996-b6cf-a4332f668adb', '我就是我，不一样烟火');
INSERT INTO `sys_message` VALUES (132, '240ea61d-a083-4fe2-b822-d2fa253017de', '我就是我，不一样烟火');

-- ----------------------------
-- Table structure for sys_message_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_message_log`;
CREATE TABLE `sys_message_log`  (
  `message_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'id',
  `message` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '消息内容',
  `try_Count` int(10) NULL DEFAULT NULL COMMENT '重试次数',
  `status` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '状态: 0 -- 发送中，1-- 成功，2 --- 失败',
  `is_del` int(2) NULL DEFAULT 0 COMMENT '0/1',
  `next_retry` datetime(0) NULL DEFAULT NULL COMMENT '下一次重试的时间',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`message_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_message_log
-- ----------------------------
INSERT INTO `sys_message_log` VALUES ('240ea61d-a083-4fe2-b822-d2fa253017de', '{\"content\":\"我就是我，不一样烟火\",\"id\":132,\"messageId\":\"240ea61d-a083-4fe2-b822-d2fa253017de\"}', 0, '1', 0, '2019-08-09 17:59:31', '2019-08-09 17:58:31', '2019-08-09 17:58:31');
INSERT INTO `sys_message_log` VALUES ('4d3c9298-36c7-4b9e-8747-1395523f630a', '{\"content\":\"我就是我，不一样烟火\",\"id\":123,\"messageId\":\"4d3c9298-36c7-4b9e-8747-1395523f630a\"}', 0, '1', 0, '2019-08-09 17:59:30', '2019-08-09 17:58:30', '2019-08-09 17:58:30');
INSERT INTO `sys_message_log` VALUES ('4d8958ee-a869-41e3-9b05-aab46031862f', '{\"content\":\"我就是我，不一样烟火\",\"id\":119,\"messageId\":\"4d8958ee-a869-41e3-9b05-aab46031862f\"}', 0, '1', 0, '2019-08-09 17:59:29', '2019-08-09 17:58:29', '2019-08-09 17:58:29');
INSERT INTO `sys_message_log` VALUES ('4fed720b-e019-4141-931e-46b0569c5390', '{\"content\":\"我就是我，不一样烟火\",\"id\":121,\"messageId\":\"4fed720b-e019-4141-931e-46b0569c5390\"}', 0, '1', 0, '2019-08-09 17:59:29', '2019-08-09 17:58:29', '2019-08-09 17:58:29');
INSERT INTO `sys_message_log` VALUES ('54f892aa-7c7e-4947-bdfa-a8427f61c9ab', '{\"content\":\"我就是我，不一样烟火\",\"id\":126,\"messageId\":\"54f892aa-7c7e-4947-bdfa-a8427f61c9ab\"}', 1, '1', 0, '2019-08-09 18:00:30', '2019-08-09 17:58:30', '2019-08-09 17:59:30');
INSERT INTO `sys_message_log` VALUES ('600c68ea-efb2-45be-aa43-115c92c6160c', '{\"content\":\"我就是我，不一样烟火\",\"id\":120,\"messageId\":\"600c68ea-efb2-45be-aa43-115c92c6160c\"}', 0, '1', 0, '2019-08-09 17:59:29', '2019-08-09 17:58:29', '2019-08-09 17:58:29');
INSERT INTO `sys_message_log` VALUES ('6185be77-c6ae-4c1c-aaf1-bcfd383f1f5d', '{\"content\":\"我就是我，不一样烟火\",\"id\":127,\"messageId\":\"6185be77-c6ae-4c1c-aaf1-bcfd383f1f5d\"}', 0, '1', 0, '2019-08-09 17:59:30', '2019-08-09 17:58:30', '2019-08-09 17:58:30');
INSERT INTO `sys_message_log` VALUES ('67c26698-c6c1-41e9-b6e1-bbc245f311f2', '{\"content\":\"我就是我，不一样烟火\",\"id\":128,\"messageId\":\"67c26698-c6c1-41e9-b6e1-bbc245f311f2\"}', 0, '1', 0, '2019-08-09 17:59:30', '2019-08-09 17:58:30', '2019-08-09 17:58:30');
INSERT INTO `sys_message_log` VALUES ('9c9ce6b2-9f08-4256-b0ae-c5ae468b0010', '{\"content\":\"我就是我，不一样烟火\",\"id\":129,\"messageId\":\"9c9ce6b2-9f08-4256-b0ae-c5ae468b0010\"}', 0, '1', 0, '2019-08-09 17:59:30', '2019-08-09 17:58:30', '2019-08-09 17:58:30');
INSERT INTO `sys_message_log` VALUES ('a6e147ff-b59f-4e48-aa90-241f7224edc2', '{\"content\":\"我就是我，不一样烟火\",\"id\":122,\"messageId\":\"a6e147ff-b59f-4e48-aa90-241f7224edc2\"}', 0, '1', 0, '2019-08-09 17:59:29', '2019-08-09 17:58:29', '2019-08-09 17:58:29');
INSERT INTO `sys_message_log` VALUES ('b12659c5-630b-46bc-82fd-834a010806d9', '{\"content\":\"我就是我，不一样烟火\",\"id\":115,\"messageId\":\"b12659c5-630b-46bc-82fd-834a010806d9\"}', 0, '1', 0, '2019-08-09 17:59:28', '2019-08-09 17:58:28', '2019-08-09 17:58:28');
INSERT INTO `sys_message_log` VALUES ('b3c0abcb-dc8c-425f-980d-003e22ebb76d', '{\"content\":\"我就是我，不一样烟火\",\"id\":118,\"messageId\":\"b3c0abcb-dc8c-425f-980d-003e22ebb76d\"}', 0, '1', 0, '2019-08-09 17:59:29', '2019-08-09 17:58:29', '2019-08-09 17:58:29');
INSERT INTO `sys_message_log` VALUES ('ba28d1bb-0498-4406-a4ce-f5d08632f184', '{\"content\":\"我就是我，不一样烟火\",\"id\":116,\"messageId\":\"ba28d1bb-0498-4406-a4ce-f5d08632f184\"}', 0, '1', 0, '2019-08-09 17:59:28', '2019-08-09 17:58:28', '2019-08-09 17:58:28');
INSERT INTO `sys_message_log` VALUES ('c0e85870-f821-4bdd-8692-994e6f2f5b8f', '{\"content\":\"我就是我，不一样烟火\",\"id\":130,\"messageId\":\"c0e85870-f821-4bdd-8692-994e6f2f5b8f\"}', 0, '1', 0, '2019-08-09 17:59:31', '2019-08-09 17:58:31', '2019-08-09 17:58:31');
INSERT INTO `sys_message_log` VALUES ('caac8d07-6261-4ffe-87cd-99da9a19f507', '{\"content\":\"我就是我，不一样烟火\",\"id\":117,\"messageId\":\"caac8d07-6261-4ffe-87cd-99da9a19f507\"}', 0, '1', 0, '2019-08-09 17:59:29', '2019-08-09 17:58:29', '2019-08-09 17:58:29');
INSERT INTO `sys_message_log` VALUES ('d249223a-1752-40fe-b1e9-4f025735fa9d', '{\"content\":\"我就是我，不一样烟火\",\"id\":124,\"messageId\":\"d249223a-1752-40fe-b1e9-4f025735fa9d\"}', 0, '1', 0, '2019-08-09 17:59:30', '2019-08-09 17:58:30', '2019-08-09 17:58:30');
INSERT INTO `sys_message_log` VALUES ('e4fd66e7-2071-4fb9-8be6-437f47047a0e', '{\"content\":\"我就是我，不一样烟火\",\"id\":125,\"messageId\":\"e4fd66e7-2071-4fb9-8be6-437f47047a0e\"}', 1, '1', 0, '2019-08-09 18:00:30', '2019-08-09 17:58:30', '2019-08-09 17:59:30');
INSERT INTO `sys_message_log` VALUES ('f5525801-ca6a-4996-b6cf-a4332f668adb', '{\"content\":\"我就是我，不一样烟火\",\"id\":131,\"messageId\":\"f5525801-ca6a-4996-b6cf-a4332f668adb\"}', 0, '1', 0, '2019-08-09 17:59:31', '2019-08-09 17:58:31', '2019-08-09 17:58:31');

SET FOREIGN_KEY_CHECKS = 1;
