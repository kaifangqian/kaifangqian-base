/*
 Navicat Premium Data Transfer

 Source Server         : 公司本地-mysql（内网）
 Source Server Type    : MySQL
 Source Server Version : 50731 (5.7.31-log)
 Source Host           : 192.168.0.200:9968
 Source Schema         : r-paas-open-sign2

 Target Server Type    : MySQL
 Target Server Version : 50731 (5.7.31-log)
 File Encoding         : 65001

 Date: 14/12/2023 20:16:20
*/

CREATE DATABASE `opensign` CHARACTER SET 'utf8mb4' COLLATE 'utf8mb4_general_ci';
use opensign;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
-- ----------------------------
-- Table structure for annex_image
-- ----------------------------
DROP TABLE IF EXISTS `annex_image`;
CREATE TABLE `annex_image` (
  `id` varchar(64) NOT NULL,
  `image_annex_id` varchar(64) DEFAULT NULL COMMENT '图片真实文件id',
  `page` int(3) DEFAULT NULL COMMENT '页码',
  `annex_id` varchar(64) DEFAULT NULL COMMENT '关联真实文件id',
  `create_by` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文件转换图片记录表';

-- ----------------------------
-- Records of annex_image
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for annex_storage
-- ----------------------------
DROP TABLE IF EXISTS `annex_storage`;
CREATE TABLE `annex_storage` (
  `id` varchar(64) NOT NULL COMMENT 'ID',
  `father_id` varchar(64) DEFAULT NULL COMMENT '父ID',
  `data_category` varchar(32) DEFAULT NULL COMMENT '附件种类',
  `real_name` varchar(255) DEFAULT NULL COMMENT '文件真实的名称',
  `name` varchar(255) DEFAULT NULL COMMENT '文件名',
  `comment` varchar(255) DEFAULT NULL COMMENT '备注',
  `suffix` varchar(32) DEFAULT NULL COMMENT '后缀',
  `path` varchar(255) DEFAULT NULL COMMENT '路径',
  `type` varchar(32) DEFAULT NULL COMMENT '类型',
  `size` varchar(100) DEFAULT NULL COMMENT '大小',
  `status` int(2) NOT NULL COMMENT '状态',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` bit(1) NOT NULL COMMENT '删除标志0：未删除1：已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除者',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='附件表';

-- ----------------------------
-- Records of annex_storage
-- ----------------------------
BEGIN;
INSERT INTO `annex_storage` (`id`, `father_id`, `data_category`, `real_name`, `name`, `comment`, `suffix`, `path`, `type`, `size`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`) VALUES ('0e3ff681-b10d-40a9-b1bf-c3592b276bc5', 'ff8080818c68362f018c68362f220000', 'authorizebook', '开源工具版说明.pdf', '开源工具版说明', NULL, 'pdf', '3cdd6ffc-c2d0-4b58-b983-513d77dcde6d.pdf', '文档', '711.01KB   ', 1, '12300000000', '2023-12-14 20:03:47', '12300000000', '2023-12-14 20:03:51', b'0', NULL, NULL);
INSERT INTO `annex_storage` (`id`, `father_id`, `data_category`, `real_name`, `name`, `comment`, `suffix`, `path`, `type`, `size`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`) VALUES ('37a58454-8a85-4254-9323-458353cf7bf8', '01cd0de7-899e-4048-a9ff-baa7c27d970e', 'cert_pfx', 'e7248db1-2612-4e58-8bcb-36710cc16cec.pfx', 'e7248db1-2612-4e58-8bcb-36710cc16cec.pfx', NULL, 'pfx', 'e7248db1-2612-4e58-8bcb-36710cc16cec.pfx', 'pfx', '3.50KB   ', 1, NULL, '2023-12-14 19:59:59', NULL, NULL, b'0', NULL, NULL);
INSERT INTO `annex_storage` (`id`, `father_id`, `data_category`, `real_name`, `name`, `comment`, `suffix`, `path`, `type`, `size`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`) VALUES ('5d19d298-6525-4939-9654-a4982cbe9b79', 'ff8080818c68362f018c68362f220000', 'idpic1', '产品封面.png', '产品封面', NULL, 'png', '9e7cec16-3e72-4612-8551-718dd9a9fb80.png', '图片', '364.16KB   ', 1, '12300000000', '2023-12-14 20:02:39', '12300000000', '2023-12-14 20:03:51', b'0', NULL, NULL);
INSERT INTO `annex_storage` (`id`, `father_id`, `data_category`, `real_name`, `name`, `comment`, `suffix`, `path`, `type`, `size`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`) VALUES ('60686811-24a8-4da9-a7c4-9ddca0370c64', 'ff8080818c68362f018c68362f220000', 'organizationpic', '产品封面.png', '产品封面', NULL, 'png', '31499713-1b6e-49e6-8f5a-88779de84f1b.png', '图片', '364.16KB   ', 1, '12300000000', '2023-12-14 20:01:37', '12300000000', '2023-12-14 20:03:51', b'0', NULL, NULL);
INSERT INTO `annex_storage` (`id`, `father_id`, `data_category`, `real_name`, `name`, `comment`, `suffix`, `path`, `type`, `size`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`) VALUES ('6415b18f-47b2-41cd-88ed-3b2185df704c', 'ff8080818c68362f018c68362f220000', 'idpic2', '产品封面.png', '产品封面', NULL, 'png', '1ed5cabf-f517-4d8f-b24c-f0a1289bf759.png', '图片', '364.16KB   ', 1, '12300000000', '2023-12-14 20:02:48', '12300000000', '2023-12-14 20:03:51', b'0', NULL, NULL);
INSERT INTO `annex_storage` (`id`, `father_id`, `data_category`, `real_name`, `name`, `comment`, `suffix`, `path`, `type`, `size`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`) VALUES ('8c5aa7f7-5fcd-4b6d-a0d4-d4e5ff939ab6', '28ae80b7-9883-4bca-86f5-fd593b3b9568', 'cert_pfx', 'ac3fbcdd-7d56-4cf7-aa80-82e2e4552529.pfx', 'ac3fbcdd-7d56-4cf7-aa80-82e2e4552529.pfx', NULL, 'pfx', 'ac3fbcdd-7d56-4cf7-aa80-82e2e4552529.pfx', 'pfx', '3.50KB   ', 1, '12300000000', '2023-12-14 20:03:51', NULL, NULL, b'0', NULL, NULL);
INSERT INTO `annex_storage` (`id`, `father_id`, `data_category`, `real_name`, `name`, `comment`, `suffix`, `path`, `type`, `size`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`) VALUES ('b07139a0-4f52-4a56-aeec-9f2867743009', NULL, 'seal_file_person', 'c8070812-272b-4f3c-902a-35a05026acf5.png', 'c8070812-272b-4f3c-902a-35a05026acf5.png', NULL, 'png', 'c8070812-272b-4f3c-902a-35a05026acf5.png', 'png', '15.72KB   ', 1, '12300000000', '2023-12-14 20:00:30', NULL, NULL, b'0', NULL, NULL);
INSERT INTO `annex_storage` (`id`, `father_id`, `data_category`, `real_name`, `name`, `comment`, `suffix`, `path`, `type`, `size`, `status`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`) VALUES ('cf3c35db-b3d7-4d53-be39-7db0b5bd475d', '6c0a9c35-c1e4-4ec1-b3e4-99b70e720c89', 'cert_pfx', 'd24bce1c-55d8-41e8-b466-883bb9ce8e6f.pfx', 'd24bce1c-55d8-41e8-b466-883bb9ce8e6f.pfx', NULL, 'pfx', 'd24bce1c-55d8-41e8-b466-883bb9ce8e6f.pfx', 'pfx', '3.53KB   ', 1, '12300000000', '2023-12-14 20:04:40', NULL, NULL, b'0', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for app_info
-- ----------------------------
DROP TABLE IF EXISTS `app_info`;
CREATE TABLE `app_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(255) DEFAULT NULL,
  `current_server` varchar(255) DEFAULT NULL,
  `gmt_create` datetime(6) DEFAULT NULL,
  `gmt_modified` datetime(6) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `appNameUK` (`app_name`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COMMENT='定时任务-应用表';

-- ----------------------------
-- Records of app_info
-- ----------------------------
BEGIN;
INSERT INTO `app_info` (`id`, `app_name`, `current_server`, `gmt_create`, `gmt_modified`, `password`) VALUES (5, 'paas-sign', '192.168.0.105:10086', '2022-12-19 14:34:18.464000', '2023-06-07 10:49:39.541000', '123456');
INSERT INTO `app_info` (`id`, `app_name`, `current_server`, `gmt_create`, `gmt_modified`, `password`) VALUES (6, 'paas-sign-prod', '192.168.0.201:23999', '2022-12-19 14:34:32.453000', '2023-12-01 10:08:27.684000', '123456');
COMMIT;

-- ----------------------------
-- Table structure for certificate_info
-- ----------------------------
DROP TABLE IF EXISTS `certificate_info`;
CREATE TABLE `certificate_info` (
  `id` varchar(64) NOT NULL,
  `holder_type` tinyint(1) DEFAULT NULL COMMENT '证书所属类型（1、个人；2、企业）',
  `cert_subject` varchar(100) DEFAULT NULL COMMENT '证书主题',
  `cert_suqe_no` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '证书序列号',
  `issue_time` datetime DEFAULT NULL COMMENT '证书颁发时间',
  `issue_org` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '颁发机构',
  `cert_password` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '证书密码',
  `cert_type` tinyint(1) DEFAULT NULL COMMENT '证书类型（1、云证书）',
  `algorithm_type` varchar(10) DEFAULT NULL COMMENT '加密算法（1、RSA；2、SM2）',
  `term_of_validity_start_time` datetime DEFAULT NULL COMMENT '证书有效期起始时间',
  `term_of_validity_end_time` datetime DEFAULT NULL COMMENT '证书有效期结束时间',
  `storage_medium` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '存储介质',
  `create_by` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='证书信息表';

-- ----------------------------
-- Records of certificate_info
-- ----------------------------
BEGIN;
INSERT INTO `certificate_info` (`id`, `holder_type`, `cert_subject`, `cert_suqe_no`, `issue_time`, `issue_org`, `cert_password`, `cert_type`, `algorithm_type`, `term_of_validity_start_time`, `term_of_validity_end_time`, `storage_medium`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`) VALUES ('01cd0de7-899e-4048-a9ff-baa7c27d970e', 1, 'C=CN,ST=北京,L=北京,O=开放签,OU=产品部,CN=12300000000@12300000000', '1701b66f61ac4fa6a16fc87633ab0c3e', '2023-12-14 19:59:58', '开放签', '123456', 1, 'RSA', '2023-12-14 19:59:58', '2123-11-20 19:59:58', NULL, NULL, '2023-12-14 19:59:58', NULL, NULL, 0, NULL, NULL);
INSERT INTO `certificate_info` (`id`, `holder_type`, `cert_subject`, `cert_suqe_no`, `issue_time`, `issue_org`, `cert_password`, `cert_type`, `algorithm_type`, `term_of_validity_start_time`, `term_of_validity_end_time`, `storage_medium`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`) VALUES ('28ae80b7-9883-4bca-86f5-fd593b3b9568', 2, 'C=CN,ST=北京,L=北京,O=开放签,OU=产品部,CN=开放签平台管理员@', '6b8895df63c8411b9ff2fbc07ffaabb2', '2023-12-14 20:03:51', '开放签', '123456', 1, 'RSA', '2023-12-14 20:03:50', '2123-11-20 20:03:50', NULL, '12300000000', '2023-12-14 20:03:51', NULL, NULL, 0, NULL, NULL);
INSERT INTO `certificate_info` (`id`, `holder_type`, `cert_subject`, `cert_suqe_no`, `issue_time`, `issue_org`, `cert_password`, `cert_type`, `algorithm_type`, `term_of_validity_start_time`, `term_of_validity_end_time`, `storage_medium`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`) VALUES ('6c0a9c35-c1e4-4ec1-b3e4-99b70e720c89', 2, 'C=CN,ST=北京,L=北京,O=开放签,OU=产品部,CN=开放签平台管理员@111111111111111111', 'efab2bf757f948a295b4291639a05b9a', '2023-12-14 20:04:40', '开放签', '123456', 1, 'RSA', '2023-12-14 20:04:40', '2123-11-20 20:04:40', NULL, '12300000000', '2023-12-14 20:04:40', NULL, NULL, 0, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for container_info
-- ----------------------------
DROP TABLE IF EXISTS `container_info`;
CREATE TABLE `container_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_id` bigint(20) DEFAULT NULL,
  `container_name` varchar(255) DEFAULT NULL,
  `gmt_create` datetime(6) DEFAULT NULL,
  `gmt_modified` datetime(6) DEFAULT NULL,
  `last_deploy_time` datetime(6) DEFAULT NULL,
  `source_info` varchar(255) DEFAULT NULL,
  `source_type` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `version` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX8hixyaktlnwil2w9up6b0p898` (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='定时任务-容器表';

-- ----------------------------
-- Records of container_info
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for error_log
-- ----------------------------
DROP TABLE IF EXISTS `error_log`;
CREATE TABLE `error_log` (
  `id` varchar(64) NOT NULL COMMENT 'ID',
  `system_name` varchar(64) DEFAULT NULL COMMENT '系统名称',
  `module_name` varchar(64) DEFAULT NULL COMMENT '模块名称',
  `method_name` varchar(64) DEFAULT NULL COMMENT '方法名称',
  `username` varchar(64) DEFAULT NULL COMMENT '操作人账号',
  `realname` varchar(64) DEFAULT NULL COMMENT '操作人姓名',
  `ip` varchar(64) DEFAULT NULL COMMENT 'IP',
  `request_url` varchar(256) DEFAULT NULL COMMENT '请求路径',
  `method` varchar(256) DEFAULT NULL COMMENT '请求类名+方法名',
  `request_param` varchar(2048) DEFAULT NULL COMMENT '请求参数',
  `request_time` datetime DEFAULT NULL COMMENT '操作时间',
  `error_message` varchar(1024) DEFAULT NULL COMMENT '异常信息',
  `error_stack_info` varchar(2048) DEFAULT NULL COMMENT '错误堆栈信息',
  `log_type` int(2) DEFAULT NULL COMMENT '日志类型（1 全局操作日志，2 自定义操作日志）',
  `thread_name` varchar(64) DEFAULT NULL COMMENT '线程号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT=' 错误日志表';

-- ----------------------------
-- Records of error_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for global_id_config
-- ----------------------------
DROP TABLE IF EXISTS `global_id_config`;
CREATE TABLE `global_id_config` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `id_key` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '单号标识',
  `id_name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '单号名称',
  `content` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '内容',
  `link` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '连接符',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '是否删除',
  `delete_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='单号配置主表';

-- ----------------------------
-- Records of global_id_config
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for global_id_config_info
-- ----------------------------
DROP TABLE IF EXISTS `global_id_config_info`;
CREATE TABLE `global_id_config_info` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `config_id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '规则id',
  `rule_type` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '类型',
  `rule_content` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '规则内容',
  `rule_length` int(11) DEFAULT NULL COMMENT '长度',
  `order_num` int(11) NOT NULL COMMENT '顺序',
  `inti_rule` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '序列号初始化方案',
  `job_id` bigint(20) DEFAULT NULL COMMENT '任务ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='单号配置详情表';

-- ----------------------------
-- Records of global_id_config_info
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for instance_info
-- ----------------------------
DROP TABLE IF EXISTS `instance_info`;
CREATE TABLE `instance_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `actual_trigger_time` bigint(20) DEFAULT NULL,
  `app_id` bigint(20) DEFAULT NULL,
  `expected_trigger_time` bigint(20) DEFAULT NULL,
  `finished_time` bigint(20) DEFAULT NULL,
  `gmt_create` datetime(6) DEFAULT NULL,
  `gmt_modified` datetime(6) DEFAULT NULL,
  `instance_id` bigint(20) DEFAULT NULL,
  `instance_params` longtext,
  `job_id` bigint(20) DEFAULT NULL,
  `job_params` longtext,
  `last_report_time` bigint(20) DEFAULT NULL,
  `result` longtext,
  `running_times` bigint(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `task_tracker_address` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `wf_instance_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX5b1nhpe5je7gc5s1ur200njr7` (`job_id`),
  KEY `IDXjnji5lrr195kswk6f7mfhinrs` (`app_id`),
  KEY `IDXa98hq3yu0l863wuotdjl7noum` (`instance_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='定时任务-实例表';

-- ----------------------------
-- Records of instance_info
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for job_info
-- ----------------------------
DROP TABLE IF EXISTS `job_info`;
CREATE TABLE `job_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_id` bigint(20) DEFAULT NULL,
  `concurrency` int(11) DEFAULT NULL,
  `designated_workers` varchar(255) DEFAULT NULL,
  `dispatch_strategy` int(11) DEFAULT NULL,
  `execute_type` int(11) DEFAULT NULL,
  `extra` varchar(255) DEFAULT NULL,
  `gmt_create` datetime(6) DEFAULT NULL,
  `gmt_modified` datetime(6) DEFAULT NULL,
  `instance_retry_num` int(11) DEFAULT NULL,
  `instance_time_limit` bigint(20) DEFAULT NULL,
  `job_description` varchar(255) DEFAULT NULL,
  `job_name` varchar(255) DEFAULT NULL,
  `job_params` longtext,
  `lifecycle` varchar(255) DEFAULT NULL,
  `max_instance_num` int(11) DEFAULT NULL,
  `max_worker_count` int(11) DEFAULT NULL,
  `min_cpu_cores` double NOT NULL,
  `min_disk_space` double NOT NULL,
  `min_memory_space` double NOT NULL,
  `next_trigger_time` bigint(20) DEFAULT NULL,
  `notify_user_ids` varchar(255) DEFAULT NULL,
  `processor_info` varchar(255) DEFAULT NULL,
  `processor_type` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `task_retry_num` int(11) DEFAULT NULL,
  `time_expression` varchar(255) DEFAULT NULL,
  `time_expression_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDXk2xprmn3lldmlcb52i36udll1` (`app_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='定时任务-任务表';

-- ----------------------------
-- Records of job_info
-- ----------------------------
BEGIN;
INSERT INTO `job_info` (`id`, `app_id`, `concurrency`, `designated_workers`, `dispatch_strategy`, `execute_type`, `extra`, `gmt_create`, `gmt_modified`, `instance_retry_num`, `instance_time_limit`, `job_description`, `job_name`, `job_params`, `lifecycle`, `max_instance_num`, `max_worker_count`, `min_cpu_cores`, `min_disk_space`, `min_memory_space`, `next_trigger_time`, `notify_user_ids`, `processor_info`, `processor_type`, `status`, `task_retry_num`, `time_expression`, `time_expression_type`) VALUES (1, 6, 5, NULL, 1, 1, NULL, '2023-12-14 11:09:41.482000', '2023-12-14 11:09:41.474000', 0, 0, '签署文档截止时间监控', '签署文档截止时间监控', NULL, NULL, 0, 0, 0, 0, 0, 1702573260000, NULL, 'com.resrun.modules.system.job.BasicSysUserEditJob', 1, 1, 0, '* 1 1 * * ? *', 2);
INSERT INTO `job_info` (`id`, `app_id`, `concurrency`, `designated_workers`, `dispatch_strategy`, `execute_type`, `extra`, `gmt_create`, `gmt_modified`, `instance_retry_num`, `instance_time_limit`, `job_description`, `job_name`, `job_params`, `lifecycle`, `max_instance_num`, `max_worker_count`, `min_cpu_cores`, `min_disk_space`, `min_memory_space`, `next_trigger_time`, `notify_user_ids`, `processor_info`, `processor_type`, `status`, `task_retry_num`, `time_expression`, `time_expression_type`) VALUES (2, 6, 5, NULL, 1, 1, NULL, '2023-12-14 11:10:01.348000', '2023-12-14 11:10:01.347000', 0, 0, '证书失效监控', '证书失效监控', NULL, NULL, 0, 0, 0, 0, 0, 1702573260000, NULL, 'com.resrun.modules.system.job.BasicSysUserEditJob', 1, 1, 0, '* 1 1 * * ? *', 2);
COMMIT;

-- ----------------------------
-- Table structure for message_3rd_record
-- ----------------------------
DROP TABLE IF EXISTS `message_3rd_record`;
CREATE TABLE `message_3rd_record` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `message_info_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '消息详情ID',
  `channel_type` varchar(36) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发送通道',
  `template_code` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '消息模板code',
  `mes_send_time` datetime DEFAULT NULL COMMENT '发送时间',
  `mes_send_status` int(11) DEFAULT NULL COMMENT '发送状态',
  `mes_send_result` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发送结果',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标识',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `delete_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '删除人',
  `mes_title` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '消息标题',
  `mes_content` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '消息内容',
  `mes_receiver3rds` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '接收人第三方账号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息-第三方表';

-- ----------------------------
-- Records of message_3rd_record
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for message_info_button
-- ----------------------------
DROP TABLE IF EXISTS `message_info_button`;
CREATE TABLE `message_info_button` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `info_template_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '消息详情模块ID',
  `button_name` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `button_code` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'code',
  `button_route` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '路由',
  `button_style` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '样式',
  `route_method` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '方法',
  `button_order` int(11) DEFAULT NULL COMMENT '排序',
  `button_para` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '参数集合',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息对应操作表';

-- ----------------------------
-- Records of message_info_button
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for message_info_email
-- ----------------------------
DROP TABLE IF EXISTS `message_info_email`;
CREATE TABLE `message_info_email` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `info_template_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '消息实例模板id',
  `mes_copy_users` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '抄送人，逗号分隔',
  `mes_dark_users` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '密送，逗号分隔',
  `attachment` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '附件列表 逗号分隔',
  `send_user_account` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发送人账号',
  `send_user_nick_name` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '发送人名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息-邮件表';

-- ----------------------------
-- Records of message_info_email
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for message_info_template
-- ----------------------------
DROP TABLE IF EXISTS `message_info_template`;
CREATE TABLE `message_info_template` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `app_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '接收人-应用ID',
  `send_type` int(3) DEFAULT NULL COMMENT '发送类型（及时发送，调度发送）',
  `job_json` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '调度参数',
  `job_id` bigint(20) DEFAULT NULL COMMENT '调度任务ID',
  `channel_type` int(11) DEFAULT NULL COMMENT '发送类型',
  `template_code` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '模板code',
  `mes_title` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '消息标题',
  `mes_content` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '消息内容',
  `tenant_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '接收人-租户ID',
  `depart_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '接收人-部门ID',
  `tenant_user_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '接收人-租户用户ID',
  `user_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '接收人用户ID',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `message_para` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '参数（短信）',
  `receivers` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '第三方接受list',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='模板生成消息主表';

-- ----------------------------
-- Records of message_info_template
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for message_send_record
-- ----------------------------
DROP TABLE IF EXISTS `message_send_record`;
CREATE TABLE `message_send_record` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `app_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '所属应用',
  `message_info_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '消息详情id',
  `template_code` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '模板编号',
  `mes_title` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '消息标题',
  `tenant_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户ID',
  `depart_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门ID',
  `user_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户ID',
  `tenant_user_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户用户ID',
  `mes_content` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '消息内容',
  `mes_send_time` datetime DEFAULT NULL COMMENT '发送时间',
  `send_status` int(11) DEFAULT NULL COMMENT '发送状态',
  `is_read` tinyint(1) DEFAULT NULL COMMENT '是否已读',
  `read_time` datetime DEFAULT NULL COMMENT '阅读时间',
  `remark` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标识',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `delete_by` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '删除人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息-发送记录表';

-- ----------------------------
-- Records of message_send_record
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for message_template
-- ----------------------------
DROP TABLE IF EXISTS `message_template`;
CREATE TABLE `message_template` (
  `id` varchar(50) NOT NULL,
  `app_id` varchar(64) DEFAULT NULL COMMENT '所属应用',
  `template_type` varchar(50) DEFAULT NULL COMMENT '模板类型',
  `template_code` varchar(255) DEFAULT NULL COMMENT '模板编号',
  `template_title` varchar(1024) DEFAULT NULL COMMENT '模板标题',
  `template_content` text COMMENT '模板内容',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建者',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新者',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识位',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `delete_by` varchar(64) DEFAULT NULL COMMENT '删除人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `template_code` (`template_code`) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='消息模板表';

-- ----------------------------
-- Records of message_template
-- ----------------------------
BEGIN;
INSERT INTO `message_template` (`id`, `app_id`, `template_type`, `template_code`, `template_title`, `template_content`, `create_by`, `update_by`, `create_time`, `update_time`, `delete_flag`, `delete_time`, `delete_by`) VALUES ('09147a83-2af0-471b-8dc9-cd24b34b125b', '13131313', 'f60edc86-9ea1-41c9-add6-10c4c18aca3f', 'ent_certificate_chenge', '印章变更提醒', '<p>${operator}对企业信息进行变更申请，目前该申请处于审核中，审核期间，您管理的${category}，${sealName}处于禁用状态</p>', 'admin', 'admin', '2024-01-03 15:47:37', '2024-01-04 19:58:29', 0, NULL, NULL);
INSERT INTO `message_template` (`id`, `app_id`, `template_type`, `template_code`, `template_title`, `template_content`, `create_by`, `update_by`, `create_time`, `update_time`, `delete_flag`, `delete_time`, `delete_by`) VALUES ('0f1081a2-4b7a-44f3-af6d-367b18c62c3e', '490489ab-d8b4-414c-ad77-d856962c286f', 'efab6159-65b4-4da5-8991-32a596148f5d', 'contract_sign_expired', '您发送的文件《${contract}》已过期', '<p>您发送的文件《${contract}》已过期，无法继续签署，请知悉</p>', 'admin', 'admin', '2024-01-03 17:14:49', '2024-01-04 20:47:36', 0, NULL, NULL);
INSERT INTO `message_template` (`id`, `app_id`, `template_type`, `template_code`, `template_title`, `template_content`, `create_by`, `update_by`, `create_time`, `update_time`, `delete_flag`, `delete_time`, `delete_by`) VALUES ('2838cc9b-c411-4e5a-9cdd-b28848c60477', '490489ab-d8b4-414c-ad77-d856962c286f', 'efab6159-65b4-4da5-8991-32a596148f5d', 'opensign_contract_wrirte', '${sender}给您发送了一份文件《${contract}》，有部分内容需要您填写', '<p>${sender}给您发送了一份文件《${contract}》，有部分内容需要您填写</p>', 'admin', 'admin', '2024-01-03 16:59:40', '2024-01-05 17:08:02', 0, NULL, NULL);
INSERT INTO `message_template` (`id`, `app_id`, `template_type`, `template_code`, `template_title`, `template_content`, `create_by`, `update_by`, `create_time`, `update_time`, `delete_flag`, `delete_time`, `delete_by`) VALUES ('40ee0b74-4a87-4f93-a41f-dcecc65af5d8', '13131313', 'f60edc86-9ea1-41c9-add6-10c4c18aca3f', 'ent_certificate_chenge_recover', '印章变更提醒', '<p>由于企业信息变更未涉及${category}，${sealName}章面信息变更，该印章解除禁用状态</p>', 'admin', 'admin', '2024-01-03 16:54:06', '2024-01-04 19:54:42', 0, NULL, NULL);
INSERT INTO `message_template` (`id`, `app_id`, `template_type`, `template_code`, `template_title`, `template_content`, `create_by`, `update_by`, `create_time`, `update_time`, `delete_flag`, `delete_time`, `delete_by`) VALUES ('42d5a3b9-bf3b-48e2-8ac6-4e083036d3b2', '490489ab-d8b4-414c-ad77-d856962c286f', 'efab6159-65b4-4da5-8991-32a596148f5d', 'opensign_sign_reject', '您发送的文件《${contract}》被${signer}拒绝签署', '<p>您发送的文件《${contract}》被${signer}拒绝签署</p>', 'admin', 'admin', '2024-01-03 17:09:08', '2024-01-04 20:47:50', 0, NULL, NULL);
INSERT INTO `message_template` (`id`, `app_id`, `template_type`, `template_code`, `template_title`, `template_content`, `create_by`, `update_by`, `create_time`, `update_time`, `delete_flag`, `delete_time`, `delete_by`) VALUES ('5d379683-d212-49d4-a2ab-60c70fce9e05', '13131313', '6c722a79-a5ca-4302-96a7-37cc62f666f7', 'join_faild', '申请加入${companyName}的信息审核未通过', '<p>您申请加入${companyName}的信息审核未通过。</p>', 'admin', 'admin', '2024-01-03 12:00:25', '2024-01-04 20:45:48', 0, NULL, NULL);
INSERT INTO `message_template` (`id`, `app_id`, `template_type`, `template_code`, `template_title`, `template_content`, `create_by`, `update_by`, `create_time`, `update_time`, `delete_flag`, `delete_time`, `delete_by`) VALUES ('8869828d-300a-4e5d-8a11-f813bbc7504c', '13131313', '6c722a79-a5ca-4302-96a7-37cc62f666f7', 'join_success', '申请加入${companyName}的信息已审核通过', '<p>您申请加入${companyName}的信息已审核通过</p>', 'admin', 'admin', '2024-01-03 11:56:46', '2024-01-04 20:45:57', 0, NULL, NULL);
INSERT INTO `message_template` (`id`, `app_id`, `template_type`, `template_code`, `template_title`, `template_content`, `create_by`, `update_by`, `create_time`, `update_time`, `delete_flag`, `delete_time`, `delete_by`) VALUES ('a08eb89e-e942-4cdf-acf6-b4e3ecc0c76d', '70588803-52e4-ss3d-a61f-0a68e1febd72', '7abbfbe2-4ad6-443f-b3ed-4cdf7b890182', 'ent_auth_success', '您提交的【${companyName}】认证信息已审核通过', '<p>您提交的【${companyName}】认证信息已审核通过</p>', 'admin', 'admin', '2024-01-03 12:01:10', '2024-01-05 19:28:55', 0, NULL, NULL);
INSERT INTO `message_template` (`id`, `app_id`, `template_type`, `template_code`, `template_title`, `template_content`, `create_by`, `update_by`, `create_time`, `update_time`, `delete_flag`, `delete_time`, `delete_by`) VALUES ('a1a355c2-9148-4627-832a-9807a7310f1c', NULL, '950107ae-e72e-4895-8627-db11e8aa707e', 'paas_check_email_code', '系统验证码', '<p>${code}验证码，若非本人操作，请勿泄露</p>', 'admin', '13341081511', '2022-09-29 18:10:38', '2022-11-01 15:59:12', 0, NULL, NULL);
INSERT INTO `message_template` (`id`, `app_id`, `template_type`, `template_code`, `template_title`, `template_content`, `create_by`, `update_by`, `create_time`, `update_time`, `delete_flag`, `delete_time`, `delete_by`) VALUES ('aef55dac-0ec6-411d-8342-db2396b0c3c4', '70588803-52e4-ss3d-a61f-0a68e1febd72', 'f60edc86-9ea1-41c9-add6-10c4c18aca3f', 'ent_certificate_chenge_no_recover', '印章变更提醒', '<p>企业信息发生变更，您管理的${category}，${sealName}章面信息与现在的企业信息不一致，印章需要重新制作</p>', 'admin', 'admin', '2024-01-03 16:53:38', '2024-01-04 19:54:59', 0, NULL, NULL);
INSERT INTO `message_template` (`id`, `app_id`, `template_type`, `template_code`, `template_title`, `template_content`, `create_by`, `update_by`, `create_time`, `update_time`, `delete_flag`, `delete_time`, `delete_by`) VALUES ('b3e25c70-9f8c-4803-a8b0-da232573b288', '490489ab-d8b4-414c-ad77-d856962c286f', 'efab6159-65b4-4da5-8991-32a596148f5d', 'opensign_sign', '${sender}给您发送了一份文件《${contract}》需要您签署', '<p>${sender}给您发送了一份文件《${contract}》需要您签署</p>', 'admin', 'admin', '2024-01-03 17:07:12', '2024-01-04 20:48:07', 0, NULL, NULL);
INSERT INTO `message_template` (`id`, `app_id`, `template_type`, `template_code`, `template_title`, `template_content`, `create_by`, `update_by`, `create_time`, `update_time`, `delete_flag`, `delete_time`, `delete_by`) VALUES ('b5e7351d-2e9a-43e9-8fb1-26653da193e6', '490489ab-d8b4-414c-ad77-d856962c286f', 'efab6159-65b4-4da5-8991-32a596148f5d', 'sign_done', '您发起的文件《${contract}》已完成签署', '<p>您发起的文件《${contract}》已完成签署</p>', 'admin', 'admin', '2024-01-03 17:18:21', '2024-01-04 20:46:39', 0, NULL, NULL);
INSERT INTO `message_template` (`id`, `app_id`, `template_type`, `template_code`, `template_title`, `template_content`, `create_by`, `update_by`, `create_time`, `update_time`, `delete_flag`, `delete_time`, `delete_by`) VALUES ('d426a3f7-66a0-4038-b7fc-d70567569ffb', NULL, '950107ae-e72e-4895-8627-db11e8aa707e', 'bpms-test-mes', '${title}', NULL, 'admin', '15313927710', '2022-11-18 16:52:06', '2023-04-06 14:27:22', 0, NULL, NULL);
INSERT INTO `message_template` (`id`, `app_id`, `template_type`, `template_code`, `template_title`, `template_content`, `create_by`, `update_by`, `create_time`, `update_time`, `delete_flag`, `delete_time`, `delete_by`) VALUES ('dba7f026-0943-4aca-9c03-7492115e8386', '490489ab-d8b4-414c-ad77-d856962c286f', 'efab6159-65b4-4da5-8991-32a596148f5d', 'sign_about_expired', '您发起的电子签约文件《${contract}》还未完成签署，即将逾期，请尽快处理', '<p>您发起的电子签约文件《${contract}》还未完成签署，签署截止日期：${expire_date}，请尽快催办处理。</p>', 'admin', 'admin', '2024-01-03 17:16:58', '2024-01-04 20:47:14', 0, NULL, NULL);
INSERT INTO `message_template` (`id`, `app_id`, `template_type`, `template_code`, `template_title`, `template_content`, `create_by`, `update_by`, `create_time`, `update_time`, `delete_flag`, `delete_time`, `delete_by`) VALUES ('e134c14e-1d8b-42ce-92de-ecadd9f48904', '13131313', '950107ae-e72e-4895-8627-db11e8aa707e', 'announcement_mail', '系统公告通知：${title}', '<p>系统公告发布：${title}</p>', 'admin', 'admin', '2022-09-26 16:53:53', '2023-10-13 14:25:12', 0, NULL, NULL);
INSERT INTO `message_template` (`id`, `app_id`, `template_type`, `template_code`, `template_title`, `template_content`, `create_by`, `update_by`, `create_time`, `update_time`, `delete_flag`, `delete_time`, `delete_by`) VALUES ('eb922500-029f-4bba-80e7-af65abad924e', '490489ab-d8b4-414c-ad77-d856962c286f', '6c722a79-a5ca-4302-96a7-37cc62f666f7', 'invite_user', '${companyName}添加您为企业成员', '<p><span style=\"color: #000000; font-family: NeverMind, sans-serif, \'Microsoft YaHei\', \'PingFang SC\', \'Microsoft JhengHei\', \'Apple Color Emoji\', \'Segoe UI Emoji\', \'Segoe UI Symbol\', \'Noto Color Emoji\'; font-size: 14px; white-space-collapse: break-spaces; background-color: #ffffff;\">${companyName}添加您为企业成员</span></p>', 'admin', 'admin', '2024-01-03 10:21:48', '2024-01-04 20:46:12', 0, NULL, NULL);
INSERT INTO `message_template` (`id`, `app_id`, `template_type`, `template_code`, `template_title`, `template_content`, `create_by`, `update_by`, `create_time`, `update_time`, `delete_flag`, `delete_time`, `delete_by`) VALUES ('f8fe562f-b7c4-42ae-ba48-358cfef215cd', '490489ab-d8b4-414c-ad77-d856962c286f', 'efab6159-65b4-4da5-8991-32a596148f5d', 'opensign_contract_wrirte_reject', '您发送的文件《${contract}》被${signer}拒绝填写', '<p>您发送的文件《${contract}》被${signer}拒绝填写</p>', 'admin', 'admin', '2024-01-03 17:04:57', '2024-01-04 20:48:16', 0, NULL, NULL);
INSERT INTO `message_template` (`id`, `app_id`, `template_type`, `template_code`, `template_title`, `template_content`, `create_by`, `update_by`, `create_time`, `update_time`, `delete_flag`, `delete_time`, `delete_by`) VALUES ('fa710d7f-8c0f-445d-86ef-ac3a6cb01a07', '70588803-52e4-ss3d-a61f-0a68e1febd72', '7abbfbe2-4ad6-443f-b3ed-4cdf7b890182', 'ent_auth_faild', '您提交的【${companyName}】认证审核未通过，原因【${reason}】', '<p>提交的【${companyName}】认证审核未通过，原因【${reason}】</p>', 'admin', 'admin', '2024-01-03 12:01:47', '2024-01-05 19:28:46', 0, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for message_template_button
-- ----------------------------
DROP TABLE IF EXISTS `message_template_button`;
CREATE TABLE `message_template_button` (
  `id` varchar(50) NOT NULL,
  `template_id` varchar(50) DEFAULT NULL COMMENT '模板id',
  `button_name` varchar(255) DEFAULT NULL COMMENT '按钮名称',
  `button_code` varchar(255) DEFAULT NULL COMMENT '按钮code',
  `button_desc` varchar(255) DEFAULT NULL COMMENT '按钮说明',
  `button_route` varchar(100) DEFAULT NULL COMMENT '按钮路由地址',
  `button_style` varchar(255) DEFAULT NULL COMMENT '按钮样式',
  `route_method` varchar(255) DEFAULT NULL COMMENT '路由跳转方式',
  `button_order` int(3) DEFAULT NULL COMMENT '按钮排序',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `template_id` (`template_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='消息模板操作表';

-- ----------------------------
-- Records of message_template_button
-- ----------------------------
BEGIN;
INSERT INTO `message_template_button` (`id`, `template_id`, `button_name`, `button_code`, `button_desc`, `button_route`, `button_style`, `route_method`, `button_order`) VALUES ('1499b2ca-b45c-4e9b-8499-ef2cadfa6eb7', 'fa710d7f-8c0f-445d-86ef-ac3a6cb01a07', '修改', 're_auth', '', '/enterprise', 'primary', '', NULL);
INSERT INTO `message_template_button` (`id`, `template_id`, `button_name`, `button_code`, `button_desc`, `button_route`, `button_style`, `route_method`, `button_order`) VALUES ('15a6612e-e66f-4c26-b027-2ee92ea5a579', 'a1a355c2-9148-4627-832a-9807a7310f1c', '', '', '', '', '', '', NULL);
INSERT INTO `message_template_button` (`id`, `template_id`, `button_name`, `button_code`, `button_desc`, `button_route`, `button_style`, `route_method`, `button_order`) VALUES ('1c6c9827-05e1-4529-8489-914a06cce86e', 'f8fe562f-b7c4-42ae-ba48-358cfef215cd', '查看', 'contract_wrirte_reject', '', '/contract/params', '', '', NULL);
INSERT INTO `message_template_button` (`id`, `template_id`, `button_name`, `button_code`, `button_desc`, `button_route`, `button_style`, `route_method`, `button_order`) VALUES ('3b7a00a6-6ea1-41ba-bbb7-112987270a26', '8869828d-300a-4e5d-8a11-f813bbc7504c', '', '', '', '', '', '', NULL);
INSERT INTO `message_template_button` (`id`, `template_id`, `button_name`, `button_code`, `button_desc`, `button_route`, `button_style`, `route_method`, `button_order`) VALUES ('3d905f3a-5745-40f2-bc43-89a022dda8a2', '0f1081a2-4b7a-44f3-af6d-367b18c62c3e', '查看', 'contract_expired', '', '/contract/detail', '', '', NULL);
INSERT INTO `message_template_button` (`id`, `template_id`, `button_name`, `button_code`, `button_desc`, `button_route`, `button_style`, `route_method`, `button_order`) VALUES ('3dd85c38-1997-4a0b-a783-27956300a357', 'b3e25c70-9f8c-4803-a8b0-da232573b288', '去签署', 'contract_sign', '', '/contract/sign', 'primary', '', NULL);
INSERT INTO `message_template_button` (`id`, `template_id`, `button_name`, `button_code`, `button_desc`, `button_route`, `button_style`, `route_method`, `button_order`) VALUES ('4f576f3b-74d8-4eb9-87af-9d91a368c69b', 'dba7f026-0943-4aca-9c03-7492115e8386', '查看', 'about_expired', '', '/contract/sign', '', '', NULL);
INSERT INTO `message_template_button` (`id`, `template_id`, `button_name`, `button_code`, `button_desc`, `button_route`, `button_style`, `route_method`, `button_order`) VALUES ('50d95f32-47d2-4aed-a8b1-cc7f2a99c2c5', '2838cc9b-c411-4e5a-9cdd-b28848c60477', '去填写', 'contract_wrirte', '', '/contract/params', '', '', NULL);
INSERT INTO `message_template_button` (`id`, `template_id`, `button_name`, `button_code`, `button_desc`, `button_route`, `button_style`, `route_method`, `button_order`) VALUES ('5676282f-7294-416f-b37a-a01c56224f1a', '40ee0b74-4a87-4f93-a41f-dcecc65af5d8', '', '', '', '', '', '', NULL);
INSERT INTO `message_template_button` (`id`, `template_id`, `button_name`, `button_code`, `button_desc`, `button_route`, `button_style`, `route_method`, `button_order`) VALUES ('56c1b89d-f2cd-4ae0-baa5-1f4e8c3fea44', 'e134c14e-1d8b-42ce-92de-ecadd9f48904', '应用跳转', 'test', '测试多个按钮', '/overview', 'primary', 'router', 2);
INSERT INTO `message_template_button` (`id`, `template_id`, `button_name`, `button_code`, `button_desc`, `button_route`, `button_style`, `route_method`, `button_order`) VALUES ('5c8dde16-5fea-4d3d-9daa-22fe92f37db0', 'e134c14e-1d8b-42ce-92de-ecadd9f48904', '详情', 'info', '公告详情', '/myannounce/', 'primary', 'router', 1);
INSERT INTO `message_template_button` (`id`, `template_id`, `button_name`, `button_code`, `button_desc`, `button_route`, `button_style`, `route_method`, `button_order`) VALUES ('79c641c4-a291-4ea9-863b-7a43dbc7c1ad', 'aef55dac-0ec6-411d-8342-db2396b0c3c4', '', '', '', '', '', '', NULL);
INSERT INTO `message_template_button` (`id`, `template_id`, `button_name`, `button_code`, `button_desc`, `button_route`, `button_style`, `route_method`, `button_order`) VALUES ('ab9f2b8a-005a-40f0-a795-8ae4662e423b', '5d379683-d212-49d4-a2ab-60c70fce9e05', '', '', '', '', '', '', NULL);
INSERT INTO `message_template_button` (`id`, `template_id`, `button_name`, `button_code`, `button_desc`, `button_route`, `button_style`, `route_method`, `button_order`) VALUES ('ac0b0673-507b-497c-8027-6d27d4e2fb9c', '09147a83-2af0-471b-8dc9-cd24b34b125b', '', '', '', '', '', '', NULL);
INSERT INTO `message_template_button` (`id`, `template_id`, `button_name`, `button_code`, `button_desc`, `button_route`, `button_style`, `route_method`, `button_order`) VALUES ('e44dd4db-0e28-4525-8a05-30f8ef1eb6c4', 'a08eb89e-e942-4cdf-acf6-b4e3ecc0c76d', '', '', '', '', '', '', NULL);
INSERT INTO `message_template_button` (`id`, `template_id`, `button_name`, `button_code`, `button_desc`, `button_route`, `button_style`, `route_method`, `button_order`) VALUES ('ef39290e-9b2e-43ab-aef0-f35c891f66c6', 'b5e7351d-2e9a-43e9-8fb1-26653da193e6', '查看', 'sign_complete', '', '/contract/detail/sign', '', '', NULL);
INSERT INTO `message_template_button` (`id`, `template_id`, `button_name`, `button_code`, `button_desc`, `button_route`, `button_style`, `route_method`, `button_order`) VALUES ('f82daac7-2818-433d-8f41-8e7be3790059', '42d5a3b9-bf3b-48e2-8ac6-4e083036d3b2', '查看', 'sign_reject', '', '/contract/sign', 'primary', '', NULL);
INSERT INTO `message_template_button` (`id`, `template_id`, `button_name`, `button_code`, `button_desc`, `button_route`, `button_style`, `route_method`, `button_order`) VALUES ('f8c8806b-5117-40b4-99d0-23990a0416cf', 'eb922500-029f-4bba-80e7-af65abad924e', '加入', 'join', '点击加入企业', '/join', '', '', NULL);
COMMIT;

-- ----------------------------
-- Table structure for message_template_button_para
-- ----------------------------
DROP TABLE IF EXISTS `message_template_button_para`;
CREATE TABLE `message_template_button_para` (
  `id` varchar(50) NOT NULL,
  `button_id` varchar(50) DEFAULT NULL COMMENT '模板按钮id',
  `para_name` varchar(255) DEFAULT NULL COMMENT '参数名称',
  `para_type` varchar(100) DEFAULT NULL COMMENT '参数类型',
  `send_type` varchar(255) DEFAULT NULL COMMENT '传参方式',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='消息模板操作参数表';

-- ----------------------------
-- Records of message_template_button_para
-- ----------------------------
BEGIN;
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('0b7ad451-bbac-4966-9255-509f3a07d146', '56c1b89d-f2cd-4ae0-baa5-1f4e8c3fea44', '', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('0eb4d2a9-131b-4530-befe-3e65d7265a6d', 'ef39290e-9b2e-43ab-aef0-f35c891f66c6', '__full__', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('11ba3e3c-e055-47b1-9b18-503a5597ed58', '5c8dde16-5fea-4d3d-9daa-22fe92f37db0', 'infoId', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('16a399fc-4177-41f7-9323-bc45cd759c6a', 'f82daac7-2818-433d-8f41-8e7be3790059', 'signRuId', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('17394b27-ee2d-45c0-a5f3-5e4168b6851f', '4f576f3b-74d8-4eb9-87af-9d91a368c69b', 'type', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('1a5e17ff-521f-4f08-be86-a381f216a41b', '79c641c4-a291-4ea9-863b-7a43dbc7c1ad', '', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('1ffaaa5b-57ae-4941-8495-d6551a0e4a46', 'f82daac7-2818-433d-8f41-8e7be3790059', 'isDetail', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('26ad6582-92fe-4de5-8400-5e88ca930acb', '50d95f32-47d2-4aed-a8b1-cc7f2a99c2c5', 'type', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('2c6bc440-7a60-4806-a0c8-1a9b04a21c48', '3dd85c38-1997-4a0b-a783-27956300a357', 'type', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('361ab7a5-2da7-4c0d-a628-ed35c1a060d8', 'f82daac7-2818-433d-8f41-8e7be3790059', 'type', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('3ae06cc8-2a66-41b5-9516-b435b8439e12', 'e44dd4db-0e28-4525-8a05-30f8ef1eb6c4', '', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('3bfebf39-c875-42d6-81d8-383e91dd6313', '4f576f3b-74d8-4eb9-87af-9d91a368c69b', '__full__', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('3e413a3b-d871-487b-aefe-79f632398080', '3d905f3a-5745-40f2-bc43-89a022dda8a2', '__full__', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('3eaeb58d-33fe-46ba-9e6b-59edb0a83c4e', '50d95f32-47d2-4aed-a8b1-cc7f2a99c2c5', '__full__', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('477741ad-851c-44bd-a2f1-f73d5d8476f6', '50d95f32-47d2-4aed-a8b1-cc7f2a99c2c5', 'signRuId', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('503f1af6-fcc2-450b-9f09-93c7edf00c54', '3dd85c38-1997-4a0b-a783-27956300a357', 'from', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('519e23f6-936d-4a8c-9105-3900636647f2', '3d905f3a-5745-40f2-bc43-89a022dda8a2', 'signRuId', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('5ac32d4a-cdf6-40df-8735-6b9df333689f', '50d95f32-47d2-4aed-a8b1-cc7f2a99c2c5', 'signReId', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('5cb7b056-5e70-4a22-b466-f864ec607033', '3dd85c38-1997-4a0b-a783-27956300a357', '__full__', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('5e8dcd31-b53c-4dd3-8f1c-f5c177a286a2', '1499b2ca-b45c-4e9b-8499-ef2cadfa6eb7', '', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('6251620a-93e6-4a39-ae49-27ce675687f1', 'ac0b0673-507b-497c-8027-6d27d4e2fb9c', '', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('628c536c-ffd1-4849-9575-b82a62219597', '50d95f32-47d2-4aed-a8b1-cc7f2a99c2c5', 'taskId', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('63a7260d-b126-4928-a67c-ebb97cfb0dd6', 'f8c8806b-5117-40b4-99d0-23990a0416cf', 'key', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('667b42a3-40b2-436f-a03f-39fb5685a97a', '1c6c9827-05e1-4529-8489-914a06cce86e', 'isDetail', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('67dc015a-c2a9-4dc1-b061-efaadd36251e', '1c6c9827-05e1-4529-8489-914a06cce86e', '__full__', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('6a668e25-35e2-4011-b8f6-97435a763fc2', '15a6612e-e66f-4c26-b027-2ee92ea5a579', '', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('6a7d0915-3c2a-4aa3-b522-643501dc0242', '3dd85c38-1997-4a0b-a783-27956300a357', 'signReId', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('6addc9ac-b2c4-4dcf-90b8-9e07e55e6bbc', '4f576f3b-74d8-4eb9-87af-9d91a368c69b', 'from', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('858c48c8-64ac-403d-957a-5c7df7d12426', '1c6c9827-05e1-4529-8489-914a06cce86e', 'signReId', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('97b0333b-cce5-481c-bad9-4b4ce16385d6', 'f8c8806b-5117-40b4-99d0-23990a0416cf', 'type', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('98ab1ad0-23de-42cd-96fd-a4c1344f1e1a', '3dd85c38-1997-4a0b-a783-27956300a357', 'taskId', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('a561ecd4-13b9-49da-b2fe-570e8dfe1efc', '4f576f3b-74d8-4eb9-87af-9d91a368c69b', 'taskId', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('a797ef18-77df-4c64-a1cc-b765ea8c0c9e', 'f82daac7-2818-433d-8f41-8e7be3790059', '__full__', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('b1495e13-da54-4ae8-a2d7-0489f1dc2051', '50d95f32-47d2-4aed-a8b1-cc7f2a99c2c5', 'from', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('baf60583-1c77-4f78-b5de-28e3ddc2552d', 'ab9f2b8a-005a-40f0-a795-8ae4662e423b', '', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('bb07372d-3ee6-44c7-b043-31556d867fde', '5676282f-7294-416f-b37a-a01c56224f1a', '', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('bd04effe-4b8d-4167-be8d-a88ccaf0757a', 'ef39290e-9b2e-43ab-aef0-f35c891f66c6', 'signRuId', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('becd008b-2eba-423f-9f56-13aaddb259ad', '3b7a00a6-6ea1-41ba-bbb7-112987270a26', '', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('bf701327-4a08-4f6e-88f2-3d58764a56f1', '3dd85c38-1997-4a0b-a783-27956300a357', 'signRuId', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('d8322ab5-06b3-4f12-84c4-49f89132d01a', '4f576f3b-74d8-4eb9-87af-9d91a368c69b', 'signReId', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('dee8a92f-27f3-40a2-97dc-539b336c96d5', '1c6c9827-05e1-4529-8489-914a06cce86e', 'signRuId', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('e3369858-f7da-4ab9-9523-68044d167b95', 'f82daac7-2818-433d-8f41-8e7be3790059', 'signReId', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('e9cb31d0-1714-494b-8de9-5111c0ac98e3', '3dd85c38-1997-4a0b-a783-27956300a357', 'isDetail', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('f401b816-97f0-499a-9ca9-c8b0911ea440', '50d95f32-47d2-4aed-a8b1-cc7f2a99c2c5', 'isDetail', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('f40bd5e2-0f01-4fd2-a0c5-f47c0187cec9', '4f576f3b-74d8-4eb9-87af-9d91a368c69b', 'isDetail', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('f7b0e7ca-0e7f-4b53-acb2-2df6cd5366eb', '4f576f3b-74d8-4eb9-87af-9d91a368c69b', 'signRuId', NULL, '');
INSERT INTO `message_template_button_para` (`id`, `button_id`, `para_name`, `para_type`, `send_type`) VALUES ('fd1d2e1b-d144-45cf-8ef2-b74245268519', 'f82daac7-2818-433d-8f41-8e7be3790059', 'from', NULL, '');
COMMIT;

-- ----------------------------
-- Table structure for message_template_type
-- ----------------------------
DROP TABLE IF EXISTS `message_template_type`;
CREATE TABLE `message_template_type` (
  `id` varchar(64) NOT NULL,
  `parent_id` varchar(64) DEFAULT NULL COMMENT '父ID',
  `type_name` varchar(10) DEFAULT NULL COMMENT '类型名称',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='消息-分类表';

-- ----------------------------
-- Records of message_template_type
-- ----------------------------
BEGIN;
INSERT INTO `message_template_type` (`id`, `parent_id`, `type_name`, `description`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('6c722a79-a5ca-4302-96a7-37cc62f666f7', '', '企业成员管理', NULL, 'admin', '2024-01-04 19:52:59', NULL, NULL);
INSERT INTO `message_template_type` (`id`, `parent_id`, `type_name`, `description`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('7abbfbe2-4ad6-443f-b3ed-4cdf7b890182', '', '实名认证', NULL, 'admin', '2024-01-04 19:47:50', NULL, NULL);
INSERT INTO `message_template_type` (`id`, `parent_id`, `type_name`, `description`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('950107ae-e72e-4895-8627-db11e8aa707e', '', '系统消息', '佛挡杀佛', 'admin', '2022-09-19 16:19:30', 'admin', '2024-01-04 19:54:07');
INSERT INTO `message_template_type` (`id`, `parent_id`, `type_name`, `description`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('efab6159-65b4-4da5-8991-32a596148f5d', '', '文件签署', '23', 'admin', '2022-09-19 16:08:08', 'admin', '2024-01-04 19:46:48');
INSERT INTO `message_template_type` (`id`, `parent_id`, `type_name`, `description`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f60edc86-9ea1-41c9-add6-10c4c18aca3f', '', '电子印章', NULL, 'admin', '2024-01-04 19:47:33', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for monitor_blacklist
-- ----------------------------
DROP TABLE IF EXISTS `monitor_blacklist`;
CREATE TABLE `monitor_blacklist` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '类型',
  `ip_start` varchar(30) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'ip开始地址',
  `ip_end` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'ip结束地址',
  `ip_desc` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '创建人',
  `create_time` datetime NOT NULL COMMENT '创建日期',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `ip_start_format` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `ip_end_format` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='黑白名单表';

-- ----------------------------
-- Records of monitor_blacklist
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for monitor_config
-- ----------------------------
DROP TABLE IF EXISTS `monitor_config`;
CREATE TABLE `monitor_config` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '主键',
  `name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `path` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '路径',
  `method` varchar(20) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '请求方法',
  `module_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '所属模块名称',
  `system_name` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '系统名称',
  `monitor_flag` tinyint(1) NOT NULL COMMENT '是否监控',
  `usable_flag` tinyint(1) NOT NULL COMMENT '可用标识',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标志0：未删除1：已删除',
  `delete_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '删除者',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `sensitive_flag` tinyint(1) DEFAULT NULL COMMENT '敏感操作标识',
  `sensitive_desc` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述',
  `perms` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '权限标识',
  `sensitive_type` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '校验类型',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作控制配置表';

-- ----------------------------
-- Records of monitor_config
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for oms_lock
-- ----------------------------
DROP TABLE IF EXISTS `oms_lock`;
CREATE TABLE `oms_lock` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` datetime(6) DEFAULT NULL,
  `gmt_modified` datetime(6) DEFAULT NULL,
  `lock_name` varchar(255) DEFAULT NULL,
  `max_lock_time` bigint(20) DEFAULT NULL,
  `ownerip` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `lockNameUK` (`lock_name`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COMMENT='定时任务-锁表';

-- ----------------------------
-- Records of oms_lock
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for onl_form_table
-- ----------------------------
DROP TABLE IF EXISTS `onl_form_table`;
CREATE TABLE `onl_form_table` (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `table_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '表名',
  `table_version` int(11) DEFAULT NULL COMMENT '版本',
  `table_txt` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '表描述',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `app_info_id` varchar(64) DEFAULT NULL COMMENT '应用ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='在线表单主表';

-- ----------------------------
-- Records of onl_form_table
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for onl_table_field
-- ----------------------------
DROP TABLE IF EXISTS `onl_table_field`;
CREATE TABLE `onl_table_field` (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `form_table_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '父id',
  `db_field_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '字段名',
  `db_field_txt` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '字段描述',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `auth_visible` tinyint(1) DEFAULT NULL COMMENT '配置权限时是否可见1可见0不可见',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='在线表单字段表';

-- ----------------------------
-- Records of onl_table_field
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for onl_table_form
-- ----------------------------
DROP TABLE IF EXISTS `onl_table_form`;
CREATE TABLE `onl_table_form` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `table_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '表ID',
  `sys_permission_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='在线表单主表-表单';

-- ----------------------------
-- Records of onl_table_form
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for opensign
-- ----------------------------
DROP TABLE IF EXISTS `opensign`;
CREATE TABLE `opensign` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `field1` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'field1',
  `field2` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'field2',
  `field3` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'field3',
  `field4` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'field4',
  `field5` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'field5',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='集成表单demo2';

-- ----------------------------
-- Records of opensign
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for operate_log
-- ----------------------------
DROP TABLE IF EXISTS `operate_log`;
CREATE TABLE `operate_log` (
  `id` varchar(64) NOT NULL COMMENT 'ID',
  `system_name` varchar(64) DEFAULT NULL COMMENT '系统名称',
  `module_name` varchar(64) DEFAULT NULL COMMENT '模块名称',
  `method_name` varchar(64) DEFAULT NULL COMMENT '方法名称',
  `username` varchar(64) DEFAULT NULL COMMENT '操作人账号',
  `realname` varchar(64) DEFAULT NULL COMMENT '操作人姓名',
  `ip` varchar(64) DEFAULT NULL COMMENT 'IP',
  `request_url` varchar(256) DEFAULT NULL COMMENT '请求路径',
  `method` varchar(256) DEFAULT NULL COMMENT '请求类名+方法名',
  `request_param` varchar(1024) DEFAULT NULL COMMENT '请求参数',
  `request_time` datetime DEFAULT NULL COMMENT '操作时间',
  `response_param` varchar(2048) DEFAULT NULL COMMENT '返回参数',
  `response_time` datetime DEFAULT NULL COMMENT '响应时间',
  `cost_time` int(10) DEFAULT NULL COMMENT '耗时',
  `log_type` int(2) DEFAULT NULL COMMENT '日志类型（1 全局操作日志，2 自定义操作日志）',
  `operate_type` int(2) DEFAULT NULL COMMENT '操作类型（枚举类：OperateLogEnum）',
  `log_status` int(2) DEFAULT NULL COMMENT '日志状态（1：正常日志 -1：异常日志 0:警告日志）',
  `error_log_id` varchar(64) DEFAULT NULL COMMENT '异常日志Id',
  `thread_name` varchar(64) DEFAULT NULL COMMENT '线程号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT=' 操作日志表';

-- ----------------------------
-- Records of operate_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sensitive_operate
-- ----------------------------
DROP TABLE IF EXISTS `sensitive_operate`;
CREATE TABLE `sensitive_operate` (
  `id` varchar(64) NOT NULL COMMENT 'ID',
  `user_id` varchar(64) DEFAULT NULL COMMENT '用户ID',
  `sensitive_type` varchar(10) DEFAULT NULL COMMENT '校验方式',
  `create_time` datetime DEFAULT NULL COMMENT '校验时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='敏感操作表';

-- ----------------------------
-- Records of sensitive_operate
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for server_info
-- ----------------------------
DROP TABLE IF EXISTS `server_info`;
CREATE TABLE `server_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gmt_create` datetime(6) DEFAULT NULL,
  `gmt_modified` datetime(6) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKtk8ytgpl7mpukhnvhbl82kgvy` (`ip`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COMMENT='定时任务-服务器表';

-- ----------------------------
-- Records of server_info
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_business_auth
-- ----------------------------
DROP TABLE IF EXISTS `sign_business_auth`;
CREATE TABLE `sign_business_auth` (
  `id` varchar(64) NOT NULL,
  `business_type` tinyint(1) DEFAULT NULL COMMENT '业务类型，1为签章，2为模板，3为文档，4为业务线',
  `business_type_role` tinyint(1) DEFAULT NULL COMMENT '业务类型角色，具体数值参照枚举类',
  `business_relation_id` varchar(64) DEFAULT NULL COMMENT '业务关联数据id',
  `auth_type` tinyint(1) DEFAULT NULL COMMENT '授权类型，1用户(租户下用户)，2部门，3角色',
  `auth_relation_id` varchar(64) DEFAULT NULL COMMENT '权限关联id，租户下用户id或者部门ID或者角色ID',
  `sys_tenant_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户id',
  `sys_account_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统账号id',
  `sys_user_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户下用户id',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门编码',
  `sys_dept_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门id',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(50) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务数据授权表';

-- ----------------------------
-- Records of sign_business_auth
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_business_auth_permission
-- ----------------------------
DROP TABLE IF EXISTS `sign_business_auth_permission`;
CREATE TABLE `sign_business_auth_permission` (
  `id` varchar(64) NOT NULL,
  `business_type` tinyint(1) DEFAULT NULL COMMENT '业务类型，1为签章，2为模板，3为文档，4为业务线',
  `business_type_role` tinyint(1) DEFAULT NULL COMMENT '业务类型角色，具体数值参照枚举类',
  `permission_value` varchar(64) DEFAULT NULL COMMENT '业务权限值',
  `permission_code` tinyint(1) DEFAULT NULL COMMENT '业务权限值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务数据权限组表';

-- ----------------------------
-- Records of sign_business_auth_permission
-- ----------------------------
BEGIN;
INSERT INTO `sign_business_auth_permission` (`id`, `business_type`, `business_type_role`, `permission_value`, `permission_code`) VALUES ('admin_01', 1, 1, '印章新增或保存', 1);
INSERT INTO `sign_business_auth_permission` (`id`, `business_type`, `business_type_role`, `permission_value`, `permission_code`) VALUES ('admin_02', 1, 1, '印章编辑', 2);
INSERT INTO `sign_business_auth_permission` (`id`, `business_type`, `business_type_role`, `permission_value`, `permission_code`) VALUES ('admin_03', 1, 1, '印章章面变更', 3);
INSERT INTO `sign_business_auth_permission` (`id`, `business_type`, `business_type_role`, `permission_value`, `permission_code`) VALUES ('admin_04', 1, 1, '印章启用', 4);
INSERT INTO `sign_business_auth_permission` (`id`, `business_type`, `business_type_role`, `permission_value`, `permission_code`) VALUES ('admin_05', 1, 1, '印章停用', 5);
INSERT INTO `sign_business_auth_permission` (`id`, `business_type`, `business_type_role`, `permission_value`, `permission_code`) VALUES ('admin_06', 1, 1, '印章收缴', 6);
INSERT INTO `sign_business_auth_permission` (`id`, `business_type`, `business_type_role`, `permission_value`, `permission_code`) VALUES ('admin_07', 1, 1, '印章销毁', 7);
COMMIT;

-- ----------------------------
-- Table structure for sign_cert_info
-- ----------------------------
DROP TABLE IF EXISTS `sign_cert_info`;
CREATE TABLE `sign_cert_info` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `sys_tenant_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户id',
  `sys_account_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统账号id',
  `sys_user_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户下用户id',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门编码',
  `sys_dept_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门id',
  `holder_type` tinyint(1) DEFAULT NULL COMMENT '证书所属类型（1、个人；2、企业）',
  `cert_suqe_no` varchar(255) DEFAULT NULL COMMENT '证书序列号',
  `issue_time` datetime DEFAULT NULL COMMENT '证书颁发时间',
  `issue_org` varchar(255) DEFAULT NULL COMMENT '颁发机构',
  `cert_password` varchar(255) DEFAULT NULL COMMENT '证书密码',
  `cert_type` tinyint(1) DEFAULT NULL COMMENT '证书类型（1、云证书）',
  `pfx_path` varchar(255) DEFAULT NULL COMMENT '证书文件地址',
  `algorithm_type` tinyint(1) DEFAULT NULL COMMENT '加密算法（1、RSA；2、SM2）',
  `validate_status` tinyint(1) DEFAULT NULL COMMENT '证书有效状态（1、有效；2、无效）',
  `term_of_validity_start_time` datetime DEFAULT NULL COMMENT '证书有效期起始时间',
  `term_of_validity_end_time` datetime DEFAULT NULL COMMENT '证书有效期结束时间',
  `storage_medium` varchar(255) DEFAULT NULL COMMENT '存储介质',
  `notes` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数字证书信息表';

-- ----------------------------
-- Records of sign_cert_info
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_doc
-- ----------------------------
DROP TABLE IF EXISTS `sign_doc`;
CREATE TABLE `sign_doc` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `sys_tenant_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户id',
  `sys_account_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统账号id',
  `sys_user_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户下用户id',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门编码',
  `sys_dept_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门id',
  `doc_subject` varchar(200) DEFAULT NULL COMMENT '文档主题',
  `business_type` varchar(64) DEFAULT NULL COMMENT '业务类型字典id',
  `scene_type` tinyint(1) DEFAULT NULL COMMENT '用印场景类型（1、加盖电子印章；2、加盖物理印章；）',
  `seal_type` tinyint(1) DEFAULT NULL COMMENT '用章类型',
  `use_count` int(10) DEFAULT NULL COMMENT '用印份数',
  `seal_id` varchar(64) DEFAULT NULL COMMENT '签章id',
  `reason` varchar(500) DEFAULT NULL COMMENT '用印事由',
  `send_dept` varchar(50) DEFAULT NULL COMMENT '发往单位',
  `expire_time` datetime DEFAULT NULL COMMENT '签署截止时间',
  `note` varchar(500) DEFAULT NULL COMMENT '备注',
  `doc_status` tinyint(2) DEFAULT NULL COMMENT '文档状态（1、待发起；2、待重新发起；3、待审批；4、审批未通过；5、待签章；6、签署失败；7、已完成；8、已过期；9、作废）',
  `apply_time` datetime DEFAULT NULL COMMENT '发起时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文档表';

-- ----------------------------
-- Records of sign_doc
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_doc_control
-- ----------------------------
DROP TABLE IF EXISTS `sign_doc_control`;
CREATE TABLE `sign_doc_control` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `sys_tenant_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户id',
  `sys_account_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统账号id',
  `sys_user_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户下用户id',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门编码',
  `sys_dept_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门id',
  `doc_id` varchar(64) DEFAULT NULL COMMENT '关联文档Id',
  `type` varchar(50) DEFAULT NULL COMMENT '控件类型（控件类型，1为签署，2为填写）',
  `name` varchar(50) DEFAULT NULL COMMENT '控件名称',
  `offset_x` varchar(50) DEFAULT NULL COMMENT '控件X坐标(左上角)',
  `offset_y` varchar(50) DEFAULT NULL COMMENT '控件Y坐标(左上角)',
  `width` varchar(50) DEFAULT NULL COMMENT '控件宽度',
  `height` varchar(50) DEFAULT NULL COMMENT '控件高度',
  `page` int(10) DEFAULT NULL COMMENT '控件所属页码',
  `is_filled` tinyint(1) DEFAULT NULL COMMENT '是否已经填充，1为已填充，2为未填写',
  `is_required` tinyint(1) DEFAULT NULL COMMENT '是否为必填项，1为必填项，2为非必填项',
  `font_family` varchar(50) DEFAULT NULL COMMENT '文字字体',
  `text_align` varchar(50) DEFAULT NULL COMMENT '对其方式',
  `font_size` varchar(50) DEFAULT NULL COMMENT '文字大小',
  `placeholder` varchar(50) DEFAULT NULL COMMENT '控件提示值',
  `value` varchar(50) DEFAULT NULL COMMENT '填写值',
  `format` varchar(50) DEFAULT NULL COMMENT '解析格式',
  `page_width` varchar(50) DEFAULT NULL COMMENT '当前文件页面宽度',
  `page_height` varchar(50) DEFAULT NULL COMMENT '当前文件页面高度',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(50) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文档控件表';

-- ----------------------------
-- Records of sign_doc_control
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_doc_image_convert
-- ----------------------------
DROP TABLE IF EXISTS `sign_doc_image_convert`;
CREATE TABLE `sign_doc_image_convert` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `doc_id` varchar(64) DEFAULT NULL COMMENT '关联文档ID',
  `annex_id` varchar(64) DEFAULT NULL COMMENT '文件id',
  `page` int(10) DEFAULT NULL COMMENT '页码',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `delete_flag` int(11) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `sys_tenant_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户id',
  `sys_account_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统账号id',
  `sys_user_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户下用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文档文件图片转换临时记录表';

-- ----------------------------
-- Records of sign_doc_image_convert
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_doc_image_record
-- ----------------------------
DROP TABLE IF EXISTS `sign_doc_image_record`;
CREATE TABLE `sign_doc_image_record` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `doc_id` varchar(64) DEFAULT NULL COMMENT '关联文档ID',
  `doc_record_id` varchar(64) DEFAULT NULL COMMENT '签署记录id',
  `doc_page` int(10) DEFAULT NULL COMMENT '签署文档页数',
  `is_current` tinyint(1) DEFAULT NULL COMMENT '是否为最新签署的，1为是，2为否',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `annex_id` varchar(64) DEFAULT NULL COMMENT '文件id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文档签署图片记录';

-- ----------------------------
-- Records of sign_doc_image_record
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_doc_log_apply
-- ----------------------------
DROP TABLE IF EXISTS `sign_doc_log_apply`;
CREATE TABLE `sign_doc_log_apply` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `sys_tenant_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户id',
  `sys_account_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统账号id',
  `sys_user_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户下用户id',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门编码',
  `sys_dept_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门id',
  `doc_id` varchar(64) DEFAULT NULL COMMENT '文档id',
  `process_instance_id` varchar(64) DEFAULT NULL COMMENT '流程实例id',
  `apply_time` datetime DEFAULT NULL COMMENT '操作时间',
  `apply_status` tinyint(2) DEFAULT NULL COMMENT '文档状态（待发起、待重新发起、待审批、审批未通过、待签章、签署失败、已完成、已过期、作废）',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文档申请日志';

-- ----------------------------
-- Records of sign_doc_log_apply
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_doc_log_error
-- ----------------------------
DROP TABLE IF EXISTS `sign_doc_log_error`;
CREATE TABLE `sign_doc_log_error` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `sys_tenant_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户id',
  `sys_account_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统账号id',
  `sys_user_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户下用户id',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门编码',
  `sys_dept_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门id',
  `doc_id` varchar(64) DEFAULT NULL COMMENT '文档id',
  `process_instance_id` varchar(64) DEFAULT NULL COMMENT '流程实例id',
  `error_type` tinyint(1) DEFAULT NULL COMMENT '异常类型',
  `error_text` varchar(255) DEFAULT NULL COMMENT '异常原因',
  `task_data_id` varchar(64) DEFAULT NULL COMMENT '任务id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文档申请异常日志';

-- ----------------------------
-- Records of sign_doc_log_error
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_doc_log_operate
-- ----------------------------
DROP TABLE IF EXISTS `sign_doc_log_operate`;
CREATE TABLE `sign_doc_log_operate` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `sys_tenant_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户id',
  `sys_account_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统账号id',
  `sys_user_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户下用户id',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门编码',
  `sys_dept_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门id',
  `doc_id` varchar(64) DEFAULT NULL COMMENT '文档id',
  `operate_type` tinyint(2) DEFAULT NULL COMMENT '操作类型',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文档操作日志';

-- ----------------------------
-- Records of sign_doc_log_operate
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_doc_record
-- ----------------------------
DROP TABLE IF EXISTS `sign_doc_record`;
CREATE TABLE `sign_doc_record` (
  `id` varchar(64) NOT NULL COMMENT '文档签署记录ID',
  `sys_tenant_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户id',
  `sys_account_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统账号id',
  `sys_user_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户下用户id',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门编码',
  `sys_dept_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门id',
  `doc_id` varchar(64) DEFAULT NULL COMMENT '文档id',
  `seal_id` varchar(64) DEFAULT NULL COMMENT '签章id',
  `cert_id` varchar(64) DEFAULT NULL COMMENT '证书id',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  `operate_status` tinyint(1) DEFAULT NULL COMMENT '操作状态,0为草稿，1为填写，2为签署,3拒绝签署',
  `operate_notes` varchar(255) DEFAULT NULL COMMENT '操作备注',
  `validate_type` tinyint(1) DEFAULT NULL COMMENT '意愿校验类型（多选：0、无需校验；1、短信校验；2、扫脸校验）',
  `validate_status` tinyint(1) DEFAULT NULL COMMENT '意愿校验状态，1通过，2未通过',
  `is_current` tinyint(1) DEFAULT NULL COMMENT '是否为最新签署的，1为是，2为否',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `annex_id` varchar(64) DEFAULT NULL COMMENT '文件id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文档签署记录';

-- ----------------------------
-- Records of sign_doc_record
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_ent_seal
-- ----------------------------
DROP TABLE IF EXISTS `sign_ent_seal`;
CREATE TABLE `sign_ent_seal` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `sys_tenant_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户id',
  `sys_account_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统账号id',
  `sys_user_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户下用户id',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门编码',
  `sys_dept_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门id',
  `admin_id` varchar(64) DEFAULT NULL COMMENT '印章管理员id，印章所属系统租户下用户id',
  `seal_name` varchar(100) DEFAULT NULL COMMENT '印章名称',
  `seal_type` tinyint(1) DEFAULT NULL COMMENT '印章类型（1、公章；2、财务专用章；3、合同专用章；4、人事专用章；5、其他）',
  `seal_style` tinyint(1) DEFAULT NULL COMMENT '印章样式（1、公章；2、圆形章；3、椭圆形；）',
  `create_type` tinyint(1) DEFAULT NULL COMMENT '创建类型（1、模板创建；2、上传创建）',
  `seal_status` tinyint(1) DEFAULT NULL COMMENT '印章状态（1、制作中；2、制作失败；3、已停用；4、已启用；5、已收缴；6、已销毁）',
  `color` tinyint(1) DEFAULT NULL COMMENT '颜色(1、红色；2、蓝色；3、黑色)',
  `top_text` varchar(255) DEFAULT NULL COMMENT '上排环绕文字',
  `middle_text` varchar(255) DEFAULT NULL COMMENT '横排文字',
  `bottom_text` varchar(255) DEFAULT NULL COMMENT '下弦文',
  `description` varchar(255) DEFAULT NULL COMMENT '用途说明',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '1有效，2失效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='印章信息表';

-- ----------------------------
-- Records of sign_ent_seal
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_ent_seal_log_apply
-- ----------------------------
DROP TABLE IF EXISTS `sign_ent_seal_log_apply`;
CREATE TABLE `sign_ent_seal_log_apply` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `sys_tenant_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户id',
  `sys_account_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统账号id',
  `sys_user_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户下用户id',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门编码',
  `sys_dept_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门id',
  `admin_id` varchar(64) DEFAULT NULL COMMENT '印章管理员id，印章所属系统租户下用户id',
  `seal_name` varchar(100) DEFAULT NULL COMMENT '印章名称',
  `seal_type` tinyint(1) DEFAULT NULL COMMENT '印章类型（1、公章；2、财务专用章；3、合同专用章；4、人事专用章；5、其他）',
  `seal_style` tinyint(1) DEFAULT NULL COMMENT '印章样式（1、公章；2、圆形章；3、椭圆形；）',
  `create_type` tinyint(1) DEFAULT NULL COMMENT '创建类型（1、模板创建；2、上传创建）',
  `color` tinyint(1) DEFAULT NULL COMMENT '颜色(1、红色；2、蓝色；3、黑色)',
  `top_text` varchar(255) DEFAULT NULL COMMENT '上排环绕文字',
  `middle_text` varchar(255) DEFAULT NULL COMMENT '横排文字',
  `bottom_text` varchar(255) DEFAULT NULL COMMENT '下弦文',
  `description` varchar(255) DEFAULT NULL COMMENT '用途说明',
  `seal_id` varchar(64) DEFAULT NULL COMMENT '印章id',
  `operate_type` tinyint(1) DEFAULT NULL COMMENT '申请操作类型,1、制作，2、编辑，3、章面变更，4、停用，5、激活，6、收缴，7销毁',
  `apply_status` tinyint(1) DEFAULT NULL COMMENT '申请状态（1、待提交，2、待重新提交，3、待审批，4、审批未通过，5、审批通过，6、作废）',
  `process_instance_id` varchar(64) DEFAULT NULL COMMENT '流程实例id',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `reason` varchar(255) DEFAULT NULL COMMENT '停用原因',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='企业印章申请日志';

-- ----------------------------
-- Records of sign_ent_seal_log_apply
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_ent_seal_log_error
-- ----------------------------
DROP TABLE IF EXISTS `sign_ent_seal_log_error`;
CREATE TABLE `sign_ent_seal_log_error` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `sys_tenant_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户id',
  `sys_account_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统账号id',
  `sys_user_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户下用户id',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门编码',
  `sys_dept_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门id',
  `seal_id` varchar(64) DEFAULT NULL COMMENT '企业签章id',
  `seal_log_apply_id` varchar(64) DEFAULT NULL COMMENT '企业签章申请记录id',
  `process_instance_id` varchar(64) DEFAULT NULL COMMENT '流程实例id',
  `error_type` tinyint(1) DEFAULT NULL COMMENT '异常类型',
  `error_text` varchar(255) DEFAULT NULL COMMENT '异常原因',
  `task_data_id` varchar(64) DEFAULT NULL COMMENT '任务id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='企业签章申请异常日志';

-- ----------------------------
-- Records of sign_ent_seal_log_error
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_ent_seal_log_operate
-- ----------------------------
DROP TABLE IF EXISTS `sign_ent_seal_log_operate`;
CREATE TABLE `sign_ent_seal_log_operate` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `sys_tenant_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户id',
  `sys_account_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统账号id',
  `sys_user_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户下用户id',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门编码',
  `sys_dept_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门id',
  `seal_id` varchar(64) DEFAULT NULL COMMENT '印章id',
  `operate_type` tinyint(2) DEFAULT NULL COMMENT '操作类型,1、制作，2、编辑，3、章面变更，4、停用，5、激活，6、收缴，7销毁',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='企业印章操作日志';

-- ----------------------------
-- Records of sign_ent_seal_log_operate
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_person_seal
-- ----------------------------
DROP TABLE IF EXISTS `sign_person_seal`;
CREATE TABLE `sign_person_seal` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `seal_name` varchar(100) DEFAULT NULL COMMENT '签名名称',
  `sys_tenant_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户id',
  `sys_account_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统账号id',
  `sys_user_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户下用户id',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门编码',
  `sys_dept_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门id',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `is_default` tinyint(1) DEFAULT NULL COMMENT '是否为默认，1为默认，2为非默认',
  `status` tinyint(1) DEFAULT NULL COMMENT '1有效，2失效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='个人签名';

-- ----------------------------
-- Records of sign_person_seal
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_re
-- ----------------------------
DROP TABLE IF EXISTS `sign_re`;
CREATE TABLE `sign_re` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `name` varchar(255) DEFAULT NULL COMMENT '业务线名称',
  `code_type` tinyint(1) DEFAULT NULL COMMENT '文件编号生成类型，1自定义、2规则',
  `subject_type` tinyint(1) DEFAULT NULL COMMENT '文件主题生成类型，1自定义，2规则',
  `signer_type` tinyint(1) DEFAULT NULL COMMENT '签署人类型，1自定义、2预设流程',
  `sign_order_type` tinyint(1) DEFAULT NULL COMMENT '签署顺序类型，1有序签署、2无序签署',
  `cced_type` tinyint(1) DEFAULT NULL COMMENT '是否抄送，1为是，2为否',
  `cced_opportunity_type` tinyint(1) DEFAULT NULL COMMENT '抄送时机，1为文件发起时，2为文件签署完成时',
  `internal_ccer_type` tinyint(1) DEFAULT NULL COMMENT '是否支持内部抄送，1为是，2为否',
  `external_ccer_type` tinyint(1) DEFAULT NULL COMMENT '是否支持外部抄送，1为是，2为否',
  `add_ccer_type` tinyint(1) DEFAULT NULL COMMENT '是否允许添加抄送人，1为是，2为否',
  `add_file_type` tinyint(1) DEFAULT NULL COMMENT '是否允许添加签约文件，1为是，2为否',
  `delete_file_type` tinyint(1) DEFAULT NULL COMMENT '是否允许删除签约文件，1为是，2为否',
  `add_annex_type` tinyint(1) DEFAULT NULL COMMENT '是否允许添加附件，1为是，2为否',
  `ca_sign_type` tinyint(1) DEFAULT NULL COMMENT '使用证书签署方式，1使用ca证书，2使用防篡改证书，3不使用证书',
  `before_start_approve_type` tinyint(1) DEFAULT NULL COMMENT '发起前是否有审批，1为是，2为否',
  `before_start_approve_id` varchar(64) DEFAULT NULL COMMENT '发起前审批流程id',
  `before_sign_approve_type` tinyint(1) DEFAULT NULL COMMENT '签署前是否有审批，1为是，2为否',
  `before_sign_approve_id` varchar(64) DEFAULT NULL COMMENT '签署前审批流程id',
  `error_status` tinyint(1) DEFAULT NULL COMMENT '异常状态，1为是，2为否',
  `create_by` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态',
  `sys_tenant_id` varchar(64) DEFAULT NULL COMMENT '系统租户id',
  `sys_account_id` varchar(64) DEFAULT NULL COMMENT '系统账号id',
  `sys_user_id` varchar(64) DEFAULT NULL COMMENT '系统租户下用户id',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '系统部门编码',
  `sys_dept_id` varchar(64) DEFAULT NULL COMMENT '系统部门id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务线配置主表';

-- ----------------------------
-- Records of sign_re
-- ----------------------------
BEGIN;
INSERT INTO `sign_re` (`id`, `name`, `code_type`, `subject_type`, `signer_type`, `sign_order_type`, `cced_type`, `cced_opportunity_type`, `internal_ccer_type`, `external_ccer_type`, `add_ccer_type`, `add_file_type`, `delete_file_type`, `add_annex_type`, `ca_sign_type`, `before_start_approve_type`, `before_start_approve_id`, `before_sign_approve_type`, `before_sign_approve_id`, `error_status`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `status`, `sys_tenant_id`, `sys_account_id`, `sys_user_id`, `sys_org_code`, `sys_dept_id`) VALUES ('1', '业务线名称-测试', 1, 1, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 2, NULL, 2, NULL, 2, NULL, NULL, NULL, NULL, 0, NULL, NULL, 1, NULL, NULL, NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sign_re_auth
-- ----------------------------
DROP TABLE IF EXISTS `sign_re_auth`;
CREATE TABLE `sign_re_auth` (
  `id` varchar(64) NOT NULL,
  `sign_re_id` varchar(64) DEFAULT NULL COMMENT '业务线主表id',
  `auth_type` tinyint(1) DEFAULT NULL COMMENT '权限类型，1管理员，2使用范围，3查看权限，4下载权限',
  `user_type` tinyint(1) DEFAULT NULL COMMENT '用户类型',
  `user_id` varchar(64) DEFAULT NULL COMMENT '用户id',
  `downloader_type` tinyint(1) DEFAULT NULL COMMENT '下载权限类型，1参与人，2查看人，3全部',
  `create_by` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务线权限控制表';

-- ----------------------------
-- Records of sign_re_auth
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_re_ccer
-- ----------------------------
DROP TABLE IF EXISTS `sign_re_ccer`;
CREATE TABLE `sign_re_ccer` (
  `id` varchar(64) NOT NULL,
  `sign_re_id` varchar(64) DEFAULT NULL COMMENT '业务线主表id',
  `ccer_type` tinyint(1) DEFAULT NULL COMMENT '抄送人类型，1内部，2外部',
  `internal_ccer_id` varchar(64) DEFAULT NULL COMMENT '内部抄送人id',
  `external_ccer_name` varchar(64) DEFAULT NULL COMMENT '外部抄送人名称',
  `external_cced_type` tinyint(1) DEFAULT NULL COMMENT '外部抄送人抄送类型，1手机号，2邮箱号',
  `external_cced_value` varchar(255) DEFAULT NULL COMMENT '外部抄送人抄送值',
  `create_by` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务线抄送人表';

-- ----------------------------
-- Records of sign_re_ccer
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_re_doc
-- ----------------------------
DROP TABLE IF EXISTS `sign_re_doc`;
CREATE TABLE `sign_re_doc` (
  `id` varchar(64) NOT NULL,
  `sign_re_id` varchar(64) DEFAULT NULL COMMENT '业务线主表id',
  `doc_type` tinyint(1) DEFAULT NULL COMMENT '文档类型，1上传，2模版',
  `doc_id` varchar(64) DEFAULT NULL COMMENT '关联文档id',
  `create_by` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务线配置-签约文件表\n';

-- ----------------------------
-- Records of sign_re_doc
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_re_doc_control
-- ----------------------------
DROP TABLE IF EXISTS `sign_re_doc_control`;
CREATE TABLE `sign_re_doc_control` (
  `id` varchar(64) NOT NULL,
  `sign_re_id` varchar(64) DEFAULT NULL COMMENT '业务线主表id',
  `sign_re_doc_id` varchar(64) DEFAULT NULL COMMENT '业务线签约文件id',
  `signer_type` tinyint(1) DEFAULT NULL COMMENT '人员类型，1发起方，2接收方',
  `signer_id` varchar(64) DEFAULT NULL COMMENT '签署方id，signerId或者senderId',
  `control_type` varchar(64) DEFAULT NULL COMMENT '控件类型（控件类型，1为签署，2为填写）',
  `error_status` tinyint(1) DEFAULT NULL COMMENT '异常状态，1为是，2为否',
  `name` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '控件名称',
  `offset_x` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '控件X坐标(左上角)',
  `offset_y` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '控件Y坐标(左上角)',
  `width` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '控件宽度',
  `height` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '控件高度',
  `page` int(10) DEFAULT NULL COMMENT '控件所属页码',
  `is_filled` tinyint(1) DEFAULT NULL COMMENT '是否已经填充，1为已填充，2为未填写',
  `is_required` tinyint(1) DEFAULT NULL COMMENT '是否为必填项，1为必填项，2为非必填项',
  `font_family` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '文字字体',
  `text_align` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '对其方式',
  `font_size` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '文字大小',
  `placeholder` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '控件提示值',
  `value` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '填写值',
  `format` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '解析格式',
  `page_width` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '当前文件页面宽度',
  `page_height` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '当前文件页面高度',
  `create_by` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='签署人控件配置表';

-- ----------------------------
-- Records of sign_re_doc_control
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_re_doc_param
-- ----------------------------
DROP TABLE IF EXISTS `sign_re_doc_param`;
CREATE TABLE `sign_re_doc_param` (
  `id` varchar(64) NOT NULL,
  `sign_re_id` varchar(64) DEFAULT NULL COMMENT '业务线主表id',
  `sign_re_doc_id` varchar(64) DEFAULT NULL COMMENT '业务线签约文件id',
  `signer_type` tinyint(1) DEFAULT NULL COMMENT '人员类型，1发起方，2接收方',
  `signer_id` varchar(64) DEFAULT NULL COMMENT '签署方id，signerId或者senderId',
  `interface_param_name` varchar(255) DEFAULT NULL COMMENT '接口参数名称',
  `create_by` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='控件参数关联表';

-- ----------------------------
-- Records of sign_re_doc_param
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_re_folder
-- ----------------------------
DROP TABLE IF EXISTS `sign_re_folder`;
CREATE TABLE `sign_re_folder` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `parent_re_folder_id` varchar(64) DEFAULT NULL COMMENT '业务线父文件夹id',
  `name` varchar(50) DEFAULT NULL COMMENT '业务线文件夹名称',
  `sys_tenant_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户id',
  `sys_account_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统账号id',
  `sys_user_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户下用户id',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门编码',
  `sys_dept_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门id',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='业务线分组表';

-- ----------------------------
-- Records of sign_re_folder
-- ----------------------------
BEGIN;
INSERT INTO `sign_re_folder` (`id`, `parent_re_folder_id`, `name`, `sys_tenant_id`, `sys_account_id`, `sys_user_id`, `sys_org_code`, `sys_dept_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`) VALUES ('05461c69-aff9-4df9-941c-e065955a5b0d', NULL, '22222', 'b91cfa13-285f-47e8-8a88-02423d78cade', 'e9ca23d68d884d4ebb19d07889727dae', '36b83e0e-1d44-489b-a670-18d1c8ed2291', 'A32', NULL, 'admin', '2023-08-25 18:09:56', 'admin', '2023-08-27 17:34:41', 0, NULL, NULL);
INSERT INTO `sign_re_folder` (`id`, `parent_re_folder_id`, `name`, `sys_tenant_id`, `sys_account_id`, `sys_user_id`, `sys_org_code`, `sys_dept_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`) VALUES ('34742e1f-e0a1-4876-9ba0-695e6bcec5cc', '999af5b5-1c67-4946-98b4-4a50e7fea289', '3333222', 'b91cfa13-285f-47e8-8a88-02423d78cade', 'e9ca23d68d884d4ebb19d07889727dae', '36b83e0e-1d44-489b-a670-18d1c8ed2291', 'A32', NULL, 'admin', '2023-08-25 18:14:05', 'admin', '2023-08-27 17:36:06', 0, NULL, NULL);
INSERT INTO `sign_re_folder` (`id`, `parent_re_folder_id`, `name`, `sys_tenant_id`, `sys_account_id`, `sys_user_id`, `sys_org_code`, `sys_dept_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`) VALUES ('4f634839-ea64-40e4-b3ed-81d490edbadb', '', '4444411', 'b91cfa13-285f-47e8-8a88-02423d78cade', 'e9ca23d68d884d4ebb19d07889727dae', '36b83e0e-1d44-489b-a670-18d1c8ed2291', 'A32', NULL, 'admin', '2023-08-27 18:16:07', 'admin', '2023-08-27 18:16:16', 0, NULL, NULL);
INSERT INTO `sign_re_folder` (`id`, `parent_re_folder_id`, `name`, `sys_tenant_id`, `sys_account_id`, `sys_user_id`, `sys_org_code`, `sys_dept_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`) VALUES ('55aa9b77-4276-45da-90dc-454e6552ed38', '', '印章管理模板', '3251a518-c2da-4c6f-be62-a2e935c3bc2e', 'cfe6c0f1-0ca8-4610-af8e-0322bdef72ca', '1646e72e-d677-4d25-b850-0083a3cb54dd', 'A34A01', NULL, '13341081503', '2023-09-22 15:45:39', NULL, NULL, 0, NULL, NULL);
INSERT INTO `sign_re_folder` (`id`, `parent_re_folder_id`, `name`, `sys_tenant_id`, `sys_account_id`, `sys_user_id`, `sys_org_code`, `sys_dept_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`) VALUES ('60af37f4-42c6-4aa8-8b95-b4bc963ada44', '55aa9b77-4276-45da-90dc-454e6552ed38', '公章模板', '3251a518-c2da-4c6f-be62-a2e935c3bc2e', 'cfe6c0f1-0ca8-4610-af8e-0322bdef72ca', '1646e72e-d677-4d25-b850-0083a3cb54dd', 'A34A01', NULL, '13341081503', '2023-09-22 15:46:29', NULL, NULL, 0, NULL, NULL);
INSERT INTO `sign_re_folder` (`id`, `parent_re_folder_id`, `name`, `sys_tenant_id`, `sys_account_id`, `sys_user_id`, `sys_org_code`, `sys_dept_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`) VALUES ('657a00d9-743c-4bc1-86a4-5e22093c857f', '', '测试文件夹222333', 'b91cfa13-285f-47e8-8a88-02423d78cade', 'e9ca23d68d884d4ebb19d07889727dae', '36b83e0e-1d44-489b-a670-18d1c8ed2291', 'A32', NULL, 'admin', '2023-08-25 17:36:30', 'admin', '2023-08-27 17:43:13', 0, NULL, NULL);
INSERT INTO `sign_re_folder` (`id`, `parent_re_folder_id`, `name`, `sys_tenant_id`, `sys_account_id`, `sys_user_id`, `sys_org_code`, `sys_dept_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`) VALUES ('8be07d83-7a73-426c-8de3-7c47c9ac7898', '05461c69-aff9-4df9-941c-e065955a5b0d', '22222211111', 'b91cfa13-285f-47e8-8a88-02423d78cade', 'e9ca23d68d884d4ebb19d07889727dae', '36b83e0e-1d44-489b-a670-18d1c8ed2291', 'A32', NULL, 'admin', '2023-08-25 18:10:14', NULL, NULL, 0, NULL, NULL);
INSERT INTO `sign_re_folder` (`id`, `parent_re_folder_id`, `name`, `sys_tenant_id`, `sys_account_id`, `sys_user_id`, `sys_org_code`, `sys_dept_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`) VALUES ('8caeb809-2daa-4582-a4a6-e37603afb2e5', '', '44444', 'b91cfa13-285f-47e8-8a88-02423d78cade', 'e9ca23d68d884d4ebb19d07889727dae', '36b83e0e-1d44-489b-a670-18d1c8ed2291', 'A32', NULL, 'admin', '2023-08-25 18:15:23', 'admin', '2023-08-25 19:55:37', 1, NULL, NULL);
INSERT INTO `sign_re_folder` (`id`, `parent_re_folder_id`, `name`, `sys_tenant_id`, `sys_account_id`, `sys_user_id`, `sys_org_code`, `sys_dept_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`) VALUES ('999af5b5-1c67-4946-98b4-4a50e7fea289', '', '33333322', 'b91cfa13-285f-47e8-8a88-02423d78cade', 'e9ca23d68d884d4ebb19d07889727dae', '36b83e0e-1d44-489b-a670-18d1c8ed2291', 'A32', NULL, 'admin', '2023-08-25 18:13:54', 'admin', '2023-08-27 18:10:51', 0, NULL, NULL);
INSERT INTO `sign_re_folder` (`id`, `parent_re_folder_id`, `name`, `sys_tenant_id`, `sys_account_id`, `sys_user_id`, `sys_org_code`, `sys_dept_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`) VALUES ('bd482dee-4af7-45a0-8c8a-815729a49759', NULL, '1122', 'b91cfa13-285f-47e8-8a88-02423d78cade', 'e9ca23d68d884d4ebb19d07889727dae', '36b83e0e-1d44-489b-a670-18d1c8ed2291', 'A32', NULL, 'admin', '2023-08-27 18:16:29', 'admin', '2023-08-27 18:18:03', 0, NULL, NULL);
INSERT INTO `sign_re_folder` (`id`, `parent_re_folder_id`, `name`, `sys_tenant_id`, `sys_account_id`, `sys_user_id`, `sys_org_code`, `sys_dept_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`) VALUES ('c276071e-5736-413c-bf26-f4383b1b1eae', NULL, '用印管理模板', '3251a518-c2da-4c6f-be62-a2e935c3bc2e', '63827719-cde6-4adf-96e5-7150531153bf', '9b8655c3-88fd-4b7b-9e2d-0fe8634bc301', 'A34', NULL, '13341081511', '2023-09-25 16:49:05', '13341081511', '2023-09-25 16:49:27', 1, NULL, NULL);
INSERT INTO `sign_re_folder` (`id`, `parent_re_folder_id`, `name`, `sys_tenant_id`, `sys_account_id`, `sys_user_id`, `sys_org_code`, `sys_dept_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`) VALUES ('c6986221-6427-4121-b052-d7b101dfa1da', '34742e1f-e0a1-4876-9ba0-695e6bcec5cc', '111222', 'b91cfa13-285f-47e8-8a88-02423d78cade', 'e9ca23d68d884d4ebb19d07889727dae', '36b83e0e-1d44-489b-a670-18d1c8ed2291', 'A32', NULL, 'admin', '2023-08-25 18:09:38', 'admin', '2023-08-27 17:38:40', 0, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sign_re_folder_relation
-- ----------------------------
DROP TABLE IF EXISTS `sign_re_folder_relation`;
CREATE TABLE `sign_re_folder_relation` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `sign_re_id` varchar(64) DEFAULT NULL COMMENT '业务线主键',
  `sign_re_folder_id` varchar(64) DEFAULT NULL COMMENT '业务线文件夹主键',
  `sys_tenant_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户id',
  `sys_account_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统账号id',
  `sys_user_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户下用户id',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门编码',
  `sys_dept_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门id',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模版文件夹模板关联表';

-- ----------------------------
-- Records of sign_re_folder_relation
-- ----------------------------
BEGIN;
INSERT INTO `sign_re_folder_relation` (`id`, `sign_re_id`, `sign_re_folder_id`, `sys_tenant_id`, `sys_account_id`, `sys_user_id`, `sys_org_code`, `sys_dept_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`) VALUES ('0eae6233-a8b4-495a-820c-cc0b77e2bc01', '193ce869-18ed-4fe2-b883-3a492a36bec5', '999af5b5-1c67-4946-98b4-4a50e7fea289', 'b91cfa13-285f-47e8-8a88-02423d78cade', 'e9ca23d68d884d4ebb19d07889727dae', '36b83e0e-1d44-489b-a670-18d1c8ed2291', 'A32', NULL, 'admin', '2023-08-25 19:41:49', 'admin', '2023-08-27 17:38:28', 1, NULL, NULL);
INSERT INTO `sign_re_folder_relation` (`id`, `sign_re_id`, `sign_re_folder_id`, `sys_tenant_id`, `sys_account_id`, `sys_user_id`, `sys_org_code`, `sys_dept_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`) VALUES ('24c6220e-1b6e-41cb-a910-e328bebd23c9', '193ce869-18ed-4fe2-b883-3a492a36bec5', '05461c69-aff9-4df9-941c-e065955a5b0d', 'b91cfa13-285f-47e8-8a88-02423d78cade', 'e9ca23d68d884d4ebb19d07889727dae', '36b83e0e-1d44-489b-a670-18d1c8ed2291', 'A32', NULL, 'admin', '2023-08-25 19:41:36', 'admin', '2023-08-25 19:41:49', 1, NULL, NULL);
INSERT INTO `sign_re_folder_relation` (`id`, `sign_re_id`, `sign_re_folder_id`, `sys_tenant_id`, `sys_account_id`, `sys_user_id`, `sys_org_code`, `sys_dept_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`) VALUES ('6b35f09e-6f78-484d-b390-e1c4ab984d9d', '193ce869-18ed-4fe2-b883-3a492a36bec5', 'c6986221-6427-4121-b052-d7b101dfa1da', 'b91cfa13-285f-47e8-8a88-02423d78cade', 'e9ca23d68d884d4ebb19d07889727dae', '36b83e0e-1d44-489b-a670-18d1c8ed2291', 'A32', NULL, 'admin', '2023-08-27 17:38:28', NULL, NULL, 0, NULL, NULL);
INSERT INTO `sign_re_folder_relation` (`id`, `sign_re_id`, `sign_re_folder_id`, `sys_tenant_id`, `sys_account_id`, `sys_user_id`, `sys_org_code`, `sys_dept_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`) VALUES ('89197b95-2b3a-43af-af5d-2ba3a551691e', '7ba22b89-7a52-4a3d-89be-f205d851565a', '60af37f4-42c6-4aa8-8b95-b4bc963ada44', '3251a518-c2da-4c6f-be62-a2e935c3bc2e', '63827719-cde6-4adf-96e5-7150531153bf', '9b8655c3-88fd-4b7b-9e2d-0fe8634bc301', 'A34', NULL, '13341081511', '2023-09-22 17:06:52', NULL, NULL, 0, NULL, NULL);
INSERT INTO `sign_re_folder_relation` (`id`, `sign_re_id`, `sign_re_folder_id`, `sys_tenant_id`, `sys_account_id`, `sys_user_id`, `sys_org_code`, `sys_dept_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`) VALUES ('a24dcf2d-6e45-43ef-8224-d20ef47518d9', '193ce869-18ed-4fe2-b883-3a492a36bec5', '999af5b5-1c67-4946-98b4-4a50e7fea289', 'b91cfa13-285f-47e8-8a88-02423d78cade', 'e9ca23d68d884d4ebb19d07889727dae', '36b83e0e-1d44-489b-a670-18d1c8ed2291', 'A32', NULL, 'admin', '2023-08-25 19:41:49', 'admin', '2023-08-27 17:38:28', 1, NULL, NULL);
INSERT INTO `sign_re_folder_relation` (`id`, `sign_re_id`, `sign_re_folder_id`, `sys_tenant_id`, `sys_account_id`, `sys_user_id`, `sys_org_code`, `sys_dept_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`) VALUES ('f6b3141c-e611-43c0-b272-137c2e608f6f', '0104fe61-dac7-4ac9-8b9d-f5c80f150f7c', 'bd482dee-4af7-45a0-8c8a-815729a49759', 'd21fabea-6a17-4264-8644-1426b9d11584', '9c629edb-10f5-40a9-b242-70366a731fb3', '07ea4013-7f6b-4fc9-b005-50a775d52816', 'A36', NULL, '13271928495', '2023-09-21 18:34:54', NULL, NULL, 0, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sign_re_rule
-- ----------------------------
DROP TABLE IF EXISTS `sign_re_rule`;
CREATE TABLE `sign_re_rule` (
  `id` varchar(64) NOT NULL,
  `rule_type` tinyint(1) DEFAULT NULL COMMENT '业务规则类型',
  `rule_generate_id` varchar(64) DEFAULT NULL COMMENT '规则生成id',
  `link` varchar(50) DEFAULT NULL COMMENT '连接字段',
  `sign_re_id` varchar(64) DEFAULT NULL COMMENT '业务线主表id',
  `create_by` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='规则对应表';

-- ----------------------------
-- Records of sign_re_rule
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_re_rule_detail
-- ----------------------------
DROP TABLE IF EXISTS `sign_re_rule_detail`;
CREATE TABLE `sign_re_rule_detail` (
  `id` varchar(64) NOT NULL,
  `rule_id` varchar(64) DEFAULT NULL COMMENT '规则对应表id',
  `content` varchar(50) DEFAULT NULL COMMENT '规则内容',
  `content_type` tinyint(1) DEFAULT NULL COMMENT '规则内容类型',
  `content_length` int(5) DEFAULT NULL COMMENT '规则内容长度',
  `content_order` tinyint(2) DEFAULT NULL COMMENT '顺序',
  `create_by` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='规则详细配置表';

-- ----------------------------
-- Records of sign_re_rule_detail
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_re_sender
-- ----------------------------
DROP TABLE IF EXISTS `sign_re_sender`;
CREATE TABLE `sign_re_sender` (
  `id` varchar(64) NOT NULL,
  `signer_id` varchar(64) DEFAULT NULL COMMENT '签署方主表id',
  `sender_order` tinyint(1) DEFAULT NULL COMMENT '顺序',
  `sneder_type` tinyint(1) DEFAULT NULL COMMENT '发起方类型',
  `sender_name` varchar(20) DEFAULT NULL COMMENT '发起方名称',
  `sender_seal_id` varchar(64) DEFAULT NULL COMMENT '发起方指定签章id',
  `sender_sign_type` tinyint(1) DEFAULT NULL COMMENT '发起方盖章方式，1自动盖章，2指定位置盖章',
  `sender_user_id` varchar(64) DEFAULT NULL COMMENT '发起方id，租户下用户id',
  `create_by` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发起方内部设置';

-- ----------------------------
-- Records of sign_re_sender
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_re_signer
-- ----------------------------
DROP TABLE IF EXISTS `sign_re_signer`;
CREATE TABLE `sign_re_signer` (
  `id` varchar(64) NOT NULL,
  `sign_re_id` varchar(64) DEFAULT NULL COMMENT '业务线主表id',
  `signer_type` tinyint(1) DEFAULT NULL COMMENT '签署方类型，1发起方，2接收方',
  `signer_name` varchar(50) DEFAULT NULL COMMENT '签署方名称',
  `signer_order` tinyint(2) DEFAULT NULL COMMENT '签署方顺序',
  `create_by` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `signer_user_id` varchar(64) DEFAULT NULL COMMENT '签署人租户下用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='签署人主表';

-- ----------------------------
-- Records of sign_re_signer
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_re_temporary
-- ----------------------------
DROP TABLE IF EXISTS `sign_re_temporary`;
CREATE TABLE `sign_re_temporary` (
  `id` varchar(64) NOT NULL,
  `sign_re_id` varchar(64) DEFAULT NULL COMMENT '业务线配置主表id',
  `name` varchar(50) DEFAULT NULL COMMENT '文件名称',
  `annex_id` varchar(64) DEFAULT NULL COMMENT '真实文件id',
  `create_by` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='临时签约文件表';

-- ----------------------------
-- Records of sign_re_temporary
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_re_temporary_image
-- ----------------------------
DROP TABLE IF EXISTS `sign_re_temporary_image`;
CREATE TABLE `sign_re_temporary_image` (
  `id` varchar(64) NOT NULL,
  `temporary_id` varchar(64) DEFAULT NULL COMMENT '临时签约文件id',
  `page` int(3) DEFAULT NULL COMMENT '页码',
  `annex_id` varchar(64) DEFAULT NULL COMMENT '真实文件id',
  `create_by` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='临时签约文件图片表';

-- ----------------------------
-- Records of sign_re_temporary_image
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_ru
-- ----------------------------
DROP TABLE IF EXISTS `sign_ru`;
CREATE TABLE `sign_ru` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `sign_re_id` varchar(64) DEFAULT NULL COMMENT '业务线ID',
  `code` varchar(50) DEFAULT NULL COMMENT '文件编号',
  `subject` varchar(50) DEFAULT NULL COMMENT '文件主题',
  `expire_date` datetime DEFAULT NULL COMMENT '签署截止时间',
  `signer_type` tinyint(1) DEFAULT NULL COMMENT '签署人类型，1自定义、2预设流程',
  `sign_order_type` tinyint(1) DEFAULT NULL COMMENT '签署顺序类型，1有序签署、2无序签署',
  `cced_type` tinyint(1) DEFAULT NULL COMMENT '是否抄送，1为是，2为否',
  `cced_opportunity_type` tinyint(1) DEFAULT NULL COMMENT '抄送时机，1为文件发起时，2为文件签署完成时',
  `internal_ccer_type` tinyint(1) DEFAULT NULL COMMENT '是否支持内部抄送，1为是，2为否',
  `external_ccer_type` tinyint(1) DEFAULT NULL COMMENT '是否支持外部抄送，1为是，2为否',
  `add_ccer_type` tinyint(1) DEFAULT NULL COMMENT '是否允许添加抄送人，1为是，2为否',
  `add_file_type` tinyint(1) DEFAULT NULL COMMENT '是否允许添加签约文件，1为是，2为否',
  `delete_file_type` tinyint(1) DEFAULT NULL COMMENT '是否允许删除签约文件，1为是，2为否',
  `add_annex_type` tinyint(1) DEFAULT NULL COMMENT '是否允许添加附件，1为是，2为否',
  `ca_sign_type` tinyint(1) DEFAULT NULL COMMENT '使用证书签署方式，1使用ca证书，2使用防篡改证书，3不使用证书',
  `before_start_approve_type` tinyint(1) DEFAULT NULL COMMENT '发起前是否有审批，1为是，2为否',
  `before_start_approve_id` varchar(64) DEFAULT NULL COMMENT '发起前审批流程id',
  `before_sign_approve_type` tinyint(1) DEFAULT NULL COMMENT '签署前是否有审批，1为是，2为否',
  `before_sign_approve_id` varchar(64) DEFAULT NULL COMMENT '签署前审批流程id',
  `error_status` tinyint(1) DEFAULT NULL COMMENT '异常状态，1为是，2为否',
  `status` tinyint(2) DEFAULT '1' COMMENT '状态，默认1草稿',
  `create_by` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `sys_tenant_id` varchar(64) DEFAULT NULL COMMENT '系统租户id',
  `sys_account_id` varchar(64) DEFAULT NULL COMMENT '系统账号id',
  `sys_user_id` varchar(64) DEFAULT NULL COMMENT '系统租户下用户id',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '系统部门编码',
  `start_time` datetime DEFAULT NULL COMMENT '发起时间',
  `finish_time` datetime DEFAULT NULL COMMENT '完成时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务线实例主表';

-- ----------------------------
-- Records of sign_ru
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_ru_approve
-- ----------------------------
DROP TABLE IF EXISTS `sign_ru_approve`;
CREATE TABLE `sign_ru_approve` (
  `id` varchar(64) NOT NULL,
  `sign_ru_id` varchar(64) DEFAULT NULL COMMENT '业务线实例主表id',
  `approve_type` tinyint(1) DEFAULT NULL COMMENT '审批类型，1发起前审批，2签署前审批',
  `approve_order` tinyint(1) DEFAULT NULL COMMENT '审批序号',
  `approve_id` varchar(64) DEFAULT NULL COMMENT '审批流id',
  `create_by` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务线实例关联审批流表';

-- ----------------------------
-- Records of sign_ru_approve
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_ru_ccer
-- ----------------------------
DROP TABLE IF EXISTS `sign_ru_ccer`;
CREATE TABLE `sign_ru_ccer` (
  `id` varchar(64) NOT NULL,
  `sign_ru_id` varchar(64) DEFAULT NULL COMMENT '业务线主表id',
  `ccer_add_type` tinyint(1) DEFAULT NULL COMMENT '新增抄送人类型，1业务线配置，2用户新增',
  `ccer_type` tinyint(1) DEFAULT NULL COMMENT '抄送人类型，1内部，2外部',
  `tenant_user_id` varchar(64) DEFAULT NULL COMMENT '租户下用户IDid',
  `external_ccer_name` varchar(64) DEFAULT NULL COMMENT '外部抄送人名称',
  `external_cced_type` tinyint(1) DEFAULT NULL COMMENT '外部抄送人抄送类型，1手机号，2邮箱号',
  `external_cced_value` varchar(255) DEFAULT NULL COMMENT '外部抄送人抄送值',
  `create_by` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务线实例抄送人表';

-- ----------------------------
-- Records of sign_ru_ccer
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_ru_doc
-- ----------------------------
DROP TABLE IF EXISTS `sign_ru_doc`;
CREATE TABLE `sign_ru_doc` (
  `id` varchar(64) NOT NULL,
  `sign_ru_id` varchar(64) DEFAULT NULL COMMENT '业务线实例主表id',
  `doc_type` tinyint(1) DEFAULT NULL COMMENT '文档类型，1上传，2模版',
  `doc_origin_id` varchar(64) DEFAULT NULL COMMENT '签约文档来源id',
  `doc_name` varchar(50) DEFAULT NULL COMMENT '签约文件名称',
  `create_by` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `origin_type` tinyint(1) DEFAULT NULL COMMENT '来源类型，1、业务线，2、自行上传',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务线实例-签约文件表\n';

-- ----------------------------
-- Records of sign_ru_doc
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_ru_doc_control
-- ----------------------------
DROP TABLE IF EXISTS `sign_ru_doc_control`;
CREATE TABLE `sign_ru_doc_control` (
  `id` varchar(64) NOT NULL,
  `sign_ru_id` varchar(64) DEFAULT NULL COMMENT '业务线实例主表id',
  `sign_ru_doc_id` varchar(64) DEFAULT NULL COMMENT '业务线实例签约文件id',
  `signer_type` tinyint(1) DEFAULT NULL COMMENT '人员类型，1发起方，2接收方',
  `signer_id` varchar(64) DEFAULT NULL COMMENT '签署方id，signerId或者senderId',
  `control_type` varchar(64) DEFAULT NULL COMMENT '控件类型（控件类型，1为签署，2为填写）',
  `error_status` tinyint(1) DEFAULT NULL COMMENT '异常状态，1为是，2为否',
  `name` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '控件名称',
  `offset_x` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '控件X坐标(左上角)',
  `offset_y` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '控件Y坐标(左上角)',
  `width` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '控件宽度',
  `height` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '控件高度',
  `page` int(10) DEFAULT NULL COMMENT '控件所属页码',
  `is_filled` tinyint(1) DEFAULT '2' COMMENT '是否已经填充，1为已填充，2为未填写',
  `is_required` tinyint(1) DEFAULT '2' COMMENT '是否为必填项，1为必填项，2为非必填项',
  `font_family` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '文字字体',
  `text_align` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '对其方式',
  `font_size` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '文字大小',
  `placeholder` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '控件提示值',
  `value` varchar(200) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '填写值',
  `format` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '解析格式',
  `page_width` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '当前文件页面宽度',
  `page_height` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '当前文件页面高度',
  `create_by` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `interface_param_name` varchar(50) DEFAULT NULL COMMENT '接口参数名称',
  `origin_type` tinyint(1) DEFAULT NULL COMMENT '来源类型，1、业务线，2、发起时设置、3、操作时设置',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务线实例-签署人控件配置表';

-- ----------------------------
-- Records of sign_ru_doc_control
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_ru_doc_operate
-- ----------------------------
DROP TABLE IF EXISTS `sign_ru_doc_operate`;
CREATE TABLE `sign_ru_doc_operate` (
  `id` varchar(64) NOT NULL,
  `sign_ru_id` varchar(64) DEFAULT NULL COMMENT '业务线实例主表id',
  `doc_id` varchar(64) DEFAULT NULL COMMENT '签约文件主表id',
  `is_current` tinyint(1) DEFAULT NULL COMMENT '是否为最新文件，1为是，2为否',
  `annex_id` varchar(64) DEFAULT NULL COMMENT '真实文件id',
  `create_by` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务线实例-签约文件操作记录表';

-- ----------------------------
-- Records of sign_ru_doc_operate
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_ru_operator
-- ----------------------------
DROP TABLE IF EXISTS `sign_ru_operator`;
CREATE TABLE `sign_ru_operator` (
  `id` varchar(64) NOT NULL,
  `sign_ru_id` varchar(64) DEFAULT NULL COMMENT '业务线实例id',
  `operate_type` tinyint(1) DEFAULT NULL COMMENT '操作类型，1填写，2签署',
  `signer_type` tinyint(1) DEFAULT NULL COMMENT '签署方类型，1发起方，2接收方',
  `signer_id` varchar(64) DEFAULT NULL COMMENT '签署方id，signerId或者senderId',
  `operate_order` tinyint(2) DEFAULT NULL COMMENT '序号',
  `operate_name` varchar(50) DEFAULT NULL COMMENT '操作人名称',
  `operate_external_type` tinyint(1) DEFAULT NULL COMMENT '外部签署人类型，1手机号，2邮箱号',
  `operate_external_value` varchar(255) DEFAULT NULL COMMENT '外部签署人接受值',
  `operate_user_id` varchar(64) DEFAULT NULL COMMENT '操作人id，租户下用户id',
  `operate_status` tinyint(1) DEFAULT NULL COMMENT '操作状态，0:不需要, 1:未完成，2:待完成 3：已完成 4:已拒绝\n',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  `create_by` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务线实例-操作人（包括所有签约方、填写方）';

-- ----------------------------
-- Records of sign_ru_operator
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_ru_relation
-- ----------------------------
DROP TABLE IF EXISTS `sign_ru_relation`;
CREATE TABLE `sign_ru_relation` (
  `id` varchar(64) NOT NULL,
  `sign_ru_id` varchar(64) DEFAULT NULL,
  `relation_type` tinyint(1) DEFAULT NULL COMMENT '关联类型,1发起人，2抄送人',
  `tenant_user_id` varchar(64) DEFAULT NULL COMMENT '租户下用户id，发起人id，抄送人id',
  `external_cced_type` varchar(255) DEFAULT NULL COMMENT '外部抄送人抄送类型，1手机号，2邮箱号',
  `external_cced_value` varchar(255) DEFAULT NULL COMMENT '外部抄送人抄送值',
  `create_by` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `task_link_type` varchar(255) DEFAULT NULL COMMENT '节点关联类型:system,bind,register',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='实例关联人表';

-- ----------------------------
-- Records of sign_ru_relation
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_ru_sender
-- ----------------------------
DROP TABLE IF EXISTS `sign_ru_sender`;
CREATE TABLE `sign_ru_sender` (
  `id` varchar(64) NOT NULL,
  `signer_id` varchar(64) DEFAULT NULL COMMENT '签署方主表id',
  `sender_order` tinyint(1) DEFAULT NULL COMMENT '顺序',
  `sender_type` tinyint(1) DEFAULT NULL COMMENT '发起方类型',
  `sender_name` varchar(20) DEFAULT NULL COMMENT '发起方名称',
  `sender_seal_id` varchar(64) DEFAULT NULL COMMENT '发起方指定签章id',
  `sender_sign_type` tinyint(1) DEFAULT NULL COMMENT '发起方盖章方式，1自动盖章，2指定位置盖章',
  `sender_user_id` varchar(64) DEFAULT NULL COMMENT '发起方id，租户下用户id',
  `create_by` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='发起方内部设置';

-- ----------------------------
-- Records of sign_ru_sender
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_ru_signer
-- ----------------------------
DROP TABLE IF EXISTS `sign_ru_signer`;
CREATE TABLE `sign_ru_signer` (
  `id` varchar(64) NOT NULL,
  `sign_ru_id` varchar(64) DEFAULT NULL COMMENT '业务线实例主表id',
  `signer_type` tinyint(1) DEFAULT NULL COMMENT '签署方类型，1发起方，2接收方',
  `signer_name` varchar(50) DEFAULT NULL COMMENT '签署方名称',
  `signer_order` tinyint(2) DEFAULT NULL COMMENT '签署方顺序',
  `signer_external_type` tinyint(1) DEFAULT NULL COMMENT '外部签署人类型，1手机号，2邮箱号',
  `signer_external_value` varchar(255) DEFAULT NULL COMMENT '外部签署人接受值',
  `signer_user_id` varchar(64) DEFAULT NULL COMMENT '签署人租户下用户id',
  `create_by` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `sign_flag` tinyint(1) DEFAULT '1' COMMENT '是否需要签署',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务线实例-签署人主表';

-- ----------------------------
-- Records of sign_ru_signer
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_ru_task
-- ----------------------------
DROP TABLE IF EXISTS `sign_ru_task`;
CREATE TABLE `sign_ru_task` (
  `id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `sign_ru_id` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '流程记录主表ID',
  `task_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '节点类型',
  `task_link_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '节点关联类型:system,bind,register',
  `phone` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '待执行人手机号',
  `email` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '待执行人邮箱',
  `user_type` int(11) DEFAULT NULL COMMENT '待执行人类型：发起人，签署人',
  `user_task_id` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '对应表ID：内部流程配置节点表ID，实例-签约方对应表ID',
  `user_id` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '待执行人账户id',
  `tenant_user_id` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '待执行人租户用户id',
  `tenant_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '待执行人租户id',
  `complete_user_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '办理用户账户ID',
  `complete_tenant_user_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '办理租户用户ID',
  `complete_tenant_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '办理租户ID',
  `task_status` int(10) DEFAULT NULL COMMENT '任务状态：1待办理，2已办理',
  `check_menu_type` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '办理动作类型：approve,reject',
  `check_message` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '办理意见',
  `check_time` datetime DEFAULT NULL COMMENT '办理日期',
  `create_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标识',
  `delete_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '删除用户',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `sys_org_code` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '所属部门',
  `sys_tenant_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户ID',
  `sys_user_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `sys_account_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='任务节点数据详情表';

-- ----------------------------
-- Records of sign_ru_task
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_template
-- ----------------------------
DROP TABLE IF EXISTS `sign_template`;
CREATE TABLE `sign_template` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `sys_tenant_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户id',
  `sys_account_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统账号id',
  `sys_user_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户下用户id',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门编码',
  `sys_dept_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门id',
  `template_code` varchar(200) DEFAULT NULL COMMENT '模板编号',
  `template_name` varchar(200) DEFAULT NULL COMMENT '模板名称',
  `business_type` varchar(64) DEFAULT NULL COMMENT '业务类型字典id',
  `template_type` tinyint(1) DEFAULT NULL COMMENT '模板类型（1、有参数模板；2、无参数模板；）',
  `seal_id` varchar(64) DEFAULT NULL COMMENT '签章id',
  `note` varchar(500) DEFAULT NULL COMMENT '备注',
  `template_status` tinyint(1) DEFAULT NULL COMMENT '模板状态（1、制作中，2、制作失败，3、已停用，4、已启用）',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模板表';

-- ----------------------------
-- Records of sign_template
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_template_control
-- ----------------------------
DROP TABLE IF EXISTS `sign_template_control`;
CREATE TABLE `sign_template_control` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `sys_tenant_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户id',
  `sys_account_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统账号id',
  `sys_user_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户下用户id',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门编码',
  `sys_dept_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门id',
  `template_id` varchar(64) DEFAULT NULL COMMENT '关联模板Id或者模板申请记录id',
  `type` varchar(50) DEFAULT NULL COMMENT '控件类型（控件类型，1为签署，2为填写）',
  `name` varchar(50) DEFAULT NULL COMMENT '控件名称',
  `offset_x` varchar(50) DEFAULT NULL COMMENT '控件X坐标(左上角)',
  `offset_y` varchar(50) DEFAULT NULL COMMENT '控件Y坐标(左上角)',
  `width` varchar(50) DEFAULT NULL COMMENT '控件宽度',
  `height` varchar(50) DEFAULT NULL COMMENT '控件高度',
  `page` int(10) DEFAULT NULL COMMENT '控件所属页码',
  `is_filled` tinyint(1) DEFAULT '2' COMMENT '是否已经填充，1为已填充，2为未填写',
  `is_required` tinyint(1) DEFAULT '2' COMMENT '是否为必填项，1为必填项，2为非必填项',
  `font_family` varchar(50) DEFAULT NULL COMMENT '文字字体',
  `text_align` varchar(50) DEFAULT NULL COMMENT '对其方式',
  `font_size` varchar(50) DEFAULT NULL COMMENT '文字大小',
  `placeholder` varchar(50) DEFAULT NULL COMMENT '控件提示值',
  `value` varchar(50) DEFAULT NULL COMMENT '填写值',
  `format` varchar(50) DEFAULT NULL COMMENT '解析格式',
  `page_width` varchar(50) DEFAULT NULL COMMENT '当前文件页面宽度',
  `page_height` varchar(50) DEFAULT NULL COMMENT '当前文件页面高度',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(50) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `interface_param_name` varchar(50) DEFAULT NULL COMMENT '接口参数名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模板控件表';

-- ----------------------------
-- Records of sign_template_control
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_template_folder
-- ----------------------------
DROP TABLE IF EXISTS `sign_template_folder`;
CREATE TABLE `sign_template_folder` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `parent_template_folder_id` varchar(64) DEFAULT NULL COMMENT '模板父文件夹id',
  `name` varchar(50) DEFAULT NULL COMMENT '模板文件夹名称',
  `sys_tenant_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户id',
  `sys_account_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统账号id',
  `sys_user_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户下用户id',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门编码',
  `sys_dept_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门id',
  `create_by` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模板文件夹表';

-- ----------------------------
-- Records of sign_template_folder
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_template_folder_relation
-- ----------------------------
DROP TABLE IF EXISTS `sign_template_folder_relation`;
CREATE TABLE `sign_template_folder_relation` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `template_id` varchar(64) DEFAULT NULL COMMENT '模板主键',
  `template_folder_id` varchar(64) DEFAULT NULL COMMENT '模板文件夹主键',
  `sys_tenant_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户id',
  `sys_account_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统账号id',
  `sys_user_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户下用户id',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门编码',
  `sys_dept_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门id',
  `create_by` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模版文件夹模板关联表';

-- ----------------------------
-- Records of sign_template_folder_relation
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_template_image_convert
-- ----------------------------
DROP TABLE IF EXISTS `sign_template_image_convert`;
CREATE TABLE `sign_template_image_convert` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `template_id` varchar(64) DEFAULT NULL COMMENT '关联模板ID',
  `annex_id` varchar(64) DEFAULT NULL COMMENT '文件id',
  `page` int(10) DEFAULT NULL COMMENT '页码',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `delete_flag` int(11) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `sys_tenant_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户id',
  `sys_account_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统账号id',
  `sys_user_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户下用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模板文件图片转换临时记录表';

-- ----------------------------
-- Records of sign_template_image_convert
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_template_image_record
-- ----------------------------
DROP TABLE IF EXISTS `sign_template_image_record`;
CREATE TABLE `sign_template_image_record` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `template_id` varchar(64) DEFAULT NULL COMMENT '关联模板ID或者模板申请id',
  `template_record_id` varchar(64) DEFAULT NULL COMMENT '记录id',
  `template_page` int(10) DEFAULT NULL COMMENT '签署文档页数',
  `is_current` tinyint(1) DEFAULT NULL COMMENT '是否为最新签署的，1为是，2为否',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `annex_id` varchar(64) DEFAULT NULL COMMENT '文件id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模板操作图片记录';

-- ----------------------------
-- Records of sign_template_image_record
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_template_log_apply
-- ----------------------------
DROP TABLE IF EXISTS `sign_template_log_apply`;
CREATE TABLE `sign_template_log_apply` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `sys_tenant_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户id',
  `sys_account_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统账号id',
  `sys_user_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户下用户id',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门编码',
  `sys_dept_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门id',
  `template_id` varchar(64) DEFAULT NULL COMMENT '模板id',
  `operate_type` tinyint(1) DEFAULT NULL COMMENT '申请类型',
  `apply_time` datetime DEFAULT NULL COMMENT '申请时间',
  `apply_status` tinyint(1) DEFAULT NULL COMMENT '申请状态（1、待提交，2、待重新提交，3、待审批，4、审批未通过，5、审批通过，6、作废）',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `template_code` varchar(64) DEFAULT NULL COMMENT '模板编号',
  `template_name` varchar(64) DEFAULT NULL COMMENT '模板名称',
  `business_type` varchar(64) DEFAULT NULL COMMENT '业务类型字典id',
  `template_type` tinyint(1) DEFAULT NULL COMMENT '模板类型（1、有参数模板；2、无参数模板；',
  `seal_id` varchar(64) DEFAULT NULL COMMENT '签章id',
  `note` varchar(64) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模板申请日志';

-- ----------------------------
-- Records of sign_template_log_apply
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_template_log_error
-- ----------------------------
DROP TABLE IF EXISTS `sign_template_log_error`;
CREATE TABLE `sign_template_log_error` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `sys_tenant_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户id',
  `sys_account_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统账号id',
  `sys_user_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户下用户id',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门编码',
  `sys_dept_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门id',
  `template_id` varchar(64) DEFAULT NULL COMMENT '模板id',
  `template_log_apply_id` varchar(64) DEFAULT NULL COMMENT '模板申请记录id',
  `process_instance_id` varchar(64) DEFAULT NULL COMMENT '流程实例id',
  `error_type` tinyint(1) DEFAULT NULL COMMENT '异常类型',
  `error_text` varchar(255) DEFAULT NULL COMMENT '异常原因',
  `task_data_id` varchar(64) DEFAULT NULL COMMENT '任务id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模板申请异常日志';

-- ----------------------------
-- Records of sign_template_log_error
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_template_log_operate
-- ----------------------------
DROP TABLE IF EXISTS `sign_template_log_operate`;
CREATE TABLE `sign_template_log_operate` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `sys_tenant_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户id',
  `sys_account_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统账号id',
  `sys_user_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户下用户id',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门编码',
  `sys_dept_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门id',
  `template_id` varchar(64) DEFAULT NULL COMMENT '模板id',
  `operate_type` tinyint(2) DEFAULT NULL COMMENT '操作类型',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模板操作日志';

-- ----------------------------
-- Records of sign_template_log_operate
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_template_log_reference
-- ----------------------------
DROP TABLE IF EXISTS `sign_template_log_reference`;
CREATE TABLE `sign_template_log_reference` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `sys_tenant_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户id',
  `sys_account_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统账号id',
  `sys_user_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户下用户id',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门编码',
  `sys_dept_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门id',
  `template_id` varchar(64) DEFAULT NULL COMMENT '文档id',
  `system_auth_code` varchar(64) DEFAULT NULL COMMENT '系统授权标识',
  `reference_time` datetime DEFAULT NULL COMMENT '引用时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模板引用日志';

-- ----------------------------
-- Records of sign_template_log_reference
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sign_template_record
-- ----------------------------
DROP TABLE IF EXISTS `sign_template_record`;
CREATE TABLE `sign_template_record` (
  `id` varchar(64) NOT NULL COMMENT '主键',
  `sys_tenant_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户id',
  `sys_account_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统账号id',
  `sys_user_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统租户下用户id',
  `sys_org_code` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门编码',
  `sys_dept_id` varchar(64) DEFAULT NULL COMMENT '印章所属系统部门id',
  `template_id` varchar(64) DEFAULT NULL COMMENT '模板id或者模板申请记录id',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  `operate_status` tinyint(1) DEFAULT NULL COMMENT '操作状态,0为草稿，1为填写，2为签署,3拒绝签署',
  `operate_notes` varchar(255) DEFAULT NULL COMMENT '操作备注',
  `validate_type` tinyint(1) DEFAULT NULL COMMENT '意愿校验类型（多选：0、无需校验；1、短信校验；2、扫脸校验）',
  `validate_status` tinyint(1) DEFAULT NULL COMMENT '意愿校验状态，1通过，2未通过',
  `is_current` tinyint(1) DEFAULT NULL COMMENT '是否为最新签署的，1为是，2为否',
  `create_by` varchar(255) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `annex_id` varchar(64) DEFAULT NULL COMMENT '文件id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='模板操作记录';

-- ----------------------------
-- Records of sign_template_record
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_announcement
-- ----------------------------
DROP TABLE IF EXISTS `sys_announcement`;
CREATE TABLE `sys_announcement` (
  `id` varchar(64) NOT NULL,
  `title` varchar(100) DEFAULT NULL COMMENT '标题',
  `announcement_type` varchar(64) DEFAULT NULL COMMENT '公告类型',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `priority` varchar(255) DEFAULT NULL COMMENT '优先级（L低，M中，H高）',
  `msg_abstract` text COMMENT '摘要',
  `msg_type` varchar(10) DEFAULT NULL COMMENT '通告对象类型（USER:指定用户，ALL:全体用户）',
  `user_ids` text COMMENT '指定用户',
  `msg_content` longtext COMMENT '内容',
  `sender` varchar(100) DEFAULT NULL COMMENT '发布人',
  `send_status` varchar(10) DEFAULT NULL COMMENT '发布状态（0未发布，1已发布，2已撤销）',
  `send_time` datetime DEFAULT NULL COMMENT '发布时间',
  `cancel_time` datetime DEFAULT NULL COMMENT '撤销时间',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `delete_by` varchar(64) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统通告表';

-- ----------------------------
-- Records of sys_announcement
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_announcement_send
-- ----------------------------
DROP TABLE IF EXISTS `sys_announcement_send`;
CREATE TABLE `sys_announcement_send` (
  `id` varchar(64) DEFAULT NULL,
  `annt_id` varchar(64) DEFAULT NULL COMMENT '通告ID',
  `user_id` varchar(64) DEFAULT NULL COMMENT '用户id',
  `is_read` tinyint(1) DEFAULT NULL COMMENT '阅读状态（0未读，1已读）',
  `read_time` datetime DEFAULT NULL COMMENT '阅读时间',
  `create_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户通告阅读标记表';

-- ----------------------------
-- Records of sys_announcement_send
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_announcement_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_announcement_type`;
CREATE TABLE `sys_announcement_type` (
  `id` varchar(64) NOT NULL,
  `parent_id` varchar(64) DEFAULT NULL COMMENT '父ID',
  `type_name` varchar(10) DEFAULT NULL COMMENT '类型名称',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='系统公告分类表';

-- ----------------------------
-- Records of sys_announcement_type
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_app_info
-- ----------------------------
DROP TABLE IF EXISTS `sys_app_info`;
CREATE TABLE `sys_app_info` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `app_name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `app_code` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'code',
  `app_icon_address` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'icon地址',
  `app_icon` varchar(10000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '类型',
  `app_type` int(3) NOT NULL DEFAULT '0' COMMENT '类型：1团体 2个人 3不限制',
  `app_status` int(11) NOT NULL DEFAULT '1' COMMENT '状态',
  `app_desc` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述',
  `app_address` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '应用地址',
  `app_front` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '首页',
  `default_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否默认应用：1是0否',
  `useful` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否全员可见',
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  `delete_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户-应用详情表';

-- ----------------------------
-- Records of sys_app_info
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_app_version
-- ----------------------------
DROP TABLE IF EXISTS `sys_app_version`;
CREATE TABLE `sys_app_version` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `app_info_id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '所属app',
  `version_name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '版本名称',
  `version_code` varchar(32) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '版本code',
  `version_status` int(3) NOT NULL DEFAULT '1' COMMENT '状态',
  `version_type` int(3) NOT NULL DEFAULT '0' COMMENT '类型：1团体 2个人 3不限制',
  `version_desc` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '版本描述',
  `default_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否默认初始化权限1是0否',
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  `delete_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户-应用版本表';

-- ----------------------------
-- Records of sys_app_version
-- ----------------------------
BEGIN;
INSERT INTO `sys_app_version` (`id`, `app_info_id`, `version_name`, `version_code`, `version_status`, `version_type`, `version_desc`, `default_flag`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('2e1f31de-651b-49da-a89c-03f6b5daa9be', '70588803-52e4-433d-a61f-0a68e1febd72', '标准版V1.0', 'business_v1.0', 1, 1, NULL, 1, 0, NULL, NULL, 'admin', '2023-12-13 17:29:57', 'admin', '2023-12-13 17:30:00');
INSERT INTO `sys_app_version` (`id`, `app_info_id`, `version_name`, `version_code`, `version_status`, `version_type`, `version_desc`, `default_flag`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('425865a4-c1bd-4ead-8271-0a25e54da6d0', '490489ab-d8b4-414c-ad77-d856962c286f', '个人版本V1.0', 'person_v1.0', 1, 2, NULL, 1, 0, NULL, NULL, 'admin', '2023-12-13 17:18:36', 'admin', '2023-12-13 17:22:50');
INSERT INTO `sys_app_version` (`id`, `app_info_id`, `version_name`, `version_code`, `version_status`, `version_type`, `version_desc`, `default_flag`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('67265942-ca4a-41c9-be25-e9c85a95a686', '490489ab-d8b4-414c-ad77-d856962c286f', '企业版本V1.0', 'enterprise_v1.0', 1, 1, NULL, 1, 0, NULL, NULL, 'admin', '2023-12-13 17:20:08', 'admin', '2023-12-13 17:22:51');
INSERT INTO `sys_app_version` (`id`, `app_info_id`, `version_name`, `version_code`, `version_status`, `version_type`, `version_desc`, `default_flag`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', '13131313', '标准版V1.0', 'tenant_v1.0', 1, 1, NULL, 1, 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', 'admin', '2023-12-13 17:27:53');
INSERT INTO `sys_app_version` (`id`, `app_info_id`, `version_name`, `version_code`, `version_status`, `version_type`, `version_desc`, `default_flag`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '313123131', '标准版V1.0', 'dev_v1.0', 1, 1, NULL, 1, 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', '13300000001', '2023-12-14 10:40:40');
COMMIT;

-- ----------------------------
-- Table structure for sys_app_version_group
-- ----------------------------
DROP TABLE IF EXISTS `sys_app_version_group`;
CREATE TABLE `sys_app_version_group` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `app_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'appid',
  `app_version_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '应用版本ID',
  `group_name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `group_desc` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述',
  `use_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '可用标识',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  `delete_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `init_flag` tinyint(1) DEFAULT NULL COMMENT '是否需要初始化给基础版本',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户-应用版本对应权限组表';

-- ----------------------------
-- Records of sys_app_version_group
-- ----------------------------
BEGIN;
INSERT INTO `sys_app_version_group` (`id`, `app_id`, `app_version_id`, `group_name`, `group_desc`, `use_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `init_flag`) VALUES ('1e80284a-ec81-4c56-b3c8-ced69a0d21fa', '490489ab-d8b4-414c-ad77-d856962c286f', '425865a4-c1bd-4ead-8271-0a25e54da6d0', '基础权限', '个人版本基础权限', 1, 'admin', '2023-12-13 17:21:43', 'admin', '2023-12-13 17:22:53', 0, NULL, NULL, 1);
INSERT INTO `sys_app_version_group` (`id`, `app_id`, `app_version_id`, `group_name`, `group_desc`, `use_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `init_flag`) VALUES ('ec8b30a7-d887-47a9-b7c2-d094b0eda350', '490489ab-d8b4-414c-ad77-d856962c286f', '67265942-ca4a-41c9-be25-e9c85a95a686', '基础权限', '企业用户基础权限', 1, 'admin', '2023-12-13 17:22:30', 'admin', '2023-12-13 17:22:54', 0, NULL, NULL, 1);
COMMIT;

-- ----------------------------
-- Table structure for sys_app_version_group_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_app_version_group_permission`;
CREATE TABLE `sys_app_version_group_permission` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `group_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '权限组ID',
  `permission_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '菜单id',
  `permission_perms` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '菜单权限ID',
  `permission_data_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '权限策略id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户-应用版本权限组对应权限表';

-- ----------------------------
-- Records of sys_app_version_group_permission
-- ----------------------------
BEGIN;
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('03d118ca-6b99-4a89-9246-b2b8f1fd74c8', 'ec8b30a7-d887-47a9-b7c2-d094b0eda350', '222da7d5-af33-4f5f-822a-92a414aa2e76', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('0f6efbc0-8c08-4945-a006-593b1c6afbe2', '94916fa7-ca18-49f0-8933-56bc3fb10e43', 'c7c78143-0eb4-411d-a91c-4b52686e0247', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('1220f709-5060-42d8-84cb-5f1d088b79c7', '5f26489f-09d9-4737-ba6e-139af900f6b3', 'de81ec9b-f423-415a-a7c6-d72da9714e7d', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('1dc342db-efd7-4336-96d0-1044f82c3d1c', 'e73d8c4c-dd26-4ded-952b-3decbae004eb', '00636191-c944-424f-9a81-f0a6a70dbea8', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('1ee471f3-edeb-47c8-96ab-18577b412a27', '1e80284a-ec81-4c56-b3c8-ced69a0d21fa', 'b4ed03ce-5cb2-4c3a-b152-9137c81058f4', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('219ee9d7-770e-4fd0-8209-e7855a8e7050', '29d626f7-23d5-408e-8fb7-329b06fdd4c2', 'fc8da44b-3a70-4864-a838-6244ad11880f', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('22d2a0b1-d960-4d80-92fa-549cb7ba9458', '47f7588c-d075-43ce-a306-d4b0834bdf8a', 'a1b4cc9a-6525-4946-8081-5f176dfed6e0', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('239036f0-0937-4945-8268-0dc4f2456632', '1e80284a-ec81-4c56-b3c8-ced69a0d21fa', '98e7b2ce-be53-4285-ba5f-4aae2a80c383', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('24f3a8b6-2788-481c-9ea2-7379c6c9a077', '58bfba82-0c3e-402d-a25b-73a869342861', '0dca1d94-3005-4b60-a8eb-b47717128487', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('2c7705ae-1217-4b66-81d8-20393983a20d', 'fa325057-1588-41e1-a954-148a74d5fc4a', '02b907ef-65f1-4e9b-87fb-1099edd1cb06', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('2d6dce5b-ce8f-46ba-a85e-c04c5ce314b0', '29d626f7-23d5-408e-8fb7-329b06fdd4c2', 'e4eee1a4-791e-4f85-9c81-ee85e69c9b23', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('33b0c327-92bf-4189-8085-8080bc3e0c48', 'ec8b30a7-d887-47a9-b7c2-d094b0eda350', '06705052-19d4-4e89-8d78-b7ad5e23e943', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('34f1c0eb-042c-4f73-8ab2-ac55ec3bb8f3', '36db3cbe-21c9-4463-a426-c2ee94c4da90', '7c60ff65-95c8-4cc9-86bd-d0f7da8723c2', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('3aa7e7a0-5d83-4ea0-abd9-7be96d205531', '58bfba82-0c3e-402d-a25b-73a869342861', 'cad8a8ec-c75c-466b-b028-8e32d0e960f2', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('3d4e025e-f059-4b2b-935b-339dcac3831a', 'ec8b30a7-d887-47a9-b7c2-d094b0eda350', '9790d902-372c-4dcd-98d2-0db3db02f95d', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('4107720b-31bc-446e-8256-c4a5c006212e', '1e80284a-ec81-4c56-b3c8-ced69a0d21fa', 'c9f25c08-a442-4bb6-9d02-9526d295fa56', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('41147434-228a-4198-bac5-5ce6dc0ff2b3', '937d45f0-c917-42a5-a95e-427050025a54', 'a1b4cc9a-6525-4946-8081-5f176dfed6e0', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('41e50283-85bf-4211-9bb6-6a8c10eee475', 'fa325057-1588-41e1-a954-148a74d5fc4a', '1271d364-33fe-48c4-b83c-79617520a433', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('43900011-5f29-45c7-bac6-8fa09eb228a4', '47f7588c-d075-43ce-a306-d4b0834bdf8a', '5771c6b2-2c66-476c-9462-d945e3eea2a9', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('468d003a-3f28-4a40-8889-6ae058a3936e', '36db3cbe-21c9-4463-a426-c2ee94c4da90', 'eb307448-927f-48e2-b66b-73726fdb2943', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('47597cf1-d149-43e0-b999-3eef18ac381f', '47f7588c-d075-43ce-a306-d4b0834bdf8a', '7c60ff65-95c8-4cc9-86bd-d0f7da8723c2', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('4e5d0771-f129-4d36-aa0b-6ee65ac210d6', '1e80284a-ec81-4c56-b3c8-ced69a0d21fa', 'f3c931cb-5955-495c-9b9a-d578cebefca2', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('52406c07-52b3-4e6d-87b7-fa81b2052988', '1e80284a-ec81-4c56-b3c8-ced69a0d21fa', '222da7d5-af33-4f5f-822a-92a414aa2e76', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('54fbe682-847a-4628-a4c4-2b2f13300bbe', '36db3cbe-21c9-4463-a426-c2ee94c4da90', '5771c6b2-2c66-476c-9462-d945e3eea2a9', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('5d05503e-beb4-4952-a336-0afe6c9ff0b0', '6c3e5ff8-0b51-4340-93c0-c5ea0b69123d', '77b94c28-59ba-4c89-95cd-1eea7d61f49d', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('63afe8a2-8372-416d-9cb4-5d9dcefdfc81', '6c3e5ff8-0b51-4340-93c0-c5ea0b69123d', 'ce84ec5b-5b5d-4ea9-ae08-c180866f5f9a', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('658c0100-5101-480d-83b1-fd72c79b68bd', 'ec8b30a7-d887-47a9-b7c2-d094b0eda350', '1acee96b-1927-42fc-8324-a4d0c05632cf', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('67d34f27-2187-4ebd-8d8e-382b907dd63b', '1e80284a-ec81-4c56-b3c8-ced69a0d21fa', '06705052-19d4-4e89-8d78-b7ad5e23e943', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('6c0154be-80b4-402e-b6cf-b47d078407d1', '29d626f7-23d5-408e-8fb7-329b06fdd4c2', '811b7378-11b2-43d3-aa04-cfb24f679850', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('6cd7530a-a2c5-42de-a139-dabb6fdcb3bf', 'fa325057-1588-41e1-a954-148a74d5fc4a', '1271d364-33fe-48c4-b83c-79617520a433', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('6e16cd30-696d-442f-b1e7-e62204d193f6', '36db3cbe-21c9-4463-a426-c2ee94c4da90', '9b744648-be9f-4ec6-815c-4dc751dc3b12', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('6f4c6535-1d46-4ed0-b9ad-9b9f6bfcd39e', '5f7a82d0-1a44-4b4c-b5c4-b3944aa99b29', '3cfbc6c1-38af-46fe-a0e5-fa7d9b83fc62', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('7188b5a4-4d14-4688-8e2d-9901be258ce0', '7df7387d-f0ea-4638-8daa-32234db0bc9d', 'ce84ec5b-5b5d-4ea9-ae08-c180866f5f9a', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('71e9b7e5-5716-45cd-b214-0ebc28ecff3a', 'ec8b30a7-d887-47a9-b7c2-d094b0eda350', '929dfa20-8337-4cee-afe5-b7827ccc2a6d', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('724b5dac-988d-491a-bd42-4458862509a4', 'ec8b30a7-d887-47a9-b7c2-d094b0eda350', 'f3c931cb-5955-495c-9b9a-d578cebefca2', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('76141bf3-3e38-4e50-936e-aa03913659d4', '43df8d34-c97e-42ac-9ff1-231cbb782e07', '2e045ba8-ae2c-49d8-bf8c-bc1b92c11b3e', NULL, '2445476d-cc2b-4f8b-aaa4-93cc67dd6f50');
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('76903a6d-73da-424c-98fe-619d312faa65', '47f7588c-d075-43ce-a306-d4b0834bdf8a', 'f5dcde63-0bdb-4fd9-ab62-5aa36c3eb1f4', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('76a53514-4d35-4dd5-9c56-0e938091a533', '58bfba82-0c3e-402d-a25b-73a869342861', '80768f3d-ecd5-4fe2-a5de-d2f87a443c58', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('76e8f378-906c-4893-8160-a6dbb8b4275f', '1e80284a-ec81-4c56-b3c8-ced69a0d21fa', '5cd00038-ce3a-4ae6-90d3-7898a33f37f5', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('78048bff-3585-42b9-b0fa-b3f40e948df2', '36db3cbe-21c9-4463-a426-c2ee94c4da90', 'a1b4cc9a-6525-4946-8081-5f176dfed6e0', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('7953308e-d8cd-48b0-90a0-7ece02bb1ff8', '47f7588c-d075-43ce-a306-d4b0834bdf8a', 'b859dd18-446a-4904-918e-02c883a9df65', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('7cf590d6-8161-4f71-a510-8f821ec77ea3', '5f7a82d0-1a44-4b4c-b5c4-b3944aa99b29', '02b907ef-65f1-4e9b-87fb-1099edd1cb06', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('7e8757f8-cfe3-4764-84ad-08342a21b97b', '1e80284a-ec81-4c56-b3c8-ced69a0d21fa', '1acee96b-1927-42fc-8324-a4d0c05632cf', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('854e48f2-d7f1-4a61-bb13-109a83d4a068', '43df8d34-c97e-42ac-9ff1-231cbb782e07', '2e045ba8-ae2c-49d8-bf8c-bc1b92c11b3e', NULL, 'abb87ede-5ada-4a61-a929-34d84075b1d4');
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('856f3541-7dfe-4fea-9b87-ba40d1b2d525', '36db3cbe-21c9-4463-a426-c2ee94c4da90', '8c0a2d92-3e55-4ee1-bf28-64a227b4a266', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('88fe3a72-2ca1-4a5e-a7a7-2a660cd3ec5c', '1e80284a-ec81-4c56-b3c8-ced69a0d21fa', '929dfa20-8337-4cee-afe5-b7827ccc2a6d', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('8b4c4466-0315-4cef-b694-ae5909b04173', 'ec8b30a7-d887-47a9-b7c2-d094b0eda350', 'b4ed03ce-5cb2-4c3a-b152-9137c81058f4', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('9652ef53-1f1e-47a1-9f2d-cb4fedcab97f', '5f26489f-09d9-4737-ba6e-139af900f6b3', 'fea5091b-da9a-467b-8430-0da02548adbcd', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('98d26504-9d34-4dd2-b6c1-5e1ae0c3c936', '36db3cbe-21c9-4463-a426-c2ee94c4da90', '3b2649a2-888d-411c-99f0-61efd5fb32e8', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('9ef34e53-d284-4fc6-99bb-05c499504a88', '1e80284a-ec81-4c56-b3c8-ced69a0d21fa', '4f0121fb-328f-4061-a1e2-efa412262a81', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('a0e6034c-3a08-4153-b11f-bfd8a3a61303', '47f7588c-d075-43ce-a306-d4b0834bdf8a', '8dae1531-bc6f-4b59-a275-fb15282cfef1', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('a40f1e0c-f7b9-437f-ba4c-726541693028', '47f7588c-d075-43ce-a306-d4b0834bdf8a', '9b744648-be9f-4ec6-815c-4dc751dc3b12', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('a4bb034d-d3df-460e-8ee7-34e4fb226e76', 'fa325057-1588-41e1-a954-148a74d5fc4a', '3cfbc6c1-38af-46fe-a0e5-fa7d9b83fc62', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('a5cf0770-33a5-4caf-b61f-26c5711c613f', '1e80284a-ec81-4c56-b3c8-ced69a0d21fa', '4a150a15-fe3a-4534-97c3-ba89587eb886', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('a60c5a13-ba97-43f6-b55a-997a24fcaeb8', '58bfba82-0c3e-402d-a25b-73a869342861', '3cfbc6c1-38af-46fe-a0e5-fa7d9b83fc62', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('a630b7fe-0930-46d6-8a56-e90b4352a621', '94916fa7-ca18-49f0-8933-56bc3fb10e43', '035faa2f-4aad-45ac-a303-988c7a568347', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('a7209d06-d143-41e5-bc07-4ece83961529', 'fa325057-1588-41e1-a954-148a74d5fc4a', '02b907ef-65f1-4e9b-87fb-1099edd1cb06', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('a7a56d7b-3032-4262-a587-e87062e88c93', '29d626f7-23d5-408e-8fb7-329b06fdd4c2', '0ca98386-4d83-408d-896b-fa486e8f61e0', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('a9323be0-81c6-4dc1-8ca8-efce98de674e', '5f26489f-09d9-4737-ba6e-139af900f6b3', '28669186-7902-4262-8c14-b4da242edd67', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('af8264df-0d15-4c38-9d49-d805a79db381', '36db3cbe-21c9-4463-a426-c2ee94c4da90', '8b356af7-f03d-4655-854e-1133a89fdeac', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('b4e495b2-130d-4eda-8a02-8cced0093d46', '36db3cbe-21c9-4463-a426-c2ee94c4da90', 'f5dcde63-0bdb-4fd9-ab62-5aa36c3eb1f4', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('b6404d1a-8194-43e1-8daf-7763c5d6f549', 'ec8b30a7-d887-47a9-b7c2-d094b0eda350', '98e7b2ce-be53-4285-ba5f-4aae2a80c383', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('bc7d1946-e519-459a-ad16-16b9896070bc', '36db3cbe-21c9-4463-a426-c2ee94c4da90', 'b859dd18-446a-4904-918e-02c883a9df65', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('c2a1ef4e-7057-475e-810c-599575abeccd', 'fa325057-1588-41e1-a954-148a74d5fc4a', '3cfbc6c1-38af-46fe-a0e5-fa7d9b83fc62', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('c5314cbd-b57d-4ae5-8495-efeae7bf0438', '43df8d34-c97e-42ac-9ff1-231cbb782e07', '80768f3d-ecd5-4fe2-a5de-d2f87a443c58', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('c5917134-217a-4385-8386-b9a35d08c454', '58bfba82-0c3e-402d-a25b-73a869342861', '1271d364-33fe-48c4-b83c-79617520a433', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('cb1bfe57-72d3-454a-b0e1-f2a69b25f5ef', '7df7387d-f0ea-4638-8daa-32234db0bc9d', 'bed526d3-08f4-480a-b185-d81260bec3a1', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('d240e353-9ef1-4f25-ad89-7622635aa32f', '29d626f7-23d5-408e-8fb7-329b06fdd4c2', '72dac2b0-7ce7-4e5a-88ad-fb6821305dff', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('d4f14bae-98fb-4862-9ea6-6154a07eec1a', 'ec8b30a7-d887-47a9-b7c2-d094b0eda350', 'c9f25c08-a442-4bb6-9d02-9526d295fa56', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('d6868d50-1aa3-4a08-8b4b-03c421c7eb27', '58bfba82-0c3e-402d-a25b-73a869342861', '02b907ef-65f1-4e9b-87fb-1099edd1cb06', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('df0896cf-c60f-4081-a2bf-013306d552fc', '7df7387d-f0ea-4638-8daa-32234db0bc9d', '77b94c28-59ba-4c89-95cd-1eea7d61f49d', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('e09149b6-a13c-4278-9f7a-a7ff3528d3d5', 'ec8b30a7-d887-47a9-b7c2-d094b0eda350', '65df1122-e38c-411f-af09-a334423c5eee', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('e9bc1408-6c65-4c61-9c3e-da5c3f9cd0ae', '94916fa7-ca18-49f0-8933-56bc3fb10e43', '836ca3f0-91ac-4201-8be7-9d444b14f42c', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('ebd22ab4-dcb2-4347-8d2c-e08ad4b4ef56', '1e80284a-ec81-4c56-b3c8-ced69a0d21fa', 'ac7ef3fa-edad-45d7-b796-77827a5fe91b', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('ebebd658-2b94-4aec-ba7d-9bb111efe151', '47f7588c-d075-43ce-a306-d4b0834bdf8a', 'eb307448-927f-48e2-b66b-73726fdb2943', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('ee49c60e-7ba7-43e8-a776-6cff591412b8', '58bfba82-0c3e-402d-a25b-73a869342861', '2a2c89ee-49d9-44e1-82a6-0108af3828fd', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('f199cfa7-f12c-441e-a5e7-e9c5417a6595', '231b19cd-ab3f-4b71-9f32-53b37b5710b4', 'fea5091b-da9a-467b-8430-0da02548adbcd', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('f1a7fe32-d70d-4121-bd4c-4651cc13dba7', '47f7588c-d075-43ce-a306-d4b0834bdf8a', '8b356af7-f03d-4655-854e-1133a89fdeac', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('f42116c3-c996-42c5-b637-ac1c9951e63e', '5f7a82d0-1a44-4b4c-b5c4-b3944aa99b29', '1271d364-33fe-48c4-b83c-79617520a433', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('f4284ffc-424d-45ff-9f08-63ca9410e21e', 'ec8b30a7-d887-47a9-b7c2-d094b0eda350', '5cd00038-ce3a-4ae6-90d3-7898a33f37f5', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('f731cc34-5420-4f0a-9a78-315d25946f14', 'ec8b30a7-d887-47a9-b7c2-d094b0eda350', '4a150a15-fe3a-4534-97c3-ba89587eb886', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('f74f88db-b97b-4c82-b42a-195df477fec4', '1e80284a-ec81-4c56-b3c8-ced69a0d21fa', '9790d902-372c-4dcd-98d2-0db3db02f95d', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('f789fce6-56ab-4b83-b98f-03f338d55415', '29d626f7-23d5-408e-8fb7-329b06fdd4c2', 'e9bee19a-d254-4492-8afb-b658aa864f6a', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('f82ced46-a543-40d5-9e62-715a6cd89981', '36db3cbe-21c9-4463-a426-c2ee94c4da90', '8dae1531-bc6f-4b59-a275-fb15282cfef1', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('f96ee2f1-0fe4-4ee0-b0c4-1389bd0302b6', 'ec8b30a7-d887-47a9-b7c2-d094b0eda350', '4f0121fb-328f-4061-a1e2-efa412262a81', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('fab01f6c-939f-4ecc-96a8-210154a1809e', '43df8d34-c97e-42ac-9ff1-231cbb782e07', '3cfbc6c1-38af-46fe-a0e5-fa7d9b83fc62', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('ffb9967c-91ce-4ef0-876d-eba818d1c78a', '29d626f7-23d5-408e-8fb7-329b06fdd4c2', '0b45502d-b4fe-45f4-9488-0f622c854534', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_app_version_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_app_version_permission`;
CREATE TABLE `sys_app_version_permission` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `app_info_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '应用Id',
  `app_version_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '应用版本ID',
  `permission_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '应用功能ID',
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  `delete_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户-应用版本对应权限表';

-- ----------------------------
-- Records of sys_app_version_permission
-- ----------------------------
BEGIN;
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('00354106-61a7-401a-bace-d0bea0f8d167', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '89d0feab-b72c-4c8d-bb31-702348fc7560', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('0052b1bc-fc74-499d-9304-647a9c4343f4', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', 'f676e451-d0c5-4398-ad22-a9ca0f5d6aaa', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('01cc4137-bc00-4165-a2b4-d0d0ce2dbb7b', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '970c5276-12c1-40a2-b0b4-375540faa804', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('029fa13a-791a-489e-ad6a-b671118f2aaa', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', 'b0f7f7c1-4b6e-4a15-bcd0-3b7453170285', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('02c27967-21b7-4406-82b9-c8d96b66ed7e', '490489ab-d8b4-414c-ad77-d856962c286f', '425865a4-c1bd-4ead-8271-0a25e54da6d0', '1acee96b-1927-42fc-8324-a4d0c05632cf', 0, NULL, NULL, 'admin', '2023-12-13 17:18:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('042413ad-efcb-4b9b-9bb7-424a665f09c5', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', 'c4f29c35-8b8c-4cef-9119-aab9bfd21a5d', 0, NULL, NULL, 'admin', '2023-12-13 17:29:57', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('0539753f-ec63-4952-8ebf-d62a11921850', '490489ab-d8b4-414c-ad77-d856962c286f', '67265942-ca4a-41c9-be25-e9c85a95a686', '5cd00038-ce3a-4ae6-90d3-7898a33f37f5', 0, NULL, NULL, 'admin', '2023-12-13 17:20:08', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('06025e85-fddc-4c1f-be5b-bfc94c75df74', '490489ab-d8b4-414c-ad77-d856962c286f', '425865a4-c1bd-4ead-8271-0a25e54da6d0', 'c9f25c08-a442-4bb6-9d02-9526d295fa56', 0, NULL, NULL, 'admin', '2023-12-13 17:18:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('084b3d60-935b-424a-a85d-61a5e9030ac8', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '21da524a-deee6-4227-bdc7-5cd9dd919be5', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('090ee24c-a117-4a29-9417-35f03224dc6b', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', 'cec8dd67-bc4e-496d-8597-c09dc191395e', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('094ea44f-a582-4495-9e01-3e03f8d1a21b', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '822424f4-f96f-4c1c-95e7-001faf9f4eea', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('0b74f9ce-9d51-4b76-9d36-d6d183a9f63b', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '7c499e02-72a2-46da-b676-bb8fe14f29df', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('0ee9b0a1-5342-4732-88bf-f2118f34117a', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', '3ab5da08-9e72-40d8-a182-77ad93848721', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('0f3ef421-d663-42b7-a765-3a1059a91204', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', 'cd686ddd-a565-4e73-b061-90eaf6c6b333', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1085f4e3-1ff6-40f7-939b-aab6776804f9', '490489ab-d8b4-414c-ad77-d856962c286f', '67265942-ca4a-41c9-be25-e9c85a95a686', '65df1122-e38c-411f-af09-a334423c5eee', 0, NULL, NULL, 'admin', '2023-12-13 17:20:08', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('11689259-b91a-4eb5-a3c9-b061e18765f1', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', '181fb0f9-d6de-4c2f-946c-d80841bf4e5f', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('174b971b-9361-4021-9ec0-81bfed7edfc0', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', '23001cd0-f685-4732-8efc-4a6dfdcc251c', 0, NULL, NULL, 'admin', '2023-12-13 17:29:57', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('17658d6e-b18b-47ef-a4e9-c005cc28b9f7', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '1ecaafa8-2a7e-4fb4-b51e-b3ed641b5b99', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1a551c27-7c74-44df-99ec-e56077e7ed2a', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', 'bdf0166b-a5c1-4719-8d5d-bf3a032170b4', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1b82525b-e02c-476c-a5cb-20c4b852eb4c', '490489ab-d8b4-414c-ad77-d856962c286f', '67265942-ca4a-41c9-be25-e9c85a95a686', '1acee96b-1927-42fc-8324-a4d0c05632cf', 0, NULL, NULL, 'admin', '2023-12-13 17:20:08', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1d3037d9-5755-426d-86a7-78afe0a95428', '490489ab-d8b4-414c-ad77-d856962c286f', '425865a4-c1bd-4ead-8271-0a25e54da6d0', '98e7b2ce-be53-4285-ba5f-4aae2a80c383', 0, NULL, NULL, 'admin', '2023-12-13 17:18:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1dce4e17-3985-41db-824b-8c0e91dfada3', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', '167cee36-ad77-4c29-a8b0-7e9d8e72cc2d', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1f0f2451-2349-4e0e-a0de-c938b50d8b8f', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', 'b0a16615-1931-4fdc-9136-70b6954e57ae', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1f9402da-fa2e-4d82-a75c-320a5a97a45a', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', 'cad8a8ec-c75c-466b-b028-8e32d0e960f2', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('1fd4df30-0f47-4049-b31e-542a0c672179', '490489ab-d8b4-414c-ad77-d856962c286f', '425865a4-c1bd-4ead-8271-0a25e54da6d0', 'ac7ef3fa-edad-45d7-b796-77827a5fe91b', 0, NULL, NULL, 'admin', '2023-12-13 17:18:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('24ef9ec6-9836-48e2-8309-2b607a524448', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '8d4d2216-69b9-4a29-be24-f5096f4841f4', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('25525a4b-d744-4b0e-8f0c-6614fc095c1a', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', 'b4334106-7967-4f9e-9353-7045d21dae09', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('29e8dc80-3034-486c-a142-67c91bb50913', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', 'bbb95924-a8a4-408c-b61e-3a989409b3c6', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('2a02a13d-b8b8-4d65-a2eb-6fad9a71e42d', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', 'a42e3e3f-3609-436e-ae51-da2b01b22d56', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('2b8b7cec-bb91-4871-b159-37bc46431df8', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', '65df1122-e38c-411f-af09-a334423c5ee1', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('304b846a-8bf6-4574-aec2-e669d5e17dd9', '490489ab-d8b4-414c-ad77-d856962c286f', '425865a4-c1bd-4ead-8271-0a25e54da6d0', '06705052-19d4-4e89-8d78-b7ad5e23e943', 0, NULL, NULL, 'admin', '2023-12-13 17:18:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('305eeb24-8383-40c7-b319-cda5d6145e3e', '313123131', '121c06b2-a0e0-430f-bc84-a3747d036f3e', '8d4d2216-69b9-4a29-be24-f5096f4841f4', 0, NULL, NULL, NULL, '2023-12-13 12:25:03', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('3377749e-c997-493d-b247-c9acb50c0ec3', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '21de11a-dc26-4227-bdc7-5cd9dd919be5', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('342760e8-0646-4c61-9a21-a8da75253a64', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', '9a73f770-13ca-450d-80ca-ac50d1de4eee', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('34f8fd42-63cc-4bc6-8ee8-167071a21403', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '12c5210f-6053-4664-8b51-52e0c1443639', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('350546e5-79f8-4060-bdaf-470b83821f1b', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', '0ae1a7c0-9892-48f9-9f0c-1aa28cf6dcf0', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('395ee5a5-911f-4189-8b6c-4df5a1ca0e76', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', '12c5210f-6053-4664-8b51-52e0c1443639', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('3b72c138-7ec8-443c-b320-fd98b1e99619', '490489ab-d8b4-414c-ad77-d856962c286f', '67265942-ca4a-41c9-be25-e9c85a95a686', '9790d902-372c-4dcd-98d2-0db3db02f95d', 0, NULL, NULL, 'admin', '2023-12-13 17:20:08', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('3b7aa475-a889-4eb9-bc41-44934c3ea95e', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '9a7232d1-c819-484c-bff4-2d3c076d2dd5', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('3c25e740-d94c-4f67-a4fe-ad6b3238b2ed', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', '283c45d1-d385-402e-9f76-2a3ae1cacfd3', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('3d140669-fc70-4663-8db8-10dbc7039097', '490489ab-d8b4-414c-ad77-d856962c286f', '425865a4-c1bd-4ead-8271-0a25e54da6d0', '222da7d5-af33-4f5f-822a-92a414aa2e76', 0, NULL, NULL, 'admin', '2023-12-13 17:18:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('3d6acede-5157-4849-a9ca-7f622c0bbd70', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '0ae1a7c0-9892-48f9-9f0c-1aa28cf6dcf0', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('3d723f4d-6fc3-456a-bf97-dab47fa6493b', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', '2a2c89ee-49d9-44e1-82a6-0108af3828fd', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('3e9ff11a-0f53-40a0-b596-15d911b0aa32', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', '8d4d2216-69b9-4a29-be24-f5096f4841f4', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('40f76900-e990-43e3-8b80-1958fdceba49', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', 'dbdcd97e-d02a-4592-930d-81c5adaaa815', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('4189af14-efe2-4559-b412-ae0a486c50c5', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', '66bc7819-1f52-4eb2-83de-ed3d6097bb10', 0, NULL, NULL, 'admin', '2023-12-13 17:29:57', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('42820863-b0e3-49a1-9124-d563b68840b5', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', '2e045ba8-ae2c-49d8-bf8c-bc1b92c11b3e', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('42daa209-c28b-4ab3-a3bc-e086f11dbb35', '490489ab-d8b4-414c-ad77-d856962c286f', '67265942-ca4a-41c9-be25-e9c85a95a686', '98e7b2ce-be53-4285-ba5f-4aae2a80c383', 0, NULL, NULL, 'admin', '2023-12-13 17:20:08', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('43d1b974-38ee-4feb-ae44-abb3d61e0ce2', '490489ab-d8b4-414c-ad77-d856962c286f', '67265942-ca4a-41c9-be25-e9c85a95a686', '929dfa20-8337-4cee-afe5-b7827ccc2a6d', 0, NULL, NULL, 'admin', '2023-12-13 17:20:08', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('44c92db1-1e6a-4677-9dc5-91b675b43803', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', 'de063884-2b7e-4c0e-b27e-34c99280bf28', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('460c95fa-ebe0-47fe-aef3-b40729574a43', '490489ab-d8b4-414c-ad77-d856962c286f', '425865a4-c1bd-4ead-8271-0a25e54da6d0', '929dfa20-8337-4cee-afe5-b7827ccc2a6d', 0, NULL, NULL, 'admin', '2023-12-13 17:18:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('4b09a2a4-fad4-4a47-958b-1f8a29213e55', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', '59bce42f-42b4-43b9-9bb6-4c5854afc607', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('4bc33f20-ee3f-4868-be7d-dc08afcca590', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', '21da524a-dc26-4227-bdc7-5cd9b6919be5', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('4c8d2535-17cf-41ad-8049-4fb90bdee510', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', '0ca98386-4d83-408d-896b-fa486e8f61e0', 0, NULL, NULL, 'admin', '2023-12-13 17:29:57', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('4cd2ea46-129b-4afa-bb25-8ceb19ce79e2', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '283c45d1-d385-402e-9f76-2a3ae1cacfd3', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('4dd3c8b7-7b39-4c9c-a6c1-602177765ede', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', 'b72b8b58-945e-4636-b8bd-e5b7efb9328b', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5104cf27-af34-4143-a93b-e94051b19fc9', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', '3f56a291-b655-45d3-967c-67c760d983e8', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('51086c24-5c13-430b-b555-e9e320eec2ca', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', 'e9bee19a-d254-4492-8afb-b658aa864f6a', 0, NULL, NULL, 'admin', '2023-12-13 17:29:57', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('516c33bd-b829-4d15-bfe6-abd4bcf90f9b', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', '4cea7972-37e8-4976-8bc7-cbd67414048d', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('51b750ec-00d6-47c1-a28c-d9d5d11d3879', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', '0b45502d-b4fe-45f4-9488-0f622c854534', 0, NULL, NULL, 'admin', '2023-12-13 17:29:57', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('51e5c62c-bdb0-4220-9f60-5b86139b96f4', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', '3cfbc6c1-38af-46fe-a0e5-fa7d9b83fc62', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('52d7ca1a-a3cc-4475-80c3-a0123e697336', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', 'e2390176-7732-489e-ba36-7d25452f0368', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5415b34b-07da-4c80-892a-e8f4b87409ff', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', '0d36db88-1c74-484c-aa33-0e0f98e9eff4', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('541d47bb-b894-4f6c-9df3-b8602fe3abf8', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', '268080c8-68c6-4caa-9580-9fd72f704be1', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('541fb399-cf3f-4a2c-8aad-eb64700a57c0', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', 'ee25afe0-0865-4b91-8efa-6c1a4e007002', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('542a2d27-e071-4b05-bab0-5b83b3a9f490', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', '12bf4556-80b8-4b2d-b471-69651ffe5dd4', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('54e5a268-d89a-4cf3-a700-9d53b9792756', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', '1e847cc5-99da-4871-bafc-ff224283573c', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5552ac37-4a9f-4d84-b18f-abbdd98f5c78', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', 'a255f616-815a-4845-b6aa-5fd998dac3a4', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5721bfbf-87cd-4127-be94-959c08a25d78', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '9b596006-7155-4b45-a417-751e85fb29f3', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('57576ef5-5520-4045-b734-6f686d367d4b', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', 'f15b2fb6-d17e-4619-b194-45c5df276350', 0, NULL, NULL, 'admin', '2023-12-13 17:29:57', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('590c3d9e-94f1-4960-b67e-b699a7f1efa1', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', '9a7232d1-c819-484c-bff4-2d3c076d2dd5', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5a80e2dd-f12c-404b-b9de-733ff7e8249b', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', 'b4334106-7967-4f9e-9353-7045d21dae09', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5b33dda5-6160-4290-b7ea-1ff678a9fc2c', '490489ab-d8b4-414c-ad77-d856962c286f', '67265942-ca4a-41c9-be25-e9c85a95a686', 'c9f25c08-a442-4bb6-9d02-9526d295fa56', 0, NULL, NULL, 'admin', '2023-12-13 17:20:08', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5ccb5134-78d8-463c-b2f2-338d5e0d0081', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '21de24a-dc26-4227-bdc7-5cd9dd919be5', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('609d6c77-bd04-4ba7-9e02-4a9293656eaf', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '0921ba27-7910-48b2-ba5f-f74f5134be31', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('6247706b-78cc-429d-930e-117c174779e6', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '164e93b1-461e-4c6a-aec9-cdf0fa984158', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('64ff84ad-774f-44ce-a094-0074bb52c103', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '9058c86f-b402-4cd3-8def-4d05160a13de', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('66b8e01a-6b2a-4fc3-b856-d0294d57fbe4', '490489ab-d8b4-414c-ad77-d856962c286f', '425865a4-c1bd-4ead-8271-0a25e54da6d0', '4f0121fb-328f-4061-a1e2-efa412262a81', 0, NULL, NULL, 'admin', '2023-12-13 17:18:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('67d46e7b-bcd1-45d4-b66e-b59b89eee593', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', '98ed899a-2084-4422-ace0-fc9b0ff9f3e7', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('67f6d9af-2fe4-4ef0-aed7-92548f16d97a', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', '1e39f079-d40a-4489-aac4-afac365e74f2', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('6a0ecf5e-bf8b-4437-aa29-a50efdd2df85', '313123131', '314141441', '8d4d2216-69b9-4a29-be24-f5096f4841f4', 0, NULL, NULL, NULL, '2023-12-13 12:17:54', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('6d207251-a7d1-4eb0-b5e2-b1c3c89e9a17', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '76d89fe9-f673-49f5-a643-8a2696e802ed', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('70aa858c-5aa5-4506-8ff6-29974d70218b', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', '134a2410-1da6-4959-8c81-148e1bc7907a', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('7134ec1a-bd99-4b20-ab90-dc7ea828912c', '490489ab-d8b4-414c-ad77-d856962c286f', '67265942-ca4a-41c9-be25-e9c85a95a686', '4f0121fb-328f-4061-a1e2-efa412262a81', 0, NULL, NULL, 'admin', '2023-12-13 17:20:08', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('71c9a6bf-cee5-4ca1-ae85-aefc5578c8c8', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', '76d89fe9-f673-49f5-a643-8a2696e802ed', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('72fb95d1-e258-46a3-a3b8-ae08de95cf8c', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', '9058c86f-b402-4cd3-8def-4d05160a13de', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('7493952d-d7e0-47ae-9f39-e5eef9a53f88', '490489ab-d8b4-414c-ad77-d856962c286f', '67265942-ca4a-41c9-be25-e9c85a95a686', '06705052-19d4-4e89-8d78-b7ad5e23e943', 0, NULL, NULL, 'admin', '2023-12-13 17:20:08', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('758b55d2-f9f2-43d7-903a-d38af2108d55', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '21de24a-dc26-4227-bss7-5cd9dd919be5', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('7a45233e-e3d0-4c45-a6c9-674e3c10ba81', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', '970c5276-12c1-40a2-b0b4-375540faa804', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('7c054eb6-91fe-4135-91f4-a6fe559ff8d1', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', '164e93b1-461e-4c6a-aec9-cdf0fa984158', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('7c98ec92-db63-4266-a987-6d5398078b2b', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', '6033bf17-2109-4622-afdc-bee2380f3012', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('7f842b03-cac1-45ff-b001-101be464ebcd', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', '9b596006-7155-4b45-a417-751e85fb29f3', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('7fe75674-8b65-4973-8eee-b002a8710477', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', 'a255f616-815a-4845-b6aa-5fd998dac3a2', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('80114204-b220-4f6b-9cd1-faa183f5fc7d', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', '0921ba27-7910-48b2-ba5f-f74f5134be31', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('805352ec-d73c-4e90-bda5-e7c75c146d10', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', 'fc8da44b-3a70-4864-a838-6244ad11880f', 0, NULL, NULL, 'admin', '2023-12-13 17:29:57', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('810bb795-3ca7-49ee-85ff-cf212f333bfc', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '12bf4556-80b8-4b2d-b471-69651ffe5dd4', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('81aa44b3-d4f2-4816-9527-26be8e4378e6', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '167cee36-ad77-4c29-a8b0-7e9d8e72cc2d', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('81c6f5c0-d022-4a0c-952f-e7b0355c3d73', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', 'e3c53f1d-8001-4727-b6b7-f2570df48123', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('847956a4-1cfc-43d5-9567-69a8b4f0f1d1', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', 'f591252e-7651-45bb-9070-dfe4a15d6ea0', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('85275208-f1fd-438e-9da0-79189de1b144', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', '28cb929d-b000-4296-9431-2314ae214add', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('87f538cb-51b9-4136-9ddf-a1649186fb7f', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', 'd0aa8da7-7760-4135-aa80-2c354bbcdf0d', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('8d6435f5-8a9f-42b5-ab19-f967018fa518', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', 'f466f2fe-6e90-4ed4-84ad-ec85a9e5bf97', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('8fd0aa23-a5b3-4bf9-804f-8d97a9941850', '490489ab-d8b4-414c-ad77-d856962c286f', '425865a4-c1bd-4ead-8271-0a25e54da6d0', '5cd00038-ce3a-4ae6-90d3-7898a33f37f5', 0, NULL, NULL, 'admin', '2023-12-13 17:18:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('9249687d-9f70-4e56-bfd1-7cfceb60c7fc', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', '72dac2b0-7ce7-4e5a-88ad-fb6821305dff', 0, NULL, NULL, 'admin', '2023-12-13 17:29:57', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('93555c5b-f636-43c6-8a2d-1984a5d66821', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', 'f466f2fe-6e90-4ed4-84ad-ec85a9e5bf97', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('93f8dd61-4302-4097-a935-63e1d3e39fa4', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', '30e0e37d-6f49-4807-9130-ace0abffc586', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('942553f7-4535-4cd8-8713-22ce4cc1c047', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '0d36db88-1c74-484c-aa33-0e0f98e9eff4', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('955302d8-a4dd-42b1-ae5e-01c6dce0396a', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '28cb929d-b000-4296-9431-2314ae214add', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('97b7f0bb-1b55-4f4f-bcca-18568076d167', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '180d3b1d-15a7-487c-8b8c-feda5442c6db', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('98a58011-3508-495f-8e32-35c1302c2d78', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', '0d41fc8e-99e4-49fe-9046-d46e632273b8', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('992d5e24-d74c-4074-9192-ae0739b9a056', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '21de24a-dc26-4227-bdc7-5cd9ss19be5', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('9c55b79c-1884-4403-a51d-69c9acf3a5b3', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', 'de063884-2b7e-4c0e-b27e-34c99280bf28', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('9c789ebf-3625-48b1-8dfc-5dd46b7bbe99', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '3ab5da08-9e72-40d8-a182-77ad93848721', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('9d26fcec-d4c1-463b-ac05-1ad2dae97f59', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '159c9b22-b5a4-47f3-8e0a-9b29510621ed', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('9f3498ef-8ad1-4027-b4be-ad86370962e5', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', '33958197-8846-44e2-ac0a-98186b9ad4e6', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('a485fd5e-667a-4115-82d5-cd1a578d2daa', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '21daww4a-deee6-4227-bdc7-5cd9dd919be5', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('a494e51f-a352-478e-9e14-7398c2ef63a4', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', '8419dfe7-3860-4d0e-9fd2-29a59412bdc1', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('aa751922-ceb0-47be-9260-b91119a239e4', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '21da114a-deee6-4227-bdc7-5cd9dd919be5', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('aa8a6d2b-2ecc-436a-b6ad-ea90a34ea9e3', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', 'a0a96cd9-6a2d-4cf6-a4f4-60d3ef59035a', 0, NULL, NULL, 'admin', '2023-12-13 17:29:57', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ac8be4d5-e63f-494f-9ed4-e03834bd21de', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', 'd0c2ca72-12da-4730-8293-3d081fe5bb2f', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ada6cf02-d986-4fea-a900-6256c2095686', '490489ab-d8b4-414c-ad77-d856962c286f', '425865a4-c1bd-4ead-8271-0a25e54da6d0', 'f3c931cb-5955-495c-9b9a-d578cebefca2', 0, NULL, NULL, 'admin', '2023-12-13 17:18:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('aeda5f04-1635-4bea-9723-bf93095244ab', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', 'c6ae153e-bbd6-45c6-938d-28e3c516eeb0', 0, NULL, NULL, 'admin', '2023-12-13 17:29:57', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('b10534fb-8e61-464c-88c9-2d1b7a19e83e', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '21da524a-dc26-4227-bdc7-5cd9dd919be5', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('b1a9c0a4-09bd-4de0-b838-4bb78fbb6f23', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', 'a255f616-815a-4845-b6aa-5fd998dac3a4', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('b3919b75-2050-404d-907f-991e925afcd7', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', 'e2390176-7732-489e-ba36-7d25452f0368', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('b56cba12-1c96-4c1c-8de6-ea022aebb7ae', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '0d41fc8e-99e4-49fe-9046-d46e632273b8', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('b641bea3-eac9-4120-8b09-f23630c9322a', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', '811b7378-11b2-43d3-aa04-cfb24f679850', 0, NULL, NULL, 'admin', '2023-12-13 17:29:57', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('b9abddea-f7dc-4e7c-99da-837b75714f74', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', 'b3b95e14-1aa2-4a1d-b373-3fba540b9f3f', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('c02d51a7-a81f-4681-bf09-3a4780ee0662', '490489ab-d8b4-414c-ad77-d856962c286f', '67265942-ca4a-41c9-be25-e9c85a95a686', 'b4ed03ce-5cb2-4c3a-b152-9137c81058f4', 0, NULL, NULL, 'admin', '2023-12-13 17:20:08', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('c1d765f7-56c2-4cf0-9950-daa4717b5816', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', '8fb55b34-66f0-46a2-9842-b20700c6b465', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('c42e851a-e24f-44e0-8e79-64dd9eed4461', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', '0dca1d94-3005-4b60-a8eb-b47717128487', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('c6878894-52ea-43eb-8d6d-4af448e17d10', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '21da524a-deee6-4527-bdc7-5cd9dd919be5', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('c86eff52-6711-4f00-be44-42216664464d', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', 'a255f616-815a-4845-b6aa-5fd998dac3a2', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('cb849113-9764-481b-9c46-73ef1ad0a4e1', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', '95bef937-64ee-4104-ad69-2a3220b45527', 0, NULL, NULL, 'admin', '2023-12-13 17:29:57', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('cb9b131b-b4cb-40e5-9dc4-60ebef81e1e8', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', 'f676e451-d0c5-4398-ad22-a9ca0f5d6aaa', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ce6b262d-8053-44e5-8a14-803bcac585b3', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', 'f9d05ba1-d802-4832-8f75-c0896db62d11', 0, NULL, NULL, 'admin', '2023-12-13 17:29:57', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('d192b164-8a96-4375-bb5b-7e7e09aaca76', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', 'b0a16615-1931-4fdc-9136-70b6954e57ae', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('d1bd0f36-bb23-45f5-8f5f-f4ddc1e1e8af', '490489ab-d8b4-414c-ad77-d856962c286f', '425865a4-c1bd-4ead-8271-0a25e54da6d0', 'b4ed03ce-5cb2-4c3a-b152-9137c81058f4', 0, NULL, NULL, 'admin', '2023-12-13 17:18:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('d482a39e-3c30-41b8-8150-056a2b753638', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', 'ef271138-e07b-40df-bb8d-482c82733e11', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('d49745d8-efe1-4165-bb1d-f48e2b5cd4cd', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', '7c499e02-72a2-46da-b676-bb8fe14f29df', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('d55dae21-20e8-4bba-9b2a-c8f06e056aca', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', 'fe7d832f-a185-4cb8-9ac7-f0cc9f39fe11', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('d5e78393-ba96-4491-929a-7f1a884bf0ad', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '90d5c59b-55b6-4dcd-867c-ec0567667262', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('dad90584-7494-49e2-9b2a-141ddd70014f', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', 'f638163b-ee94-48fe-85cc-b3e519c99a38', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('dbba549d-b68b-46db-ba94-65100717aefa', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', '8abf3229-9b85-4177-bc40-e0552ec8b8de', 0, NULL, NULL, 'admin', '2023-12-13 17:29:57', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('dbfd34a6-0a85-45b8-8333-2a08e30b4314', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '21da524a-dc26-4227-bdc7-5cd9b6919be5', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('df0be4c6-5d5c-4c16-8e81-dfe065fa07cc', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', 'd0aa8da7-7760-4135-aa80-2c354bbcdf0d', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('e56b6cd9-da53-4ca5-8f3d-3b6003cbf8f6', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', 'b72b8b58-945e-4636-b8bd-e5b7efb9328b', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('e81b04cb-2592-4a5c-9dad-8d77396cf145', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', '180d3b1d-15a7-487c-8b8c-feda5442c6db', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('e9171c45-12fd-462a-95ad-c823277e2539', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', 'a2699e8c-dbe9-41c1-8e8a-8c9930ec9fbd', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ea4aade9-2929-4ee6-87d2-860748fd5881', '490489ab-d8b4-414c-ad77-d856962c286f', '425865a4-c1bd-4ead-8271-0a25e54da6d0', '9790d902-372c-4dcd-98d2-0db3db02f95d', 0, NULL, NULL, 'admin', '2023-12-13 17:18:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ebad6677-996b-44da-84f8-cdc453cff8db', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', 'cd686ddd-a565-4e73-b061-90eaf6c6b333', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ebff4c3f-cecb-4785-b8d0-bab5f1980a7a', '490489ab-d8b4-414c-ad77-d856962c286f', '67265942-ca4a-41c9-be25-e9c85a95a686', '4a150a15-fe3a-4534-97c3-ba89587eb886', 0, NULL, NULL, 'admin', '2023-12-13 17:20:08', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ec5151b2-1638-4b2b-b508-34e22d26b291', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', 'e4eee1a4-791e-4f85-9c81-ee85e69c9b23', 0, NULL, NULL, 'admin', '2023-12-13 17:29:57', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ecd785a0-8fc1-464e-b2cc-d8037edb0767', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', '822424f4-f96f-4c1c-95e7-001faf9f4eea', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('eddfb4bf-a55e-4fbd-92dd-53e5d91f20a1', '313123131', '1ba532e3-5d56-4cda-9a93-47bcbb28a5bc', '8d4d2216-69b9-4a29-be24-f5096f4841f4', 0, NULL, NULL, NULL, '2023-12-13 12:21:53', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ee33360f-8ad4-4831-af31-e4e52ee59a4f', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', '89d0feab-b72c-4c8d-bb31-702348fc7560', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f082009b-8309-4cab-b3e1-b971aea7f3fb', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', 'd0c2ca72-12da-4730-8293-3d081fe5bb2f', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f0c12f86-9c59-4096-90d0-2d0a8380d009', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', '47b9c905-6356-48e4-997c-2df8a4f2e49c', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f262e81e-a864-47a8-8562-a6483fbb5a5b', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', '1ecaafa8-2a7e-4fb4-b51e-b3ed641b5b99', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f6210251-9ebc-4228-9066-12dfb829f287', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', '159c9b22-b5a4-47f3-8e0a-9b29510621ed', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f623163f-4391-4f2b-b29f-fc5c3ab7bacd', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '8fb55b34-66f0-46a2-9842-b20700c6b465', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f674ab1f-49c9-417f-8429-73e952e94e1b', '490489ab-d8b4-414c-ad77-d856962c286f', '425865a4-c1bd-4ead-8271-0a25e54da6d0', '4a150a15-fe3a-4534-97c3-ba89587eb886', 0, NULL, NULL, 'admin', '2023-12-13 17:18:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f7f18f11-0afb-443d-95b6-e37ac7d2206d', '490489ab-d8b4-414c-ad77-d856962c286f', '67265942-ca4a-41c9-be25-e9c85a95a686', '222da7d5-af33-4f5f-822a-92a414aa2e76', 0, NULL, NULL, 'admin', '2023-12-13 17:20:08', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f7ff7172-a047-4b43-ae7d-a26aedcd4cda', '313123131', 'f7a0e469-5d70-4ce1-9d2f-c004d633035d', '90d5c59b-55b6-4dcd-867c-ec0567667262', 0, NULL, NULL, 'admin', '2023-12-13 17:32:07', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('fa7d7598-35c2-49b6-a64b-cad6c2730ff0', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '9a73f770-13ca-450d-80ca-ac50d1de4eee', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ff1c2487-e7d7-4890-8e75-e11e3419ec38', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', '72a9b863-e338-4dec-bd2b-a806b65f506c', 0, NULL, NULL, 'admin', '2023-12-13 17:27:37', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('ff46a03f-6f34-4b9d-9c07-f3afeb380440', '490489ab-d8b4-414c-ad77-d856962c286f', '67265942-ca4a-41c9-be25-e9c85a95a686', 'f3c931cb-5955-495c-9b9a-d578cebefca2', 0, NULL, NULL, 'admin', '2023-12-13 17:20:08', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('00354106-61a7-401a-bace-d12a0f8d167', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '123a667c-15d8-48cd-bf42-93cfd039442b', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('00354106-61a7-401a-bace-d0120f8d167', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '63b5dbd8-a773-4ae5-8878-e30fc61a37a8', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('00354106-61a7-401a-bace-d043a0f8d167', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', '3c775f1a-6733-4b7e-b9c7-cc20981b34d7', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('00354106-61a7-401a-bace-d0be44f8d167', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', 'e0d27c1a-516a-4f45-92f3-06ea1a22672b', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('00354106-61a7-401a-bace-d0bea458d167', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', '22ec8ed1-5ad3-4867-a711-850828cb1da4', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('00354106-61a7-401a-bace-d0be87f8d167', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', '84b01339-7754-48c0-b1de-f4520f3e44cf', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('00354106-61a7-401a-bace-d0bea348d167', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', '9159262d-685f-4294-af9b-2894f6523da3', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('00354106-61a7-401a-bace-d0bea079d167', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', 'c57e39c6-3062-4d03-902b-20cf5f01052f', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('00354106-61a7-401a-bace-d0b3458d167', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', 'be2b0983-7dd1-48eb-8bad-7c432da28685', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('00354106-61a7-401a-bace-d0bea0099167', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', '8db4a2eb-1532-40c8-8986-e7c69bdef985', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('00354106-61a7-401a-bace-d0be3348d167', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', 'c477bad3-b7cb-444f-bc29-0de184a4270b', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('00354106-61a7-401a-bace-d0550f8d167', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', '37f98649-c5c3-4eb4-8d24-310ce16d6aec', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('00354106-61a7-401a-bace-d02340f8d167', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', 'e11f6d0a-7ca6-4184-8255-ac050a58a17c', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('00354106-61a7-401a-bace-d023ddf8d167', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', '7699487e-6992-4c9c-864d-d541d0129186', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_article
-- ----------------------------
DROP TABLE IF EXISTS `sys_article`;
CREATE TABLE `sys_article` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `title` varchar(128) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '标题',
  `type_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '分类ID',
  `author` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '作者',
  `summary` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '摘要',
  `content` longtext COLLATE utf8mb4_unicode_ci COMMENT '内容',
  `status` int(11) DEFAULT '0' COMMENT '状态：0草稿 1已发布 2已下架',
  `publish_time` datetime DEFAULT NULL COMMENT '发布日期',
  `up_flag` tinyint(1) DEFAULT '0' COMMENT '0 否 1是',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Records of sys_article
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_article_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_article_type`;
CREATE TABLE `sys_article_type` (
  `id` varchar(64) NOT NULL,
  `type_name` varchar(10) DEFAULT NULL COMMENT '类型名称',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='资讯-分类表';

-- ----------------------------
-- Records of sys_article_type
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_auth_group
-- ----------------------------
DROP TABLE IF EXISTS `sys_auth_group`;
CREATE TABLE `sys_auth_group` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tenant_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户ID',
  `parent_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '父id',
  `group_name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `group_desc` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述',
  `system_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '1:系统 0用户',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  `delete_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `group_type` int(3) DEFAULT '0' COMMENT '类型：0未知   1总权限 2基础权限 3：初始化权限 4、自定义权限',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限组表';

-- ----------------------------
-- Records of sys_auth_group
-- ----------------------------
BEGIN;
INSERT INTO `sys_auth_group` (`id`, `tenant_id`, `parent_id`, `group_name`, `group_desc`, `system_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `group_type`) VALUES ('07b3e2cd-eb1b-4e07-8844-685478909734', 'bf6fbbdd-c424-4cad-b591-9cd5277d2be5', '1394db94-6f0c-41cb-93cb-a9a070c88eb0', '全部权限', '全部权限', 1, '12300000000', '2023-12-14 20:03:50', NULL, NULL, 0, NULL, NULL, 1);
INSERT INTO `sys_auth_group` (`id`, `tenant_id`, `parent_id`, `group_name`, `group_desc`, `system_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `group_type`) VALUES ('1394db94-6f0c-41cb-93cb-a9a070c88eb0', 'bf6fbbdd-c424-4cad-b591-9cd5277d2be5', '', '默认权限组分类', '默认权限组分类', 1, '12300000000', '2023-12-14 20:03:50', NULL, NULL, 0, NULL, NULL, 0);
INSERT INTO `sys_auth_group` (`id`, `tenant_id`, `parent_id`, `group_name`, `group_desc`, `system_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `group_type`) VALUES ('1cd9bf0d-cf27-440e-a38f-0accef16af8d', '3efc0be2-3e09-4b6f-8ac0-0e5ea7065d29', 'a89d8d02-26dc-45d2-91ba-705faf24b54f', '全部权限', '全部权限', 1, NULL, '2023-12-14 19:59:58', NULL, NULL, 0, NULL, NULL, 1);
INSERT INTO `sys_auth_group` (`id`, `tenant_id`, `parent_id`, `group_name`, `group_desc`, `system_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `group_type`) VALUES ('30b88cb7-58b1-4770-9298-7a68ce7c0c5e', '3efc0be2-3e09-4b6f-8ac0-0e5ea7065d29', 'a89d8d02-26dc-45d2-91ba-705faf24b54f', '基础权限', '基础权限', 1, NULL, '2023-12-14 19:59:58', NULL, NULL, 0, NULL, NULL, 2);
INSERT INTO `sys_auth_group` (`id`, `tenant_id`, `parent_id`, `group_name`, `group_desc`, `system_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `group_type`) VALUES ('a89d8d02-26dc-45d2-91ba-705faf24b54f', '3efc0be2-3e09-4b6f-8ac0-0e5ea7065d29', '', '默认权限组分类', '默认权限组分类', 1, NULL, '2023-12-14 19:59:58', NULL, NULL, 0, NULL, NULL, 0);
INSERT INTO `sys_auth_group` (`id`, `tenant_id`, `parent_id`, `group_name`, `group_desc`, `system_flag`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `group_type`) VALUES ('c15216cd-2e7b-4f0a-959a-36c6a41817ba', 'bf6fbbdd-c424-4cad-b591-9cd5277d2be5', '1394db94-6f0c-41cb-93cb-a9a070c88eb0', '基础权限', '基础权限', 1, '12300000000', '2023-12-14 20:03:50', NULL, NULL, 0, NULL, NULL, 2);
COMMIT;

-- ----------------------------
-- Table structure for sys_auth_group_member
-- ----------------------------
DROP TABLE IF EXISTS `sys_auth_group_member`;
CREATE TABLE `sys_auth_group_member` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tenant_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户ID',
  `group_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '权限组id',
  `auth_type` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '权限类型',
  `auth_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '权限id',
  `depart_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '部门Id',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限组对应成员表';

-- ----------------------------
-- Records of sys_auth_group_member
-- ----------------------------
BEGIN;
INSERT INTO `sys_auth_group_member` (`id`, `tenant_id`, `group_id`, `auth_type`, `auth_id`, `depart_id`, `create_by`, `create_time`) VALUES ('010e22b7-a4a8-491a-8ccd-bc7fb75b5f22', 'bf6fbbdd-c424-4cad-b591-9cd5277d2be5', '07b3e2cd-eb1b-4e07-8844-685478909734', 'user', 'be990527-878e-4c30-b0f7-53dde299edb3', NULL, '12300000000', '2023-12-14 20:03:50');
INSERT INTO `sys_auth_group_member` (`id`, `tenant_id`, `group_id`, `auth_type`, `auth_id`, `depart_id`, `create_by`, `create_time`) VALUES ('1448006e-a35c-4516-859f-cfbd0d2cd11e', '3efc0be2-3e09-4b6f-8ac0-0e5ea7065d29', '1cd9bf0d-cf27-440e-a38f-0accef16af8d', 'user', 'be990527-878e-4c30-b0f7-53dde299edb3', NULL, NULL, '2023-12-14 19:59:58');
COMMIT;

-- ----------------------------
-- Table structure for sys_auth_group_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_auth_group_permission`;
CREATE TABLE `sys_auth_group_permission` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `group_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '权限组ID',
  `app_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'appID',
  `permission_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '菜单id',
  `permission_perms` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '菜单权限ID',
  `permission_data_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '权限策略id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限组对应权限表';

-- ----------------------------
-- Records of sys_auth_group_permission
-- ----------------------------
BEGIN;
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('002d5627-e15a-46d7-8d1b-a146a7376387', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', 'c4f29c35-8b8c-4cef-9119-aab9bfd21a5d', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('00be931b-8c04-487a-bdfd-2dcda6f06e6a', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', 'b72b8b58-945e-4636-b8bd-e5b7efb9328b', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('02a7843f-5b35-49d1-8c78-bcf429afd7a5', 'c15216cd-2e7b-4f0a-959a-36c6a41817ba', '490489ab-d8b4-414c-ad77-d856962c286f', '929dfa20-8337-4cee-afe5-b7827ccc2a6d', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('02ae31f3-0eee-44b8-88e4-1c71e7c94b05', '1cd9bf0d-cf27-440e-a38f-0accef16af8d', '490489ab-d8b4-414c-ad77-d856962c286f', '06705052-19d4-4e89-8d78-b7ad5e23e943', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('038acd8f-3aa2-47a3-86bd-3368f77c1ccc', '1cd9bf0d-cf27-440e-a38f-0accef16af8d', '490489ab-d8b4-414c-ad77-d856962c286f', '929dfa20-8337-4cee-afe5-b7827ccc2a6d', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('04440f14-bd7e-4efe-a091-0a83519f112e', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', 'b0a16615-1931-4fdc-9136-70b6954e57ae', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('05509bd0-642d-493f-a888-076a74938869', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '8d4d2216-69b9-4a29-be24-f5096f4841f4', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('065b14e5-c5ea-4f4a-abee-891924c1cb88', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '822424f4-f96f-4c1c-95e7-001faf9f4eea', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('0954f808-a0f7-48a0-b2ba-4ea95eadf0a3', '30b88cb7-58b1-4770-9298-7a68ce7c0c5e', '490489ab-d8b4-414c-ad77-d856962c286f', '1acee96b-1927-42fc-8324-a4d0c05632cf', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('098165a2-4661-418a-a952-c505dfb54bed', 'c15216cd-2e7b-4f0a-959a-36c6a41817ba', '490489ab-d8b4-414c-ad77-d856962c286f', '98e7b2ce-be53-4285-ba5f-4aae2a80c383', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('098978f5-41d3-498e-b115-9e7edc102d67', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '21de11a-dc26-4227-bdc7-5cd9dd919be5', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('0d8e1132-4f02-40de-9306-1656610c11dd', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '283c45d1-d385-402e-9f76-2a3ae1cacfd3', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('0eea36ca-2e91-4726-a795-54c3d078167f', '30b88cb7-58b1-4770-9298-7a68ce7c0c5e', '490489ab-d8b4-414c-ad77-d856962c286f', '222da7d5-af33-4f5f-822a-92a414aa2e76', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('1801d2ce-4618-466f-ac5f-781823a4e427', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', 'de063884-2b7e-4c0e-b27e-34c99280bf28', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('2068ebd9-ca50-4bc4-a1a8-69f772a8106d', '07b3e2cd-eb1b-4e07-8844-685478909734', '490489ab-d8b4-414c-ad77-d856962c286f', '4f0121fb-328f-4061-a1e2-efa412262a81', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('22160978-c6b0-4172-a190-5c2a2370b430', '30b88cb7-58b1-4770-9298-7a68ce7c0c5e', '490489ab-d8b4-414c-ad77-d856962c286f', '98e7b2ce-be53-4285-ba5f-4aae2a80c383', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('2254ed43-6b10-4c56-96ab-9f22e30b43bd', '30b88cb7-58b1-4770-9298-7a68ce7c0c5e', '490489ab-d8b4-414c-ad77-d856962c286f', '929dfa20-8337-4cee-afe5-b7827ccc2a6d', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('226d0344-aa43-4929-973e-2825ad236907', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', '2a2c89ee-49d9-44e1-82a6-0108af3828fd', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('22a91d70-80ce-49d7-b351-f9382f02458b', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', 'd0c2ca72-12da-4730-8293-3d081fe5bb2f', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('26e1bab1-09fe-4c83-8410-877fabe2f16d', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', 'cad8a8ec-c75c-466b-b028-8e32d0e960f2', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('271e830d-0a57-4b6b-8c77-0e081fabff16', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '159c9b22-b5a4-47f3-8e0a-9b29510621ed', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('2b3d7033-e13b-4395-9062-162e73f653c7', '1cd9bf0d-cf27-440e-a38f-0accef16af8d', '490489ab-d8b4-414c-ad77-d856962c286f', 'f3c931cb-5955-495c-9b9a-d578cebefca2', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('2e3b6b64-6689-4d8f-98c5-2fdfaa51c3f0', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', 'ee25afe0-0865-4b91-8efa-6c1a4e007002', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('30d6c95e-61f9-4ea7-8d89-4631e2fe50ac', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', 'f15b2fb6-d17e-4619-b194-45c5df276350', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('316764f1-ed68-4503-8a6e-03f59c70b762', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '9058c86f-b402-4cd3-8def-4d05160a13de', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('318e3a13-8c5f-4d1c-905d-b5dee786485b', '07b3e2cd-eb1b-4e07-8844-685478909734', '490489ab-d8b4-414c-ad77-d856962c286f', '1acee96b-1927-42fc-8324-a4d0c05632cf', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('32bdcde4-7d5c-4e7f-b1d2-69f80479b041', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', '3f56a291-b655-45d3-967c-67c760d983e8', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('3378131d-66c7-474a-b115-6e072c57975e', '1cd9bf0d-cf27-440e-a38f-0accef16af8d', '490489ab-d8b4-414c-ad77-d856962c286f', 'ac7ef3fa-edad-45d7-b796-77827a5fe91b', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('345ac8a9-2958-4053-8952-707892af1f8e', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '164e93b1-461e-4c6a-aec9-cdf0fa984158', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('36a613e5-89af-4b70-9d24-2b4960f3857f', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '3ab5da08-9e72-40d8-a182-77ad93848721', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('384ca23e-9ae6-44c4-8609-559517b02def', '1cd9bf0d-cf27-440e-a38f-0accef16af8d', '490489ab-d8b4-414c-ad77-d856962c286f', '5cd00038-ce3a-4ae6-90d3-7898a33f37f5', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('38e5128a-e95f-4b8d-82d8-b795f8c2506e', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '21da524a-dc26-4227-bdc7-5cd9dd919be5', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('3bf6db23-f9f7-43a3-8ab0-b6c22066d042', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', 'bbb95924-a8a4-408c-b61e-3a989409b3c6', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('3c133bbd-bf1c-4295-9d26-25dd0aaefca7', '07b3e2cd-eb1b-4e07-8844-685478909734', '490489ab-d8b4-414c-ad77-d856962c286f', '929dfa20-8337-4cee-afe5-b7827ccc2a6d', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('3ca80c76-90de-4080-b82f-8cfe9bfb54d5', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '7c499e02-72a2-46da-b676-bb8fe14f29df', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('3d0c0689-88d8-48ee-86b4-47fc1bfd348c', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', '72dac2b0-7ce7-4e5a-88ad-fb6821305dff', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('3d8c184d-4034-4803-bc49-f5038cc13175', '30b88cb7-58b1-4770-9298-7a68ce7c0c5e', '490489ab-d8b4-414c-ad77-d856962c286f', '4a150a15-fe3a-4534-97c3-ba89587eb886', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('3f79757a-d798-471f-9fd8-17b42c4fdfdc', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', 'a42e3e3f-3609-436e-ae51-da2b01b22d56', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('41295c29-2eda-4b74-afb4-6bff8b71f6e4', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', '2e045ba8-ae2c-49d8-bf8c-bc1b92c11b3e', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('4190997a-b6f5-4fe1-b48d-90e021615335', '30b88cb7-58b1-4770-9298-7a68ce7c0c5e', '490489ab-d8b4-414c-ad77-d856962c286f', '5cd00038-ce3a-4ae6-90d3-7898a33f37f5', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('428728e4-9dac-45da-9110-1f741dae05bf', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '21da524a-deee6-4227-bdc7-5cd9dd919be5', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('471c1f5f-e718-4a5c-a3bb-e18c9b712130', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', '95bef937-64ee-4104-ad69-2a3220b45527', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('4b4b9d35-5c01-41ae-8068-b116a51105fe', '1cd9bf0d-cf27-440e-a38f-0accef16af8d', '490489ab-d8b4-414c-ad77-d856962c286f', '9790d902-372c-4dcd-98d2-0db3db02f95d', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('4bdefd7f-d503-4c02-9532-7f4ba04314fb', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '9a73f770-13ca-450d-80ca-ac50d1de4eee', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('4dd72f39-93a8-49e4-9c49-68dab2eee41f', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', 'c6ae153e-bbd6-45c6-938d-28e3c516eeb0', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('4e9d7da0-5aee-4b8c-b428-4ec85a4ec7fd', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '21da114a-deee6-4227-bdc7-5cd9dd919be5', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('4fd6e0c5-4afb-4789-8b31-9aff865b8d67', '07b3e2cd-eb1b-4e07-8844-685478909734', '490489ab-d8b4-414c-ad77-d856962c286f', '9790d902-372c-4dcd-98d2-0db3db02f95d', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('4ffd4248-e1c7-4b9a-9aa9-fe8f1740561f', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', 'bdf0166b-a5c1-4719-8d5d-bf3a032170b4', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('511e9f4b-6cf0-47a4-ac29-04019cc55b12', '07b3e2cd-eb1b-4e07-8844-685478909734', '490489ab-d8b4-414c-ad77-d856962c286f', '98e7b2ce-be53-4285-ba5f-4aae2a80c383', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('549d3ee3-684c-4305-89ed-a68bf47fe3b3', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', 'e2390176-7732-489e-ba36-7d25452f0368', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('5763bd18-150e-4c0b-8c82-63eb368bfb3c', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '1ecaafa8-2a7e-4fb4-b51e-b3ed641b5b99', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('5868ee6a-d114-41f8-b799-04060b353cfa', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', 'a2699e8c-dbe9-41c1-8e8a-8c9930ec9fbd', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('5dd14c90-acdc-425d-a287-521bc2a3b337', 'c15216cd-2e7b-4f0a-959a-36c6a41817ba', '490489ab-d8b4-414c-ad77-d856962c286f', '4a150a15-fe3a-4534-97c3-ba89587eb886', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('5f6431e7-c587-4278-abcc-d1f5d4b41981', '07b3e2cd-eb1b-4e07-8844-685478909734', '490489ab-d8b4-414c-ad77-d856962c286f', 'c9f25c08-a442-4bb6-9d02-9526d295fa56', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('63273a79-219f-48a8-9563-4c419f5394ef', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '21da524a-dc26-4227-bdc7-5cd9b6919be5', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('63742ebf-e867-45d2-8caa-a744f8a65859', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', 'e9bee19a-d254-4492-8afb-b658aa864f6a', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('6c4a1b4e-5c50-4934-8f43-05c6fbe9d349', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '970c5276-12c1-40a2-b0b4-375540faa804', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('6c7b7d65-01a8-4915-8be8-7c204afb9588', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', 'cec8dd67-bc4e-496d-8597-c09dc191395e', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('6dd81374-a659-4b36-b565-ea386c95d723', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '0d36db88-1c74-484c-aa33-0e0f98e9eff4', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('6ff03f84-6cb8-405b-af0d-64709b320a9b', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', '134a2410-1da6-4959-8c81-148e1bc7907a', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('72230886-be72-467a-854c-b589ecd1567b', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', 'a255f616-815a-4845-b6aa-5fd998dac3a2', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('723c862e-b0c6-4ea6-a40d-e5f41e509710', '30b88cb7-58b1-4770-9298-7a68ce7c0c5e', '490489ab-d8b4-414c-ad77-d856962c286f', 'c9f25c08-a442-4bb6-9d02-9526d295fa56', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('7673b19d-42b7-4418-a25e-c43b782bdd2d', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', '98ed899a-2084-4422-ace0-fc9b0ff9f3e7', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('788f8ab8-1d40-4850-9578-e1b5a00c626b', '30b88cb7-58b1-4770-9298-7a68ce7c0c5e', '490489ab-d8b4-414c-ad77-d856962c286f', 'f3c931cb-5955-495c-9b9a-d578cebefca2', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('7db87268-8323-42c8-8a0a-85eb5fa0fdb9', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '90d5c59b-55b6-4dcd-867c-ec0567667262', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('7ef1fa22-8e44-440d-98be-7f030d28de02', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', '0ca98386-4d83-408d-896b-fa486e8f61e0', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('7fa9ed58-889e-4611-91c9-2b7dc68bcdc5', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '28cb929d-b000-4296-9431-2314ae214add', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('80e0bf14-44e3-4ecd-9735-17a206fd652d', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', '8419dfe7-3860-4d0e-9fd2-29a59412bdc1', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('823d83a4-51cf-4171-ae4a-35524d124da6', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', 'f676e451-d0c5-4398-ad22-a9ca0f5d6aaa', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('82a5ce8f-5ddd-42ac-b89f-2df2040ca899', 'c15216cd-2e7b-4f0a-959a-36c6a41817ba', '490489ab-d8b4-414c-ad77-d856962c286f', 'c9f25c08-a442-4bb6-9d02-9526d295fa56', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('843cf55e-61f9-45bd-9b30-9217c132f2c4', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '21de24a-dc26-4227-bss7-5cd9dd919be5', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('87fc108e-d0fc-409c-ac96-8b7ed5c9ca64', 'c15216cd-2e7b-4f0a-959a-36c6a41817ba', '490489ab-d8b4-414c-ad77-d856962c286f', '4f0121fb-328f-4061-a1e2-efa412262a81', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('887468ac-2a4c-4fbf-9c4a-a316668b859e', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '0d41fc8e-99e4-49fe-9046-d46e632273b8', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('8afbbff0-4557-4e62-88be-20c4ea2cee79', 'c15216cd-2e7b-4f0a-959a-36c6a41817ba', '490489ab-d8b4-414c-ad77-d856962c286f', 'b4ed03ce-5cb2-4c3a-b152-9137c81058f4', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('8b65a1cd-a0a1-405c-8976-3098a759d9d4', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '21de24a-dc26-4227-bdc7-5cd9ss19be5', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('9104b91f-3ef1-4484-80fc-90eaf7116d2b', '07b3e2cd-eb1b-4e07-8844-685478909734', '490489ab-d8b4-414c-ad77-d856962c286f', '5cd00038-ce3a-4ae6-90d3-7898a33f37f5', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('9276302f-613d-4afc-bcef-ba52b07bb267', '1cd9bf0d-cf27-440e-a38f-0accef16af8d', '490489ab-d8b4-414c-ad77-d856962c286f', '4a150a15-fe3a-4534-97c3-ba89587eb886', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('94189904-1439-4b69-b8b0-724f7d1e0000', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '0ae1a7c0-9892-48f9-9f0c-1aa28cf6dcf0', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('94baf267-974a-4e56-b42c-2cd3f7671364', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', 'f9d05ba1-d802-4832-8f75-c0896db62d11', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('94d9c291-9a33-44cf-94ed-157866c6eba5', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', 'ef271138-e07b-40df-bb8d-482c82733e11', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('962205ad-3afd-45c1-bc45-d68c992187e2', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', '1e847cc5-99da-4871-bafc-ff224283573c', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('96429edc-3cd8-45df-bbf1-6d0527c821ff', '07b3e2cd-eb1b-4e07-8844-685478909734', '490489ab-d8b4-414c-ad77-d856962c286f', '222da7d5-af33-4f5f-822a-92a414aa2e76', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('973d088e-87fc-462e-934a-d0792d93fc02', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', '59bce42f-42b4-43b9-9bb6-4c5854afc607', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('9786097d-9873-40e9-9cf4-84c415f60e19', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', '8abf3229-9b85-4177-bc40-e0552ec8b8de', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('97882cd2-749d-414d-a8cd-72a6f26f648a', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '21de24a-dc26-4227-bdc7-5cd9dd919be5', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('992aaca0-ce6e-4943-9c99-7ffb356f6032', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', 'd0aa8da7-7760-4135-aa80-2c354bbcdf0d', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('9b20d804-255a-47b4-ade2-3cbc60d4b04b', '07b3e2cd-eb1b-4e07-8844-685478909734', '490489ab-d8b4-414c-ad77-d856962c286f', 'f3c931cb-5955-495c-9b9a-d578cebefca2', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('9b98ca91-2747-42db-a5c9-e4f2d522340a', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', 'f591252e-7651-45bb-9070-dfe4a15d6ea0', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('9cd66332-baf6-4e3c-ba82-33af7a7bb17f', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '9b596006-7155-4b45-a417-751e85fb29f3', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('a04afb51-4fb2-44db-a6b1-3f0af32cded7', 'c15216cd-2e7b-4f0a-959a-36c6a41817ba', '490489ab-d8b4-414c-ad77-d856962c286f', 'f3c931cb-5955-495c-9b9a-d578cebefca2', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('a08a5253-f4d9-4508-8db5-8ab185389d06', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '8fb55b34-66f0-46a2-9842-b20700c6b465', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('a5e2dd98-836b-435c-a481-dd4b42d2cc70', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '0921ba27-7910-48b2-ba5f-f74f5134be31', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('a6c06737-7fa9-49eb-a246-fb00d7a86813', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', '72a9b863-e338-4dec-bd2b-a806b65f506c', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('a8a6d1da-a538-4584-9b6b-92cb3f8ce6bc', 'c15216cd-2e7b-4f0a-959a-36c6a41817ba', '490489ab-d8b4-414c-ad77-d856962c286f', '06705052-19d4-4e89-8d78-b7ad5e23e943', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('a9d49325-3aaa-4b0b-b12f-5510b3138d00', 'c15216cd-2e7b-4f0a-959a-36c6a41817ba', '490489ab-d8b4-414c-ad77-d856962c286f', '222da7d5-af33-4f5f-822a-92a414aa2e76', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('ac115757-03a2-4509-b7f3-8ab5336059d9', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', 'a255f616-815a-4845-b6aa-5fd998dac3a4', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('adad8d4b-d967-4614-a02b-0c3847aee445', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', 'dbdcd97e-d02a-4592-930d-81c5adaaa815', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('add02320-5196-494b-8f9d-dc508819401d', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', '4cea7972-37e8-4976-8bc7-cbd67414048d', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('ae8ebc76-c706-48fc-964a-776623f2d5a4', '07b3e2cd-eb1b-4e07-8844-685478909734', '490489ab-d8b4-414c-ad77-d856962c286f', 'b4ed03ce-5cb2-4c3a-b152-9137c81058f4', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('b18cc2fd-fdaf-463b-b50e-18db184666b7', '30b88cb7-58b1-4770-9298-7a68ce7c0c5e', '490489ab-d8b4-414c-ad77-d856962c286f', '9790d902-372c-4dcd-98d2-0db3db02f95d', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('b1cfbc81-4ad6-420a-893d-c53ecb254d1c', '07b3e2cd-eb1b-4e07-8844-685478909734', '490489ab-d8b4-414c-ad77-d856962c286f', '4a150a15-fe3a-4534-97c3-ba89587eb886', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('b203148b-525e-41a4-a455-966743a0a9c8', 'c15216cd-2e7b-4f0a-959a-36c6a41817ba', '490489ab-d8b4-414c-ad77-d856962c286f', '1acee96b-1927-42fc-8324-a4d0c05632cf', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('b367f90d-77f5-466a-a1db-7532763bd23d', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '21da524a-deee6-4527-bdc7-5cd9dd919be5', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('b3d4d17a-87b9-454a-afda-b8506561932c', 'c15216cd-2e7b-4f0a-959a-36c6a41817ba', '490489ab-d8b4-414c-ad77-d856962c286f', '65df1122-e38c-411f-af09-a334423c5eee', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('b4255bc9-40ac-4a10-87f8-9671161bc76a', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', '30e0e37d-6f49-4807-9130-ace0abffc586', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('b4b495b8-c16c-4ef1-8c39-3f4799573d5b', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', '1e39f079-d40a-4489-aac4-afac365e74f2', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('b528099b-8e03-43d5-a88f-fa6e53d6f5cd', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', 'a0a96cd9-6a2d-4cf6-a4f4-60d3ef59035a', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('b79eea60-31cb-44b1-a323-0aefed2cc9c3', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', '268080c8-68c6-4caa-9580-9fd72f704be1', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('b8d539c4-ba7a-40fb-9d9d-a969941ef64f', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '167cee36-ad77-4c29-a8b0-7e9d8e72cc2d', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('b8d988f1-3a50-408c-a39c-dcc51413530f', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', 'f466f2fe-6e90-4ed4-84ad-ec85a9e5bf97', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('bb4e8b59-aca7-4f73-b68d-af4b9bde80e8', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', 'fc8da44b-3a70-4864-a838-6244ad11880f', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('bb8b0735-1b74-4fcc-be2c-1d094d321727', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '9a7232d1-c819-484c-bff4-2d3c076d2dd5', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('be00d8d0-3d21-471f-a18b-b84789f0a1a8', 'c15216cd-2e7b-4f0a-959a-36c6a41817ba', '490489ab-d8b4-414c-ad77-d856962c286f', '5cd00038-ce3a-4ae6-90d3-7898a33f37f5', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('bf075769-9907-4d0c-a954-30ba2934790e', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '12bf4556-80b8-4b2d-b471-69651ffe5dd4', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('bf6f22e9-98aa-4e2d-b02d-4aabf565ef37', '30b88cb7-58b1-4770-9298-7a68ce7c0c5e', '490489ab-d8b4-414c-ad77-d856962c286f', 'ac7ef3fa-edad-45d7-b796-77827a5fe91b', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('c078c895-8723-4e11-b424-603f2e5759e9', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', '3cfbc6c1-38af-46fe-a0e5-fa7d9b83fc62', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('c2360a5c-40d9-4051-b8b4-fdaddc16dacb', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', '0b45502d-b4fe-45f4-9488-0f622c854534', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('c30b0681-9dbe-436c-bdc2-b6b93459609a', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '76d89fe9-f673-49f5-a643-8a2696e802ed', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('c3b561fb-d6da-472d-9cfc-dbd5e1be8100', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '89d0feab-b72c-4c8d-bb31-702348fc7560', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('c416b5c7-7ada-47ee-aef1-2a40a26c5663', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', 'b3b95e14-1aa2-4a1d-b373-3fba540b9f3f', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('c4210dd2-f87c-47ff-87ce-4eab4993655c', '1cd9bf0d-cf27-440e-a38f-0accef16af8d', '490489ab-d8b4-414c-ad77-d856962c286f', '4f0121fb-328f-4061-a1e2-efa412262a81', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('c603005e-385d-4fd7-9c39-232ef0abc6f4', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', '181fb0f9-d6de-4c2f-946c-d80841bf4e5f', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('c6c04dc5-8937-4c95-837e-0641445a9e51', '07b3e2cd-eb1b-4e07-8844-685478909734', '490489ab-d8b4-414c-ad77-d856962c286f', '06705052-19d4-4e89-8d78-b7ad5e23e943', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('c7216668-5b1b-4199-8359-b871f41185b0', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', '811b7378-11b2-43d3-aa04-cfb24f679850', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('c97ce14d-c4e1-4ab6-bb30-47bbbc139673', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', 'fe7d832f-a185-4cb8-9ac7-f0cc9f39fe11', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('ca1ade0f-47da-4a19-8228-6d60847b8a3b', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', '23001cd0-f685-4732-8efc-4a6dfdcc251c', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('ca7223b1-78a1-478b-942f-d4d9760b1db5', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', '0dca1d94-3005-4b60-a8eb-b47717128487', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('ceffe1af-8d10-4172-b0b2-7834045f69cb', '30b88cb7-58b1-4770-9298-7a68ce7c0c5e', '490489ab-d8b4-414c-ad77-d856962c286f', '4f0121fb-328f-4061-a1e2-efa412262a81', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('cf9e124a-e2c5-4638-aa3a-7bebe25af916', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', 'e4eee1a4-791e-4f85-9c81-ee85e69c9b23', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('cf9f711c-7aed-4777-a323-3c9ac0512b6a', '1cd9bf0d-cf27-440e-a38f-0accef16af8d', '490489ab-d8b4-414c-ad77-d856962c286f', '98e7b2ce-be53-4285-ba5f-4aae2a80c383', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('d067cc3c-fbf0-45b5-be59-28e074dac189', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', 'e3c53f1d-8001-4727-b6b7-f2570df48123', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('d346d23f-bd98-4320-a393-cb69d2e2c37e', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', 'b4334106-7967-4f9e-9353-7045d21dae09', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('d3b55db7-1572-4985-b110-50c7642baff8', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', '33958197-8846-44e2-ac0a-98186b9ad4e6', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('d565c99a-d2ee-48fe-989c-73b253829b4c', 'c15216cd-2e7b-4f0a-959a-36c6a41817ba', '490489ab-d8b4-414c-ad77-d856962c286f', '9790d902-372c-4dcd-98d2-0db3db02f95d', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('daaad8e0-516b-4ae5-8993-07ccf0314da4', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '180d3b1d-15a7-487c-8b8c-feda5442c6db', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('e0004efd-d635-403e-a3a6-2e6a7d5e46a1', '1cd9bf0d-cf27-440e-a38f-0accef16af8d', '490489ab-d8b4-414c-ad77-d856962c286f', 'b4ed03ce-5cb2-4c3a-b152-9137c81058f4', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('e0201dae-5e59-4ba1-9256-7f68a87b8fc5', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', 'cd686ddd-a565-4e73-b061-90eaf6c6b333', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('e231a1b8-66cd-4c1a-90d0-79fe65db57d5', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', 'f638163b-ee94-48fe-85cc-b3e519c99a38', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('e5543d0c-06e0-438f-8bdd-a193f64f784d', '1cd9bf0d-cf27-440e-a38f-0accef16af8d', '490489ab-d8b4-414c-ad77-d856962c286f', '1acee96b-1927-42fc-8324-a4d0c05632cf', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('e750d1c2-1865-4d90-a0e8-d609efd03f86', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', '66bc7819-1f52-4eb2-83de-ed3d6097bb10', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('e995ba00-2557-483c-9f02-d64ca39c8832', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', 'b0f7f7c1-4b6e-4a15-bcd0-3b7453170285', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('eab70d93-1deb-46c6-9bff-41e38e703eb3', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', '65df1122-e38c-411f-af09-a334423c5ee1', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('ead0b4dd-8f86-4990-b98a-99722101615c', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', '6033bf17-2109-4622-afdc-bee2380f3012', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('ebf06278-fde8-4b49-98d8-17d957d4c39a', '30b88cb7-58b1-4770-9298-7a68ce7c0c5e', '490489ab-d8b4-414c-ad77-d856962c286f', '06705052-19d4-4e89-8d78-b7ad5e23e943', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('ef33b0f5-ebb8-40bb-9282-a65d4e138d2f', '30b88cb7-58b1-4770-9298-7a68ce7c0c5e', '490489ab-d8b4-414c-ad77-d856962c286f', 'b4ed03ce-5cb2-4c3a-b152-9137c81058f4', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('f09cbff9-1145-4a01-a908-74550aeb4bed', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '12c5210f-6053-4664-8b51-52e0c1443639', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('f2b467cf-aa9d-41ab-9289-445deebdae97', '1cd9bf0d-cf27-440e-a38f-0accef16af8d', '490489ab-d8b4-414c-ad77-d856962c286f', '222da7d5-af33-4f5f-822a-92a414aa2e76', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('f6a7d6f9-a51f-41c6-a913-bb69faa4f7f5', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', '47b9c905-6356-48e4-997c-2df8a4f2e49c', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('f793c438-ee9f-49fa-8a3e-04145aeff26e', '07b3e2cd-eb1b-4e07-8844-685478909734', '490489ab-d8b4-414c-ad77-d856962c286f', '65df1122-e38c-411f-af09-a334423c5eee', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('fc2030da-f372-4c23-a03d-cdfb997d4ad8', '1cd9bf0d-cf27-440e-a38f-0accef16af8d', '490489ab-d8b4-414c-ad77-d856962c286f', 'c9f25c08-a442-4bb6-9d02-9526d295fa56', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('fe58e6c0-9c8b-4da0-b96a-e86754adf46e', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '21daww4a-deee6-4227-bdc7-5cd9dd919be5', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('0033931b-8c04-487a-bdfd-2dcda6f06e6a', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', '22ec8ed1-5ad3-4867-a711-850828cb1da4', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('00be231b-8c04-487a-bdfd-2dcda6f06e6a', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', '84b01339-7754-48c0-b1de-f4520f3e44cf', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('00be541b-8c04-487a-bdfd-2dcda6f06e6a', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', '9159262d-685f-4294-af9b-2894f6523da3', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('00b2331b-8c04-487a-bdfd-2dcda6f06e6a', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', 'c57e39c6-3062-4d03-902b-20cf5f01052f', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('00b5531b-8c04-487a-bdfd-2dcda6f06e6a', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', 'be2b0983-7dd1-48eb-8bad-7c432da28685', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('00b6631b-8c04-487a-bdfd-2dcda6f06e6a', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', '8db4a2eb-1532-40c8-8986-e7c69bdef985', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('008931b-8c04-487a-bdfd-2dcda6f06e6a', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', 'c477bad3-b7cb-444f-bc29-0de184a4270b', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('00b8931b-8c04-487a-bdfd-2dcda6f06e6a', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', '37f98649-c5c3-4eb4-8d24-310ce16d6aec', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('00b4531b-8c04-487a-bdfd-2dcda6f06e6a', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', 'e11f6d0a-7ca6-4184-8255-ac050a58a17c', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('00b4531b-8c04-487a-00fd-2dcda6f06e6a', '07b3e2cd-eb1b-4e07-8844-685478909734', '13131313', '7699487e-6992-4c9c-864d-d541d0129186', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('00b0031b-8c04-487a-bdfd-2dcda6f06e6a', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '123a667c-15d8-48cd-bf42-93cfd039442b', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('00b2331b-8c04-487a-b12fd-2d1a6f06e6a', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '63b5dbd8-a773-4ae5-8878-e30fc61a37a8', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('0047731b-8c04-487a-bdfd-2dcda6f06e6a', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', '3c775f1a-6733-4b7e-b9c7-cc20981b34d7', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('00b1231b-8c04-487a-bdfd-2dcda6f06e6a', '07b3e2cd-eb1b-4e07-8844-685478909734', '313123131', 'e0d27c1a-516a-4f45-92f3-06ea1a22672b', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_auth_group_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_auth_group_role`;
CREATE TABLE `sys_auth_group_role` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `group_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '权限组ID',
  `role_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限组对应角色表';

-- ----------------------------
-- Records of sys_auth_group_role
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_check_rule
-- ----------------------------
DROP TABLE IF EXISTS `sys_check_rule`;
CREATE TABLE `sys_check_rule` (
  `id` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '主键id',
  `rule_name` varchar(100) DEFAULT NULL COMMENT '规则名称',
  `rule_code` varchar(100) DEFAULT NULL COMMENT '规则Code',
  `rule_json` varchar(1024) DEFAULT NULL COMMENT '规则JSON',
  `rule_description` varchar(200) DEFAULT NULL COMMENT '规则描述',
  `update_by` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `create_by` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uni_sys_check_rule_code` (`rule_code`) USING BTREE,
  UNIQUE KEY `uk_scr_rule_code` (`rule_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='编码校验规则表\n';

-- ----------------------------
-- Records of sys_check_rule
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `type` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '类型',
  `value` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '值',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- ----------------------------
-- Records of sys_config
-- ----------------------------
BEGIN;
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5c9ad6af-1eff-ad09-4fb4-493b58d61607', '租户实名认证开关', 'tenant_auth_switch', 'false', NULL, NULL, 'admin', '2023-10-23 10:05:06');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5coo6af-1eff-0009-4fb4-o93b22d61607', '异地登录限制', 'offsite_login', 'false', NULL, NULL, 'admin', '2022-11-04 17:20:25');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5coo6af-1eff-ad09-4f09-o93b22d61607', '登录重试限制', 'login_repeat_limit', '10', NULL, NULL, 'admin', '2022-11-04 17:20:25');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5coo6af-1eff-ad09-4fb4-o93b20d61607', '最大登录保持时间', 'max_keep_alive', '24', NULL, NULL, 'admin', '2022-11-04 17:20:25');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5coo6af-1eff-ad09-4fb4-o93b22d61p07', '长时间登录设置', 'long_time_no_login', '0', NULL, NULL, 'admin', '2022-11-04 17:20:25');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5coo6af-1eff-ad09-4foo-o93b22d61607', '无操作登录保持时间', 'no_opetate_keep_alive', '24', NULL, NULL, 'admin', '2022-11-04 17:20:25');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5cpp6af-1eff-ad09-4fb4-o93b22d61607', '是否发送短信', 'send_message', 'false', NULL, NULL, 'admin', '2022-11-04 17:20:25');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5coo6af-1eff-a779-4fb4-o93b22d61607', 'ip生效名单', 'ip_type', 'black', NULL, NULL, '13341081511', '2022-11-01 15:51:52');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5coo6af-1eff-ad09-4fb4-o9lb22d61607', 'ip限制开关', 'ip_limit_switch', 'false', NULL, NULL, '13341081511', '2022-11-01 15:51:52');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5coo6af-1eff-a119-4fb4-o93b22d61607', '定期失效时间', 'password_invalid_time', '0', NULL, NULL, '13341081511', '2022-11-01 15:51:42');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5coo6af-1eff-ad09-4884-o93b22d61607', '密码重复限制', 'password_repeat_limit', '0', NULL, NULL, '13341081511', '2022-11-01 15:51:42');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5coo6af-1eff-ad09-4fb4-o93bood61607', '密码最小长度', 'password_minimum_len', '6', NULL, NULL, '13341081511', '2022-11-01 15:51:42');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5coo6af-33ff-ad09-4fb4-o93b22d61607', '密码组成', 'password_composition', 'num', NULL, NULL, '13341081511', '2022-11-01 15:51:42');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5c9ad6af-1eff-ad09-4fb4-493b58d61123', 'CA证书服务开关，false:测试证书，true:CA证书', 'ca_type', 'false', NULL, NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_depart
-- ----------------------------
DROP TABLE IF EXISTS `sys_depart`;
CREATE TABLE `sys_depart` (
  `id` varchar(64) NOT NULL COMMENT 'ID',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT '租户ID',
  `parent_id` varchar(64) DEFAULT NULL COMMENT '父机构ID',
  `depart_name` varchar(100) NOT NULL COMMENT '机构/部门名称',
  `org_code` varchar(64) NOT NULL COMMENT '机构编码',
  `description` varchar(500) DEFAULT NULL COMMENT '描述',
  `mobile` varchar(32) DEFAULT NULL COMMENT '手机号',
  `address` varchar(100) DEFAULT NULL COMMENT '地址',
  `depart_order` int(11) DEFAULT '0' COMMENT '排序',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态（0，正常，1已删除）',
  `delete_by` varchar(64) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_depart_org_code` (`org_code`) USING BTREE,
  KEY `index_depart_parent_id` (`parent_id`) USING BTREE,
  KEY `index_depart_depart_order` (`depart_order`) USING BTREE,
  KEY `index_depart_org_code` (`org_code`) USING BTREE,
  KEY `idx_sd_parent_id` (`parent_id`) USING BTREE,
  KEY `idx_sd_depart_order` (`depart_order`) USING BTREE,
  KEY `idx_sd_org_code` (`org_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='组织机构表';

-- ----------------------------
-- Records of sys_depart
-- ----------------------------
BEGIN;
INSERT INTO `sys_depart` (`id`, `tenant_id`, `parent_id`, `depart_name`, `org_code`, `description`, `mobile`, `address`, `depart_order`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`) VALUES ('4c6508e5-cc16-420c-b516-3608a99c0516', '3efc0be2-3e09-4b6f-8ac0-0e5ea7065d29', '', '12300000000的个人空间', 'A01', NULL, NULL, NULL, 0, NULL, '2023-12-14 19:59:58', NULL, NULL, 0, NULL, NULL);
INSERT INTO `sys_depart` (`id`, `tenant_id`, `parent_id`, `depart_name`, `org_code`, `description`, `mobile`, `address`, `depart_order`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`) VALUES ('d867bbc3-4fef-4c71-9589-05b6a3d0fa6c', 'bf6fbbdd-c424-4cad-b591-9cd5277d2be5', '', '开放签平台管理员', 'A02', NULL, NULL, NULL, 0, '12300000000', '2023-12-14 20:03:50', NULL, NULL, 0, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict` (
  `id` varchar(64) NOT NULL,
  `parent_id` varchar(64) DEFAULT NULL COMMENT '父ID',
  `type` varchar(10) DEFAULT NULL COMMENT '类型',
  `dict_name` varchar(100) NOT NULL COMMENT '字典名称',
  `dict_code` varchar(100) NOT NULL COMMENT '字典编码',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `sort_order` int(10) DEFAULT NULL COMMENT '排序',
  `status` int(10) DEFAULT NULL COMMENT '状态（1启用 0不启用）',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_sd_dict_code` (`dict_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='字典分类表';

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_item`;
CREATE TABLE `sys_dict_item` (
  `id` varchar(64) NOT NULL,
  `dict_id` varchar(64) DEFAULT NULL COMMENT '字典id',
  `parent_id` varchar(64) DEFAULT NULL COMMENT '父id',
  `item_text` varchar(100) NOT NULL COMMENT '字典项文本',
  `item_value` varchar(100) NOT NULL COMMENT '字典项值',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `sort_order` int(10) DEFAULT NULL COMMENT '排序',
  `status` int(11) DEFAULT NULL COMMENT '状态（1启用 0不启用）',
  `create_by` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_by` varchar(32) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_table_dict_id` (`dict_id`) USING BTREE,
  KEY `index_table_sort_order` (`sort_order`) USING BTREE,
  KEY `index_table_dict_status` (`status`) USING BTREE,
  KEY `idx_sdi_role_dict_id` (`dict_id`) USING BTREE,
  KEY `idx_sdi_role_sort_order` (`sort_order`) USING BTREE,
  KEY `idx_sdi_status` (`status`) USING BTREE,
  KEY `idx_sdi_dict_val` (`dict_id`,`item_value`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='字典详情表';

-- ----------------------------
-- Records of sys_dict_item
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_fill_rule
-- ----------------------------
DROP TABLE IF EXISTS `sys_fill_rule`;
CREATE TABLE `sys_fill_rule` (
  `id` varchar(64) CHARACTER SET utf8 NOT NULL COMMENT '主键ID',
  `rule_name` varchar(100) DEFAULT NULL COMMENT '规则名称',
  `rule_code` varchar(100) DEFAULT NULL COMMENT '规则Code',
  `rule_class` varchar(100) DEFAULT NULL COMMENT '规则实现类',
  `rule_params` varchar(200) DEFAULT NULL COMMENT '规则参数',
  `update_by` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `create_by` varchar(32) CHARACTER SET utf8 DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uni_sys_fill_rule_code` (`rule_code`) USING BTREE,
  UNIQUE KEY `uk_sfr_rule_code` (`rule_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='填值规则表\n';

-- ----------------------------
-- Records of sys_fill_rule
-- ----------------------------
BEGIN;
INSERT INTO `sys_fill_rule` (`id`, `rule_name`, `rule_code`, `rule_class`, `rule_params`, `update_by`, `update_time`, `create_by`, `create_time`) VALUES ('1202551334738382850', '机构编码生成', 'org_num_role', 'com.resrun.modules.system.rule.OrgCodeRule', '{\"parentId\":\"c6d7cb4deeac411cb3384b1b31278596\"}', 'admin', '2019-12-09 10:37:06', 'admin', '2019-12-05 19:32:35');
INSERT INTO `sys_fill_rule` (`id`, `rule_name`, `rule_code`, `rule_class`, `rule_params`, `update_by`, `update_time`, `create_by`, `create_time`) VALUES ('1202787623203065858', '分类字典编码生成', 'category_code_rule', 'com.resrun.modules.system.rule.CategoryCodeRule', '{\"pid\":\"\"}', 'admin', '2022-05-31 17:56:00', 'admin', '2019-12-06 11:11:31');
COMMIT;

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
  `id` varchar(64) NOT NULL COMMENT '主键id',
  `app_id` varchar(64) DEFAULT NULL COMMENT '应用ID',
  `name` varchar(100) DEFAULT NULL COMMENT '菜单标题',
  `menu_type` int(11) NOT NULL COMMENT '菜单类型(0:一级菜单; 1:子菜单:2:按钮权限)',
  `parent_id` varchar(64) DEFAULT NULL COMMENT '父id',
  `sort_no` int(5) NOT NULL DEFAULT '0' COMMENT '菜单排序',
  `path` varchar(255) DEFAULT NULL COMMENT '路径',
  `component` varchar(255) DEFAULT NULL COMMENT '组件',
  `perms` varchar(255) DEFAULT NULL COMMENT '菜单权限编码',
  `icon` varchar(100) DEFAULT NULL COMMENT '菜单图标',
  `route_flag` tinyint(1) DEFAULT '1' COMMENT '是否路由菜单: 0:不是  1:是（默认值1）',
  `hidden_flag` tinyint(1) DEFAULT '0' COMMENT '是否隐藏路由: 0否,1是',
  `keep_alive_flag` tinyint(1) DEFAULT NULL COMMENT '是否缓存该页面:    1:是   0:不是',
  `internal_or_external` tinyint(1) DEFAULT NULL COMMENT '外链菜单打开方式 0/内部打开 1/外部打开',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `status` int(2) NOT NULL DEFAULT '1' COMMENT '按钮权限状态(0无效1有效)',
  `rule_flag` int(2) DEFAULT '0' COMMENT '是否可以添加数据权限1是0否',
  `form_table_id` varchar(64) DEFAULT NULL COMMENT '权限表',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除状态 0正常 1已删除',
  `delete_by` varchar(64) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `auth_visible` tinyint(1) DEFAULT NULL COMMENT '配置权限时是否可见1可见0不可见',
  `fast_icon` varchar(10000) DEFAULT NULL COMMENT '快捷图标',
  `fast_icon_address` varchar(64) DEFAULT NULL COMMENT '快捷图标地址',
  `fast_flag` tinyint(1) DEFAULT '0' COMMENT '是否快捷操作',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_prem_pid` (`parent_id`) USING BTREE,
  KEY `index_prem_is_route` (`route_flag`) USING BTREE,
  KEY `index_prem_sort_no` (`sort_no`) USING BTREE,
  KEY `index_prem_del_flag` (`delete_flag`) USING BTREE,
  KEY `index_menu_type` (`menu_type`) USING BTREE,
  KEY `index_menu_hidden` (`hidden_flag`) USING BTREE,
  KEY `index_menu_status` (`status`) USING BTREE,
  KEY `idx_sp_parent_id` (`parent_id`) USING BTREE,
  KEY `idx_sp_is_route` (`route_flag`) USING BTREE,
  KEY `idx_sp_sort_no` (`sort_no`) USING BTREE,
  KEY `idx_sp_del_flag` (`delete_flag`) USING BTREE,
  KEY `idx_sp_menu_type` (`menu_type`) USING BTREE,
  KEY `idx_sp_hidden` (`hidden_flag`) USING BTREE,
  KEY `idx_sp_status` (`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='菜单权限表';

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
BEGIN;
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('035faa2f-4aad-45ac-a303-988c7a568347', '30732c59-9d74-46be-8526-d8d58465ba9d', '权限管理', 0, '', 4, '/auth', 'LAYOUT', NULL, 'ant-design:idcard-twotone', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-04-23 10:23:01', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('06705052-19d4-4e89-8d78-b7ad5e23e943', '490489ab-d8b4-414c-ad77-d856962c286f', '文件验签', 1, '', 3, '/doc/verification', '/signature/doc/verification', NULL, '', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-08-14 14:28:06', 'admin', '2023-12-13 16:22:55', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('06d584d6-6497-4bf9-8929-5d194345e440', '330ba5b5-2339-49ce-9cf0-e032528eff65', '发票管理', 1, 'a16b91f8-c5ca-47ba-9c6b-e97c1d97cc09', 1, '1', NULL, NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, '13793014727', '2023-01-16 10:44:29', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('0921ba27-7910-48b2-ba5f-f74f5134be31', '313123131', '新增字典分类', 2, '3ab5da08-9e72-40d8-a182-77ad93848721', 1, NULL, NULL, 'dictcategory:add', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-29 11:22:22', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('0ae1a7c0-9892-48f9-9f0c-1aa28cf6dcf0', '313123131', '日志审计', 1, '', 7, '/logs', '/sys/logs/index', NULL, 'ant-design:calendar-outlined', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-04-12 13:22:56', 'admin', '2023-12-08 15:03:04', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('0b45502d-b4fe-45f4-9488-0f622c854534', '70588803-52e4-433d-a61f-0a68e1febd72', '个人证书', 1, '0ca98386-4d83-408d-896b-fa486e8f61e0', 21, '/certification/personal', '/certificate/personal', NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, '13341081511', '2023-10-12 17:29:44', NULL, NULL, 0, NULL, NULL, 1, '', 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('0ca98386-4d83-408d-896b-fa486e8f61e0', '70588803-52e4-433d-a61f-0a68e1febd72', '证书管理', 0, '', 2, '/certification', 'LAYOUT', NULL, 'ant-design:file-protect-outlined', 0, 0, 0, 0, NULL, 1, 0, NULL, '13341081511', '2023-10-12 17:28:56', 'admin', '2023-12-08 15:28:06', 0, NULL, NULL, 1, '', 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('0d36db88-1c74-484c-aa33-0e0f98e9eff4', '313123131', '租户应用授权', 2, 'a255f616-815a-4845-b6aa-5fd998dac3a2', 1, NULL, NULL, 'tanant:appauth', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-07 16:21:19', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('0d41fc8e-99e4-49fe-9046-d46e632273b8', '313123131', '权限策略管理', 1, '', 12, '/auth/ploy', '/auth/authPloy', NULL, 'ant-design:property-safety-twotone', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-01-05 17:17:44', '13300000000', '2023-12-14 09:57:18', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('0dca1d94-3005-4b60-a8eb-b47717128487', '13131313', '添加成员', 2, '72a9b863-e338-4dec-bd2b-a806b65f506c', 1, NULL, NULL, 'authgroup:addmember', NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-01-05 18:06:36', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('12bf4556-80b8-4b2d-b471-69651ffe5dd4', '313123131', '模板编辑', 2, 'd0aa8da7-7760-4135-aa80-2c354bbcdf0d', 0, NULL, NULL, 'template:edit', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-10-13 11:05:42', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('12c5210f-6053-4664-8b51-52e0c1443639', '313123131', '模板详情', 2, 'd0aa8da7-7760-4135-aa80-2c354bbcdf0d', 0, NULL, NULL, 'template:info', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-10-13 13:22:47', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('134a2410-1da6-4959-8c81-148e1bc7907a', '13131313', '删除成员', 2, '72a9b863-e338-4dec-bd2b-a806b65f506c', 1, NULL, NULL, 'authgroup:deletemember', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-05 18:07:15', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('159c9b22-b5a4-47f3-8e0a-9b29510621ed', '313123131', '应用编辑', 2, '822424f4-f96f-4c1c-95e7-001faf9f4eea', 1, NULL, NULL, 'app:edit', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-07 16:22:33', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('164e93b1-461e-4c6a-aec9-cdf0fa984158', '313123131', '任务调度中心', 0, '', 6, '/task', 'LAYOUT', NULL, 'ant-design:translation-outlined', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-04-12 13:20:09', 'admin', '2023-04-12 13:23:05', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('167cee36-ad77-4c29-a8b0-7e9d8e72cc2d', '313123131', '新增字典', 2, '3ab5da08-9e72-40d8-a182-77ad93848721', 1, NULL, NULL, 'dict:add', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-29 11:22:33', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('180d3b1d-15a7-487c-8b8c-feda5442c6db', '313123131', '公告撤销', 2, 'cd686ddd-a565-4e73-b061-90eaf6c6b333', 0, NULL, NULL, 'announcement:reovke', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-10-13 14:28:53', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('181fb0f9-d6de-4c2f-946c-d80841bf4e5f', '13131313', '权限策略新建', 2, '72a9b863-e338-4dec-bd2b-a806b65f506c', 1, NULL, NULL, 'authploy:add', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-05 18:08:03', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('1acee96b-1927-42fc-8324-a4d0c05632cf', '490489ab-d8b4-414c-ad77-d856962c286f', '填写文档参数', 1, 'f3c931cb-5955-495c-9b9a-d578cebefca2', 123, '/contract/params/:signReId?/:signRuId?/:__full__?/:isDetail?', '/contract/params/index', NULL, NULL, 0, 1, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-11-06 14:53:45', 'admin', '2023-12-13 16:42:40', 0, NULL, NULL, 0, '', 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('1ce54f00-9a26-41d0-bf41-2aa7a0b211b6', '330ba5b5-2339-49ce-9cf0-e032528eff65', '版权活动/培训', 1, '29933711-5231-4bd3-af41-cbc291c4f72d', 1, '11', NULL, NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, '13793014727', '2023-01-16 10:44:13', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('1e231ef4-b908-4255-9c42-be2b1c8df83b', '30732c59-9d74-46be-8526-d8d58465ba9d', '编辑部门', 2, '77b94c28-59ba-4c89-95cd-1eea7d61f49d', 1, NULL, NULL, 'dept:edit', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-04-23 10:30:19', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('1e39f079-d40a-4489-aac4-afac365e74f2', '13131313', '编辑角色', 2, '6033bf17-2109-4622-afdc-bee2380f3012', 1, NULL, NULL, 'role:edit', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-05 18:13:10', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('1e847cc5-99da-4871-bafc-ff224283573c', '13131313', '新增权限组分类', 2, '72a9b863-e338-4dec-bd2b-a806b65f506c', 1, NULL, NULL, 'authcategory:add', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-05 18:03:17', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('1e847scc5-99da-4871-bafc-ff224283573c', '30732c59-9d74-46be-8526-d8d58465ba9d', '新增权限组分类', 2, 'c7c78143-0eb4-411d-a91c-4b52686e0247', 1, NULL, NULL, 'authcategory:add', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-05 18:03:17', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('1ecaafa8-2a7e-4fb4-b51e-b3ed641b5b99', '313123131', '权限策略删除', 2, '0d41fc8e-99e4-49fe-9046-d46e632273b8', 1, NULL, NULL, 'authploy:delete', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-07 16:19:07', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('1ecaafa8-2sa7e-4fb4-b51e-b3ed641b5b99', '30732c59-9d74-46be-8526-d8d58465ba9d', '权限策略删除', 2, '836ca3f0-91ac-4201-8be7-9d444b14f42c', 1, NULL, NULL, 'authploy:delete', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-07 16:19:07', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('21da114a-deee6-4227-bdc7-5cd9dd919be5', '313123131', '查询任务列表', 2, '283c45d1-d385-402e-9f76-2a3ae1cacfd3', 1, NULL, NULL, 'task:history', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-07 16:18:50', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('21da524a-dc26-4227-bdc7-5cd9b6919be5', '313123131', '权限策略新建', 2, '0d41fc8e-99e4-49fe-9046-d46e632273b8', 1, NULL, NULL, 'authploy:add', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-07 16:18:50', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('21da524a-dc26-4227-bdc7-5cd9dd919be5', '313123131', '新增任务', 2, '283c45d1-d385-402e-9f76-2a3ae1cacfd3', 1, NULL, NULL, 'task:add', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-07 16:18:50', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('21da524a-deee6-4227-bdc7-5cd9dd919be5', '313123131', '停用任务', 2, '283c45d1-d385-402e-9f76-2a3ae1cacfd3', 1, NULL, NULL, 'task:able', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-07 16:18:50', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('21da524a-deee6-4527-bdc7-5cd9dd919be5', '313123131', '删除任务', 2, '283c45d1-d385-402e-9f76-2a3ae1cacfd3', 1, NULL, NULL, 'task:delete', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-07 16:18:50', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('21daww4a-deee6-4227-bdc7-5cd9dd919be5', '313123131', '运行任务', 2, '283c45d1-d385-402e-9f76-2a3ae1cacfd3', 1, NULL, NULL, 'task:run', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-07 16:18:50', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('21de11a-dc26-4227-bdc7-5cd9dd919be5', '313123131', '重试任务实例', 2, '8fb55b34-66f0-46a2-9842-b20700c6b465', 1, NULL, NULL, 'instance:retry', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-07 16:18:50', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('21de24a-dc26-4227-bdc7-5cd9dd919be5', '313123131', '停止任务实例', 2, '8fb55b34-66f0-46a2-9842-b20700c6b465', 1, NULL, NULL, 'instance:stop', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-07 16:18:50', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('21de24a-dc26-4227-bdc7-5cd9ss19be5', '313123131', '查询任务实例日志', 2, '8fb55b34-66f0-46a2-9842-b20700c6b465', 1, NULL, NULL, 'instance:log', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-07 16:18:50', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('21de24a-dc26-4227-bss7-5cd9dd919be5', '313123131', '查询任务实例详情', 2, '8fb55b34-66f0-46a2-9842-b20700c6b465', 1, NULL, NULL, 'instance:info', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-07 16:18:50', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('222da7d5-af33-4f5f-822a-92a414aa2e76', '490489ab-d8b4-414c-ad77-d856962c286f', '首页', 1, '', 1, '/dashboard/workbench', '/dashboard/workbench/index', NULL, '', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-08-14 14:11:51', 'admin', '2023-12-05 16:28:59', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('23001cd0-f685-4732-8efc-4a6dfdcc251c', '70588803-52e4-433d-a61f-0a68e1febd72', '敏感操作保护设置', 1, '8abf3229-9b85-4177-bc40-e0552ec8b8de', 54, '/safe/sensitive', '/sys/safe/Sensitive', NULL, NULL, 0, 1, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-12-08 14:57:10', 'admin', '2023-12-13 16:43:58', 0, NULL, NULL, 0, '', 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('268080c8-68c6-4caa-9580-9fd72f704be1', '13131313', '用户编辑', 2, 'bbb95924-a8a4-408c-b61e-3a989409b3c6', 1, NULL, NULL, 'user:edit', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-05 18:15:25', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('26d6d844-7854-4eb2-ab9a-8759b1f0a1e0', '30732c59-9d74-46be-8526-d8d58465ba9d', '用户编辑', 2, 'bed526d3-08f4-480a-b185-d81260bec3a1', 1, NULL, NULL, '6', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-04-24 15:42:36', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('283c45d1-d385-402e-9f76-2a3ae1cacfd3', '313123131', '任务管理', 1, '164e93b1-461e-4c6a-aec9-cdf0fa984158', 61, '/task/manage', '/task/TaskScheduling', NULL, '', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-04-12 13:21:16', '13300000002', '2023-12-14 10:51:11', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('28669186-7902-4262-8c14-b4da242edd67', '123456789', '流程中心', 1, '', 2, 'http://localhost:3003/#/bpmns/application/initiate', '/bpms/index', NULL, 'ant-design:apartment-outlined', 1, 0, 0, 1, NULL, 1, 0, NULL, 'admin', '2023-08-14 17:42:53', 'admin', '2023-08-14 17:59:37', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('28cb929d-b000-4296-9431-2314ae214add', '313123131', '编辑字典', 2, '3ab5da08-9e72-40d8-a182-77ad93848721', 1, NULL, NULL, 'dict:edit', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-29 11:23:16', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('29933711-5231-4bd3-af41-cbc291c4f72d', '330ba5b5-2339-49ce-9cf0-e032528eff65', '社会服务体系业务', 0, '', 1, '1', NULL, NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, '13793014727', '2023-01-16 10:43:08', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('2a2c89ee-49d9-44e1-82a6-0108af3828fd', '13131313', '批量删除', 2, 'bbb95924-a8a4-408c-b61e-3a989409b3c6', 1, NULL, NULL, 'user:deletebatch', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-11 16:54:08', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('2e045ba8-ae2c-49d8-bf8c-bc1b92c11b3e', '13131313', '设置主管', 2, 'bbb95924-a8a4-408c-b61e-3a989409b3c6', 1, NULL, NULL, 'dept:setmanager', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-11 16:51:23', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('30e0e37d-6f49-4807-9130-ace0abffc586', '13131313', '章面变更', 1, '', 30, '/seals/change', 'signature/seal/SealChange', NULL, NULL, 0, 1, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-11-23 19:07:40', 'admin', '2023-12-13 16:39:23', 0, NULL, NULL, 0, '', 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('312e2623-4971-4ec1-ac67-ec899a6f962d', '30732c59-9d74-46be-8526-d8d58465ba9d', '删除权限组成员', 2, 'c7c78143-0eb4-411d-a91c-4b52686e0247', 1, NULL, NULL, 'authgroup:deletemember', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-04-24 15:08:02', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('33958197-8846-44e2-ac0a-98186b9ad4e6', '13131313', '成员加入', 1, 'dbdcd97e-d02a-4592-930d-81c5adaaa815', 24, '/user/join', '/tenant/organize/UserJoin', NULL, '', 0, 0, 0, 0, NULL, 1, 0, NULL, '13341081511', '2023-10-12 15:20:24', 'admin', '2023-12-13 17:33:22', 0, NULL, NULL, 1, '', 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('38e41910-56be-4e89-9bef-3752eafd9682', '30732c59-9d74-46be-8526-d8d58465ba9d', '编辑角色', 2, 'bed526d3-08f4-480a-b185-d81260bec3a1', 1, NULL, NULL, '4', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-04-24 15:42:09', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('3a3abdd0-dd06-48db-b4ef-5ae0bc27b37c', '30732c59-9d74-46be-8526-d8d58465ba9d', '添加成员', 2, 'bed526d3-08f4-480a-b185-d81260bec3a1', 1, NULL, NULL, '1', NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-04-24 15:41:18', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('3ab5da08-9e72-40d8-a182-77ad93848721', '313123131', '数据字典', 1, '89d0feab-b72c-4c8d-bb31-702348fc7560', 41, '/sys/dict', '/dataconfig/Dict', NULL, '', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-01-29 11:21:49', 'admin', '2023-04-12 14:17:53', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('3b2649a2-888d-411c-99f0-61efd5fb32e8', '826b4285-5602-467d-aea4-dcf7f8678424', '我的已办', 1, '', 4, '/bpmns/application/done', 'bpmns/done/Done', NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-08-21 18:45:06', 'admin', '2023-08-21 18:45:27', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('3cfbc6c1-38af-46fe-a0e5-fa7d9b83fc62', '13131313', '组织概览', 1, '', 1, '/overview', '/overview/index', NULL, 'ant-design:appstore-filled', 0, 0, 0, 0, NULL, 1, 0, NULL, '18612927144', '2023-04-04 17:49:26', '13522104081', '2023-12-14 09:53:25', 0, NULL, NULL, 0, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('3f56a291-b655-45d3-967c-67c760d983e8', '13131313', '添加成员', 2, 'bbb95924-a8a4-408c-b61e-3a989409b3c6', 1, NULL, NULL, 'user:add', NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-01-11 16:52:21', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('417875f3-284d-4725-b3f5-8025e5013a05', '30732c59-9d74-46be-8526-d8d58465ba9d', '编辑角色组', 2, 'bed526d3-08f4-480a-b185-d81260bec3a1', 1, NULL, NULL, '8', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-04-24 15:43:14', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('4671025f-c693-44b7-b833-5fe2f140b660', '490489ab-d8b4-414c-ad77-d856962c286f', '用印申请流程', 1, 'f644efc5-1fad-43e7-b608-f9ebaaa02197', 2, '/seals/apply/process', '/signature/form/SealApplyProcess', NULL, NULL, 0, 1, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-08-18 14:58:17', 'admin', '2023-08-18 15:15:38', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('47b9c905-6356-48e4-997c-2df8a4f2e49c', '13131313', '删除权限组', 2, '72a9b863-e338-4dec-bd2b-a806b65f506c', 1, NULL, NULL, 'authgroup:delete', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-05 18:05:17', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('47b9c9s05-6356-48e4-997c-2df8a4f2e49c', '30732c59-9d74-46be-8526-d8d58465ba9d', '删除权限组', 2, 'c7c78143-0eb4-411d-a91c-4b52686e0247', 1, NULL, NULL, 'authgroup:delete', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-05 18:05:17', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('4a150a15-fe3a-4534-97c3-ba89587eb886', '490489ab-d8b4-414c-ad77-d856962c286f', '文档管理', 1, '', 2, '/contract/doc/:key?', '/contract/document/index', NULL, '', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-11-20 18:15:42', 'admin', '2023-12-13 16:21:27', 0, NULL, NULL, 1, '', 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('4c227ab0-5a94-4c12-a94c-e29e85cbd75f', '70588803-52e4-433d-a61f-0a68e1febd72', '菜单编辑', 2, '00636191-c944-424f-9a81-f0a6a70dbea8', 1, NULL, NULL, 'menu:edit', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-04-23 10:55:03', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('4cea7972-37e8-4976-8bc7-cbd67414048d', '13131313', '编辑角色组', 2, '6033bf17-2109-4622-afdc-bee2380f3012', 1, NULL, NULL, 'rolegroup:edit', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-05 18:12:49', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('4f0121fb-328f-4061-a1e2-efa412262a81', '490489ab-d8b4-414c-ad77-d856962c286f', '审批详情', 1, 'f3c931cb-5955-495c-9b9a-d578cebefca2', 125, '/contract/detail/approve', '/contract/detail/approveDetail', NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-11-24 14:48:06', 'admin', '2023-12-13 16:43:00', 0, NULL, NULL, 0, '', 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('4f03a298-b7bc-41ba-9e6d-808aef6088c7', '490489ab-d8b4-414c-ad77-d856962c286f', '印章制作流程', 1, 'f644efc5-1fad-43e7-b608-f9ebaaa02197', 5, '/seals/make/process', '/signature/form/SealCreatedProcess', NULL, NULL, 0, 1, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-08-16 10:41:19', 'admin', '2023-08-18 15:13:53', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('50594ad1-6603-487f-8de5-5003d8643c1a', '69ea885b-7c9f-46e0-bf7e-d7ea2cb7424c', '登记审核', 1, '', 1, '/dashboard/workbench', '/dashboard/workbench/index', NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-03-28 19:34:36', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('5771c6b2-2c66-476c-9462-d945e3eea2a9', '826b4285-5602-467d-aea4-dcf7f8678424', '流程管理', 1, '', 7, '/bpmns/control/model', 'bpmns-control/model/Model', NULL, 'ant-design:build-twotone', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-04-27 16:30:59', 'admin', '2023-08-18 10:17:23', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('59bce42f-42b4-43b9-9bb6-4c5854afc607', '13131313', '新增权限组', 2, '72a9b863-e338-4dec-bd2b-a806b65f506c', 1, NULL, NULL, 'authgroup:add', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-05 18:04:28', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('59bce4ss2f-42b4-43b9-9bb6-4c5854afc607', '30732c59-9d74-46be-8526-d8d58465ba9d', '新增权限组', 2, 'c7c78143-0eb4-411d-a91c-4b52686e0247', 1, NULL, NULL, 'authgroup:add', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-05 18:04:28', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('5cd00038-ce3a-4ae6-90d3-7898a33f37f5', '490489ab-d8b4-414c-ad77-d856962c286f', '发起签署', 1, 'f3c931cb-5955-495c-9b9a-d578cebefca2', 121, '/contract/start/:signReId?/:signRuId?/:id?/:__full__?/:from?', '/contract/index', NULL, NULL, 0, 1, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-11-06 14:46:07', 'admin', '2023-12-13 16:42:26', 0, NULL, NULL, 0, '', 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('5e7da3dc-f8d8-4a07-9d36-04cb31dfbb88', '30732c59-9d74-46be-8526-d8d58465ba9d', '新增权限组成员', 2, 'c7c78143-0eb4-411d-a91c-4b52686e0247', 1, NULL, NULL, 'authgroup:addmember', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-04-24 15:06:38', 'admin', '2023-04-24 15:08:10', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('6033bf17-2109-4622-afdc-bee2380f3012', '13131313', '角色管理', 1, 'dbdcd97e-d02a-4592-930d-81c5adaaa815', 23, '/organize/role', '/tenant/organize/index', NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-12-08 11:28:15', '13522104081', '2023-12-14 10:02:56', 0, NULL, NULL, 0, '', 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('64c1328e-918b-4a89-b2f4-46cd12a3c760', '490489ab-d8b4-414c-ad77-d856962c286f', '印章-章面变更', 1, 'f644efc5-1fad-43e7-b608-f9ebaaa02197', 999, '/seals/change/process', '/signature/form/SealChangeProcess', NULL, NULL, 0, 1, 0, 0, NULL, 1, 0, NULL, '13271928495', '2023-09-07 17:02:46', '13271928495', '2023-09-07 17:03:14', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('65df1122-e38c-411f-af09-a334423c5ee1', '13131313', '印章管理', 1, '', 3, '/seals/manage', '/signature/seal/index', NULL, 'ant-design:align-center-outlined', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-08-14 14:17:10', 'admin', '2023-12-08 11:21:47', 0, NULL, NULL, 1, '', NULL, 1);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('65df1122-e38c-411f-af09-a334423c5eee', '490489ab-d8b4-414c-ad77-d856962c286f', '我的印章', 1, '', 41, '/seals/my', '/signature/seal/mySeal', NULL, 'ant-design:align-center-outlined', 0, 1, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-08-14 14:17:10', '13300000000', '2023-12-13 18:05:29', 0, NULL, NULL, 0, '', NULL, 1);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('66bc7819-1f52-4eb2-83de-ed3d6097bb10', '70588803-52e4-433d-a61f-0a68e1febd72', '访问规则设置', 1, '8abf3229-9b85-4177-bc40-e0552ec8b8de', 52, '/safe/access', '/sys/safe/Access', NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-12-08 14:55:52', NULL, NULL, 0, NULL, NULL, 1, '', 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('6b710950-442c-41b5-89cc-000b816f175f', '30732c59-9d74-46be-8526-d8d58465ba9d', '角色移除人员', 2, 'bed526d3-08f4-480a-b185-d81260bec3a1', 1, NULL, NULL, '11', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-04-24 15:43:59', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('70f04e55-d108-4357-9c39-728cc42e921c', '13131313', '数据权限策略', 1, '02b907ef-65f1-4e9b-87fb-1099edd1cb06', 42, '/auth/authPoly', '/auth/authPloy', NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, '18612927144', '2023-04-04 17:54:45', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('72a9b863-e338-4dec-bd2b-a806b65f506c', '13131313', '权限管理', 1, '', 6, '/auth/group', '/auth/authGroup', NULL, 'ant-design:ant-design-outlined', 0, 0, 0, 0, NULL, 1, 0, NULL, '18612927144', '2023-04-04 17:53:47', 'admin', '2023-12-08 11:22:37', 0, NULL, NULL, 1, '', '1f55624b-56b7-4b00-9ec2-84840545f89f', 1);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('72dac2b0-7ce7-4e5a-88ad-fb6821305dff', '70588803-52e4-433d-a61f-0a68e1febd72', '企业认证审核', 1, '', 3, '/audit/enterprise', '/audit/enterprise', NULL, 'ant-design:schedule-outlined', 0, 0, 0, 0, NULL, 1, 0, NULL, '13341081511', '2023-10-12 17:31:32', 'admin', '2023-12-08 15:26:45', 0, NULL, NULL, 1, '', 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('7699487e-6992-4c9c-864d-d541d01291po', '490489ab-d8b4-414c-ad77-d856962c286f', '模板创建', 1, 'f644efc5-1fad-43e7-b608-f9ebaaa02197', 999, '/template/created/process', '/signature/form/TemplateCreatedProcess', NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-08-21 15:03:59', 'admin', '2023-08-21 15:10:38', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('76d89fe9-f673-49f5-a643-8a2696e802ed', '313123131', '应用发布', 2, '822424f4-f96f-4c1c-95e7-001faf9f4eea', 1, NULL, NULL, 'app:public', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-07 16:22:17', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('77b94c28-59ba-4c89-95cd-1eea7d61f49d', '30732c59-9d74-46be-8526-d8d58465ba9d', '组织管理', 1, 'ce84ec5b-5b5d-4ea9-ae08-c180866f5f9a', 1, '/organize', '/tenant/organize/index', NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-04-23 10:27:24', 'admin', '2023-04-23 17:33:17', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('7c499e02-72a2-46da-b676-bb8fe14f29df', '313123131', '单号规则', 1, '89d0feab-b72c-4c8d-bb31-702348fc7560', 42, '/data/serialnumber', '/dataconfig/SerialNumber', NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-04-12 13:17:16', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('7c60ff65-95c8-4cc9-86bd-d0f7da8723c2', '826b4285-5602-467d-aea4-dcf7f8678424', '发起流程', 1, '', 6, '/bpmns/initiate/index/:flowId?/:formMap?', 'bpmns/initiate/index', NULL, 'ant-design:bank-filled', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-04-27 16:27:14', 'admin', '2023-09-19 14:38:09', 0, NULL, NULL, 1, '', NULL, 1);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('811b7378-11b2-43d3-aa04-cfb24f679850', '70588803-52e4-433d-a61f-0a68e1febd72', '个人用户', 1, 'e4eee1a4-791e-4f85-9c81-ee85e69c9b23', 11, '/usermanage/personal', '/users/personal', NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, '13341081511', '2023-10-12 17:27:38', NULL, NULL, 0, NULL, NULL, 1, '', 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('822424f4-f96f-4c1c-95e7-001faf9f4eea', '313123131', '应用管理', 1, '', 2, '/application', '/application/index', NULL, 'ant-design:appstore-outlined', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-01-05 17:15:33', 'admin', '2023-09-19 14:14:54', 0, NULL, NULL, 1, '', '0972af99-c519-4f05-978b-93ff9c08ffdc', 1);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('836ca3f0-91ac-4201-8be7-9d444b14f42c', '30732c59-9d74-46be-8526-d8d58465ba9d', '数据权限策略', 1, '035faa2f-4aad-45ac-a303-988c7a568347', 422, '/auth/authPoly', '/auth/authPloy', NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-04-23 10:25:44', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('8419dfe7-3860-4d0e-9fd2-29a59412bdc1', '13131313', '邀请成员加入', 2, 'bbb95924-a8a4-408c-b61e-3a989409b3c6', 1, NULL, NULL, 'invite:user', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-09 11:22:37', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('89d0feab-b72c-4c8d-bb31-702348fc7560', '313123131', '数据配置', 0, '', 5, '/data/', 'LAYOUT', NULL, 'ant-design:box-plot-outlined', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-04-12 13:15:32', 'admin', '2023-12-08 15:01:18', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('8abf3229-9b85-4177-bc40-e0552ec8b8de', '70588803-52e4-433d-a61f-0a68e1febd72', '安全配置', 0, '', 5, '/safe', 'LAYOUT', NULL, 'ant-design:ant-design-outlined', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-12-08 14:51:08', NULL, NULL, 0, NULL, NULL, 1, '', 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('8b356af7-f03d-4655-854e-1133a89fdeac', '826b4285-5602-467d-aea4-dcf7f8678424', '流程监控', 1, '', 8, '/bpmns/control/instance', 'bpmns-control/instance/Index', NULL, 'ant-design:monitor-outlined', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-04-27 16:35:28', 'admin', '2023-04-27 16:35:48', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('8c0a2d92-3e55-4ee1-bf28-64a227b4a266', '826b4285-5602-467d-aea4-dcf7f8678424', '流程表单123', 1, '', 10, '/bpmns/form/detail/:taskId?/:disabled?/:type?', 'bpmns-form/Index', NULL, NULL, 0, 1, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-08-18 17:21:43', 'admin', '2023-08-24 09:53:15', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('8d4d2216-69b9-4a29-be24-f5096f4841f4', '313123131', '菜单删除', 2, '822424f4-f96f-4c1c-95e7-001faf9f4eea', 0, NULL, NULL, 'menu:delete', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-12-13 12:10:18', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('8dae1531-bc6f-4b59-a275-fb15282cfef1', '826b4285-5602-467d-aea4-dcf7f8678424', '流程面板', 1, '', 9, '/bpmns/draw/:id?', 'bpmns-draw/Index', NULL, 'ant-design:ci-twotone', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-04-27 16:38:18', 'admin', '2023-08-18 10:17:33', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('8fb55b34-66f0-46a2-9842-b20700c6b465', '313123131', '任务执行记录', 1, '164e93b1-461e-4c6a-aec9-cdf0fa984158', 72, '/task/record', '/task/TaskInstance', NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-04-12 13:21:53', '13300000002', '2023-12-14 10:51:18', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('9058c86f-b402-4cd3-8def-4d05160a13de', '313123131', '租户停用', 2, 'a255f616-815a-4845-b6aa-5fd998dac3a2', 1, NULL, NULL, 'tenant:stop', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-07 16:20:58', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('90d5c59b-55b6-4dcd-867c-ec0567667262', '313123131', '模板新增', 2, 'd0aa8da7-7760-4135-aa80-2c354bbcdf0d', 0, NULL, NULL, 'template:add', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-10-13 11:05:59', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('9227fe1e-0963-429a-a689-d5cec1289102', '30732c59-9d74-46be-8526-d8d58465ba9d', '添加成员', 2, '77b94c28-59ba-4c89-95cd-1eea7d61f49d', 1, NULL, NULL, 'user:add', NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-04-23 10:29:33', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('929dfa20-8337-4cee-afe5-b7827ccc2a6d', '490489ab-d8b4-414c-ad77-d856962c286f', '用户中心', 1, '', 4, '/user/centerInfo', '/sys/user/UserCenter', NULL, '', 0, 0, 0, 0, NULL, 1, 0, NULL, '13601301008', '2023-12-01 18:21:35', '13300000000', '2023-12-13 18:18:24', 0, NULL, NULL, 1, '', 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('954a9e47-9727-49f9-b213-d1bfad562467', '30732c59-9d74-46be-8526-d8d58465ba9d', '批量删除', 2, '77b94c28-59ba-4c89-95cd-1eea7d61f49d', 1, NULL, NULL, 'user:deletebatch', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-04-23 10:28:49', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('95bef937-64ee-4104-ad69-2a3220b45527', '70588803-52e4-433d-a61f-0a68e1febd72', '密码规则设置', 1, '8abf3229-9b85-4177-bc40-e0552ec8b8de', 51, '/safe/password/rule', '/sys/safe/Password', NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-12-08 14:53:38', NULL, NULL, 0, NULL, NULL, 1, '', 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('9604d7c5-2e8e-476f-90fc-f45826b92d4d', '30732c59-9d74-46be-8526-d8d58465ba9d', '新增权限组', 2, 'bed526d3-08f4-480a-b185-d81260bec3a1', 1, NULL, NULL, '9', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-04-24 15:43:29', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('9643f1af-4132-45dc-94f7-79367ff79e54', '490489ab-d8b4-414c-ad77-d856962c286f', '印章状态变更', 1, 'f644efc5-1fad-43e7-b608-f9ebaaa02197', 999, '/seals/status/process', '/signature/form/SealStatusProcess', NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, '13271928495', '2023-09-08 15:30:18', '13271928495', '2023-09-08 15:30:45', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('970c5276-12c1-40a2-b0b4-375540faa804', '313123131', '租户启用', 2, 'a255f616-815a-4845-b6aa-5fd998dac3a2', 1, NULL, NULL, 'tenant:start', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-07 16:20:42', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('9790d902-372c-4dcd-98d2-0db3db02f95d', '490489ab-d8b4-414c-ad77-d856962c286f', '指定位置与参数设置', 1, 'f3c931cb-5955-495c-9b9a-d578cebefca2', 122, '/contract/position/:signReId?/:signRuId?/:__full__?/:isDetail?', '/contract/position/index', NULL, NULL, 0, 1, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-11-06 14:52:35', 'admin', '2023-12-13 16:42:33', 0, NULL, NULL, 0, '', 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('9832788e-fd2a-4a4b-b2f5-e74f8947cba2', '30732c59-9d74-46be-8526-d8d58465ba9d', '邀请成员加入', 2, '77b94c28-59ba-4c89-95cd-1eea7d61f49d', 1, NULL, NULL, 'invite:user', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-04-23 10:29:56', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('98e7b2ce-be53-4285-ba5f-4aae2a80c383', '490489ab-d8b4-414c-ad77-d856962c286f', '账号管理', 1, '', 43, '/user/center1', '/sys/user/UserInfo', NULL, NULL, 0, 1, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-10-16 10:15:30', '13300000000', '2023-12-13 18:05:38', 0, NULL, NULL, 0, '', '', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('98ed899a-2084-4422-ace0-fc9b0ff9f3e7', '13131313', '角色添加人员', 2, '6033bf17-2109-4622-afdc-bee2380f3012', 1, NULL, NULL, 'role:adduser', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-05 18:13:33', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('9a7232d1-c819-484c-bff4-2d3c076d2dd5', '313123131', '删除字典分类', 2, '3ab5da08-9e72-40d8-a182-77ad93848721', 1, NULL, NULL, 'dictcategory:delete', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-29 11:23:37', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('9a73f770-13ca-450d-80ca-ac50d1de4eee', '313123131', '消息公告管理', 0, '', 4, '/msg', 'LAYOUT', NULL, 'ant-design:appstore-outlined', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-04-12 11:47:40', 'admin', '2023-12-08 15:01:08', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('9b596006-7155-4b45-a417-751e85fb29f3', '313123131', '权限策略编辑', 2, '0d41fc8e-99e4-49fe-9046-d46e632273b8', 1, NULL, NULL, 'authploy:edit', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-07 16:19:25', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('9b596006-7155-4b45-a4s17-751e85fb29f3', '30732c59-9d74-46be-8526-d8d58465ba9d', '权限策略编辑', 2, '836ca3f0-91ac-4201-8be7-9d444b14f42c', 1, NULL, NULL, 'authploy:edit', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-07 16:19:25', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('9b744648-be9f-4ec6-815c-4dc751dc3b12', '826b4285-5602-467d-aea4-dcf7f8678424', '我的待阅', 1, '', 2, '/bpmns/application/pending', 'bpmns/pending/Pending', NULL, 'ant-design:read-filled', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-04-27 16:22:18', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('9c047fb8-b06d-40bb-81aa-9151c3a8da3f', 'b7ec2585-f068-47d7-b3b9-9dae87be22c4', '消息中心', 1, '', 1, '/message', '/message/index', NULL, 'ant-design:cloud-twotone', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-10-13 11:29:02', NULL, NULL, 0, NULL, NULL, 1, '', '', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('a0a96cd9-6a2d-4cf6-a4f4-60d3ef59035a', '70588803-52e4-433d-a61f-0a68e1febd72', 'IP黑白名单设置', 1, '8abf3229-9b85-4177-bc40-e0552ec8b8de', 53, '/safe/ip', '/sys/safe/IPLimit', NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-12-08 14:56:32', NULL, NULL, 0, NULL, NULL, 1, '', 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('a16b91f8-c5ca-47ba-9c6b-e97c1d97cc09', '330ba5b5-2339-49ce-9cf0-e032528eff65', '用户中心', 0, '', 1, '1', NULL, NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, '13793014727', '2023-01-16 10:43:22', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('a1b4cc9a-6525-4946-8081-5f176dfed6e0', '826b4285-5602-467d-aea4-dcf7f8678424', '我发起的', 1, '', 4, '/bpmns/application/track', 'bpmns/track/Track', NULL, 'ant-design:flag-filled', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-04-27 16:24:31', 'admin', '2023-09-19 14:37:37', 0, NULL, NULL, 1, '', '674df8a5-49c0-42ab-adea-8efccd2d3860', 1);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('a255f616-815a-4845-b6aa-5fd998dac3a2', '313123131', '租户管理', 1, '', 3, '/tenant', '/tenant/index', NULL, 'ant-design:bank-filled', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-01-05 17:16:37', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('a255f616-815a-4845-b6aa-5fd998dac3a4', '313123131', '菜单编辑', 2, '822424f4-f96f-4c1c-95e7-001faf9f4eea', 2, NULL, NULL, 'menu:edit', NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, NULL, NULL, NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('a2699e8c-dbe9-41c1-8e8a-8c9930ec9fbd', '13131313', '角色移除人员', 2, '6033bf17-2109-4622-afdc-bee2380f3012', 1, NULL, NULL, 'role:removeuser', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-05 18:13:54', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('a3c61b3e-5913-406a-b1d0-fd8a2c291094', '30732c59-9d74-46be-8526-d8d58465ba9d', '删除权限组', 2, 'bed526d3-08f4-480a-b185-d81260bec3a1', 1, NULL, NULL, '7', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-04-24 15:42:50', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('a42e3e3f-3609-436e-ae51-da2b01b22d56', '13131313', '编辑部门', 2, 'bbb95924-a8a4-408c-b61e-3a989409b3c6', 1, NULL, NULL, 'dept:edit', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-11 16:51:45', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('aa2fd62d-ee25-4290-9205-cb55d43f6615', '30732c59-9d74-46be-8526-d8d58465ba9d', '角色添加人员', 2, 'bed526d3-08f4-480a-b185-d81260bec3a1', 1, NULL, NULL, '10', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-04-24 15:43:42', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('ac7ef3fa-edad-45d7-b796-77827a5fe91b', '490489ab-d8b4-414c-ad77-d856962c286f', '签名管理', 1, '', 42, '/sign/manage', '/signature/sign/index', NULL, 'ant-design:money-collect-outlined', 0, 1, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-08-14 14:19:15', '13300000000', '2023-12-13 18:05:34', 0, NULL, NULL, 0, '', NULL, 1);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('afcc134e-9069-4f2d-8e36-0b87e76684b2', '30732c59-9d74-46be-8526-d8d58465ba9d', '新增权限组分类', 2, 'bed526d3-08f4-480a-b185-d81260bec3a1', 1, NULL, NULL, '5', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-04-24 15:42:22', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('afe2db72-1307-48cd-9168-86381a327e93', '313123131', '数据列表', 2, 'c2d34263-65de-4c17-ad2a-151e1df197e3', 1, NULL, NULL, 'testdata:list', NULL, 0, 0, 0, 0, NULL, 1, 1, '141241414', 'admin', '2023-01-16 17:01:08', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('b0a16615-1931-4fdc-9136-70b6954e57ae', '313123131', '编辑字典分类', 2, '3ab5da08-9e72-40d8-a182-77ad93848721', 1, NULL, NULL, 'dictcategory:edit', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-29 11:22:59', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('b0f7f7c1-4b6e-4a15-bcd0-3b7453170285', '13131313', '新增角色组', 2, '6033bf17-2109-4622-afdc-bee2380f3012', 1, NULL, NULL, 'rolegroup:add', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-05 18:12:17', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('b234fc85-ba18-4caa-a98d-ebd6d515bb9c', '330ba5b5-2339-49ce-9cf0-e032528eff65', '版权登记', 1, 'ef0ee2e7-ee40-4dbf-b832-1d425391dbca', 1, '11', NULL, NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, '13793014727', '2023-01-16 10:43:41', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('b3b95e14-1aa2-4a1d-b373-3fba540b9f3f', '13131313', '部门管理', 1, 'dbdcd97e-d02a-4592-930d-81c5adaaa815', 22, '/organize/dept', '/tenant/organize/index', NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-12-08 11:26:49', 'admin', '2023-12-13 16:35:15', 0, NULL, NULL, 1, '', 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('b4334106-7967-4f9e-9353-7045d21dae09', '313123131', '应用新增', 2, '822424f4-f96f-4c1c-95e7-001faf9f4eea', 1, NULL, NULL, 'app:add', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-07 16:21:57', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('b4ed03ce-5cb2-4c3a-b152-9137c81058f4', '490489ab-d8b4-414c-ad77-d856962c286f', '签约详情', 1, 'f3c931cb-5955-495c-9b9a-d578cebefca2', 124, '/contract/detail/sign/:__full__?/:signRuId?', '/contract/detail/signDetail', NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-11-24 14:46:25', 'admin', '2023-12-13 16:42:53', 0, NULL, NULL, 0, '', 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('b72b7e3d-349f-414f-8e0e-a8177d5a9dcf', '30732c59-9d74-46be-8526-d8d58465ba9d', '权限策略删除', 2, 'bed526d3-08f4-480a-b185-d81260bec3a1', 1, NULL, NULL, '15', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-04-24 15:45:00', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('b72b8b58-945e-4636-b8bd-e5b7efb9328b', '313123131', '删除字典', 2, '3ab5da08-9e72-40d8-a182-77ad93848721', 1, NULL, NULL, 'dict:delete', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-29 11:23:55', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('b859dd18-446a-4904-918e-02c883a9df65', '826b4285-5602-467d-aea4-dcf7f8678424', '任务委托', 1, '', 5, '/bpmns/application/entrust', 'bpmns/entrust/Index', NULL, 'ant-design:video-camera-twotone', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-04-27 16:25:45', 'admin', '2023-08-18 10:17:48', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('bbb95924-a8a4-408c-b61e-3a989409b3c6', '13131313', '成员管理', 1, 'dbdcd97e-d02a-4592-930d-81c5adaaa815', 21, '/organize/user', '/tenant/organize/index', NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-12-08 11:26:02', 'admin', '2023-12-13 16:35:06', 0, NULL, NULL, 1, '', 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('bdf0166b-a5c1-4719-8d5d-bf3a032170b4', '13131313', '新增角色', 2, '6033bf17-2109-4622-afdc-bee2380f3012', 1, NULL, NULL, 'role:add', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-05 18:12:32', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('be9cb5bb-5990-4ff0-8884-b139ba7002fe', '30732c59-9d74-46be-8526-d8d58465ba9d', '新增角色', 2, 'bed526d3-08f4-480a-b185-d81260bec3a1', 1, NULL, NULL, '13', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-04-24 15:44:28', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('bed526d3-08f4-480a-b185-d81260bec3a1', '30732c59-9d74-46be-8526-d8d58465ba9d', '应用管理', 1, '', 2, '/tenant/application', '/tenant/application/index', NULL, 'ant-design:appstore-filled', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-04-24 15:36:55', 'admin', '2023-04-24 15:45:04', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('c0c1cd5a-a11a-40e5-b9e3-f8c7064930d4', '30732c59-9d74-46be-8526-d8d58465ba9d', '设置主管', 2, '77b94c28-59ba-4c89-95cd-1eea7d61f49d', 1, NULL, NULL, 'dept:setmanager', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-04-23 10:29:13', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('c4f29c35-8b8c-4cef-9119-aab9bfd21a5d', '70588803-52e4-433d-a61f-0a68e1febd72', '隐私政策', 1, 'c6ae153e-bbd6-45c6-938d-28e3c516eeb0', 52, '/protocol/privacy', '/protocol/privacy', NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-10-25 15:40:29', NULL, NULL, 0, NULL, NULL, 1, '', 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('c6ae153e-bbd6-45c6-938d-28e3c516eeb0', '70588803-52e4-433d-a61f-0a68e1febd72', '协议与声明', 0, '', 4, '/protocol', 'LAYOUT', NULL, 'ant-design:fund-outlined', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-10-25 15:14:18', 'admin', '2023-12-08 14:50:29', 0, NULL, NULL, 1, '', 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('c7c78143-0eb4-411d-a91c-4b52686e0247', '30732c59-9d74-46be-8526-d8d58465ba9d', '权限组管理', 1, '035faa2f-4aad-45ac-a303-988c7a568347', 41, '/auth/group', '/auth/authGroup', NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-04-23 10:24:10', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('c9f25c08-a442-4bb6-9d02-9526d295fa56', '490489ab-d8b4-414c-ad77-d856962c286f', '签署页面', 1, 'f3c931cb-5955-495c-9b9a-d578cebefca2', 55, '/contract/sign/:signReId?/:signRuId?/:__full__?/:type?/:isDetail?/:from?', '/contract/sign/index', NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-11-17 17:52:12', 'admin', '2023-12-13 16:42:05', 0, NULL, NULL, 0, '', 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('cad8a8ec-c75c-466b-b028-8e32d0e960f2', '13131313', '应用管理', 1, '', 5, '/tenant/application', '/tenant/application/index', NULL, 'ant-design:code-sandbox-outlined', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-01-05 16:59:34', 'admin', '2023-12-08 11:22:27', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('cb84167c-b17c-4a19-b733-7aaca2f028e1', '30732c59-9d74-46be-8526-d8d58465ba9d', '添加子部门', 2, '77b94c28-59ba-4c89-95cd-1eea7d61f49d', 1, NULL, NULL, 'dept:addchild', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-04-23 10:30:36', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('cd686ddd-a565-4e73-b061-90eaf6c6b333', '313123131', '平台公告', 1, '9a73f770-13ca-450d-80ca-ac50d1de4eee', 33, '/msg/announce', '/message/Announcement', NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-04-12 13:14:55', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('cd6c3fc1-e71b-4ca3-b2f7-b48bf8a10fb9', '13131313', '流程管理', 1, 'ca65efb3-76e8-4c67-aa29-3a2e10a1d43d', 51, '/bpms/manage', '/bpms/manage', NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, '18612927144', '2023-04-04 18:00:05', 'admin', '2023-09-19 14:40:27', 0, NULL, NULL, 1, '', NULL, 1);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('ce84ec5b-5b5d-4ea9-ae08-c180866f5f9a', '30732c59-9d74-46be-8526-d8d58465ba9d', '组织管理', 0, '', 2, '/organize', 'LAYOUT', NULL, 'ant-design:apartment-outlined', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-04-23 10:22:21', 'admin', '2023-04-23 16:41:08', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('cec8dd67-bc4e-496d-8597-c09dc191395e', '13131313', '印章编辑', 1, '', 30, '/seals/edit', '/signature/form/SealCreatedEdit', NULL, NULL, 0, 1, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-08-18 17:43:01', 'admin', '2023-12-13 16:39:20', 0, NULL, NULL, 0, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('d0aa8da7-7760-4135-aa80-2c354bbcdf0d', '313123131', '消息模板', 1, '9a73f770-13ca-450d-80ca-ac50d1de4eee', 31, '/msg/msgtpl', '/message/Template', NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-04-12 13:08:19', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('d0c2ca72-12da-4730-8293-3d081fe5bb2f', '313123131', '租户开通', 2, 'a255f616-815a-4845-b6aa-5fd998dac3a2', 1, NULL, NULL, 'tenant:open', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-07 16:20:08', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('d8a20780-ba20-4968-8b81-03e2d96f80f1', '30732c59-9d74-46be-8526-d8d58465ba9d', '调整部门', 2, '77b94c28-59ba-4c89-95cd-1eea7d61f49d', 1, NULL, NULL, 'user:changedepart', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-04-23 10:30:57', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('dbdcd97e-d02a-4592-930d-81c5adaaa815', '13131313', '组织管理', 0, '', 2, '/organize', 'LAYOUT', NULL, 'ant-design:apartment-outlined', 0, 0, 0, 0, NULL, 1, 0, NULL, '18612927144', '2023-04-04 17:42:37', '13522104081', '2023-12-14 10:02:39', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('de063884-2b7e-4c0e-b27e-34c99280bf28', '313123131', '公告编辑', 2, 'cd686ddd-a565-4e73-b061-90eaf6c6b333', 0, NULL, NULL, 'announcement:edit', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-10-13 14:32:54', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('de81ec9b-f423-415a-a7c6-d72da9714e7d', '123456789', '首页', 1, '', 0, '/dashboard/workbench/', '/dashboard/workbench/index', NULL, 'ant-design:home-outlined', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-08-14 17:46:26', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('e16279e6-ca88-43f7-90c7-5513673ef869', '490489ab-d8b4-414c-ad77-d856962c286f', '模板变更', 1, 'f644efc5-1fad-43e7-b608-f9ebaaa02197', 999, '/seals/template/change/process', '/signature/form/TemplateChangeProcess', NULL, NULL, 0, 1, 0, 0, NULL, 1, 0, NULL, '13271928495', '2023-09-20 11:08:09', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('e2390176-7732-489e-ba36-7d25452f0368', '313123131', '公告新增', 2, 'cd686ddd-a565-4e73-b061-90eaf6c6b333', 0, NULL, NULL, 'announcement:add', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-10-13 14:18:41', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('e3c53f1d-8001-4727-b6b7-f2570df48123', '13131313', '添加子部门', 2, 'bbb95924-a8a4-408c-b61e-3a989409b3c6', 1, NULL, NULL, 'dept:addchild', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-11 16:52:02', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('e3ddc121-0021-4a25-8b14-c3572f053dde', '30732c59-9d74-46be-8526-d8d58465ba9d', '权限策略新建', 2, 'bed526d3-08f4-480a-b185-d81260bec3a1', 1, NULL, NULL, '3', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-04-24 15:41:45', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('e4222217-b30d-41f4-a17c-9ac12fa4c3c9', '30732c59-9d74-46be-8526-d8d58465ba9d', '新增角色组', 2, 'bed526d3-08f4-480a-b185-d81260bec3a1', 1, NULL, NULL, '12', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-04-24 15:44:15', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('e4eee1a4-791e-4f85-9c81-ee85e69c9b23', '70588803-52e4-433d-a61f-0a68e1febd72', '用户管理', 0, '', 1, '/usermanage', 'LAYOUT', NULL, 'ant-design:user-switch-outlined', 0, 0, 0, 0, NULL, 1, 0, NULL, '13341081511', '2023-10-12 17:26:47', NULL, NULL, 0, NULL, NULL, 1, '', 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('e9bee19a-d254-4492-8afb-b658aa864f6a', '70588803-52e4-433d-a61f-0a68e1febd72', '企业用户', 1, 'e4eee1a4-791e-4f85-9c81-ee85e69c9b23', 12, '/usermanage/enperprise', '/users/enterprise', NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, '13341081511', '2023-10-12 17:28:14', NULL, NULL, 0, NULL, NULL, 1, '', 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('eb307448-927f-48e2-b66b-73726fdb2943', '826b4285-5602-467d-aea4-dcf7f8678424', '我的待办', 1, '', 1, '/bpmns/application/upcoming', 'bpmns/upcoming/Upcoming', NULL, 'ant-design:alert-filled', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-04-27 16:21:08', 'admin', '2023-08-19 12:16:46', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('ee25afe0-0865-4b91-8efa-6c1a4e007002', '13131313', '调整部门', 2, 'bbb95924-a8a4-408c-b61e-3a989409b3c6', 1, NULL, NULL, 'user:changedepart', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-11 16:54:25', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('ef0ee2e7-ee40-4dbf-b832-1d425391dbca', '330ba5b5-2339-49ce-9cf0-e032528eff65', '版权登记管理', 0, '', 1, '1', NULL, NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, '13793014727', '2023-01-16 10:33:51', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('ef271138-e07b-40df-bb8d-482c82733e11', '13131313', '编辑权限组分类', 2, '72a9b863-e338-4dec-bd2b-a806b65f506c', 1, NULL, NULL, 'authcategory:edit', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-05 18:03:43', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('ef271138-e07b-40sdf-bb8d-482c82733e11', '30732c59-9d74-46be-8526-d8d58465ba9d', '编辑权限组分类', 2, 'c7c78143-0eb4-411d-a91c-4b52686e0247', 1, NULL, NULL, 'authcategory:edit', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-05 18:03:43', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('f15b2fb6-d17e-4619-b194-45c5df276350', '70588803-52e4-433d-a61f-0a68e1febd72', '服务协议', 1, 'c6ae153e-bbd6-45c6-938d-28e3c516eeb0', 51, '/protocol/serve', '/protocol/serve', NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-10-25 15:38:39', NULL, NULL, 0, NULL, NULL, 1, '', 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('f3c931cb-5955-495c-9b9a-d578cebefca2', '490489ab-d8b4-414c-ad77-d856962c286f', '文件签署', 0, '', 12, '/contract', 'lAYOUT', NULL, 'ant-design:ant-design-outlined', 0, 1, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-11-06 14:37:47', 'admin', '2023-12-13 16:41:57', 0, NULL, NULL, 0, '', 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('f466f2fe-6e90-4ed4-84ad-ec85a9e5bf97', '313123131', '公告发布', 2, 'cd686ddd-a565-4e73-b061-90eaf6c6b333', 0, NULL, NULL, 'announcement:release', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-10-13 14:29:09', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('f4a3acd8-4b31-4de2-9f6f-b4b849080a3f', '30732c59-9d74-46be-8526-d8d58465ba9d', '删除成员', 2, 'bed526d3-08f4-480a-b185-d81260bec3a1', 1, NULL, NULL, '2', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-04-24 15:41:30', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('f591252e-7651-45bb-9070-dfe4a15d6ea0', '13131313', '证书管理', 1, '', 4, '/certification/enterprise', '/certification/enterprise', NULL, 'ant-design:amazon-outlined', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-12-07 19:35:13', 'admin', '2023-12-08 11:22:15', 0, NULL, NULL, 1, '', 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('f5dcde63-0bdb-4fd9-ab62-5aa36c3eb1f4', '826b4285-5602-467d-aea4-dcf7f8678424', '我的已阅', 1, '', 3, '/bpmns/application/read', 'bpmns/read/Read', NULL, 'ant-design:file-done-outlined', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-04-27 16:23:07', 'admin', '2023-04-27 16:32:14', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('f638163b-ee94-48fe-85cc-b3e519c99a38', '13131313', '新增印章', 1, '', 30, '/seals/created', '/signature/seal/SealCreated', NULL, NULL, 0, 1, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-11-23 16:27:34', 'admin', '2023-12-13 16:39:27', 0, NULL, NULL, 0, '', 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('f676e451-d0c5-4398-ad22-a9ca0f5d6aaa', '313123131', '发送记录', 1, '9a73f770-13ca-450d-80ca-ac50d1de4eee', 32, '/msg/send', '/message/MyMessage', NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-04-12 13:09:32', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('f927a0a7-a37a-49d4-8284-2922c4d2f4b9', '13131313', '流程监控', 1, 'ca65efb3-76e8-4c67-aa29-3a2e10a1d43d', 52, '/bpms/monitor', 'bpms/monitor', NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, '18612927144', '2023-04-04 18:02:58', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('f9d05ba1-d802-4832-8f75-c0896db62d11', '70588803-52e4-433d-a61f-0a68e1febd72', '数字证书使用协议', 1, 'c6ae153e-bbd6-45c6-938d-28e3c516eeb0', 53, '/protocol/certificate', '/protocol/certificate', NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-10-25 15:42:00', NULL, NULL, 0, NULL, NULL, 1, '', 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('fc8da44b-3a70-4864-a838-6244ad11880f', '70588803-52e4-433d-a61f-0a68e1febd72', '企业证书', 1, '0ca98386-4d83-408d-896b-fa486e8f61e0', 22, '/certification/enterprise', '/certificate/enterprise', NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, '13341081511', '2023-10-12 17:30:20', NULL, NULL, 0, NULL, NULL, 1, '', 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('fd17a8bb-e5ed-42aa-98ea-4706f8f35508', '30732c59-9d74-46be-8526-d8d58465ba9d', '编辑权限组分类', 2, 'bed526d3-08f4-480a-b185-d81260bec3a1', 1, NULL, NULL, '14', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-04-24 15:44:45', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('fe7d832f-a185-4cb8-9ac7-f0cc9f39fe11', '13131313', '权限策略删除', 2, '72a9b863-e338-4dec-bd2b-a806b65f506c', 1, NULL, NULL, 'authploy:delete', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-05 18:08:23', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('fea5091b-da9a-467b-8430-0da02548adbcd', '123456789', '工作台', 1, '', 1, '/dashboard/workbench/:page*', '/dashboard/workbench/index', NULL, NULL, 1, 1, 0, 0, NULL, 1, 0, NULL, NULL, NULL, 'admin', '2023-08-14 17:51:36', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('s21da524a-dc26-4227-bdc7-5cd9b6919be5', '30732c59-9d74-46be-8526-d8d58465ba9d', '权限策略新建', 2, '836ca3f0-91ac-4201-8be7-9d444b14f42c', 1, NULL, NULL, 'authploy:add', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-07 16:18:50', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('22ec8ed1-5ad3-4867-a711-850828cb1da4', '13131313', '模板日志', 1, '', 999, '/template/record', '/template/record', NULL, NULL, 0, 1, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-12-26 14:46:22', 'admin', '2023-12-29 20:06:43', 0, NULL, NULL, 0, NULL, 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('84b01339-7754-48c0-b1de-f4520f3e44cf', '13131313', '模板预览', 1, '', 999, '/template/preview', '/template/TemplatePreview', NULL, NULL, 0, 1, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-12-26 14:08:18', 'admin', '2023-12-29 20:06:58', 0, NULL, NULL, 0, NULL, 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('123a667c-15d8-48cd-bf42-93cfd039442b', '313123131', '删除', 2, '7c499e02-72a2-46da-b676-bb8fe14f29df', 4, NULL, NULL, 'globalId:delete', NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-12-25 17:35:14', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('63b5dbd8-a773-4ae5-8878-e30fc61a37a8', '313123131', '查看', 2, '7c499e02-72a2-46da-b676-bb8fe14f29df', 3, NULL, NULL, 'globalId:info', NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-12-25 17:34:54', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('3c775f1a-6733-4b7e-b9c7-cc20981b34d7', '313123131', '编辑', 2, '7c499e02-72a2-46da-b676-bb8fe14f29df', 2, NULL, NULL, 'globalId:edit', NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-12-25 17:34:18', 'admin', '2023-12-25 17:34:20', 0, NULL, NULL, NULL, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('e0d27c1a-516a-4f45-92f3-06ea1a22672b', '313123131', '新增', 2, '7c499e02-72a2-46da-b676-bb8fe14f29df', 1, NULL, NULL, 'globalId:add', NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-12-25 17:33:42', 'admin', '2023-12-25 17:34:23', 0, NULL, NULL, NULL, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('9159262d-685f-4294-af9b-2894f6523da3', '13131313', '设置模板控件', 1, '', 999, 'template/setting/control', 'template/SettingControl', NULL, NULL, 0, 1, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-12-20 19:17:03', 'admin', '2023-12-29 20:06:54', 0, NULL, NULL, 0, NULL, 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('c57e39c6-3062-4d03-902b-20cf5f01052f', '13131313', '模板管理', 1, '', 5, 'template/manage', 'template/index', NULL, 'ant-design:profile-outlined', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-12-20 16:36:41', NULL, NULL, 0, NULL, NULL, 1, NULL, 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('be2b0983-7dd1-48eb-8bad-7c432da28685', '13131313', '指定位置与参数', 1, 'e11f6d0a-7ca6-4184-8255-ac050a58a17c', 105, '/businessLine/position/:signReId?/:__full__?/:isDetail?', '/businessLine/tabs/position', NULL, NULL, 0, 1, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-12-19 19:00:32', 'admin', '2023-12-29 20:05:22', 0, NULL, NULL, 0, NULL, 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('8db4a2eb-1532-40c8-8986-e7c69bdef985', '13131313', '业务线详情', 1, 'e11f6d0a-7ca6-4184-8255-ac050a58a17c', 103, '/businessLine/detail', '/businessLine/detail', NULL, NULL, 0, 1, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-12-18 14:37:18', 'admin', '2023-12-29 20:05:13', 0, NULL, NULL, 0, NULL, 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('c477bad3-b7cb-444f-bc29-0de184a4270b', '13131313', '业务线配置', 1, 'e11f6d0a-7ca6-4184-8255-ac050a58a17c', 102, '/businessLine/config/:signRuId?/:signReId?/:__full__?/:name?/:from?', '/businessLine/configs', NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-12-18 14:36:29', 'admin', '2023-12-29 20:05:05', 0, NULL, NULL, 0, NULL, 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('37f98649-c5c3-4eb4-8d24-310ce16d6aec', '13131313', '业务线管理', 0, '', 101, '/businessline/manage', '/businessLine/index', NULL, 'ant-design:branches-outlined', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-12-18 14:36:08', 'admin', '2023-12-26 09:58:57', 0, NULL, NULL, 1, NULL, 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('e11f6d0a-7ca6-4184-8255-ac050a58a17c', '13131313', '业务线', 0, '', 10, '/businessline', 'LAYOUT', NULL, 'ant-design:aliwangwang-outlined', 0, 1, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-12-18 14:35:47', 'admin', '2023-12-29 20:09:33', 0, 'admin', '2023-12-29 19:40:26', 0, NULL, 'false', 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('7699487e-6992-4c9c-864d-d541d0129186', '13131313', '模板配置', 1, '', 999, '/template/created', 'template/TemplateCreated', NULL, NULL, 0, 1, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-12-30 14:46:22', 'admin', '2023-12-29 20:06:49', 0, NULL, NULL, 0, '', NULL, 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_permission_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission_data`;
CREATE TABLE `sys_permission_data` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tenant_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户ID',
  `permission_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '权限菜单id',
  `data_type` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '类型',
  `data_name` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `data_desc` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述',
  `default_flag` int(11) DEFAULT NULL COMMENT '默认标识1默认 2非默认',
  `order_no` int(11) DEFAULT NULL COMMENT '排序',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限策略表\n';

-- ----------------------------
-- Records of sys_permission_data
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_permission_data_rule
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission_data_rule`;
CREATE TABLE `sys_permission_data_rule` (
  `id` varchar(64) NOT NULL COMMENT 'ID',
  `data_id` varchar(64) DEFAULT NULL COMMENT '策略ID',
  `condition_group` int(5) DEFAULT NULL COMMENT '条件组',
  `rule_column_id` varchar(64) DEFAULT NULL COMMENT '表字段ID',
  `rule_column` varchar(50) DEFAULT NULL COMMENT '字段',
  `rule_conditions` varchar(50) DEFAULT NULL COMMENT '条件',
  `rule_value` varchar(300) DEFAULT NULL COMMENT '规则值',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_by` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_fucntionid` (`data_id`) USING BTREE,
  KEY `idx_spdr_permission_id` (`data_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='权限策略详情表\n';

-- ----------------------------
-- Records of sys_permission_data_rule
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_position
-- ----------------------------
DROP TABLE IF EXISTS `sys_position`;
CREATE TABLE `sys_position` (
  `id` varchar(64) NOT NULL,
  `code` varchar(100) DEFAULT NULL COMMENT '职务编码',
  `name` varchar(100) DEFAULT NULL COMMENT '职务名称',
  `post_rank` varchar(2) DEFAULT NULL COMMENT '职级',
  `company_id` varchar(255) DEFAULT NULL COMMENT '公司id',
  `create_by` varchar(50) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(50) DEFAULT NULL COMMENT '修改人',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `sys_org_code` varchar(50) DEFAULT NULL COMMENT '组织机构编码',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_code` (`code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='职务表\n';

-- ----------------------------
-- Records of sys_position
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` varchar(64) NOT NULL COMMENT '主键id',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT '租户ID',
  `parent_id` varchar(64) DEFAULT NULL COMMENT '父id',
  `role_name` varchar(200) DEFAULT NULL COMMENT '角色名称',
  `role_code` varchar(100) NOT NULL COMMENT '角色编码',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  `delete_by` varchar(64) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_sys_role_role_code` (`role_code`) USING BTREE,
  KEY `idx_sr_role_code` (`role_code`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_tenant_app_version
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant_app_version`;
CREATE TABLE `sys_tenant_app_version` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tenant_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户ID',
  `app_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '应用id',
  `app_version_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '应用版本id',
  `useful` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否全部可用',
  `status` int(11) DEFAULT NULL COMMENT '状态',
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  `delete_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户应用版本表';

-- ----------------------------
-- Records of sys_tenant_app_version
-- ----------------------------
BEGIN;
INSERT INTO `sys_tenant_app_version` (`id`, `tenant_id`, `app_id`, `app_version_id`, `useful`, `status`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('3850dd29-6cd8-4b43-b1dd-36e970769c0c', 'bf6fbbdd-c424-4cad-b591-9cd5277d2be5', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', 0, 1, 0, NULL, NULL, '12300000000', '2023-12-14 20:03:50', NULL, NULL);
INSERT INTO `sys_tenant_app_version` (`id`, `tenant_id`, `app_id`, `app_version_id`, `useful`, `status`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('690c02e6-604c-47dd-9ec9-7be7c7abf22d', 'bf6fbbdd-c424-4cad-b591-9cd5277d2be5', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', 0, 1, 0, NULL, NULL, '12300000000', '2023-12-14 20:03:50', NULL, NULL);
INSERT INTO `sys_tenant_app_version` (`id`, `tenant_id`, `app_id`, `app_version_id`, `useful`, `status`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('76c158c9-e128-4af7-a579-182b9b7e6483', 'bf6fbbdd-c424-4cad-b591-9cd5277d2be5', '490489ab-d8b4-414c-ad77-d856962c286f', '67265942-ca4a-41c9-be25-e9c85a95a686', 1, 1, 0, NULL, NULL, '12300000000', '2023-12-14 20:03:50', NULL, NULL);
INSERT INTO `sys_tenant_app_version` (`id`, `tenant_id`, `app_id`, `app_version_id`, `useful`, `status`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('bdd77b4d-d16e-458c-b2ed-b905bc91fb58', 'bf6fbbdd-c424-4cad-b591-9cd5277d2be5', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', 1, 1, 0, NULL, NULL, '12300000000', '2023-12-14 20:03:50', NULL, NULL);
INSERT INTO `sys_tenant_app_version` (`id`, `tenant_id`, `app_id`, `app_version_id`, `useful`, `status`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('e2479bde-f0ae-4e53-bae8-00d83b34f9e3', '3efc0be2-3e09-4b6f-8ac0-0e5ea7065d29', '490489ab-d8b4-414c-ad77-d856962c286f', '425865a4-c1bd-4ead-8271-0a25e54da6d0', 1, 1, 0, NULL, NULL, NULL, '2023-12-14 19:59:58', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_tenant_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant_config`;
CREATE TABLE `sys_tenant_config` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tenant_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户ID',
  `name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `type` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '类型',
  `value` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '值',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户配置表';

-- ----------------------------
-- Records of sys_tenant_config
-- ----------------------------
BEGIN;
INSERT INTO `sys_tenant_config` (`id`, `tenant_id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('bac862d9-05bc-4c2b-af81-3f80fd41f214', '3efc0be2-3e09-4b6f-8ac0-0e5ea7065d29', '用户加入租户是否需要审核', 'tenant_user_check', 'false', NULL, '2023-12-14 19:59:58', NULL, NULL);
INSERT INTO `sys_tenant_config` (`id`, `tenant_id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f16ec09c-3bd1-4487-bceb-c460cbd861f2', 'bf6fbbdd-c424-4cad-b591-9cd5277d2be5', '用户加入租户是否需要审核', 'tenant_user_check', 'false', '12300000000', '2023-12-14 20:03:50', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_tenant_info
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant_info`;
CREATE TABLE `sys_tenant_info` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tenant_type` int(3) NOT NULL DEFAULT '2' COMMENT '类型1团体 2个人',
  `tenant_name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户名称',
  `tenant_status` int(3) NOT NULL DEFAULT '1' COMMENT '租户状态',
  `tenant_desc` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户描述',
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  `delete_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `invitation_code` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '企业邀请码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户详情表';

-- ----------------------------
-- Records of sys_tenant_info
-- ----------------------------
BEGIN;
INSERT INTO `sys_tenant_info` (`id`, `tenant_type`, `tenant_name`, `tenant_status`, `tenant_desc`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`, `invitation_code`) VALUES ('3efc0be2-3e09-4b6f-8ac0-0e5ea7065d29', 2, '12300000000', 1, '12300000000', 0, NULL, NULL, NULL, '2023-12-14 19:59:58', NULL, NULL, NULL);
INSERT INTO `sys_tenant_info` (`id`, `tenant_type`, `tenant_name`, `tenant_status`, `tenant_desc`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`, `invitation_code`) VALUES ('bf6fbbdd-c424-4cad-b591-9cd5277d2be5', 1, '开放签平台管理员', 1, NULL, 0, NULL, NULL, '12300000000', '2023-12-14 20:03:50', NULL, NULL, 'VIUUSNIC');
COMMIT;

-- ----------------------------
-- Table structure for sys_tenant_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant_user`;
CREATE TABLE `sys_tenant_user` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tenant_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户ID',
  `user_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户id',
  `nick_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '别称',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态0待激活，1正常 2冻结',
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `add_type` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '添加类型（用户申请，系统自动添加等）',
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  `delete_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户用户表';

-- ----------------------------
-- Records of sys_tenant_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_tenant_user` (`id`, `tenant_id`, `user_id`, `nick_name`, `status`, `email`, `add_type`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('14023c55-ab4c-40f9-b5aa-c74c93b588c7', '3efc0be2-3e09-4b6f-8ac0-0e5ea7065d29', 'be990527-878e-4c30-b0f7-53dde299edb3', '12300000000', 1, NULL, 'system', 0, NULL, NULL, NULL, '2023-12-14 19:59:58', NULL, NULL);
INSERT INTO `sys_tenant_user` (`id`, `tenant_id`, `user_id`, `nick_name`, `status`, `email`, `add_type`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('df3b2a91-d51c-4056-810a-b4c9649e03f7', 'bf6fbbdd-c424-4cad-b591-9cd5277d2be5', 'be990527-878e-4c30-b0f7-53dde299edb3', '12300000000', 1, NULL, 'system', 0, NULL, NULL, '12300000000', '2023-12-14 20:03:50', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_tenant_user_app
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant_user_app`;
CREATE TABLE `sys_tenant_user_app` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tenant_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户ID',
  `user_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户ID',
  `app_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '应用id',
  `app_version_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '应用版本id',
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  `delete_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户用户应用表';

-- ----------------------------
-- Records of sys_tenant_user_app
-- ----------------------------
BEGIN;
INSERT INTO `sys_tenant_user_app` (`id`, `tenant_id`, `user_id`, `app_id`, `app_version_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('3bc5ffca-a136-4fd1-81e4-91d55720d35b', 'bf6fbbdd-c424-4cad-b591-9cd5277d2be5', 'be990527-878e-4c30-b0f7-53dde299edb3', '313123131', 'bfa2c26a-e8a5-4e8a-bf25-1172cc7a4460', 0, NULL, NULL, '12300000000', '2023-12-14 20:03:50', NULL, NULL);
INSERT INTO `sys_tenant_user_app` (`id`, `tenant_id`, `user_id`, `app_id`, `app_version_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('dabeb39f-a7b4-4049-a53c-d78b2036d4d7', 'bf6fbbdd-c424-4cad-b591-9cd5277d2be5', 'be990527-878e-4c30-b0f7-53dde299edb3', '13131313', 'b9ae05f3-1614-44e9-8e92-2ba54ff60bf6', 0, NULL, NULL, '12300000000', '2023-12-14 20:03:50', NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for sys_tenant_user_fast
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant_user_fast`;
CREATE TABLE `sys_tenant_user_fast` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `tenant_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户ID',
  `user_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户id',
  `app_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'appid',
  `permission_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '菜单id',
  `join_id` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '联合id',
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  `delete_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户用户快捷操作表';

-- ----------------------------
-- Records of sys_tenant_user_fast
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_tenant_user_record
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant_user_record`;
CREATE TABLE `sys_tenant_user_record` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '账号ID',
  `tenant_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户ID',
  `tenant_user_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '租户用户id',
  `nick_name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '别称',
  `phone` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机',
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `apply_time` datetime DEFAULT NULL COMMENT '申请日期',
  `status` int(11) NOT NULL DEFAULT '0' COMMENT '状态0待审核，1已通过 2未通过',
  `check_tenant_user_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '审核人',
  `check_time` datetime DEFAULT NULL COMMENT '审核日期',
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  `delete_by` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户用户申请表';

-- ----------------------------
-- Records of sys_tenant_user_record
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_text_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_text_config`;
CREATE TABLE `sys_text_config` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '类型',
  `value` longtext COLLATE utf8mb4_unicode_ci COMMENT '值',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='租户配置表';

-- ----------------------------
-- Records of sys_text_config
-- ----------------------------
BEGIN;
INSERT INTO `sys_text_config` (`id`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('eqerwqrqwrqwr', 'serve', '<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">版本：</span>2023年</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">10</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">月</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">13</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">日</span></p>\n<h4 style=\"margin: 20pt 0pt; font-family: 宋体; font-size: 12pt; padding: 0pt; line-height: 45pt;\"><strong><span style=\"letter-spacing: 0pt; font-size: 16pt;\">一、声明与承诺</span></strong></h4>\n<p class=\"p\" style=\"margin: 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（一）本协议已对与您的权益有或可能具有重大关系的条款，及对</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">北京资源律动科技有限公司</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">（以下简称</span><span style=\"font-family: Microsoft YaHei;\">&ldquo;本公司&rdquo;）具有或可能具有免责或限制责任的条款用粗体斜体和下划线字予以标注，请您注意。</span></span></u></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">您确认，在您注册成为</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">电子签约平台（以下简称</span><span style=\"font-family: Microsoft YaHei;\">&ldquo;</span></span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">&rdquo;或&ldquo;</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">平台</span><span style=\"font-family: Microsoft YaHei;\">&rdquo;）用户以接受本服务，或您以其他本公司允许的方式实际使用本服务前，您已充分阅读、理解并接受本协议的全部内容，一旦您使用本服务，即表示您同意遵循本协议之所有约定。</span></span><strong><u><span class=\"15\" style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">阅读本协议的过程中，如果您不同意本协议或其中任何条款约定，您应立即停止注册程序。</span></u></strong></p>\n<p class=\"p\" style=\"margin: 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（二）根据有关法律法规的最新规定和监管要求，本公司有权对本协议内容进行变更，并以在本网站公告的方式予以公布；若您在本协议内容公告变更后继续使用本服务的，表示您已充分阅读、理解并接受修改后的协议内容，也将遵循修改后的协议内容使用本服务；若您不同意修改后的协议内容，您应停止使用本服务。</span></u></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（三）您保证，在您同意接受本协议并注册成为</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">用户时，您已经具备相应的民事行为能力，或者您是在中国大陆地区依法设立并合法开展经营活动或其他业务的法人或其他组织；本协议内容不受您所属国家或地区法律的排斥。在您注册成为</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">用户时或在此后任何时间不具备前述条件的，您应立即终止注册或停止使用本服务。您在使用</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">服务时，应自行判断对方是否是完全民事行为能力人，且确定他提供的信息和他本人对应，并自行决定是否与对方进行签约交易等，且您应自行承担与此相关的所有风险。本平台不承担因对方身份确认失误而造成的损失。</span></p>\n<p class=\"p\" style=\"margin: 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（四）您保证，在您同意接受本协议并注册成为</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">用户时，即表示您认可使用</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">平台作为缔约（包括但不限于电子合同）方式，认可您在</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">平台上的所有操作都是您个人意愿的表示，认可您在</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">平台上签订的电子合同（或协议）和纸质合同（或协议）具备相同的效力，认可您在</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">平台上的电子签名和您的手写签名具备相同的效力。</span></u></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（五）您同意，就您使用</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">服务，本公司有权将您的身份信息及签约合同信息相应传输至电子认证服务机构（</span><span style=\"font-family: Microsoft YaHei;\">CA, Certificate Authority）或公证处，由CA以出具数字证书之目的或公证处以出具公证文件之目的使用并保存，且前述信息可以保存至数字证书失效后或相关电子数据（包括但不限于签约合同电子数据）产生后10年，并且该等信息不因您终止</span></span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">服务而停止保存。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（六）您同意，就您使用</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">服务，本公司有权将您的身份信息、证件复印件或扫描件相应传输至第三方实名认证服务提供商及其他服务提供商，由该等机构以验证签约方身份之目的使用并保存。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（七）您确认并知悉，</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">服务不适用于以下数据电文等法律文件的签订：</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">1、涉及婚姻、收养、继承等人身关系的；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">2、涉及停止供水、供热、供气等公用事业服务的；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3、法律、行政法规规定的不适用电子文书的其他情形。</span></p>\n<h4 style=\"margin: 20pt 0pt; font-family: 宋体; font-size: 12pt; padding: 0pt; line-height: 45pt;\"><strong><span style=\"letter-spacing: 0pt; font-size: 16pt;\">二、定义及解释</span></strong></h4>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><strong><span class=\"15\" style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（一）</span></strong><strong><span class=\"15\" style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></strong><strong><span class=\"15\" style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">账户（或</span><span style=\"font-family: Microsoft YaHei;\">&ldquo;该账户&rdquo;）：</span></span></strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">是本平台向您提供的唯一编号。您可自行为该</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">账户设置密码，并用以查询或发起代表你个人、公司或者组织的签约过程。您需使用本人</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">账号或</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">手机号码，作为登录手段登录</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">账户。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><strong><span class=\"15\" style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（二）数据源：</span></strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">指合法持有您的身份信息并依法向本平台及相关供应商提供实名认证比对结果的主体，包括：（</span><span style=\"font-family: Microsoft YaHei;\">1）依法运营政府部门数据库的主体，包括各级行政机关、政府部门、公共管理部门、司法机关、公共信用信息管理部门等主体；（2）依法运营商业化数据库的主体，包括电信运营商、金融机构等企事业单位。具体以实际合作情况为准。</span></span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><strong><span class=\"15\" style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（三）身份信息：</span></strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">指您的姓名、国籍、性别、学历学籍、职业、住址（注册地址）、联系方式、营业执照、法定代表人、注册资本、经营期限、统一社会信用代码、经营范围以及您有效证件的种类、号码和有效期限等能够依法识别您身份的信息。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><strong><span class=\"15\" style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（四）您：</span></strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">指注册获得</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">账号并使用</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">各项服务的自然人、法人或其他组织。</span></p>\n<h4 style=\"margin: 20pt 0pt; font-family: 宋体; font-size: 12pt; padding: 0pt; line-height: 45pt;\"><strong><span style=\"letter-spacing: 0pt; font-size: 16pt;\">三、</span></strong><strong><span style=\"letter-spacing: 0pt; font-size: 16pt;\">开放签</span></strong><strong><span style=\"letter-spacing: 0pt; font-size: 16pt;\">服务</span></strong></h4>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><strong><span class=\"15\" style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（一）</span></strong><strong><span class=\"15\" style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></strong><strong><span class=\"15\" style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">服务（以下简称为本服务）：</span></strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">指本条（二）所列的服务，及您实际使用的本公司或本公司接受您的委托为您不时提供的服务以及提供的其他服务。</span></p>\n<p class=\"p\" style=\"margin: 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（二）</span></strong><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></strong><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">服务包括（但不限于）以下服务：</span></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">1、电子协议服务</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">通过</span><span style=\"font-family: Microsoft YaHei;\">&ldquo;</span></span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">&rdquo;平台，本公司向注册用户提供在该平台上的电子协议发送，签署，接受等服务。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">2</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">、对于通过</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">平台签订的数据电文等法律文件的，本公司有权将前述法律文件的签署信息如当事人姓名或者名称、统一社会信用代码证（或者组织机构代码证）等号码、身份证号码、数字证书号码、</span><span style=\"font-family: Microsoft YaHei;\">IP地址、签订时间等披露给数据电文的签约当事人作为法律文件签订证据。</span></span></p>\n<h4 style=\"margin: 20pt 0pt; font-family: 宋体; font-size: 12pt; padding: 0pt; line-height: 45pt;\"><strong><span style=\"letter-spacing: 0pt; font-size: 16pt;\">四、</span></strong><strong><span style=\"letter-spacing: 0pt; font-size: 16pt;\">开放签</span></strong><strong><span style=\"letter-spacing: 0pt; font-size: 16pt;\">账户</span></strong></h4>\n<p class=\"p\" style=\"margin: 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（一）注册相关</span></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">除本协议另有规定或相关产品另有规则外，您须在本网站及</span><span style=\"font-family: Microsoft YaHei;\">/或无线客户端注册并取得本公司提供给您的</span></span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">账户，并且按照本公司要求提供相关信息完成激活后方可使用本服务。您同意：</span></p>\n<p class=\"p\" style=\"margin: 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">1、按照本公司要求准确提供并在取得该账户后及时更新您正确、最新及完整的身份信息及相关资料。若本公司有合理理由怀疑您提供的身份信息及相关资料错误、 不实、过时或不完整的，本公司有权暂停或终止向您提供部分或全部</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">服务。本公司对此不承担任何责任，您将承担因此产生的任何直接或间接支出。若因国家</span> <span style=\"font-family: Microsoft YaHei;\">法律法规、部门规章或监管机构的要求，本公司需要您补充提供任何相关资料时，如您不能及时配合提供，本公司有权暂停或终止向您提供部分或全部</span></span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">服务。</span></u></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">2、您应当准确提供并及时更新您提供的电子邮件地址、联系电话、联系地址、邮政编码等联系方式，以便本公司与您进行及时、有效联系。您应完全独自承担因通过这些联系方式无法与您取得联系而导致的您在使用本服务过程中遭受的任何损失或增加任何费用等不利后果。您理解并同意，您有义务保持您提供的联系方式的有效性，如有变更需要更新的，您应按本公司的要求进行操作。</span></p>\n<p class=\"p\" style=\"margin: 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3、您应及时更新资料（包括但不限于身份证、营业执照等证件或其他身份证明文件、联系方式、作为</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">登录名的邮箱或手机号码、与</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">账户绑定的邮</span> <span style=\"font-family: Microsoft YaHei;\">箱、手机号码等），否则</span></span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">有权将</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">登录名、</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">账户绑定的邮箱、手机号码开放给其他用户注册或使用。因您未及时更新资料导致的一切后果，均应由</span> <span style=\"font-family: Microsoft YaHei;\">您自行承担，该后果包括但不限于导致本服务无法提供或提供时发生任何错误、账户及账户内信息被别人盗用，且您不得将此作为取消交易、拒绝执行协议的理由。</span></span></u></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">4、您确认，只有您本人可以使用您的</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">账户。在您决定不再使用该账户时，您应按照本条（四）的规定向本公司申请注销该账户。您同意，若您丧失全部或部分民事权利能力或民事行为能力，本公司有权根据有效法律文书（包括但不限于生效的法院判决、生效的遗嘱等）或本公司认可的其他资料处置您</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">账户内的资料。</span></p>\n<p class=\"p\" style=\"margin: 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">5、您确认，本公司有权在必要时要求核对您的有效身份证件或其他必要文件，并留存有效身份证件的彩色扫描件，您应积极配合，否则本公司有权限制或停止向您提供部分或全部</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">服务：</span></u></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">A、您要求变更身份信息或您身份信息已过有效期的；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">B、本公司认为您的交易行为或交易情况出现异常的；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">C、本公司认为您的身份资料存在疑点或本公司在向您提供服务的过程中认为您已经提供的身份资料存在疑点的；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">D、本公司认为应核对或留存您身份证件或其他必要文件的其他情形的。</span></p>\n<p class=\"p\" style=\"margin: 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（二）账户安全</span></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">您将对使用该账户或密码进行的一切操作及言论负完全的责任，您同意：</span></p>\n<p class=\"p\" style=\"margin: 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">1、除相关产品另有规则外，本公司可以通过您的</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">登录名和密码或者本公司认可的其他方式识别您的身份，您应当对该密码、身份信息等进行妥善保管，对于因密码、身份信息、校验码等泄露所致的损失由您自行承担。您保证不向其他任何人泄露您的</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">登录名及密码、校验码以及身份信息等，亦不使用其他任何人的</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">登录名及密码。本公司亦可能通过本服务应用您使用的其他产品或设备识别您的指示，您应当妥善保管处于您或应当处于您掌控下的这些产品或设备，对于这些产品或设备遗失所致的任何损失，由您自行承担。</span></u></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">2、基于计算机端、手机端以及使用其他电子设备的用户使用习惯，我们可能在您使用具体产品时设置不同的账户登录模式及采取不同的措施识别您的身份。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3、您同意，（a）如您发现有他人冒用或盗用您的</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">登录名及密码或任何其他未经合法授权之情形，或发生与</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">账户关联的手机或其他设备遗失或其他可能危及到</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">账户资金安全情形时，应立即以有效方式通知本公司，向本公司申请暂停相关服务，以保障您的合法权益；及（</span><span style=\"font-family: Microsoft YaHei;\">b）确保您在持续登录</span></span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">网站时段结束时，以正确步骤离开网站。</span><strong><u><span class=\"15\" style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">本公司不能也不会对因您未能遵守本款约定而发生的任何损失、损毁及其他不利后果负责。您理解本公司对您的请求采取行动需要合理期限，在此之前，本公司对已执行的指令及（或）所导致的您的损失不承担任何责任。</span></u></strong></p>\n<p class=\"p\" style=\"margin: 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">4、除非您本人书面提出申请或者另有法律规定或经司法裁判，且征得本公司同意，否则您的</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">登录名及密码、</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">账户不得以任何方式转让、赠与或继承（相关的财产权益除外）。</span></u></strong></p>\n<p class=\"p\" style=\"margin: 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">5、您使用本服务时同意并认可，可能系统问题造成本服务无法提供，对此本公司不承担任何责任。</span></u></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">6、您同意，基于运行和交易安全的需要，本公司可以暂时停止提供或者限制本服务部分功能，或提供新的功能，在任何功能减少、增加或者变化时，只要您仍然使用本服务，表示您仍然同意本协议或者变更后的协议。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">7、本公司有权了解您使用本公司产品或服务的真实交易背景及目的，您应如实提供本公司所需的真实、全面、准确的信息。</span><strong><u><span class=\"15\" style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">如果本公司有合理理由怀疑您提供虚假</span> <span style=\"font-family: Microsoft YaHei;\">交易信息的，本公司有权暂时或永久限制您所使用的产品或服务的部分或全部功能，并通过邮件或者站内信或客户端通知等方式通知您，您应及时予以关注。</span></span></u></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">8、您同意，本公司有权按照包括但不限于公安机关、检察机关、法院、海关、税务机关等司法机关、行政机关、军事机关的要求对您的个人信息及在</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">的交易及账户等进行查询、冻结或其他操作。</span></p>\n<p class=\"p\" style=\"margin: 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（三）注销相关</span></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">在需要终止使用本服务时，您可以申请注销您的</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">账户，您同意：</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">1、您所申请注销的</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">账户应当是您依照本协议的约定注册并由本公司提供给您本人的账户。您应当依照本公司规定的程序进行</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">账户注销。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">2、</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">账户注销将导致本公司终止为您提供本服务，本协议约定的双方的权利义务终止（依本协议其他条款另行约定不得终止的或依其性质不能终止的除外），同时还可能对于该账户产生如下结果：</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">A、任何您的交易信息都将无法查看；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">B、任何您的交易过程中产生的附属文档都将无法查看，您可以在注销之前进行保存。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">C、注销完成后，本公司无法对您之前留存下来的任何信息提供鉴权，公证等服务</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3、您可以通过自助或者人工的方式申请注销</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">账户。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">4、您申请注销的</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">账户应当处于正常状态，即您的</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">账户的账户信息和用户信息是最新、完整、正确的，且该账户可以使用所有</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">服务功能。账户信息或用户信息过时、缺失、不正确的账户或被暂停或终止提供服务的账户不能被申请注销</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">5、您申请注销的</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">账户应当不存在任何由于该账户被注销而导致的未了结的合同关系与其他基于该账户的存在而产生或维持的权利义务，及本公司认为注销该账户会由此产生未了结的权利义务而产生纠纷的情况。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">6、如果您申请注销的</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">账户一旦注销成功，将不再予以恢复。</span></p>\n<p class=\"p\" style=\"margin: 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">7、您理解并同意，如（a）您连续12个月未使用您的</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">登录名；或（</span><span style=\"font-family: Microsoft YaHei;\">b）您在</span></span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">网站有欺诈、虚假交易、侵犯他人合法权益或其他严重违反</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">网站</span> <span style=\"font-family: Microsoft YaHei;\">规则或者中华人民共和国法律的行为的，本公司有权注销您名下的全部或部分</span></span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">登录名，您将不能再登录</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">网站，所有网站的服务将同时终止。</span></u></strong></p>\n<h4 style=\"margin: 20pt 0pt; font-family: 宋体; font-size: 12pt; padding: 0pt; line-height: 45pt;\"><strong><span style=\"letter-spacing: 0pt; font-size: 16pt;\"><span style=\"font-family: 宋体;\">五、</span> </span></strong><strong><span style=\"letter-spacing: 0pt; font-size: 16pt;\">开放签</span></strong><strong><span style=\"letter-spacing: 0pt; font-size: 16pt;\">服务使用规则</span></strong></h4>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">为有效保障您使用本服务时的合法权益，您理解并同意接受以下规则：</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（一）一旦您使用本服务，您即不可撤销地授权本公司在您及（或）您指定人符合指定条件或状态时，向您的合作伙伴发送签约邀请并执行对应的操作。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><strong><u><span class=\"15\" style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（二）本公司通过以下方式接受来自您的指令：</span></u></strong><strong><span class=\"15\" style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">A、您在</span></strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">本网站或其他可使用本服务的网站或软件上通过以您的</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">账户名及密码或数字证书等安全产品登录</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">账户并依照本服务预设流程所修改或确认的交易状态或指令；</span><strong><span class=\"15\" style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">B、您通过</span></strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">您注册时作为该账户名称或者与该账户绑定的手机或其他专属于您的通讯工具（以下合称该手机）号码向本公司发送的信息（短信或电话等）回复；</span><strong><span class=\"15\" style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">C、您通过</span></strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">您注册时作为该账户名称或者与该账户名称绑定的其他硬件、终端、软件、代号、编码、代码、其他账户名等有形体或无形体向本公司发送的信息（如本方式所指有形体或无形体具备与该手机接受信息相同或类似的功能，以下第五条第（四）、（五）、</span> <span style=\"font-family: Microsoft YaHei;\">（六）项和第六条第（三）项涉及该手机的条款同样适用于本方式）；</span></span><strong><span class=\"15\" style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">D、通过</span></strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">识别您的指纹等生物特征；</span><span style=\"font-family: Microsoft YaHei;\">E、本公司与您约定或本公司认可的其他方式。</span></span><strong><u><span class=\"15\" style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">无论您通</span> <span style=\"font-family: Microsoft YaHei;\">过以上何种方式向本公司发出指令，都不可撤回或撤销，且成为本公司代理您支付或收取款项或进行其他操作的唯一指令，视为您本人的指令，您应当自己对本公司</span> <span style=\"font-family: Microsoft YaHei;\">忠实执行上述指令产生的任何结果承担责任。</span></span></u></strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">本协议所称绑定，指您的</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">账户与本条上述所称有形体或无形体存在对应的关联关系，这种关联关系使得</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">服</span> <span style=\"font-family: Microsoft YaHei;\">务的某些服务功能得以实现，且这种关联关系有时使得这些有形体或无形体能够作为本公司对您的</span></span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">账户的识别和认定依据。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（三）您确认，您在网站上的任何操作以及因为操作导致的结果都具备法律效力。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">本服务即表示您同意接受本服务的相关规则。您了解并同意本公司有权单方修改服务的相关规则，而无须征得您的同意，服务规则应以您使用服务时的页面提示（或</span> <span style=\"font-family: Microsoft YaHei;\">发送到该手机的短信或电话或客户端通知等）为准，您同意并遵照服务规则是您使用本服务的前提。</span></span></p>\n<p class=\"p\" style=\"margin: 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">（四）本公司会以站内状态通知（或发送到该手机的短信或电话等或客户端通知等）方式通知您交易进展情况以及提示您进行下一步的操作，但本公司不保证您能够收到或者及时收到该等通知，且不对此承担任何后果，因此，您应当及时查看该等通知并进行相关操作。因您没有及时查看和对交易状态进行修改或确认或未能提交</span> <span style=\"font-family: Microsoft YaHei;\">相关申请而导致的任何纠纷或损失，本公司不负任何责任。</span></span></u></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">（五）本公司对您所交易的协议内容不提供任何形式的鉴定服务。除本协议另有规定外，如您与交易对方发生交易纠纷，您不可撤销地授权由本公司根据本协议及本</span> <span style=\"font-family: Microsoft YaHei;\">网站上载明的各项规则进行处理。您为解决纠纷而支出的通讯费、文件复印费、鉴定费等均由您自行承担。因市场因素致使而使任何一方得益或者受到损失而产生的</span> <span style=\"font-family: Microsoft YaHei;\">纠纷（《争议处理规则》另有约定的除外），本公司不予处理。</span></span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">（六）您应按照本公司的要求完善您的身份信息以最终达到实名，</span> <span style=\"font-family: Microsoft YaHei;\">否则您可能会受到部分功能限制。</span></span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（七）本公司会将您的交易过程及产生的资料，严格按照法律法规或有权机关的监管要求进行管理；除本协议另有规定外，不作任何其他非您指示的用途。就您使用</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">服务，本公司有权将您的身份信息及签约合同信息相应传输至电子认证服务机构（</span><span style=\"font-family: Microsoft YaHei;\">CA, Certificate Authority）或公证处，由CA以出具数字证书之目的或公证处以出具公证文件之目的使用并保存，且前述信息可以保存至数字证书失效后或相关电子数据（包括但不限于签约合同电子数据）产生后10年，并且该等信息不因您终止</span></span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">服务而停止保存。同时，本公司有权将您的身份信息、证件复印件或扫描件相应传输至第三方实名认证服务提供商及其他具有国家认可资质的数据服务提供商，由该等机构以验证签约方身份之目的使用并保存。您和任何签约方知悉并保证：本公司是数据电文等法律文件的签订平台，而不是</span><span style=\"font-family: Microsoft YaHei;\">CA机构或者实名认证机构。因此，您或者任何签约方通过本平台协助您和任何签约方开展实名认证的，本公司不对实名结果承担法律责任。因实名和意愿性校验无效而导致签订的数据电文等法律文件被认定无效的，相关的法律责任概与本公司无关。但因本公司故意造成无效的除外。</span></span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（八）本公司并非法律机构，因交易内容或者因为协议内容产生的纠纷，本公司不承担任何解决义务，您可以向符合中华人民共和国法律规定的相关单位寻求解决。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（九）您不得将本服务用于非本公司许可的其他用途。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（十）签约风险</span></p>\n<p class=\"p\" style=\"margin: 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">1、在使用本服务时，若您或您的签约对方未遵从本服务条款或网站说明、交易页面中之操作提示、规则），则本公司有权拒绝为您与签约对方提供相关服务，且本公司不承担损害赔偿责任。</span></u></strong></p>\n<p class=\"p\" style=\"margin: 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">2、因您的过错导致的任何损失由您自行承担，该过错包括但不限于：不按照提示操作，未及时进行操作，遗忘或泄漏密码、校验码等， 密码被他人破解，您使用的计算机或其他硬件、终端等被他人侵入或丢失，或您使用的软件被他人侵入，或者您的生物特征被他人利用。</span></u></strong></p>\n<p class=\"p\" style=\"margin: 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3、当您和数据电文的签约方在签订数据电文等法律文件时，应当自行判定自己及其他当事人的主体资格是否适格。如果您和/或者其他当事人是无民事行为能力人或者限制民事行为能力人，不得签订或者仅能签订与自己的年龄、智力和精神健康状况相适应的法律文件。因主体不适格导致通过</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">平台签订的数据电文等法律文件无效的，责任和法律后果由您和该法律文件其他签约方自己承担。</span></u></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（十一）服务费用</span></p>\n<p class=\"p\" style=\"margin: 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">1、在您使用本服务时，本公司有权依照《</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">服务收费规则》向您收取服务费用。本公司拥有制订及调整服务费之权利，具体服务费用以您使用本服务时本网站或客户端、产品页面上所列之收费方式公告或您与本公司达成的其他书面协议为准。</span></u></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">2、除非另有说明或约定，您同意本公司有权自您委托本公司代收或代付的账户余额或者其他资产中直接扣除上述服务费用。</span></p>\n<p class=\"p\" style=\"margin: 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3、当您通过</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">平台在线购买订阅类型的产品</span><span style=\"font-family: Microsoft YaHei;\">/服务（此类产品/服务会在购买页面明确展示&ldquo;此产品/服务一经购买，无法退换&rdquo;的温馨提示）时，您知晓并同意：此类产品/服务不适合退换，当您同意购买并支付成功后，您将无法退换此类产品/服务；若您在购买页面看到&ldquo;此产品/服务一经购买，无法退换&rdquo;的提示后有异议的，可以咨询官方客服电话热线。若您不接受&ldquo;此产品/服务一经购买，无法退换&rdquo;的规则，您应当停止支付。</span></span></u></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（十二）您认可</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">账户的使用记录、交易状态等数据等均以本公司系统记录的数据为准。如您对该等数据存有异议的，应自您账户数据发生变动之日起三日内向本公司提出异议，并提供相关证据供本公司核实。</span></p>\n<h4 style=\"margin: 20pt 0pt; font-family: 宋体; font-size: 12pt; padding: 0pt; line-height: 45pt;\"><strong><span style=\"letter-spacing: 0pt; font-size: 16pt;\"><span style=\"font-family: 宋体;\">六、</span> </span></strong><strong><span style=\"letter-spacing: 0pt; font-size: 16pt;\">开放签</span></strong><strong><span style=\"letter-spacing: 0pt; font-size: 16pt;\">服务使用限制</span></strong></h4>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（一）您在使用本服务时应遵守中华人民共和国相关法律法规、您所在国家或地区之法令及相关国际惯例，不将本服务用于任何非法目的（包括用于禁止或限制交易物品的交易），也不以任何非法方式使用本服务。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">（二）您不得利用本服务从事侵害他人合法权益之行为，</span> <span style=\"font-family: Microsoft YaHei;\">否则本公司有权拒绝提供本服务，且您应承担所有相关法律责任，因此导致本公司或本公司雇员或其他方受损的，您应承担赔偿责任。上述行为包括但不限于：</span></span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">1、侵害他人名誉权、隐私权、商业秘密、商标权、著作权、专利权等合法权益。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">2、违反依法定或约定之保密义务。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3、冒用他人名义使用本服务。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">4、从事不法交易行为，如洗钱、恐怖融资、贩卖枪支、毒品、禁药、盗版软件、黄色淫秽物品、其他本公司认为不得使用本服务进行交易的物品等。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">5、提供赌博资讯或以任何方式引诱他人参与赌博。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">6、非法使用他人账户交易。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">7、进行与您或交易对方宣称的交易内容不符的交易， 或不真实的交易。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">8、从事任何可能含有电脑病毒或是可能侵害本服务系统、资料之行为。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">9、其他本公司有正当理由认为不适当之行为。</span></p>\n<p class=\"p\" style=\"margin: 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（三）您理解并同意，本公司不对因下述任一情况导致的任何损害赔偿承担责任，包括但不限于利润、商誉、使用、数据等方面的损失或其他无形损失的损害赔偿（无论本公司是否已被告知该等损害赔偿的可能性）：</span></u></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">1、本公司有权基于单方判断，包含但不限于本公司认为您已经违反本协议的明文规定及精神，对您名下的全部或部分</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">账户暂停、中断或终止向您提供本服务或其任何部分，并移除您的资料。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">2、本公司在发现异常交易或合理怀疑交易有疑义或有违反法律规定或本协议约定之虞时， 有权不经通知先行暂停或终止您名下全部或部分</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">账户的使用（包括但不限于对这些账户名下的在途交易采取取消交易等限制措施），并拒绝您使用本服务之部</span> <span style=\"font-family: Microsoft YaHei;\">分或全部功能，并通过邮件或站内信或客户端通知等方式通知您，您应及时予以关注。</span></span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3、您理解并同意，存在如下情形时，本公司有权对您名下</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">账户暂停或终止提供全部或部分</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">服务，且有权限制您所使用的产品或服务的部分或全部功能（包括但不限于对这些账户名下在途交易采取取消交易等限制措施），并通过邮件或站内信或者客户端通知等方式通知您，您应及时予以关注：</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">1）根据本协议的约定：</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">2）根据法律法规及法律文书的规定：</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3）根据有权机关的要求：</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">4）您使用</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">服务的行为涉嫌违反国家法律法规及行政规定的：</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">5）本公司基于单方面合理判断认为账户操作、资金进出等存在异常时：</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">6）本公司依据自行合理判断认为可能产生风险的：</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">7）您在参加市场活动时有批量注册账户、刷信用及其他舞弊等违反活动规则、违反诚实信用原则的：</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">8）您遭到他人投诉，且对方已经提供了一定证据的：</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">9）您可能错误地将他人账户进行了实名制或实名认证的。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">如您申请恢复服务、解除上述止付或限制，您应按本公司要求如实提供相关资料及您的身份证明以及本公司要求的其他信息或文件，以便本公司进行核实，且本公司</span> <span style=\"font-family: Microsoft YaHei;\">有权依照自行判断来决定是否同意您的申请。您应充分理解您的申请并不必然被允许。您拒绝如实提供相关资料及身份证明的，或未通过本公司审核的，则您确认本</span> <span style=\"font-family: Microsoft YaHei;\">公司有权长期对该等账户停止提供服务且长期限制该等产品或者服务的部分或全部功能。</span></span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">4、在本公司认为该等异常已经得到合理解释或有效证据支持或未违反国家相关法律法规及部门规章的情况下，最晚将于得到解释之日起的30个日历天内解除限制。但本公司有进一步理由相信该等异常仍可能对您或其他用户或本公司造成损失的情形除外，包括但不限于：</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">1）收到针对该等异常的投诉：</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">2）您已经实质性违反了本协议或另行签署的协议，且我们基于保护各方利益的需要必须继续止付款项或暂停执行指令：</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3）您虽未违反国家相关法律法规及部门规章规定，但该等使用涉及本公司限制合作的行业类目或商品，包括但不限于通过本公司的产品或服务从事类似金字塔或矩阵型的高额返利业务模式。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">5、在本公司合理认为有必要时，本公司无需事先通知即可终止提供本服务，并暂停、关闭或删除您名下全部或部分</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">账户及这些账户中所有相关资料及档案。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（四）如您需要注销您的</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">账户，应先经本公司审核同意。本公司注销该账户，即表明本公司与您之间的协议已终止，但您仍应对您使用本服务期间的行为承担可能的违约或损害赔偿责任，同时本公司仍可保有您的相关信息。</span></p>\n<h4 style=\"margin: 20pt 0pt; font-family: 宋体; font-size: 12pt; padding: 0pt; line-height: 45pt;\"><strong><span style=\"letter-spacing: 0pt; font-size: 16pt;\"><span style=\"font-family: 宋体;\">七、</span> <span style=\"font-family: 宋体;\">用户信息及隐私权保护</span></span></strong></h4>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（一）您同意并授权</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">在您使用</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">的服务的范围内为验证签约主体身份信息之目的自行或通过其他具有法律要求的资质的机构、行业协会自公开及合法的数据来源收集您的额外资料和信息，收集的资料和信息范围包括：</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">1、您的身份信息，包括但不限于姓名/名称、证件号码、证件类型、住所地、营业执照、法定代表人、注册资本、经营期限、统一社会信用代码、经营范围、电话号码以及其他身份信息；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">2、您在公司的关联方和合作伙伴中留存以及形成的任何数据和信息，包括但不限于您的证券账户和/或银行账户信息等；您在公司、公司的关联方或公司合作伙伴以及其他机构的网络平台留存、形成、产生的有关法律文件以及任何信息或行为数据，包括但不限于您的交易信息、金融理财产品账户信息、投融资产品账户信息、投融资项目信息、履约或违法行为信息等；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3、您的信用信息，包括但不限于您的征信记录和信用报告及其他信用信息；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">4、您的财产信息，包括但不限于您的店铺/企业经营状况、财税信息、房产信息、车辆信息、基金、保险、股票、信托、债券等投资理财信息和负债信息等；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">5、您在行政机关、司法机关留存的任何信息，包括但不限于户籍信息、工商信息、诉讼信息、执行信息和违法犯罪信息等；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">6、与您申请或使用的</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">平台服务相关的、您留存在其他自然人、法人和组织的其他相关信息。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（二）本公司重视对用户隐私的保护。关于您的身份资料和其他特定资料受到保护与规范。就您使用</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">服务，本公司有权将您的身份信息及签约合同信息相应传输至电子认证服务机构（</span><span style=\"font-family: Microsoft YaHei;\">CA, Certificate Authority）或公证处，由CA以出具数字证书之目的或公证处以出具公证文件之目的使用并保存，且前述信息可以保存至数字证书失效后或相关电子数据（包括但不限于签约合同电子数据）产生后10年，并且该等信息不因您终止</span></span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">服务而停止保存。同时，本公司有权将您的身份信息、证件复印件或扫描件相应传输至第三方实名认证服务提供商及其他具有国家认可资质的数据服务提供商，由该等机构以验证签约方身份之目的使用并保存。</span></p>\n<h4 style=\"margin: 20pt 0pt; font-family: 宋体; font-size: 12pt; padding: 0pt; line-height: 45pt;\"><strong><span style=\"letter-spacing: 0pt; font-size: 16pt;\"><span style=\"font-family: 宋体;\">八、</span> <span style=\"font-family: 宋体;\">系统中断或故障</span></span></strong></h4>\n<p class=\"p\" style=\"margin: 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">本公司系统因下列状况无法正常运作，使您无法使用各项服务时，本公司不承担损害赔偿责任，该状况包括但不限于：</span></strong></p>\n<p class=\"p\" style=\"margin: 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（一）本公司在本网站公告之系统停机维护期间。</span></u></strong></p>\n<p class=\"p\" style=\"margin: 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（二）电信设备出现故障不能进行数据传输的。</span></u></strong></p>\n<p class=\"p\" style=\"margin: 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（三）因台风、地震、海啸、洪水、停电、战争、恐怖袭击等不可抗力之因素，造成本公司系统障碍不能执行业务的。</span></u></strong></p>\n<p class=\"p\" style=\"margin: 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（四）由于黑客攻击、电信部门技术调整或故障、网站升级、银行方面的问题等原因而造成的服务中断或者延迟。</span></u></strong></p>\n<h4 style=\"margin: 20pt 0pt; font-family: 宋体; font-size: 12pt; padding: 0pt; line-height: 45pt;\"><strong><span style=\"letter-spacing: 0pt; font-size: 16pt;\"><span style=\"font-family: 宋体;\">九、</span> <span style=\"font-family: 宋体;\">责任范围及责任限制</span></span></strong></h4>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（一）本公司仅对本协议中列明的责任承担范围负责。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（二）您明确因交易所产生的任何风险应由您与交易对方承担。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（三）本公司用户信息是由用户本人自行提供的，本公司无法保证该信息之准确、及时和完整，您应对您的判断承担全部责任。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">（四）本公司不对交易标的及本服务提供任何形式的保证，包括但不限于以下事项</span><span style=\"font-family: Microsoft YaHei;\">:</span></span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">1、本服务符合您的需求。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">2、本服务不受干扰、及时提供或免于出错。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3、您经由本服务购买或取得之任何产品、服务、资讯或其他资料符合您的期望。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（五）本服务之合作单位，所提供之服务品质及内容由该合作单位自行负责。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（六）您经由本服务之使用下载或取得任何资料，应由您自行考量且自负风险，因资料之下载而导致您电脑系统之任何损坏或资料流失，您应负完全责任。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（七）您自本公司及本公司工作人员或经由本服务取得之建议和资讯，无论其为书面或口头形式，均不构成本公司对本服务之保证。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">（八）在法律允许的情况下，本公司对于与本协议有关或由本协议引起的任何间接的、惩罚性的、特殊的、派生的损失（包括业务损失、收益损失、利润损失、商誉</span> <span style=\"font-family: Microsoft YaHei;\">损失、使用数据或其他经济利益的损失），不论是如何产生的，也不论是由对本协议的违约（包括违反保证）还是由侵权造成的，均不负有任何责任，即使事先已被告知此等损失的可能性。另外即使本协议规定的排他性救济没有达到其基本目的，也应排除本公司对上述损失的责任。</span></span></p>\n<p class=\"p\" style=\"margin: 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（九）除本协议另有规定外，在任何情况下，本公司对本协议所承担的违约赔偿责任总额不超过向您收取的当次服务费用总额。</span></u></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（十）您充分知晓并同意本公司可能同时为您及您的（交易）对手方提供本服务，您同意对本公司可能存在的该等行为予以明确豁免，并不得以此来主张本公司在提供本服务时存在法律上的瑕疵。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（十一）除本协议另有规定或本公司另行同意外，您对本公司的委托及向本公司发出的指令均不可撤销。</span></p>\n<h4 style=\"margin: 20pt 0pt; font-family: 宋体; font-size: 12pt; padding: 0pt; line-height: 45pt;\"><strong><span style=\"letter-spacing: 0pt; font-size: 16pt;\"><span style=\"font-family: 宋体;\">十、</span> <span style=\"font-family: 宋体;\">商标、知识产权、专利的保护</span></span></strong></h4>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（一）本公司及关联公司所有系统及本网站上所有内容，包括但不限于著作、图片、档案、资讯、资料、网站架构、网站画面的安排、网页设计，均由本公司或本公司关联公司依法拥有其知识产权，包括但不限于商标权、专利权、著作权、商业秘密等。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（二）非经本公司或本公司关联企业书面同意，任何人不得擅自使用、修改、复制、公开传播、改变、散布、发行或公开发表本网站程序或内容。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; text-align: justify; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\" align=\"justify\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">（三）尊重知识产权是您应尽的义务，如有违反，</span> <span style=\"font-family: Microsoft YaHei;\">您应承担损害赔偿责任。</span></span></p>\n<h4 style=\"margin: 20pt 0pt; font-family: 宋体; font-size: 12pt; padding: 0pt; line-height: 45pt;\"><strong><span style=\"letter-spacing: 0pt; font-size: 16pt;\"><span style=\"font-family: 宋体;\">十一、</span> <span style=\"font-family: 宋体;\">法律适用与管辖</span></span></strong></h4>\n<p class=\"MsoNormal\" style=\"margin: 0pt 0pt 0.0001pt; text-align: justify; font-family: Calibri; font-size: 10.5pt;\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">本协议之效力、解释、变更、执行与争议解决均适用中华人民共和国法律，没有相关法律规定的，参照通用国际商业惯例和（或）行业惯例。因本协议产生之争议，均应依照中华人民共和国法律予以处理，并由被告住所地人民法院管辖。</span></u></strong></p>', NULL, NULL, '13793011113', '2023-10-27 10:15:49');
INSERT INTO `sys_text_config` (`id`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('rwrwqetwqtqy', 'privacy', '<p class=\"p\" style=\"margin: 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 30pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">最近更新日期</span>:【202</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">】年【</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">10</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">】</span> <span style=\"font-family: Microsoft YaHei;\">月【</span>1</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">】日</span></p>\n<p class=\"p\" style=\"margin: 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 30pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">生效日期：【</span><span style=\"font-family: Microsoft YaHei;\">202</span></span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">】年【</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">10</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">】</span> <span style=\"font-family: Microsoft YaHei;\">月【</span><span style=\"font-family: Microsoft YaHei;\">1</span></span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">】日</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">欢迎访问</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">北京资源律动</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">科技有限公司的电子签名平台（以下简称</span><span style=\"font-family: Microsoft YaHei;\">&ldquo;</span></span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">&rdquo;或&ldquo;我们&rdquo;），本隐私政策适用于我司电子签名平台所提供的服务。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">尊敬的</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">用户（以下简称</span><span style=\"font-family: Microsoft YaHei;\">&ldquo;您&rdquo;），我们非常重视您的隐私，您在使用</span></span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">服务时，我们将按照《隐私政策》（以下简称</span><span style=\"font-family: Microsoft YaHei;\">&ldquo;本政策&rdquo;）收集、存储、使用及对外共享您的个人信息。为了保证对您的个人隐私信息合法、合理、适度的收集、使用，并在安全、可控的情况下进行传输、存储，我们制定了本政策。希望您在使用我们的产品与/或服务前仔细阅读并确认您已经充分知悉并理解本政策所陈述的内容，一旦您开始使用或在我们更新本隐私政策后继续使用我们的产品与/或服务，即表示您已充分理解并同意本政策(含更新版本)内容，并且同意我们按照本隐私政策收集、使用、保存和共享您的相关信息。对本政策中我们认为与您的权益存在重大关系的条款和个人敏感信息，我们采用粗体字进行标注以提示您特别注意。</span></span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">本政策将帮助您了解以下内容</span>:</span></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">一、我们如何收集和使用您的个人</span>/企业信息</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">二、我们如何使用</span>Cookie和同类技术</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">三、我们如何共享、转让和公开披露您的个人</span>/企业信息</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">四、我们如何存储和保护您的个人</span>/企业信息</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">五、您的权利</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">六、我们如何处理儿童的个人信息</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">七、第三方收集和使用您信息的情况</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">八、本政策如何更新</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">一、我们如何收集和使用您的个人</span>/企业信息</span></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">我们根据合法、正当、必要的原则，收集为实现产品功能所需要的信息，并将收集信息进行合理使用。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">（一）我们如何收集您的个人</span>/企业信息</span></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">在您使用</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">服务过程中，我们会收集您在使用服务过程中主动输入或因使用服务而产生的信息：</span></p>\n<p class=\"p\" style=\"margin: 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">1.&nbsp;您在注册账户或使用我们的服务时，您需要向我们提供可用于识别用户的个人身份的信息，例如</span><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">姓名、电子邮件地址、电话号码</span></strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">等个人信息，以帮助您完成</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">用户注册，如果您拒绝提供这些信息，您可能无法完成注册或无法正常使用我们的服务。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">2.&nbsp;当您使用</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">功能或服务时，在下列情形中，您可能需要向我们提供或授权我们收集相应服务所需的个人信息。如您拒绝提供部分功能或服务所需的个人信息，您可能无法使用部分功能或服务，但这不影响您正常使用</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">的其他功能或服务：</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">您需要使用我们的部分服务（例如：签署服务），您需要向我们提供更多信息（例如：</span><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">姓名、证件号、手机号、银行卡号、人脸数据、证件数据等信息</span></strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">）以完成身份基本信息实名认证验证。您还可以升级成为企业用户，以享受更多高级功能（例如：管理企业成员、创建业务流程）。升级成为企业用户，需要您完成企业身份实名认证校验。您需要向我们提供企业的基本信息，包括：</span><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">企业名称、统一社会信用代码、法定代表人姓名、法人身份证号码、营业执照、企业对公账户的开户行以及银行账号、加盖组织机构公章的认证授权书等信息，</span></strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">以便于验证您的企业身份。具体提供的信息以您实名认证过程中收集的信息为准。同时，为了验证您提供信息的准确性和完整性，我们会与合法留存您信息的国家机关、金融机构、企事业单位进行核对。如在验证核对过程中我们需要向前述验证机构收集您的信息，我们会依照法律法规或监管规定要求相关向验证机构说明其个人信息来源，并对其个人信息来源的合法性进行确认。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">收集上述的信息是为了满足相关法律法规的实名制要求，如您拒绝提供该信息，仅会使您无法使用上述功能，但不影响您正常使用</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">的其他功能。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">此外，为了保障我们服务的可用性、连续性及您的账户安全，我们会收集您的下述信息：</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">（</span>1）设备信息：设备型号、操作系统版本、唯一设备标识符、IP&nbsp;地址信息。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">（</span>2）软件信息：软件的版本号、浏览器类型。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">）服务日志信息。您在使用我们服务时接入网络的方式、类型和状态、登录时间、操作日志、服务日志等与</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">服务相关的日志信息。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">（</span>6）IP及位置信息。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">（二）我们如何使用您的个人</span>/企业信息</span></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">为了遵守国家法律法规及监管要求，以及向您提供服务及提升服务质量，或保障您的账户安全，我们会在以下情形中使用您的信息：</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">1.我们会根据本政策的约定并为实现我们的服务或功能对所收集的您的个人信息进行使用；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">2.为了使您知晓使用</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">服务的状态，我们会向您发送服务提醒；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3.为了保障服务的稳定性与安全性，我们会将您的信息用于身份验证、安全防范、诈骗监测、预防或禁止非法活动、降低风险、存档和备份用途；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">4.根据法律法规或监管要求向相关部门进行报告；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">5.邀请您参与我们服务、产品或功能有关的客户调研；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">6.在收集您的个人信息后，我们在通过技术手段对您的信息数据进行去标识化处理后，该等去标识化的信息将无法识别信息主体。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">7.我们会对我们的服务或功能使用情况进行统计，并可能会与公众或第三方共享这些统计信息，以展示我们的服务或功能的整体使用趋势。但这些统计信息不包含您的任何身份识别信息。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">8.当我们展示您的信息时，我们会采用包括内容替换、匿名化处理方式对您的信息进行脱敏，以保护您的信息安全。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">9.因监管要求、业务规则、风控要求需加强身份验证时，我们会应用您在使用涉及核实身份业务时留存的活体检测人脸数据及身份证件人像照进行人脸识别。如您不同意我们应用前述信息，则可能无法完成需利用人脸识别加验的相关业务，但不影响您使用我们提供的其他服务。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（三）征得授权同意的例外</span></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">根据相关法律法规、监管要求及国家标准，以下情形中遇到国家有权机关或者监管机关要求我们提供的，或者出于对您的权利、权益进行充分保护的目的，或者符合此处约定的其他合理情形的，我们会收集、使用您的相关个人信息而无需另行征求您的授权同意：</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">1.与国家安全、国防安全直接相关的；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">2.与公共安全、公共卫生、重大公共利益直接相关的；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3.与犯罪侦查、起诉、审判和判决执行等直接相关的；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">4.出于维护您或其他个人的生命、财产等重大合法权益但又很难得到您本人同意的；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">5.所收集的个人信息是您自行向社会公众公开的；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">6.从合法公开披露的信息中收集个人信息的，如合法的新闻报道、政府信息公开等渠道；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">7.根据您要求签订和履行合同所必需的；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">8.用于维护所提供的产品或服务的安全稳定运行所必需的，例如发现、处置产品或服务的故障；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">9.出于公共利益开展统计或学术研究所必需，且其对外提供学术研究或描述的结果时，对结果中所包含的个人信息进行去标识化处理的；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">10.法律法规及监管规定要求的其他情形。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">二、我们如何使用</span>Cookie和同类技术</span></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">为确保服务正常运转、为您获得更轻松的访问体验，我们会在您的计算机或移动设备上存储名为</span>Cookie的小数据文件。Cookie通常包含标识符、站点名称以及一些号码和字符。我们不会在Cookie里面存储与个人身份或隐私相关的信息。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">您可根据自己的偏好管理或删除</span>Cookie。有关详情，请参见AboutCookies.org。您可以清除计算机或移动设备上保存的所有Cookie，大部分网络浏览器都设有阻止Cookie的功能。但如果您这么做，则需要在每一次访问我们的网站或移动设备时更改用户设置。如需详细了解如何更改浏览器设置，请访问您使用的浏览器的相关设置页面。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">三、我们如何共享、转让和公开披露您的个人</span>/企业信息</span></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（一）共享</span></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">我们不会与</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">服务提供者以外的公司、组织和个人共享您的个人信息，但以下情况除外</span>:</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">1.&nbsp;事先获得您明确的同意或授权；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">2.我们可能会根据法律法规规定、诉讼争议解决需要，或按行政、司法机关依法提出的强制性要求，对外共享您的个人信息。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3.为维护用户合法权益，在协助处理与您有关的纠纷或争议时，我们可能向存在利害关系的相关方提供解决纠纷或争议所必需的信息。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">4.&nbsp;与合作机构共享:为实现电子签名或其他</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">提供的服务的目的，我们的某些服务将由我们和合作机构共同提供。我们会根据法律法规及协议约定与合作伙伴共享您的某些个人信息。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">（</span>1）您的个人身份信息及签署信息需传输至电子认证服务机构(简称CA，Certificate Authority)、公证处、司法鉴定中心、区块链、互联网法院、仲裁委、第三方身份认证机构，由上述权威机构出具相应证明文件或基于处理案件的目的进行使用并保存。接入第三方服务的情形，我们将依照本政策收集的您某些信息共享给第三方服务商。我们仅会出于合法、正当、必要的目的共享您的个人信息，并且只会共享提供服务所必要的个人信息。&nbsp;&nbsp;</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">（</span>2）</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">通过</span>SDK方式共享的信息：某些产品和服务可能由我们的授权合作机构提供，我们会通过SDK的形式向您提供产品或服务。在此过程中，我们的授权合作机构可能会收集、使用和存储您的相关数据或信息。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">请您知悉，我们仅会出于合法、正当、必要的目的共享您的个人信息，并且只会共享提供服务所必要的个人信息。我们会对授权合作机构开展个人信息安全影响评估，对其收集的个人信息或需要授权获得的权限进行合理性分析，定期对其服务能力和安全能力进行评估和监控，并与其签订有关协议明确约定严格的数据保护措施，要求其应遵守相关的法律法规的规定，严格按照我们的标准、本隐私政策以及其他任何相关的保密和安全措施来处理您的个人信息。以上</span>SDK合作机构均通过去标识化、加密传输和处理的安全处理方式处理您的个人信息。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">如您在使用过程中一旦发现我们的授权合作机构有违法违规的行为，您可以通过本隐私政策提供的联系方式与我们取得联系，我们会予以核实并及时中止与该第三方的合作，尽最大可能保障您的个人数据和隐私信息不受侵害。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（二）转让</span></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">我们不会将您的个人</span>/企业信息转让给任何公司、组织和个人，但以下情况除外:</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">1.事先获得您的明确同意；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">2.根据法律法规或强制性的行政或司法要求；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3.&nbsp;在</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">服务提供者发生合并、收购或破产清算情形，或其他涉及合并、收购或破产清算情形时，如涉及到个人信息转让，我们会要求新的持有您个人信息的公司、组织继续受本政策的约束，否则我们将要求该公司、组织和个人重新向您征求授权同意。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（三）公开披露</span></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">我们仅会在以下情况下，公开披露您的个人</span>/企业信息:</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">1.我们不会公开披露您的个人信息，如确需披露，我们会获取您的同意，并告知您披露个人信息的目的、类型；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">2.请您理解，在法律、法律程序、诉讼或政府主管部门强制性要求的情况下，我们可能会公开披露您的个人/企业信息。执法机关提出披露要求，我们会及时通知您，除非执法机关另有禁止披露的要求。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">（四）共享、转让、公开披露个人</span>/企业信息时事先征得授权同意的例外</span></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">根据相关法律法规、监管规定，以下情形中遇到国家有权机关或者监管机关强制性要求的，或者出于对您的权利、权益进行充分保护的目的，或者符合此处约定的其他合理情形的，我们可能会共享、转让、公开披露用户信息而无需事先征得您授权同意：</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">1.与履行国家法律法规及行业主管部门有关规定的义务相关的；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">2.与国家安全、国防安全直接相关的；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3.与公共安全、公共卫生、重大公共利益直接相关的；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">4.与犯罪侦查、起诉、审判和判决执行等直接相关的；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">5.出于维护您或其他个人的生命、财产、声誉等重大合法权益但又很难得到本人同意的；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">6.您自行向社会公众公开的个人信息；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">7.根据您要求签订和履行合同所必需的；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">8.从合法公开披露的信息中收集的用户信息，如合法的新闻报道、政府信息公开等渠道；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">四、我们如何存储和保护您的个人</span>/企业信息</span></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（一）存储</span></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">1.信息存储的地点</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">我们在中华人民共和国境内收集和产生的个人信息将存储在中华人民共和国境内。若为处理跨境业务，确需向境外机构传输境内收集的相关个人信息的，我们会按照法律、行政法规和相关监管部门的规定执行。我们会确保您的个人信息得到足够的保护，例如匿名化、安全存储等。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">2.&nbsp;信息存储的期限</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">我们承诺始终按照法律的规定在合理必要期限内存储您个人信息。超出上述期限后，我们将删除您的个人信息。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">当我们的产品或服务发生停止运营的情形时，我们将以推送通知、公告等形式通知您，并在合理的期限内删除您的个人信息。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（二）保护</span></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">1.我们已采取符合业界标准的安全防护措施保护您提供的个人/企业信息安全，防止个人/企业信息遭到未经授权访问、公开披露、使用、修改、损坏或丢失。;我们同时对</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">网站提供</span>HTTPS(HyperTextTransferProtocoloverSecureSocketLayer)协议安全浏览方式;我们会使用加密技术提高个人/企业信息的安全性;我们会使用受信赖的保护机制防止个人/企业信息遭到恶意攻击;我们会部署访问控制机制，确保只有授权人员才可访问个人/企业信息;我们会举办安全和隐私保护培训课程，加强员工对于保护个人/企业信息重要性的认识。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">2</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">.我们会采取合理可行的措施，避免收集无关的个人/企业信息。我们只会在达成本政策所述目的所需的期限内保留您的个人/企业信息，除非需要延长保留期或受到法律的允许。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">.互联网并非绝对安全的环境，我们强烈建议您不要使用非</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">推荐的通信方式发送个人</span>/企业信息。请您妥善保护自己的个人/企业信息，仅在必要的情形下向他人提供。如您发现自己的个人/企业信息尤其是您的账户或密码发生泄露，请您立即联络</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">客服，以便我们根据您的申请采取相应措施。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">请使用复杂密码，协助我们保证您的账号安全。我们将尽力保障您发送给我们的任何信息的安全性。如果我们的物理、技术或管理防护设施遭到破坏，导致信息被非授权访问、公开披露、篡改或毁坏，导致您的合法权益受损，我们将承担相应的法律责任。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">4</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">.在不幸发生个人/企业信息安全事件后，我们将按照法律法规的要求及时向您告知：安全事件的基本情况和可能的影响、我们已采取或将要采取的处置措施、您可自主防范和降低风险的建议、对您的补救措施。事件相关情况我们将以邮件、信函、电话或推送通知的方式告知您，难以逐一告知个人/企业信息主体时，我们会采取合理、有效的方式发布公告。同时，我们还将按照监管部门要求，上报个人/企业信息安全事件的处置情况。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">5</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">.您通过</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">平台提交个人</span>/企业信息，代表您同意并授权给</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">收集、使用、共享和保护您的个人</span>/企业信息，若您因特殊原因申请撤回授权，请及时联系我们，以便于我们做相应处理。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">五、您的权利</span></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">您可以通过以下方式访问及管理您的个人信息</span>:</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（一）访问您的个人信息</span></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">您有权访问您的个人信息，法律法规规定的例外情况除外。您可以通过以下方式自行访问您的个人信息</span>:</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">账户信息</span><span style=\"font-family: Microsoft YaHei;\">&mdash;&mdash;如果您希望访问或编辑您的账户中的个人基本信息和更改您的密码、添加安全信息，您可以通过登录账号通过&ldquo;</span></span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">账号管理</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">&rdquo;执行此类操作。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">如果您无法通过上述路径访问该个人信息，您可以随时通过</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">客服与我们取得联系。我们将在</span>3个工作日内回复您的访问请求。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">对于您在使用我们的产品或服务过程中产生的其他个人信息，我们将根据本条</span><span style=\"font-family: Microsoft YaHei;\">&ldquo;(六)响应您的上述请求&rdquo;中的相关安排向您提供。</span></span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（二）更正或补充您的个人信息</span></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">当您发现我们处理的关于您的个人信息有错误时，您有权要求我们做出更正或补充。您可以通过</span><span style=\"font-family: Microsoft YaHei;\">&ldquo;（一）访问您的个人信息&rdquo;中列明的方式提出更正或补充申请。</span></span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（三）删除您的个人信息</span></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">您可以通过</span><span style=\"font-family: Microsoft YaHei;\">&ldquo;（一）访问您的个人信息&rdquo;中列明的方式删除您的部分个人信息。</span></span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">在以下情形中，您可以向我们提出删除个人信息的请求</span>:</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">1、如果我们处理个人信息的行为违反法律法规;</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">2、如果我们收集、使用您的个人信息，却未征得您的明确同意;</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3、如果我们处理个人信息的行为违反了与您的约定;</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">4、如果您不再使用我们的产品或服务，或您主动申请注销了账号;</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">5、如果我们不再为您提供产品或服务。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">若我们决定响应您的删除请求，我们还将同时通知从我们处获得您的个人信息的主体，要求其及时删除，除非法律法规另有规定，或这些主体获得您的独立授权。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">当您从我们的服务中删除信息后，我们可能不会立即从备份系统中删除相应的信息，但会在备份更新时删除这些信息。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（四）注销账户</span></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">在需要终止使用</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">产品或服务时，您可以申请注销您的</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">账户，您可以通过系统申请注销</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">账户</span>,&nbsp;若满足注销条件（即需为个人账户且没有待办事项）,账号将实时注销。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">在您主动注销账户之后，我们将停止为您提供产品或服务，根据《中华人民共和国合同法》或《中华人民共和国电子签名法》适用条款删除您的个人信息，或使其匿名化处理。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（</span></strong><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">五</span></strong><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">）约束信息系统自动决策</span></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">在某些业务功能中，我们可能仅依据信息系统、算法在内的非人工自动决策机制做出决定。如果这些决定显著影响您的合法权益，您有权要求我们做出解释，我们也将在不侵害</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">商业秘密或其他用户权益、社会公共利益的前提下提供申诉方法。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">（</span></strong><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">六</span></strong><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">）响应您的上述请求</span></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">为保障安全，您可能需要提供书面请求，或以其他方式证明您的身份。我们可能会先要求您验证自己的身份，然后再处理您的请求。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">在以下情形中，按照法律法规要求，我们将无法响应您的请求</span>:</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">1、与国家安全、国防安全有关的;</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">2、与公共安全、公共卫生、重大公共利益有关的;</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3、与犯罪侦查、起诉、审判和执行判决有关的;</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">4、有充分证据表明您存在主观恶意或滥用权利的;</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">5、响应您的请求将导致您或其他个人、组织的合法权益受到严重损害的;</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">6、涉及商业秘密的。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">六、我们如何处理儿童的个人信息</span></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">我们的产品、网站和服务主要面向成人。如果没有父母或监护人的同意，儿童（不满</span>16周岁）不得创建自己的用户账户。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">对于经父母同意而收集儿童个人信息的情况，我们只会在受到法律允许、父母或监护人明确同意或者保护儿童所必要的情况下使用或公开披露此信息。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">如果我们发现自己在未事先获得可证实的父母同意的情况下收集了儿童的个人信息，则会设法尽快删除相关数据。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">七、第三方收集和使用您信息的情况</span></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">当您在</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">中使用第三方提供的服务时，第三方可能会获取您的姓名、证件类型、证件号码、证件有效期、手机号码信息及其他提供第三方服务所必须的信息；在经过您的明示同意后，第三方可获取您的以上信息；对于您在使用第三方提供的服务时主动提供给第三方的相关信息，我司将视为您允许该第三方获取上述此类信息；对于您在使用该第三方服务时产生的信息，应由您与该第三方依法约定上述信息的收集和使用事项。在将信息提供给第三方前，我司将尽商业上合理的努力评估该第三方收集信息的合法性、正当性、必要性。我司会与第三方签订相关法律文件并要求第三方处理您的个人信息时遵守法律法规和签署的法律文件，要求第三方对您的信息采取保护措施。如您拒绝第三方在提供服务时收集、使用或者传递上述信息，将可能会导致您无法在</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">中使用第三方的相应服务，但这不影响您使用</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">的其他功能。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">八、本政策如何更新</span></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">我们的隐私政策可能变更。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">未经您明确同意，我们不会限制您按照本隐私政策所应享有的权利。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">对于重大变更，我们还会提供更为显著的通知</span>(包括我们会通过</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">网站公示的方式进行通知</span>)。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">本政策所指的重大变更包括但不限于</span>:</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">1、我们的服务模式发生重大变化。如处理个人信息的目的、处理的个人信息类型、个人信息的使用方式;</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">2、我们在控制权方面发生重大变化。如并购重组引起的所有者变更;</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3、个人/企业信息共享、转让或公开披露的主要对象发生变化;</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">4、您参与个人/企业信息处理方面的权利及其行使方式发生重大变化;</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">5、我们负责处理个人/企业信息安全的责任部门、联络方式及投诉渠道发生变化时;</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">6、个人/企业信息安全影响评估报告表明存在高风险时。</span></p>\n<p class=\"MsoNormal\" style=\"margin: 0pt 0pt 0.0001pt; text-align: justify; font-family: Calibri; font-size: 10.5pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">若您在本政策修订后继续使用</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">服务，这表示您已充分阅读、理解并接受修订后的本政策并愿意受修订后的本政策约束。</span></p>', NULL, NULL, '13793011113', '2023-10-27 10:17:15');
INSERT INTO `sys_text_config` (`id`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('rwtergsdfgsdg', 'certificate', '<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">版本：</span>2023年</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">10</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">月</span>13日</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">声明：</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">1.</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">本《数字证书使用协议》（以下简称</span><span style=\"font-family: Microsoft YaHei;\">&ldquo;本协议&rdquo;）适用于</span></span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">北京资源律动科技有限公司</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">（以下简称</span><span style=\"font-family: Microsoft YaHei;\">&ldquo;</span></span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">&rdquo;或者&ldquo;我们&rdquo;）官方网站以及使用的</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">产品或服务。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">2.您务必审慎阅读、充分理解各条款内容，如您对本协议有任何疑问或对任何条款不能准确理解的，请不要进行后续操作。</span></p>\n<p class=\"p\" style=\"margin: 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3.您承诺接受并遵守本协议的约定，届时您不可以未阅读本协议内容等理由，主张本协议无效，或要求撤销本协议。</span></u></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">本协议将帮助您了解以下内容</span><span style=\"font-family: Microsoft YaHei;\">:</span></span></p>\n<h4 style=\"margin: 20pt 0pt; font-family: 宋体; font-size: 12pt; padding: 0pt; line-height: 45pt;\"><strong><span style=\"letter-spacing: 0pt; font-size: 16pt;\">一、数字证书服务简介</span></strong></h4>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">1.您同意并认可</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">与工信部许可的电子认证服务机构合作，为通过实名认证后的互联网个人或企业申请数字证书。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">2.您同意并认可通过</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">电子签约平台完成实名认证并使用数字证书获得电子签名服务，成为</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">电子签名体系不可分割的部分。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3.本协议中的&ldquo;证书&rdquo;指个人数字证书或企业数字证书。</span></p>\n<h4 style=\"margin: 20pt 0pt; font-family: 宋体; font-size: 12pt; padding: 0pt; line-height: 45pt;\"><strong><span style=\"letter-spacing: 0pt; font-size: 16pt;\">二、名词解释</span></strong></h4>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">1.</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">用户</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">：</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">指委托</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">从第三方电子认证服务机构获得证书的个人、法人或其他组织机构。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">2.数字证书：</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">是指一段信息，它至少包含了一个名字，标识特定的</span><span style=\"font-family: Microsoft YaHei;\">CA或标识特定的</span></span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">用户</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">，包含了</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">用户</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">的公钥、证书有效期、证书序列号，及</span><span style=\"font-family: Microsoft YaHei;\">CA数字签名。</span></span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3.证书私钥：</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">数字证书包含证书本身和一个密钥对，密钥对的一部分是公钥，另一部分称为私钥。公钥公之于众，谁都可以使用，私钥只有自己知道。一般信息都是由私钥进行加密，相对应的公钥进行解密。</span></p>\n<h4 style=\"margin: 20pt 0pt; font-family: 宋体; font-size: 12pt; padding: 0pt; line-height: 45pt;\"><strong><span style=\"letter-spacing: 0pt; font-size: 16pt;\">三、</span></strong><strong><span style=\"letter-spacing: 0pt; font-size: 16pt;\">用户</span></strong><strong><span style=\"letter-spacing: 0pt; font-size: 16pt;\">的权利和义务</span></strong></h4>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">1.您应按照</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">规定的证书申请流程向</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">平台提供有关资料，并保证所填写的注册信息和所提供的资料的真实性、准确性和完整性，否则</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">平台有权拒绝您的申请请求。</span><strong><u><span class=\"15\" style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">在这些信息、资料发生改变时您应当及时通知</span></u></strong><strong><u><span class=\"15\" style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span class=\"15\" style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">。如因您的资料不真实、不完整、不准确或资料改变后未及时通知</span></u></strong><strong><u><span class=\"15\" style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span class=\"15\" style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">，造成的损失由</span></u></strong><strong><u><span class=\"15\" style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">用户</span></u></strong><strong><u><span class=\"15\" style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">自己承担。</span></u></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">2.您的证书信息在证书有效期限内变更的，应当及时书面告知</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">，并立即终止使用该证书（注：企业名称变更、企业统一社会信用代码变更、经营期限届满、姓名变更、身份证号变更等需要吊销原证书，重新申请数字证书）。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3.若您为企业用户，您企业因分立、合并、解散、注销、宣告破产或倒闭，或被吊销营业执照等导致主体资格终止的，应于上述情况发生前书面告知</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">申请吊销数字证书，并立即终止使用该证书，否则，因未尽该通知义务给</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">造成损失的，由您全部赔偿。</span></p>\n<p class=\"p\" style=\"margin: 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">4.您同意</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">平台向有关部门和个人核实您的信息。</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">平台应合法地收集、处理、传递及使用您的资料，并按照国家有关规定及本协议的约定予以保密。</span></u></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">5.您对证书享有独立的使用权， 您应当合法使用数字证书签署电子文件，并对使用数字证书的行为负责。您使用证书产生的权利，由您享有；您使用证书产生的义务、责任，由您承担。</span></p>\n<p class=\"p\" style=\"margin: 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">6.您应当对您所专有并控制的数字证书及</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">账户进行妥善保管，当您在</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">账户设置的通知方式（手机和邮箱）收到签署验证码时，应确保签署请求由本人发起，且对验证码承担保密义务。如您使用的数字证书私钥和密码泄漏、丢失，或者您不希望继续使用数字证书，或者您的主体不存在，您应当立即向</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">申请废止该数字证书。因您原因致使证书私钥泄露、损毁或者丢失的，损失由您承担。</span></u></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">7.您损害第三方电子认证服务机构利益的，须向电子认证服务机构赔偿全部损失。这些情形包括但不限于：</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">①您在申请数字证书时没有提供真实、准确、完整信息，或在这些信息变更时未及时通知</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">②您知道自己的私钥已经失密或者可能已经失密而未及时告知有关各方、并终止使用；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">③您未履行本协议相关约定的其他情形。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">8.第三方电子认证服务机构有权因安全风险因素要求</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">更换您的数字证书。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">9.您申请数字证书后，一旦发现如下情况之一时，应当立即向</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">申请吊销此证书：</span></u></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">①证据表明，此数字证书被用于签署可疑代码，包括但不限于病毒，木马，或者其他不恰当的程序；</span></u></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">②证书中内容不再正确或不再准确；</span></u></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">③此证书私钥信息已被泄露、丢失，或者其他相关部分已被错误使用。</span></u></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">10.证书一旦被吊销，</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">用户</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">将不能再使用该证书。</span></u></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">11.如果第三方电子认证服务机构发现了证书的不当使用，或者证书被用于违法甚至犯罪行为，电子认证服务机构有权直接吊销您的证书。</span></u></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">12.在发现或怀疑第三方电子认证服务机构提供的认证服务造成</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">用户</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">的网上交易信息的泄漏和</span><span style=\"font-family: Microsoft YaHei;\">/或篡改时，您应在3个月内向电子认证服务机构提出争议处理请求并通知有关各方。</span></span></u></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">13.根据《电子签名法》的规定，对于与您认证相关的信息以及您签署的电子合同等，您同意</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">有权自您的电子签名认证证书失效后保存十年。</span></u></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">14.您同意并授权</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">将您的身份信息和您签署的电子合同提供给国家司法机关、行政机关，具有司法行政职能的事业单位和社会团体以及电子合同其他签订方。</span></u></strong></p>\n<h4 style=\"margin: 20pt 0pt; font-family: 宋体; font-size: 12pt; padding: 0pt; line-height: 45pt;\"><strong><span style=\"letter-spacing: 0pt; font-size: 16pt;\">四、证书的申请、使用、更新、吊销</span></strong></h4>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">1. 您可以在</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">平台选择适合您的实名认证方式进行数字证书的申请，获得的证书和证书私钥由</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">托管，数字证书储存在</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">平台。证书仅用于</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">平台电子文件签署，</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">确保您使用证书和证书密钥制作的电子签名制作数据专属于您所有并由您控制，仅在您同意的情况下，您的证书方可被调用签署相关的电子合同等法律文件。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">2. 您每次在</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">平台签署电子文件都将使用您的数字证书。</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">只有核实了您的签署意愿后，才会依据您的意愿使用数字证书签署电子文件。</span><strong><u><span class=\"15\" style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">如果启用了指纹签署或自动盖章功能，视为您知情并同意每次签署电子文件的行为都使用您的数字证书。</span></u></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3. 本协议的有效期限为证书的有效期限，证书只能在数字证书有效期限内使用，证书有效期限届满，您需要继续使用的，应当及时办理证书更新手续，本协议有效期限顺延至证书更新期限届满日。在获得您将证书延期的授权前，</span></u><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">不会主动为您更新证书。</span></u></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">4. 证书私钥在证书有效期内损毁、丢失、泄露的，您应当及时申请办理吊销手续，吊销自手续办妥时起生效，吊销生效前发生的损失由您承担。</span></u></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">5. 您知悉证书私钥已经丢失或者可能已经丢失时，应当及时告知</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">平台。</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">将协助完成吊销该证书的工作。您应终止使用该证书。</span></p>\n<h4 style=\"margin: 20pt 0pt; font-family: 宋体; font-size: 12pt; padding: 0pt; line-height: 45pt;\"><strong><span style=\"letter-spacing: 0pt; font-size: 16pt;\">五、</span></strong><strong><span style=\"letter-spacing: 0pt; font-size: 16pt;\">开放签</span></strong><strong><span style=\"letter-spacing: 0pt; font-size: 16pt;\">的权利</span></strong></h4>\n<p class=\"p\" style=\"margin: 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">您有下列情形之一，</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">有权向第三方机构申请吊销证书并不承担任何责任。由此给</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">造成损失的，您应当向</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u></strong><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">承担赔偿责任：</span></u></strong></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">1.您向</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">平台提供的资料或者信息不真实、不准确或者不完整的。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">2.您证书的信息有变更，未终止使用该证书并通知</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">平台的。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3.您知悉证书私钥已经丢失或者可能已经丢失时，未终止使用该证书并通知</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">平台的。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">4.您超过证书的有效期限及应用范围使用证书的。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">5.您企业因分立、合并、解散、注销、宣告破产或倒闭，被吊销营业执照等导致主体资格终止而您未及时通知</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">吊销书证书的。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">6.您使用证书用于违法、犯罪活动的。</span></p>\n<h4 style=\"margin: 20pt 0pt; font-family: 宋体; font-size: 12pt; padding: 0pt; line-height: 45pt;\"><strong><span style=\"letter-spacing: 0pt; font-size: 16pt;\">六、第三方电子认证服务机构的服务、权利、义务、责任限制和免责</span></strong></h4>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">1. 第三方电子认证服务机构依法制定《电子认证业务规则》（简称CPS），并公布于第三方电子认证服务机构网站，明确第三方电子认证服务机构数字证书的功能、使用证书各方的权利、义务以及第三方电子认证服务机构的责任范围，本协议的相关条款源自CPS。包括但不限于：</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">①《中国金融认证中心电子认证业务规则（CPS）》</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">原文链接：</span><span style=\"font-family: Microsoft YaHei;\">http://www.cfca.com.cn/zhengshuzizhu/</span></span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">②《ZJCA电子认证业务规则》</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">原文链接：</span><span style=\"font-family: Microsoft YaHei;\">https://www.zjca.com.cn/web/webs/service/download.html</span></span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">③《天威诚信电子认证业务规则》</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">原文链接：</span><span style=\"font-family: Microsoft YaHei;\">https://www.itrus.com.cn/statement.html</span></span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">④《CWCA电子认证业务规则》</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">原文链接：</span><span style=\"font-family: Microsoft YaHei;\">https://cwca.aisino.com/www/uploadfiles/387239352241156455/西部安全认证中心电子认证业务规则CPS_387239352241227666.pdf</span></span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">2. 在</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">用户</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">通过安全工具使用数字证书对交易信息进行加密和签名的条件下，第三方电子认证服务机构将保证交易信息的保密性、完整性、抗抵赖性。如果发生纠纷，第三方电子认证服务机构将依据不同情形承担下述义务：</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">①提供签发</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">用户</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">数字证书的</span><span style=\"font-family: Microsoft YaHei;\">CA证书；</span></span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">②提供</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">用户</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">数字证书在交易发生时，在或不在第三方电子认证服务机构发布的数字证书废止列表内的证明；</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">③对数字证书、数字签名、时间戳的真实性、有效性进行技术确认。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3.有下列情形之一的，第三方电子认证服务机构有权吊销所签发的数字证书：</span></u></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">①</span></u><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">用户</span></u><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">申请数字证书时，提供的资料不真实、不准确、不完整；</span></u></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">②</span></u><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">用户</span></u><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">未履行本协议约定的义务；</span></u></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">③</span></u><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">用户</span></u><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">书面申请吊销数字证书；</span></u></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">④证书的安全性不能得到保证；</span></u></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">⑤法律、行政法规规定的其他情况。</span></u></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">4. 第三方电子认证服务机构将对</span></u><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">用户</span></u><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">申请数字证书时提交的信息进行审核，提供证书生命周期內的相关服务，同时向相关方提供查询服务。第三方电子认证服务机构及其注册机构均有义务保护</span></u><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">用户</span></u><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">隐私信息安全性。</span></u></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">5. 根据《电子签名法》的规定，如果</span></u><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">用户</span></u><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\"><span style=\"font-family: Microsoft YaHei;\">依法使用第三方电子认证服务机构提供的认证服务进行民事活动而遭受损失的，第三方电子认证服务机构将给予相应赔偿，除非第三方电子认证服务机构能够证明其提供的服务是按照《电子签名法》等相关法律法规和第三方电子认证服务机构向主管部门备案的</span><span style=\"font-family: Microsoft YaHei;\">CPS实施的。以下损失不在赔偿之列：</span></span></u></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">①任何直接或间接的利润或收入损失、信誉或商誉损害、任何商机或契机损失、失去项目、以及失去或无法使用任何数据、无法使用任何设备、无法使用任何软件；</span></u></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">②由上述损失相应生成或附带引起的损失或损害。</span></u></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">6. 以下损失第三方电子认证服务机构将不承担责任：</span></u></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">①非因第三方电子认证服务机构的行为而导致的损失；</span></u></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">②因不可抗力而导致的损失，如罢工、战争、灾害、电脑病毒、黑客攻击等。</span></u></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">7. 第三方电子认证服务机构对企业</span></u><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">用户</span></u><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">申请的数字证书的赔偿上限以各电子认证服务机构的官方说明为准。</span></u></p>\n<h4 style=\"margin: 20pt 0pt; font-family: 宋体; font-size: 12pt; padding: 0pt; line-height: 45pt;\"><strong><span style=\"letter-spacing: 0pt; font-size: 16pt;\">七、免责条款</span></strong></h4>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">1.因设备故障、电力故障及通讯故障或者电脑病毒、自然灾害、黑客攻击等因素造成您损失的，</span></u><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">不承担任何责任。</span></u></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">2.由于第三方电子认证服务机构的原因导致证书私钥被破译、窃取，致使您遭受损失的，由第三方电子认证服务机构向您承担赔偿责任。</span></u></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3.</span></u><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">为您提供本协议服务过程中因违反本协议约定给您造成损失的（包括因违反约定造成您签署的法律文件被司法机关终审判定无效），</span></u><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">赔偿您的全部金额不超过向您收取的当次服务费用总额。如果服务费金额超过人民币五万元的，则以五万元为限。您与</span></u><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span></u><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">签订的任何文件中有关赔偿责任限额与此约定不一致的，以此条约定为准。</span></u></p>\n<h4 style=\"margin: 20pt 0pt; font-family: 宋体; font-size: 12pt; padding: 0pt; line-height: 45pt;\"><strong><span style=\"letter-spacing: 0pt; font-size: 16pt;\">八、协议的终止</span></strong></h4>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">有下列情形之一的，本协议终止：</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">1.您证书期限届满。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">2.您证书被吊销。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">3.您向</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">申请终止本协议，</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">开放签</span><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">同意的。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">4.双方协商终止本协议的。</span></p>\n<p class=\"p\" style=\"margin: 20pt 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">5.依据法律、法规等规定，本协议应当终止的。</span></p>\n<h4 style=\"margin: 20pt 0pt; font-family: 宋体; font-size: 12pt; padding: 0pt; line-height: 45pt;\"><strong><span style=\"letter-spacing: 0pt; font-size: 16pt;\">九、争议解决条款</span></strong></h4>\n<p class=\"p\" style=\"margin: 0pt; font-family: Calibri; font-size: 12pt; padding: 0pt; line-height: 22pt;\"><strong><u><span style=\"font-family: \'Microsoft YaHei\'; letter-spacing: 0pt; font-size: 12pt;\">对于任何因履行本协议发生的或与本协议有关的一切争议，双方应首先通过友好协商的方式解决。不能协商解决的，各方均应依照中华人民共和国法律并由被告住所地的人民法院裁判。</span></u></strong></p>', NULL, NULL, '13793011113', '2023-10-27 10:17:54');
COMMIT;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` varchar(64) NOT NULL COMMENT '主键id',
  `username` varchar(100) DEFAULT NULL COMMENT '登录账号',
  `realname` varchar(100) DEFAULT NULL COMMENT '真实姓名',
  `password` varchar(255) DEFAULT NULL COMMENT '密码',
  `salt` varchar(45) DEFAULT NULL COMMENT 'md5密码盐',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `avatar_base64` varchar(10000) DEFAULT NULL COMMENT '缩略图base64',
  `post` varchar(100) DEFAULT NULL COMMENT '职务',
  `birthday` datetime DEFAULT NULL COMMENT '生日',
  `sex` int(11) DEFAULT NULL COMMENT '性别(0-默认未知,1-男,2-女)',
  `email` varchar(45) DEFAULT NULL COMMENT '电子邮件',
  `phone` varchar(45) DEFAULT NULL COMMENT '电话',
  `work_no` varchar(100) DEFAULT NULL COMMENT '工号，唯一键',
  `telephone` varchar(45) DEFAULT NULL COMMENT '座机号',
  `status` int(11) DEFAULT NULL COMMENT '状态(0,临时创建，待激活，1-正常,2-手动冻结（手动恢复），3-密码错误冻结（自动恢复）)',
  `freeze_time` datetime DEFAULT NULL COMMENT '冻结时间（自动）',
  `create_by` varchar(32) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除状态(0-正常,1-已删除)',
  `delete_by` varchar(64) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `update_password_time` datetime DEFAULT NULL COMMENT '更新密码时间',
  `password_level` varchar(64) DEFAULT NULL COMMENT '密码等级',
  `last_time_load_time` datetime DEFAULT NULL COMMENT '上次登录时间',
  `last_time_load_site` varchar(255) DEFAULT NULL COMMENT '上次登录地址',
  `user_type` varchar(255) DEFAULT NULL COMMENT '用户类型（游客：visitor）',
  `depart_id` varchar(64) DEFAULT NULL COMMENT '上次登录部门',
  `init_user_info` tinyint(1) DEFAULT NULL COMMENT '初始化用户信息',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index_user_name` (`username`) USING BTREE,
  KEY `uniq_sys_user_work_no` (`work_no`) USING BTREE,
  KEY `uniq_sys_user_email` (`email`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户表';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
BEGIN;
INSERT INTO `sys_user` (`id`, `username`, `realname`, `password`, `salt`, `avatar`, `avatar_base64`, `post`, `birthday`, `sex`, `email`, `phone`, `work_no`, `telephone`, `status`, `freeze_time`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `update_password_time`, `password_level`, `last_time_load_time`, `last_time_load_site`, `user_type`, `depart_id`, `init_user_info`) VALUES ('be990527-878e-4c30-b0f7-53dde299edb3', 'admin', '管理员', 'ac04b5d78a985942', 'RCGTeGiH', NULL, NULL, NULL, NULL, NULL, NULL, '', NULL, NULL, 1, NULL, NULL, '2023-12-14 19:59:58', 'admin', '2023-12-14 20:13:13', 0, NULL, NULL, '2023-12-14 19:59:58', NULL, '2023-12-14 20:13:11', '0|0|0|内网IP', NULL, 'd867bbc3-4fef-4c71-9589-05b6a3d0fa6c', 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_depart
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_depart`;
CREATE TABLE `sys_user_depart` (
  `ID` varchar(64) NOT NULL COMMENT 'id',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT '租户ID',
  `user_id` varchar(64) DEFAULT NULL COMMENT '用户id',
  `depart_id` varchar(64) DEFAULT NULL COMMENT '部门id',
  `manage_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否主管：0否 1是',
  PRIMARY KEY (`ID`) USING BTREE,
  KEY `index_depart_groupk_userid` (`user_id`) USING BTREE,
  KEY `index_depart_groupkorgid` (`depart_id`) USING BTREE,
  KEY `index_depart_groupk_uidanddid` (`user_id`,`depart_id`) USING BTREE,
  KEY `idx_sud_user_id` (`user_id`) USING BTREE,
  KEY `idx_sud_dep_id` (`depart_id`) USING BTREE,
  KEY `idx_sud_user_dep_id` (`user_id`,`depart_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户部门表';

-- ----------------------------
-- Records of sys_user_depart
-- ----------------------------
BEGIN;
INSERT INTO `sys_user_depart` (`ID`, `tenant_id`, `user_id`, `depart_id`, `manage_flag`) VALUES ('917f18bb-c7ea-49d7-aa9a-c0f0de4a3402', 'bf6fbbdd-c424-4cad-b591-9cd5277d2be5', 'be990527-878e-4c30-b0f7-53dde299edb3', 'd867bbc3-4fef-4c71-9589-05b6a3d0fa6c', 0);
INSERT INTO `sys_user_depart` (`ID`, `tenant_id`, `user_id`, `depart_id`, `manage_flag`) VALUES ('9ec8b062-5d9f-44bb-a1a6-6a8e88b381c5', '3efc0be2-3e09-4b6f-8ac0-0e5ea7065d29', 'be990527-878e-4c30-b0f7-53dde299edb3', '4c6508e5-cc16-420c-b516-3608a99c0516', 0);
COMMIT;

-- ----------------------------
-- Table structure for sys_user_password_his
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_password_his`;
CREATE TABLE `sys_user_password_his` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `password` varchar(256) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '类型',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户历史密码表';

-- ----------------------------
-- Records of sys_user_password_his
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` varchar(64) NOT NULL COMMENT '主键id',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT '租户ID',
  `user_id` varchar(64) DEFAULT NULL COMMENT '用户id',
  `role_id` varchar(64) DEFAULT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `index2_groupuu_user_id` (`user_id`) USING BTREE,
  KEY `index2_groupuu_ole_id` (`role_id`) USING BTREE,
  KEY `index2_groupuu_useridandroleid` (`user_id`,`role_id`) USING BTREE,
  KEY `idx_sur_user_id` (`user_id`) USING BTREE,
  KEY `idx_sur_role_id` (`role_id`) USING BTREE,
  KEY `idx_sur_user_role_id` (`user_id`,`role_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='用户角色表';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for tenant_auth_log
-- ----------------------------
DROP TABLE IF EXISTS `tenant_auth_log`;
CREATE TABLE `tenant_auth_log` (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `extend_id` varchar(36) DEFAULT NULL COMMENT '租户扩展表的id',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '姓名或企业名称',
  `organization_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '身份证号码或单位证件号码',
  `corporation` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '法定代表人',
  `corporation_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '代表人证件号',
  `phone` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `real_item` tinyint(2) DEFAULT NULL COMMENT '事项：实名认证、企业认证变更',
  `auth_status` int(2) DEFAULT NULL COMMENT '认证状态：未认证、认证审核中、认证审核失败、认证成功',
  `auth_type` int(11) DEFAULT NULL COMMENT '认证类型：首次认证、变更认证',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `delete_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '删除人',
  `sys_account_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '所属账户',
  `sys_tenant_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '所属租户',
  `sys_user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '所属用户',
  `sys_org_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '所属部门',
  `apply_time` datetime DEFAULT NULL COMMENT '申请时间',
  `check_time` datetime DEFAULT NULL COMMENT '审核时间',
  `check_msg` varchar(255) DEFAULT NULL COMMENT '审核意见',
  `check_user` varchar(64) DEFAULT NULL COMMENT '审核人',
  `tenant_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `tenant_type` int(3) DEFAULT NULL COMMENT '租户类型（个人、企业）',
  `apply_tenant_user` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='租户扩展信息历史记录表';

-- ----------------------------
-- Records of tenant_auth_log
-- ----------------------------
BEGIN;
INSERT INTO `tenant_auth_log` (`id`, `extend_id`, `name`, `organization_no`, `corporation`, `corporation_no`, `phone`, `real_item`, `auth_status`, `auth_type`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_time`, `delete_by`, `sys_account_id`, `sys_tenant_id`, `sys_user_id`, `sys_org_code`, `apply_time`, `check_time`, `check_msg`, `check_user`, `tenant_id`, `tenant_type`, `apply_tenant_user`) VALUES ('ff8080818c68362f018c68362f220000', '488a8d55-5eac-4f84-9fc1-ef3b8ad866af', '开放签平台管理员', '111111111111111111', '管理员', '341209195612121212', '12300000000', 1, 2, 1, '12300000000', '2023-12-14 20:03:51', '12300000000', '2023-12-14 20:04:40', 0, NULL, NULL, 'be990527-878e-4c30-b0f7-53dde299edb3', '3efc0be2-3e09-4b6f-8ac0-0e5ea7065d29', '14023c55-ab4c-40f9-b5aa-c74c93b588c7', 'A01', '2023-12-14 20:03:51', '2023-12-14 20:04:40', NULL, 'df3b2a91-d51c-4056-810a-b4c9649e03f7', 'bf6fbbdd-c424-4cad-b591-9cd5277d2be5', 1, '14023c55-ab4c-40f9-b5aa-c74c93b588c7');
COMMIT;

-- ----------------------------
-- Table structure for tenant_certificate
-- ----------------------------
DROP TABLE IF EXISTS `tenant_certificate`;
CREATE TABLE `tenant_certificate` (
  `id` varchar(64) NOT NULL,
  `tenant_id` varchar(36) DEFAULT NULL,
  `cert_id` varchar(36) NOT NULL,
  `cert_status` tinyint(2) DEFAULT NULL COMMENT '证书状态: 1、正常，2、吊销，3、失效',
  `cert_type` tinyint(2) DEFAULT NULL COMMENT '证书类型：1、ca根证书,2、系统防篡改根证书,3、ca个人证书,4、ca企业证书,5、系统防篡改个人证书,6、系统防篡改企业证书',
  `notes` varchar(255) DEFAULT NULL COMMENT '不可用原因',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `tenant_id` (`tenant_id`,`cert_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='租户证书关联表';

-- ----------------------------
-- Records of tenant_certificate
-- ----------------------------
BEGIN;
INSERT INTO `tenant_certificate` (`id`, `tenant_id`, `cert_id`, `cert_status`, `cert_type`, `notes`) VALUES ('33922fea-bfb0-4cd1-9cb0-7f8b86444fb5', 'bf6fbbdd-c424-4cad-b591-9cd5277d2be5', '28ae80b7-9883-4bca-86f5-fd593b3b9568', 1, 6, NULL);
INSERT INTO `tenant_certificate` (`id`, `tenant_id`, `cert_id`, `cert_status`, `cert_type`, `notes`) VALUES ('7f09ceca-9d56-44f2-a23c-21fab9c3a135', '3efc0be2-3e09-4b6f-8ac0-0e5ea7065d29', '01cd0de7-899e-4048-a9ff-baa7c27d970e', 1, 5, NULL);
INSERT INTO `tenant_certificate` (`id`, `tenant_id`, `cert_id`, `cert_status`, `cert_type`, `notes`) VALUES ('a079c0ae-5a2e-40bd-9a3c-3114da8628ac', 'bf6fbbdd-c424-4cad-b591-9cd5277d2be5', '6c0a9c35-c1e4-4ec1-b3e4-99b70e720c89', 1, 4, NULL);
COMMIT;

-- ----------------------------
-- Table structure for tenant_info_extend
-- ----------------------------
DROP TABLE IF EXISTS `tenant_info_extend`;
CREATE TABLE `tenant_info_extend` (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `tenant_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `tenant_type` int(3) DEFAULT NULL COMMENT '租户类型（个人、企业）',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '姓名或企业名称',
  `organization_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '身份证号码或单位证件号码',
  `corporation` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '法定代表人',
  `corporation_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '代表人证件号',
  `phone` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号',
  `email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `auth_status` tinyint(2) DEFAULT NULL COMMENT '认证状态：未认证、认证审核中、认证审核失败、认证成功',
  `auth_type` tinyint(2) DEFAULT NULL COMMENT '认证类型：首次认证、变更认证',
  `org_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '组织码',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `delete_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '删除人',
  `sys_account_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '所属账户',
  `sys_tenant_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '所属租户',
  `sys_user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '所属用户',
  `sys_org_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '所属部门',
  `auth_id` varchar(64) DEFAULT NULL COMMENT '最新认证记录id',
  `apply_tenant_user` varchar(64) DEFAULT NULL COMMENT '认证申请人id',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `tenant_id` (`tenant_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='租户扩展信息表';

-- ----------------------------
-- Records of tenant_info_extend
-- ----------------------------
BEGIN;
INSERT INTO `tenant_info_extend` (`id`, `tenant_id`, `tenant_type`, `name`, `organization_no`, `corporation`, `corporation_no`, `phone`, `email`, `auth_status`, `auth_type`, `org_code`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_time`, `delete_by`, `sys_account_id`, `sys_tenant_id`, `sys_user_id`, `sys_org_code`, `auth_id`, `apply_tenant_user`) VALUES ('42996452-4d61-4c59-8dd2-585a66a51c6b', '3efc0be2-3e09-4b6f-8ac0-0e5ea7065d29', 2, NULL, NULL, NULL, NULL, '12300000000', NULL, 0, NULL, NULL, NULL, '2023-12-14 19:59:59', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `tenant_info_extend` (`id`, `tenant_id`, `tenant_type`, `name`, `organization_no`, `corporation`, `corporation_no`, `phone`, `email`, `auth_status`, `auth_type`, `org_code`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_time`, `delete_by`, `sys_account_id`, `sys_tenant_id`, `sys_user_id`, `sys_org_code`, `auth_id`, `apply_tenant_user`) VALUES ('488a8d55-5eac-4f84-9fc1-ef3b8ad866af', 'bf6fbbdd-c424-4cad-b591-9cd5277d2be5', 1, '开放签平台管理员', '111111111111111111', '管理员', '341209195612121212', '12300000000', NULL, 2, 1, NULL, '12300000000', '2023-12-14 20:03:51', '12300000000', '2023-12-14 20:04:40', 0, NULL, NULL, 'be990527-878e-4c30-b0f7-53dde299edb3', '3efc0be2-3e09-4b6f-8ac0-0e5ea7065d29', '14023c55-ab4c-40f9-b5aa-c74c93b588c7', 'A01', 'ff8080818c68362f018c68362f220000', '14023c55-ab4c-40f9-b5aa-c74c93b588c7');
COMMIT;

-- ----------------------------
-- Table structure for third_service_record
-- ----------------------------
DROP TABLE IF EXISTS `third_service_record`;
CREATE TABLE `third_service_record` (
  `id` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `service_type` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '第三方类型',
  `service_url` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '调用地址',
  `req_para` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '请求参数JSON',
  `success_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '请求成功标识',
  `res_para` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci COMMENT '返回参数JSON',
  `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标识',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `delete_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '删除人',
  `sys_account_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '所属账户',
  `sys_tenant_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '所属租户',
  `sys_user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '所属用户',
  `sys_org_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '所属部门',
  `order_no` varchar(100) DEFAULT NULL COMMENT '订单号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='第三方接口调用记录表';

-- ----------------------------
-- Records of third_service_record
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `extra` varchar(255) DEFAULT NULL,
  `gmt_create` datetime(6) DEFAULT NULL,
  `gmt_modified` datetime(6) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `web_hook` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='定时任务-用户表';

-- ----------------------------
-- Records of user_info
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for warning_log
-- ----------------------------
DROP TABLE IF EXISTS `warning_log`;
CREATE TABLE `warning_log` (
  `id` varchar(64) NOT NULL COMMENT 'ID',
  `warning_name` varchar(64) DEFAULT NULL COMMENT '告警规则/警告内容',
  `system_name` varchar(64) DEFAULT NULL COMMENT '系统名称',
  `module_name` varchar(64) DEFAULT NULL COMMENT '模块名称',
  `method_name` varchar(64) DEFAULT NULL COMMENT '方法名称',
  `warning_type` int(3) DEFAULT NULL COMMENT '告警类型',
  `warning_level` int(3) DEFAULT NULL COMMENT '告警级别',
  `username` varchar(64) DEFAULT NULL COMMENT '操作人账号',
  `realname` varchar(64) DEFAULT NULL COMMENT '操作人姓名',
  `ip` varchar(64) DEFAULT NULL COMMENT 'IP',
  `request_url` varchar(256) DEFAULT NULL COMMENT '请求路径',
  `log_type` int(2) DEFAULT NULL COMMENT '日志类型（1 全局操作日志，2 自定义操作日志）',
  `thread_name` varchar(64) DEFAULT NULL COMMENT '线程号',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT=' 告警日志表';

-- ----------------------------
-- Records of warning_log
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for workflow_info
-- ----------------------------
DROP TABLE IF EXISTS `workflow_info`;
CREATE TABLE `workflow_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_id` bigint(20) DEFAULT NULL,
  `extra` varchar(255) DEFAULT NULL,
  `gmt_create` datetime(6) DEFAULT NULL,
  `gmt_modified` datetime(6) DEFAULT NULL,
  `lifecycle` varchar(255) DEFAULT NULL,
  `max_wf_instance_num` int(11) DEFAULT NULL,
  `next_trigger_time` bigint(20) DEFAULT NULL,
  `notify_user_ids` varchar(255) DEFAULT NULL,
  `pedag` longtext,
  `status` int(11) DEFAULT NULL,
  `time_expression` varchar(255) DEFAULT NULL,
  `time_expression_type` int(11) DEFAULT NULL,
  `wf_description` varchar(255) DEFAULT NULL,
  `wf_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX7uo5w0e3beeho3fnx9t7eiol3` (`app_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='定时任务-流程表';

-- ----------------------------
-- Records of workflow_info
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for workflow_instance_info
-- ----------------------------
DROP TABLE IF EXISTS `workflow_instance_info`;
CREATE TABLE `workflow_instance_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `actual_trigger_time` bigint(20) DEFAULT NULL,
  `app_id` bigint(20) DEFAULT NULL,
  `dag` longtext,
  `expected_trigger_time` bigint(20) DEFAULT NULL,
  `finished_time` bigint(20) DEFAULT NULL,
  `gmt_create` datetime(6) DEFAULT NULL,
  `gmt_modified` datetime(6) DEFAULT NULL,
  `result` longtext,
  `status` int(11) DEFAULT NULL,
  `wf_context` longtext,
  `wf_init_params` longtext,
  `wf_instance_id` bigint(20) DEFAULT NULL,
  `workflow_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='定时任务-流程实例表';

-- ----------------------------
-- Records of workflow_instance_info
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for workflow_node_info
-- ----------------------------
DROP TABLE IF EXISTS `workflow_node_info`;
CREATE TABLE `workflow_node_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `app_id` bigint(20) NOT NULL,
  `enable` bit(1) NOT NULL,
  `extra` longtext,
  `gmt_create` datetime(6) NOT NULL,
  `gmt_modified` datetime(6) NOT NULL,
  `job_id` bigint(20) DEFAULT NULL,
  `node_name` varchar(255) DEFAULT NULL,
  `node_params` longtext,
  `skip_when_failed` bit(1) NOT NULL,
  `type` int(11) DEFAULT NULL,
  `workflow_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IDX36t7rhj4mkg2a5pb4ttorscta` (`app_id`),
  KEY `IDXacr0i6my8jr002ou8i1gmygju` (`workflow_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='定时任务-流程节点表';

-- ----------------------------
-- Records of workflow_node_info
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- 1.1升级
-- ----------------------------

ALTER TABLE sign_re ADD COLUMN `downloader_type` tinyint(1) DEFAULT NULL COMMENT '下载权限类型，1参与人，2查看人，3全部' ;

ALTER TABLE sign_re_rule_detail MODIFY COLUMN content_type varchar(50);

ALTER TABLE sign_re_doc DROP COLUMN `doc_id` ;
ALTER TABLE sign_re_doc ADD COLUMN `doc_name` varchar(50) DEFAULT NULL COMMENT '签约文件名称' ;
ALTER TABLE sign_re_doc ADD COLUMN `doc_origin_id` varchar(64) DEFAULT NULL COMMENT '签约文件来源id' ;
ALTER TABLE sign_re_doc ADD COLUMN `annex_id` varchar(64) DEFAULT NULL COMMENT '真实文件id' ;

ALTER TABLE sign_re_sender CHANGE sneder_type sender_type tinyint(1);

ALTER TABLE global_id_config MODIFY COLUMN id_key varchar(50);
ALTER TABLE global_id_config MODIFY COLUMN id_name varchar(50);

ALTER TABLE sign_re_doc_control ADD COLUMN `interface_param_name` varchar(50) DEFAULT NULL COMMENT '接口参数名称' ;


ALTER TABLE sign_re_auth ADD COLUMN `tenant_id` varchar(64) DEFAULT NULL COMMENT '租户id' ;

ALTER TABLE sign_ru  MODIFY COLUMN code varchar(200);
ALTER TABLE sign_ru  MODIFY COLUMN SUBJECT varchar(200);


ALTER TABLE certificate_info ADD COLUMN `unique_code` varchar(64) DEFAULT NULL COMMENT '证书唯一码' ;

-- ----------------------------
-- 1.2升级
-- ----------------------------

ALTER TABLE tenant_certificate ADD COLUMN `holder_type` tinyint(1) DEFAULT NULL COMMENT '证书所属类型（1、个人；2、企业';
ALTER TABLE tenant_certificate MODIFY COLUMN `cert_type` tinyint(1) DEFAULT NULL COMMENT '证书类型：1、平台防篡改证书,2、测试数字证书,3、CA事件数字证书,4、CA长效数字证书';
ALTER TABLE certificate_info MODIFY COLUMN `cert_type` tinyint(1) DEFAULT NULL COMMENT '证书类型：1、平台防篡改证书,2、测试数字证书,3、CA事件数字证书,4、CA长效数字证书';
ALTER TABLE sys_config MODIFY COLUMN `name` varchar(200) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称' ;
INSERT INTO sys_config (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5c9ad6af-1eff-ad09-1234-493b58d61123', '1、均使用事件证书，2、均使用长效证书，3、企业使用长效证书，个人使用事件证书', 'ca_use_type', '1', NULL, NULL, 'admin', '2024-01-05 10:25:39');
UPDATE tenant_certificate SET `cert_type` = 2, `holder_type` = 1 WHERE `cert_type` = 3;
UPDATE tenant_certificate SET `cert_type` = 2, `holder_type` = 2 WHERE `cert_type` = 4;
UPDATE tenant_certificate SET `cert_type` = 1, `holder_type` = 1 WHERE `cert_type` = 5;
UPDATE tenant_certificate SET `cert_type` = 1, `holder_type` = 2 WHERE `cert_type` = 6;

-- ----------------------------
-- 1.3升级
-- ----------------------------

-- ----------------------------
-- 1.4升级
-- ----------------------------

ALTER TABLE monitor_config DROP COLUMN `sensitive_type`;
ALTER TABLE monitor_config ADD COLUMN `sensitive_time` double DEFAULT NULL COMMENT '敏感操作有效时间';

ALTER TABLE sign_ru_sender ADD COLUMN `sender_external_type` int(3) DEFAULT NULL COMMENT '外部企业签署人类型，1手机号，2邮箱号';
ALTER TABLE sign_ru_sender ADD COLUMN `sender_external_value` varchar(255) DEFAULT NULL COMMENT '外部签署人接受值';

ALTER TABLE sign_ru_task ADD COLUMN `tenant_name` varchar(255) DEFAULT NULL COMMENT '租户名称';

ALTER TABLE sign_ru_operator ADD COLUMN `tenant_name` varchar(255) DEFAULT NULL COMMENT '租户名称';

ALTER TABLE sign_re_sender ADD COLUMN `sender_external_type` int(3) DEFAULT NULL COMMENT '外部企业签署人类型，1手机号，2邮箱号';
ALTER TABLE sign_re_sender ADD COLUMN `sender_external_value` varchar(255) DEFAULT NULL COMMENT '外部签署人接受值';

CREATE TABLE `message_short_mapping` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `meg_code` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '消息code',
  `msg_para` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '消息内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='短连接-参数映射表';


ALTER TABLE sign_ru_signer MODIFY COLUMN `signer_type` int(11) DEFAULT NULL COMMENT '签署方类型，1发起方，2个人接收方，3企业接收方';

ALTER TABLE sign_ru_operator MODIFY COLUMN `signer_type` int(11) DEFAULT NULL COMMENT '签署方类型，1发起方，2个人接收方，3企业接收方';

ALTER TABLE sign_ru_task MODIFY COLUMN `user_type` int(11) DEFAULT NULL COMMENT '签署方类型，1发起方，2个人接收方，3企业接收方';

-- ----------------------------
-- 1.5升级
-- ----------------------------

-- ----------------------------
-- 1.0 bug
-- ----------------------------
INSERT INTO `job_info` (`id`, `app_id`, `concurrency`, `designated_workers`, `dispatch_strategy`, `execute_type`, `extra`, `gmt_create`, `gmt_modified`, `instance_retry_num`, `instance_time_limit`, `job_description`, `job_name`, `job_params`, `lifecycle`, `max_instance_num`, `max_worker_count`, `min_cpu_cores`, `min_disk_space`, `min_memory_space`, `next_trigger_time`, `notify_user_ids`, `processor_info`, `processor_type`, `status`, `task_retry_num`, `time_expression`, `time_expression_type`) VALUES (13, 6, 5, NULL, 1, 1, NULL, '2022-09-05 16:30:04.573000', '2024-03-01 14:33:50.992000', 0, 0, '修改用户状态-解冻用户', '修改用户状态', '1', NULL, 0, 0, 0, 0, 0, 1668226320000, NULL, 'com.resrun.modules.system.job.BasicSysUserEditJob', 1, 1, 0, '4000', 1);


-- ----------------------------
-- 1.6升级
-- ----------------------------

-- ----------------------------
-- 1.6 bug
-- ----------------------------
UPDATE `message_template_button` SET `template_id` = '0f1081a2-4b7a-44f3-af6d-367b18c62c3e', `button_name` = '查看', `button_desc` = '', `button_route` = '/contract/detail/sign', `button_style` = '', `route_method` = '', `button_order` = NULL WHERE `button_code` = 'contract_expired';
UPDATE `message_template_button` SET `template_id` = 'f8fe562f-b7c4-42ae-ba48-358cfef215cd', `button_name` = '查看', `button_desc` = '', `button_route` = '/contract/detail/sign', `button_style` = '', `route_method` = '', `button_order` = NULL WHERE `button_code` = 'contract_wrirte_reject';
UPDATE `message_template_button` SET `template_id` = '42d5a3b9-bf3b-48e2-8ac6-4e083036d3b2', `button_name` = '查看', `button_desc` = '', `button_route` = '/contract/detail/sign', `button_style` = 'primary', `route_method` = '', `button_order` = NULL WHERE `button_code` = 'sign_reject';





-- ----------------------------
-- 1.7升级
-- ----------------------------
CREATE TABLE `sign_re_doc_control_property` (
                                                `id` varchar(64) NOT NULL COMMENT '主键',
                                                `re_id` varchar(64) DEFAULT NULL COMMENT '业务线配置id',
                                                `control_id` varchar(64) DEFAULT NULL COMMENT '关联控件id',
                                                `property_type` varchar(64) DEFAULT NULL COMMENT '控件属性类型',
                                                `property_value` varchar(64) DEFAULT NULL COMMENT '控件属性值',
                                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='业务线配置-控件属性表';

CREATE TABLE `sign_ru_doc_control_property` (
                                                `id` varchar(64) NOT NULL COMMENT '主键',
                                                `ru_id` varchar(64) DEFAULT NULL COMMENT '业务线实例id',
                                                `control_id` varchar(64) DEFAULT NULL COMMENT '关联控件id',
                                                `property_type` varchar(64) DEFAULT NULL COMMENT '控件属性类型',
                                                `property_value` varchar(64) DEFAULT NULL COMMENT '控件属性值',
                                                PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='业务线实例-控件属性表';

ALTER TABLE sign_template_record ADD COLUMN `doc_page` int(5) DEFAULT NULL COMMENT '文件页数' ;
ALTER TABLE sign_ru_doc ADD COLUMN `doc_page` int(5) DEFAULT NULL COMMENT '文件页数' ;
ALTER TABLE sign_re_doc ADD COLUMN `doc_page` int(5) DEFAULT NULL COMMENT '文件页数' ;


-- ----------------------------
-- 2.0升级
-- ----------------------------
-- ----------------------------
-- Table structure for api_callback
-- ----------------------------
DROP TABLE IF EXISTS `api_callback`;
CREATE TABLE `api_callback` (
  `id` varchar(64) NOT NULL,
  `callback_url` varchar(128) DEFAULT NULL,
  `status` int(3) DEFAULT NULL,
  `req_para` varchar(2048) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for api_callback_record
-- ----------------------------
DROP TABLE IF EXISTS `api_callback_record`;
CREATE TABLE `api_callback_record` (
  `id` varchar(64) NOT NULL,
  `callback_id` varchar(64) DEFAULT NULL,
  `req_para` varchar(2048) DEFAULT NULL,
  `res_para` varchar(2048) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for api_developer_manage
-- ----------------------------
DROP TABLE IF EXISTS `api_developer_manage`;
CREATE TABLE `api_developer_manage` (
  `id` varchar(64) NOT NULL,
  `developer_name` varchar(255) DEFAULT NULL COMMENT '开发者名称',
  `token` varchar(255) NOT NULL COMMENT '授权码',
  `public_key` longtext COMMENT '公钥',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '1:正常 2:冻结',
  `tenant_id` varchar(255) NOT NULL,
  `create_time` datetime DEFAULT NULL,
  `callback_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_code` (`token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for api_normal_req
-- ----------------------------
DROP TABLE IF EXISTS `api_normal_req`;
CREATE TABLE `api_normal_req` (
  `id` varchar(64) NOT NULL,
  `token` varchar(64) DEFAULT NULL,
  `operator_account` varchar(255) DEFAULT NULL,
  `unique_code` varchar(255) NOT NULL,
  `req_url` varchar(255) DEFAULT NULL,
  `req_para` text COMMENT '请求参数',
  `res_para` text COMMENT '返回参数',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for api_relation_link
-- ----------------------------
DROP TABLE IF EXISTS `api_relation_link`;
CREATE TABLE `api_relation_link` (
  `id` varchar(64) NOT NULL,
  `type` varchar(16) DEFAULT NULL,
  `token` varchar(64) DEFAULT NULL,
  `external_account` varchar(255) NOT NULL,
  `system_id` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for api_warning_req
-- ----------------------------
DROP TABLE IF EXISTS `api_warning_req`;
CREATE TABLE `api_warning_req` (
  `id` varchar(64) NOT NULL,
  `code` int(11) DEFAULT NULL COMMENT '返回码',
  `token` varchar(64) DEFAULT NULL COMMENT '授权码',
  `operator_account` varchar(64) DEFAULT NULL COMMENT '服务类型',
  `unique_code` varchar(255) DEFAULT NULL,
  `req_url` varchar(255) DEFAULT NULL,
  `req_para` text COMMENT '请求参数',
  `req_ip` varchar(255) DEFAULT NULL COMMENT '请求IP',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Table structure for sign_user_confirm
-- ----------------------------
DROP TABLE IF EXISTS `sign_user_confirm`;
CREATE TABLE `sign_user_confirm` (
  `id` varchar(64) NOT NULL,
  `main_id` varchar(64) DEFAULT NULL COMMENT '任务主表id',
  `confirm_type` varchar(64) DEFAULT NULL COMMENT '校验类型',
  `confirm_para` varchar(1024) DEFAULT NULL COMMENT '校验参数',
  `final_confirm_flag` tinyint(1) DEFAULT NULL COMMENT '最终校验结果',
  `create_by` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='签署意愿确认主表';

-- ----------------------------
-- Table structure for sign_user_confirm_step
-- ----------------------------
DROP TABLE IF EXISTS `sign_user_confirm_step`;
CREATE TABLE `sign_user_confirm_step` (
  `id` varchar(64) NOT NULL,
  `confirm_id` varchar(64) DEFAULT NULL COMMENT '意愿校验主表id',
  `step` int(3) DEFAULT NULL COMMENT '第几步',
  `confirm_type` varchar(64) DEFAULT NULL COMMENT '校验类型',
  `data_type` varchar(64) DEFAULT NULL COMMENT '数据类型',
  `system_para` varchar(1024) DEFAULT NULL COMMENT '系统数据',
  `user_para` varchar(1024) DEFAULT NULL COMMENT '用户数据',
  `confirm_flag` tinyint(1) DEFAULT NULL COMMENT '校验结果',
  `create_by` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `bind_data_flag` int(3) NOT NULL DEFAULT '0' COMMENT '关联数据标识',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='签署意愿确认子表';


ALTER TABLE third_service_record MODIFY COLUMN `service_url` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '服务地址' ;


INSERT INTO `job_info` (`id`, `app_id`, `concurrency`, `designated_workers`, `dispatch_strategy`, `execute_type`, `extra`, `gmt_create`, `gmt_modified`, `instance_retry_num`, `instance_time_limit`, `job_description`, `job_name`, `job_params`, `lifecycle`, `max_instance_num`, `max_worker_count`, `min_cpu_cores`, `min_disk_space`, `min_memory_space`, `next_trigger_time`, `notify_user_ids`, `processor_info`, `processor_type`, `status`, `task_retry_num`, `time_expression`, `time_expression_type`) VALUES (14, 6, 5, NULL, 1, 1, NULL, '2024-04-09 16:01:45.451000', '2024-04-12 10:19:44.388000', 0, 0, '刷新腾讯云人脸token', '刷新腾讯云人脸token', NULL, NULL, 0, 0, 0, 0, 0, 1712889600000, NULL, 'com.resrun.modules.opensign.task.QCloudAccessProcessor', 1, 2, 0, '0 0/20 * * * ? ', 2);
INSERT INTO `job_info` (`id`, `app_id`, `concurrency`, `designated_workers`, `dispatch_strategy`, `execute_type`, `extra`, `gmt_create`, `gmt_modified`, `instance_retry_num`, `instance_time_limit`, `job_description`, `job_name`, `job_params`, `lifecycle`, `max_instance_num`, `max_worker_count`, `min_cpu_cores`, `min_disk_space`, `min_memory_space`, `next_trigger_time`, `notify_user_ids`, `processor_info`, `processor_type`, `status`, `task_retry_num`, `time_expression`, `time_expression_type`) VALUES (15, 6, 5, NULL, 1, 1, NULL, '2024-04-11 14:45:14.692000', '2024-04-11 15:53:14.600000', 0, 0, '回调任务1-2', '回调任务1-2', NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, 'com.resrun.modules.opensign.task.SignCallback1Processor', 1, 2, 0, '0 0/5 * * * ? ', 2);
INSERT INTO `job_info` (`id`, `app_id`, `concurrency`, `designated_workers`, `dispatch_strategy`, `execute_type`, `extra`, `gmt_create`, `gmt_modified`, `instance_retry_num`, `instance_time_limit`, `job_description`, `job_name`, `job_params`, `lifecycle`, `max_instance_num`, `max_worker_count`, `min_cpu_cores`, `min_disk_space`, `min_memory_space`, `next_trigger_time`, `notify_user_ids`, `processor_info`, `processor_type`, `status`, `task_retry_num`, `time_expression`, `time_expression_type`) VALUES (16, 6, 5, NULL, 1, 1, NULL, '2024-04-11 15:53:04.050000', '2024-04-11 15:53:04.050000', 0, 0, '回调任务2-3', '回调任务2-3', NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, 'com.resrun.modules.opensign.task.SignCallback2Processor', 1, 2, 0, '0 0 0/2  * * ? ', 2);
INSERT INTO `job_info` (`id`, `app_id`, `concurrency`, `designated_workers`, `dispatch_strategy`, `execute_type`, `extra`, `gmt_create`, `gmt_modified`, `instance_retry_num`, `instance_time_limit`, `job_description`, `job_name`, `job_params`, `lifecycle`, `max_instance_num`, `max_worker_count`, `min_cpu_cores`, `min_disk_space`, `min_memory_space`, `next_trigger_time`, `notify_user_ids`, `processor_info`, `processor_type`, `status`, `task_retry_num`, `time_expression`, `time_expression_type`) VALUES (17, 6, 5, NULL, 1, 1, NULL, '2024-04-11 15:53:53.643000', '2024-04-11 15:53:53.643000', 0, 0, '回调任务3-4', '回调任务3-4', NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, 'com.resrun.modules.opensign.task.SignCallback3Processor', 1, 2, 0, '0 30 23 ? * ? ', 2);
INSERT INTO `job_info` (`id`, `app_id`, `concurrency`, `designated_workers`, `dispatch_strategy`, `execute_type`, `extra`, `gmt_create`, `gmt_modified`, `instance_retry_num`, `instance_time_limit`, `job_description`, `job_name`, `job_params`, `lifecycle`, `max_instance_num`, `max_worker_count`, `min_cpu_cores`, `min_disk_space`, `min_memory_space`, `next_trigger_time`, `notify_user_ids`, `processor_info`, `processor_type`, `status`, `task_retry_num`, `time_expression`, `time_expression_type`) VALUES (18, 6, 5, NULL, 1, 1, NULL, '2024-04-11 16:20:33.742000', '2024-04-12 10:17:44.390000', 0, 0, '腾讯云刷脸数据拉取记录任务', '腾讯云刷脸数据拉取记录任务', NULL, NULL, 0, 0, 0, 0, 0, 1712890800000, NULL, 'com.resrun.modules.opensign.task.QCloudFileLinkProcessor', 1, 2, 0, '0 0 01 1-31 * ? *', 2);

CREATE TABLE `sign_re_sign_confirm` (
                                        `id` varchar(64) NOT NULL,
                                        `sign_re_id` varchar(64) DEFAULT NULL COMMENT '业务线主表id',
                                        `signer_type` tinyint(1) DEFAULT NULL COMMENT '签署方类型，1发起方，2接收方',
                                        `signer_id` varchar(64) DEFAULT NULL COMMENT 'signer表id或者sender表id',
                                        `confirm_type` varchar(50) DEFAULT NULL COMMENT '校验类型',
                                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务线配置-签署意愿数据表';

CREATE TABLE `sign_ru_sign_confirm` (
                                        `id` varchar(64) NOT NULL,
                                        `sign_ru_id` varchar(64) DEFAULT NULL COMMENT '业务线主表id',
                                        `signer_type` tinyint(1) DEFAULT NULL COMMENT '签署方类型，1发起方，2接收方',
                                        `signer_id` varchar(64) DEFAULT NULL COMMENT 'signer表id或者sender表id',
                                        `confirm_type` varchar(50) DEFAULT NULL COMMENT '校验类型',
                                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务线实例-签署意愿数据表';

CREATE TABLE `sign_ru_sign_temporary` (
                                          `id` varchar(64) NOT NULL,
                                          `sign_confirm_order_no` varchar(64) DEFAULT NULL COMMENT '签署意愿校验订单号',
                                          `sign_ru_id` varchar(64) DEFAULT NULL COMMENT '业务线实例id',
                                          `task_id` varchar(64) DEFAULT NULL COMMENT '任务id',
                                          `params` longtext COMMENT '参数',
                                          `status` tinyint(1) DEFAULT NULL COMMENT '状态,0是进行中，1是已完成',
                                          `create_by` varchar(255) DEFAULT NULL,
                                          `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                          `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
                                          `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                          `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
                                          `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
                                          `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
                                          PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务线实例-提交签署临时数据';

CREATE TABLE `sign_template_auth` (
                                      `id` varchar(64) NOT NULL,
                                      `template_id` varchar(64) DEFAULT NULL COMMENT '模版主表id',
                                      `auth_type` tinyint(1) DEFAULT NULL COMMENT '权限类型，1管理员，2使用范围',
                                      `user_type` tinyint(1) DEFAULT NULL COMMENT '用户类型',
                                      `tenant_user_id` varchar(64) DEFAULT NULL COMMENT '租户用户id',
                                      `create_by` varchar(255) DEFAULT NULL,
                                      `create_time` datetime DEFAULT NULL COMMENT '创建时间',
                                      `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
                                      `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                                      `delete_flag` tinyint(1) DEFAULT NULL COMMENT '删除标志：0未删除，1已删除',
                                      `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
                                      `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
                                      `tenant_id` varchar(64) DEFAULT NULL COMMENT '租户id',
                                      PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='模板权限控制表';

ALTER TABLE sign_template_control ADD COLUMN `properties` varchar(2000) DEFAULT NULL COMMENT '控件属性' ;

INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('de6c5fd9-9f37-44d2-bcc7-c67cb2fcc828', '70588803-52e4-433d-a61f-0a68e1febd72', '授权凭证', 1, '', 6, '/interface/authorization', '/interface/authorization', NULL, 'ant-design:file-protect-outlined', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2024-04-11 15:41:44', NULL, NULL, 0, NULL, NULL, 1, NULL, 'false', 0);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f5c47e94-6ad9-42a0-8160-8a1b2298b074', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', 'de6c5fd9-9f37-44d2-bcc7-c67cb2fcc828', 0, NULL, NULL, NULL, '2024-04-11 18:22:01', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('d92dfd47-2507-4ad9-9729-d355dbec509f', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', 'de6c5fd9-9f37-44d2-bcc7-c67cb2fcc828', NULL, NULL);
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5coo6af-1eff-ad09-4fb4-p93b22d61607', '签署校验人脸开关', 'sign_confirm_type', 'false', NULL, NULL, NULL, '2024-04-09 19:40:37');

CREATE TABLE `api_callback_page_url` (
                                         `id` varchar(64) NOT NULL,
                                         `callback_page_url` varchar(500) DEFAULT NULL,
                                         `create_time` datetime DEFAULT NULL,
                                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='待办任务链接-回调地址';

-- ----------------------------
-- 2.0.1升级
-- ----------------------------

CREATE TABLE `annex_base64` (
  `id` varchar(64) NOT NULL,
  `file_base64` longtext COMMENT '图片真实文件id',
  `create_by` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='文件base64表';

INSERT INTO `annex_base64` (`id`, `file_base64`, `create_by`, `create_time`) VALUES ('ca1a8548-6447-4c23-a9d9-af6edeffaa4c', 'iVBORw0KGgoAAAANSUhEUgAAAVUAAACTCAMAAAAJFHoBAAAAVFBMVEVHcEz///////////////////////////////////////////////////////////////////////////////////////////////////////////+DS+nTAAAAG3RSTlMAZBDux/Sr/gb6up6E2yfm0VBZGDsybnlFkR4lf4MtAAAReklEQVR42u1dh5ajuBI1IBAi5/j//7kKSCgC9ranPR5q3znPPdgYX5UqV+nxuOmmm2666aabbrrppptuuummm2666aabTqmdji4+d684Gg4+UdT/CqZxFq6DG4cVNPETd2vWFSaN77gareUQ/wugeuWKyQmrhy8Gc3f5dhV+P1xRYv1ED/FVlHj+l2PqExQIuXBLCUoQA3vtfh27G4Sgs7IqvQajj4O1n6auL5a29X0/Pif8rrYtir6batvWi/MN1crxdfw6ugZrsr0dIs8iTSC/On4aqsH6Ks2xXQ6yH2rXWK349CVYa/F22Nn5ntHwPajaYa0OmXWHaY0uKL5cgNps/9IueJ+w710Qv1rFX4Tq6rk0CCErs2bis8EFE2vcOZvRdu8sVlgVde1GS1Fg0dQUX4cqUyGYcttFcPxZTVyE0ndhpQSVD7fI+VxJ8XWotvzHWyz0mCMBywubNnJ+capIVZOi30a1/GlUj5h1eka/DOsRqvXRg+W/bmGWP41qzy83bq0dnLNqj45QXY72GPx1VDmsKAyCEpN42iDfSQi4PKfvCZADNsUMKN0b49ysKo5gS2OwfjSvYliXFtv/hpbuLMb4urNY7LfLmZXZ2D0lzEz9KahHWwjO8yGoH8CrLtvnBNULstpweQQW1amnKoEapRvz52kURQmo8jw6EU3RN6JKdy9KC6cAONNVzS5TYSqUXGCsDo5n9ZiybRG7CRurjTeM/Veg2uQVSKIoTdOZ/heFqGKvCXNh7kpUAdDUghoPo5Bl87xHSGJ5fwP8lTFUHAvpck4/5cNPkaY/iuqoyTWo/wETxbFiRj017OlV8r+Uw9rlqw7bJgKAGhdb17BQYjb9V6NqUyDJHq9yKXYGayMvyQbb9lAQe7m9//CFSVKrD538c6gGuwBwEWO1VjKaeOSLWxUjDjPgKDVfnky1OiD0/zFUsapJr6H68ASzQu5mLALVhFlOOVRYs3X6Hd/Oqz66iGqbQg1U5r0GaeN74nblJnIZMZkA669C1U/PeXVYL6KK9VWlgYqZNSG5h0msDKyGQnc8kvq75KoF1i0iKoJUQX4d1Uec7IHq7Qt0L1Zzh4NwLh6PL0P14U9dQTzfypBwsY/D9323C4lm4sQFaIqt1iHLpB0dp7A58WKB8iz9Z+dZ/6dvBVx6ozK9yXY9UDJmkKEPVJs4KB5/Db0J1dqyz0XIcLryYJ0RxILDP46qlNeLTKSvMN1gmhBwjeJ/GlXJ9Nph5WbSei4TY1kblrviK6d/AtXKimoRyhy2aF+FzoNgsr8VLlIkYK2mfxVVLXLPr6XOnIHGqKO8+4kX6yfrp+Ia1zggh+uBur4vtpIgTCLMNvk7CVRaWhG0LKTWAQczzaqgDVUIIok0U3XWVgAcP2dfKXYwNfXjSLYGqu5zUO2vp/2U0J5MRp1jrkX6KKFQ+VCluJknoaYiVb4bbYwZZ8otwfQXouomHdZSFqA8hAqRsoVhrIRMSLT/IqZruHNloy5VkBXfg6oeKXZkRkvl3xk0ojjAWdHXRdoeyWXkCt0HzrMu/k5UHYGpKg5YzF+CcTh2AorMiBxodaqWmE4YjVP7dajGjncBWnaQIlk9CUwsGfBiqEzhbvL0ZM1vBzmIvgpV31VPhi+BsN0MIhTLDkOoRwCa1AZWOdbUXhGEq/9qL7Hr1vLXYI3Hn0e1EEkoRpGk5v1OuFjEOhKVbKph1eVPmCafWL3CYIVBlZCs8zxnmNI9D5/tJH4p+3OeU5KSrspwharm7TT+a1UvtZAs1kku8JO4Pf+JtT6yK94OazP1rao2hW+O4iu+Vay58I3GKouGXLlfzhwlFw18AUVdSZafFW6dV9u+vOyxDpphX2iopnuUKnGYADqzwhycgposmSqKPyw4UFmr9i6jOmso9trd9hR0LIx43RJqFdsW1OfJWbqIUxq40i+/TYXe3vAkqomWqe+0v3miNd+rAyrzIXYZkBCmq8MrqGJxNM3lR1av7d718hKqgVYvPOnuEzcK+vmglHV7UzD32tcfVbRvy5hV8OPqAkRwpHy8gmqhl+rUukbiMGfBgQgkjhiK9oBYjevisIUyjoPnNV424ro/8p83kLo3UIJIiVm13vxZuqp3VIBeRdXTrdjGqHXf9mhotTXEfYC9N9WvM4DWqtAZAQzLB4esZ0c3xFVUI10DeUYQe7CKxCOaGjnTRaJglbk9IGg/FdS9205zma6iGuhO6GB0CemFQWctqDWQ3rMVqsqJLuEgfiysuwkTxq+g2hsMOJrCU60+P+kQ6IC6xrnRPCOM2fBDhUAPXYX1F1EVfJPpNkVnOrWX/PVMs75G3UVZ4Oc2sxoGzPQSqpXRcD1bMv75cXRPVU9IFUjF1tduLCQMPlQATG4OuoZqYer1yGL9yvoKndlAQGPWQFulfD0Z8/DL1JZuFXINVb7dITA+6Fv4z4xX2Z4KqsyaqiKmv25K/E78SophoPYlVEtDrIpEtiuyfd4fEanM2ql/zs8UFf2q/rc4kZdQnVZzjkWwlZvIoAI58XJKmzHF47hbkxDanbBPZlU5LxD4Dul2XBGVmGLVN2NIbfVkD9+syopKZvLmOs//BtXwUC9fQbWWm4BU9pWcoUWNnl6olWTcCflSzbKQAZ/TDWzjVDn+nsevoLrXUkpmWaZv0EYP6l2YaRMp7lktCdb+icEYf15Rqe5O/XgF1WE1A6Z+oKp6nMA30ifVaXypV2RwK9URpnws0wc6AEvliFVaUHWa2lIAXzBOkahGwWRN9KHTYGigxBYSgaqYvJJ+HqN6arl9fjROyYnqbpdBMqZiTpIEBOrQC4yxImdCkb3Puwtuq4j4tlRQp5J8+TyzqtH4x97CkB+Umeh2WfYw59BMjyWFCqiglZ2Bk5K+Eqe+p30uxDj5inz5NF3V6HU3oZ1tzlAdNQ9CL4tpEk2eJr4Sd9RL+lJSn0CD/5SiKhtMSkVJPC1RmElRB65S+HUjqzFk3HTiNdn32gR1sVxpNSZaAWwaa+FczcpCWjP8MzUs1a/Dqo4vgag+iUY7rG0pgrCFjrwjEEKuz/zQAetpy+sRZY+PgjV0SrfwGNUO6QZAq8hqFaS8txm5ik33v1D9AIug2TV14FbF6CTe1kHdrZLgKidZQuwzK/TMgGR+/O2o7kNO8gP7BJ5V2myNe3uWo+ewBiSNvAuEUhcyewmFlGz561Hl+/cwmRad1i8x/0zyHDGsME+yTjV4LVN9hWeXHajRvw9VGq9O4yu++HrQge8hNR7X99ItWbt0bl8UNg5ASQv8H2b9lO7WeD4dYRwllI68mCI6yHLi1ojQPS69w/aswmLN4DVNTYdDXyRce92xaVYfWxjwBirm9vhy8bjppptuuummm2666aabbrrppptuuummm/4cLUP2eoiyyPM9irokb0rOJ1LOoR6tUdt2EEHw9H3n3/mz/ANjHBV2VY1l/6fIluQMkFxtrhYY+WNko+y5VRzk4qwAF8ybPwUXOfKK44Kcpvee8jZcXIZ65bmc1Ymjq1q3yGbSSKpTLf2mRR7iTYrLoLo+rjKBC5WSEi6h1GxYmM1hhMo905LYSvJ/hghUqLiE6uBC1XWijFSCCvaxayV7P0jI2RbVlhZrTlCtDtJRi/wciN22p9+Y0SMyRpnjSTP8lgiDFxoQX6VKWVPvILHnudKU56h6UoEOKpSMHlvRmk5ro2M/i5gUUYM4HqRhBPkFVKVeu3VsxdEYGp/MnDfGdw5hiEu5eM47SEI7US0iQMbe7BRo8+c7KBU+BeoUNglVJnALykVAXeE03E7ZwkT2OX8dBCEX0Eof7FC65AiZQFRyWfC+yit6tCHfCY1RhpIgMStRGZ5YOW/IyiNgIvYdHTsf1qzamtb6QK+hMz6aYev1JxdBmpJruOUfF7UFaQocg609awkiLa+MKFfgU0RTUso6lkQf4aN0uICPAKZwDcj/0ROdyQvwnmwjFjVibLmJqmvvQZf9xUSoVDUx083Y0zFVBauIzijLSIn+5lxbdfwwIcLQ3vZaFK/SlQO0nA5XHxNzBR/rOGg7D76qEV+CtVZ+nYnqzqviFbTP8W3ZWMRq0dQ7EZAV4VGQB8wYAMrvcUxVkmsR7eWT2W5j0HIs8gNGgiZaWJeLvKfQn0RVIhPVSjz6Lld9y7g59hZoKejHJiLVOjUY6YRkSPX+AFFqsYxJ6SoHYprljW5n5nFXQlU8YbkU4GI70nkzsYdHmYoqoHXDWDSU9MX4DlSXNBuxZdkoqJJZwAs+O9yCKjxBNaUCVQerSCfJkcC/jLxBGyPGd/KyH+7XQWUgEeG7kRBxKGbyIpNNI2+mSjEpyFDQtmI26aJyCOJskXMF3b4D1UyvD2uMnQGe4dX5uFGKl5YHhu4thHThexzWSK2NgvzbhbZqtdopagXM+nms0sqEfwbV8QjVwURV4tXAtUpOw5q1s1TEQoBZbKJqqz6XlB7+O6SnlRJlT1+UekUa6TiEs7toLeRrp6zhj6NaYDszkZEgqOYVprzc2nKAXVtdQzWWwGPdf3lMG+MhUsH3S7gfegP52TfhsQInz7FDliGUEESzqqS/gBGUUQvfUCoYz0ChjUE7+camtnLN4gt2G0Ea/62MA5f7gftSNPxRnoXYkrT54As1kDyKqnpAhdVKkM4GAWRX53oHQyjbvCHp+iIE+YtA8KqngjNfRlV/pFig6h2jikJCSLwKJVRPDhmn3xEPzOqF20LyQz7CyCPIThU+CZMT+We2Ajmx1XPRcSXk6sCb5lS5mlDjwQtCQakFVadcrazgvBPVI7ma4O0qfocEfEh8zBC7F+1ccjZL0pmNaU1DuQ0rk0beG5JVBLcEqhlvm1VRjagXkGnbKZTjGpINAHVUkxdRfcStQv7jaVTPbIDRoq2UGTUSbFCEYI4PY4t2VBHdtHSn8O07aqiOpAWT0Obu66iWNFCByD4hlEjdNTZwXqYXefUyqmwQZTjozODXRJVQG6ltRQgdKX1X+NLi714AhIZ/JaMKWUgm4EZebkH1T/lW7+ZV7FtBgO854bAnYOXuRF5SB6uLdtcnwjJja2nlIqTsTp34WebVhD4kDqhgqiyowo9CVew8iMTWQ5dRlWXQmAczi7ig5MKBAorrS+KyHnPuI3sOB6hJBQPVqdkIq8OAv37P9GCG6sAO471uWcHnUC0AytkEmpAHN0Nfzp4FxjEtegA9Y8al5wgQXkBVlvVv7semqHqbUrRHV6zhpOdQrZjrCNj9e6VFqxcjkuWuq8FIS1BYaWB0saPKJEBIDw1INVQnnFsTyTUSXeGvB9yW7b0FVXpUjH85ZnXoAdtRnVg+Y2BvmJXhSsLkRbIdXpppCW9LmYDHEa/atVVy2JldvANVYcHY46u6DcD29Fg8I1cBBakQsWUJsYaG5aF2ShO0JHv60N0yx2wAYllRjZizIRdXUH3H0ItJVqYmqgOoosLGq6mlpe8A1Y6lrkPixdZaIm7G89SptjpDlU+qtPUSMgng9AJG7JTwbBdxS2C5p77C8scT2KPsSzYH4wo9de/ljmGMri78gH46wsqfxq4N8KEWsRksJwbsBzDBXK/AEF6A27f6c9qKjeefc3KCF3M5me+Jk4CZmSfYzwGheWL5HRmJ0UF3CKgWo7uJrjLPnoeSocq9XxlV7vmW0X6YcxVhHbTlriKbvCVmmAXV8t2oUtcnsgVJYGQTwJvFigxxVDm7rwPNZlqt8wBt4o4DAgK0fYpmTMZA9rG2pGJinDBQh/RT3S+gSmbWAWb56MoitVQOWF10OeBt0c8HfdJAjf3ocrXRVgzxDnDsTiDNN0oMpNjoHGhx6cP3n3LRXT3wYanU8w/VBFWcw7CMbJ5KCXlIWiOcENxlph4JbxAUUWuaKgj1MUz9DEp8D1+KrxrgrchmiyJH1P3rKG6yzO08jvNpmcmUJLrLNYLR3kPvZdndyX3TTTfddNNNN91000033XTTTTfddNNX0H9T3B4qZP76bQAAAABJRU5ErkJggg==', 'admin', '2024-04-23 21:47:18');
INSERT INTO `annex_base64` (`id`, `file_base64`, `create_by`, `create_time`) VALUES ('10d80605-a88f-42e0-aa5f-b60a2ef54a5d', 'iVBORw0KGgoAAAANSUhEUgAAAVUAAACTCAMAAAAJFHoBAAAANlBMVEVHcEwSf9ITfdIRftMSf9IRdtQSftMSf9ISf9ISf9MRf9MSf9ISftIQetMSf9IRftISf9ISf9JEZjRAAAAAEXRSTlMA2x8/6wdS9sllhqgwErl3mQp3L1YAAA74SURBVHja7Z1HduM4EECRc6r7X3YWYEAkKduyOWpWb1pPssJnoTJAhB555JFHHnnkkUceeeSRRx555JFHHnnkkVMh9OBJ/9p7casP/oKIf4UpNxL0nAMowV94Nw0AYfoXAZjm/wJUzQBgjlUDADP08tspAACQYfgXLj+n/Ycz9ZkCwIxbyk+zdO396PJ2MMRqlyft7bA6QalzhHjvPb8g3ntCnKN0uCw5Xn6omnzc+ry8hjWsVEfa77Yn492oMviqpBFWAYfK6re/voRV7J9Gp3p/ZHD+f1THWFdlxGeY7AXHh7dXL86ee+Ic5YvjWwXzD6I6VBF3qKxm+1tGzr9b3D9KSimlXB4YXqmqpD4LIcRRKjT5OKqbC8Fzl35x0RJ59MHzZyGQj6NKmkVbLWn5yqK1c9tTWdVe7MdRPVJW+orXjnBEVRx9MfznESb7aapurqybfrFzVXXyiCpht6a6YZWMMcbw9m0Z3mVfuBgzxtjiN/RRPgTA5gvDnENlh9GHgntTRZ54z3svTUfB+P467gk5izLF1AC4U091qIsmwd2pTmKfE6pXbHWcGQB1mqkWUG1aFBNba0NQCmN7YprCJ1J1DABkIrMM4dRXaVl5JtpYlEJTg3POLd85UkqF0DpG9xFUBcYqWGvTIlYCzg+sDUFhHOq8XYtdtI7RGGP2Cgkv13fgCPH6KxVPY1/kwbdb99+k2gdBcrAmzeGq3QpPFEOLTZWGw+/PS1ctAvdZVM15FBYKA3CItQpEJanenyDkPPKydYfmpub0p3W1E1ZEABPJqkbKoInWUUVEDmTQuI3P1uf9v0fVXqOKdJ9mkI1qWMLmWjXJPEn+cKpeXqTqbZ+7RQBgVviNOGOLyc0iP5GqT+dUz8lvzoaqNnUjuSVI9yuDI2kTjyA+y65ewMrwdaqIhzYf9m3C1YRRTKb7RgBfzgI4dY54v+XnokptnaO7qmpKBaVU0M3bJyF0LCNWxNNgNdelAVV9F3fvPus3cys18xuqrn3yk0osIqf1FubQ/0beRFUM1vlxM+aoNHDXJuAvUy36erYnfUXp9DC14P801Thqf2ygzm1iVRooHB8T/zDVunNHmo+S50UwVWWxvniI6T9AFY+oNpX79Tl7scbMY3VRKEI+lMHArbhyIYSglFLnHMkjQd7vgefaZPfe7z9ibbvnxrvop4K2iSBbCO47pKVen9SwHe6vCQ9wU64Ovi+RzwrTVeWpTlZVnWYel5pInV9IMSyT3YfrT1DtsI4zqKYE0Ez0pIPv2JRk5E5P1G/KjPscqm1UNGniMQZdfU+c9ltoW+bCbm4ZALCh/DOpTgpTipdYYx1tjaMjZ/C0XdAWt3ZdtpGSj6PKJ69SyGOAVBnW1EZapTWNg4Z/r9MCjws5yn4UVT/vr3glt1iClyFAG64SnfCwlChyvLIKpULoMLE4f4aVx5+nunkga5O1ydrCzXu6LXqBikm2OrCi+Ecu9t/1WxesDIdgbUrGGFMMheTHWbZfujzOLWnMZEuVNvpH6izVFV6fjkMA/zNU05/mAY7wcRFD8iu5Fffj0hQeU11DBFwGnHFW3fqGsHuVW81waOdyxqqbwN41qpN2BxUmdcBOWbE6b4uTJmC4WXFADaf2LlNNDUXXvNvegt4HhFu1qmc/lUDnTbGAEKKJFQ3dWwmBYRB5mWpoKNLmsd8sBJ37FVLgon0WNZ1W4xvYm41bGBgGkZepsqZKT1vbuQYFLh2Mstp1q5trPv7cN1Gj7tfBxmMNukq160SJluqmvOzABHIGIO1eEBM2pWRMjFFrrU3UWmuhdZ57U0xZWse75l6+yk32K1ylqtsoVnQdJtzUW+ToLfVkb6oXRklQrlUEFQm6r8x2212lalsPpDsrHV8e4KWijbrUQBHUbbkWjRD3JaqsTUJ7qv40uW/CV1W8xvdhw3aVbot1rwE16/IiVdcpYOyNpz2seY0bVq6xH3oQCsqbUnXTbWAXqcbOr5ueHX0lXzfNko+tCSCTWZb7SJjmJhepqu7P00Aj8QsGYB0kdDVE2V9IdlNVpXMNukbV9fbDDqLfcl5C+mtXWjWW23WX6KbjQQTPNegaVdP79TDISr18obREGmipNjHutnuBlshbDeYgXqOK+3RpfdNZZftcw2ytrLR+mG6uqumoOHmJKh1YZdaX+8vBE3XhatcXmleXaeuG3VRV42FxMlyZiAq9WeW9nS6hXtpxnuorXW0C0vdWVXHsl9UFqmKQLtFOJwl+KQfYr8x6qSrDqu66b7VPIwcHIlygWsxS0s5/bZy7ol5CVy2r6JNWd+dBVm6Hw2WvUdWDsGyrPy8rfbR74Dx4d5W+lxFrOqrQ3CikmmjPOdViI8SmOC7UQcG4fSpPi6Gs8nhhe7SFaPZ+iqrrIZ7hgSjqYCCijcsYR8iEEBSrR81dXXvG8vIIqukbjDiV8fHtfJXGV7Yw4DOqhQ0xfQUFaDvaB8qXycDJSB8DsPsIlYnUV2HV3XxVN1EjKfoK1djs523HYroRk+Cr9lQ70pfyfEKMUUcdtcVGx+qfjjpu18kuIwrJWhvCnytut4NhAnWf9HOncZmtTcbs3JSmnNs48tP234GoP8eaLjqOE6qFBV1KR/rod0s9qAhUwet3qF44KedXscqpdZPHVGmncPXqng6hcjyO6b5FNd3AsrIre+3kyUwI7dKqAhejdD6Eaofhx7eo3iHO2nYy4QN7dDpps2zc27sc2yA0S740CN0GKTHqlf3fdXVz2MqfJ40H4wt5U0nhcBwGwGE9a1mNp6WrzM5MyhL/T6rIMwDLr+TiR0MhWtb1OOeKt8w7eyfhfm75VW2Bb20AuUt+lU6/iQ0hhHB4BCc5etZhkHF65WhoVExorUWerSYXxBHn3HKa1cefHV5hPR7TIfONPfz03yOPPPLII4888sgjjzzyyCOPPPLII4888rKQaL5eonQY7zedJO9qzoei5yDGVVsft9+Q3lfG9tWhr1zHKTnznSFbC/tx8yh1A0Y+2pG8uEMyFs1vxIbNobhPdzoAad5Tg/Ws6jrHg+nEOKNKTN5B2ogofhMp+6y+H4KejQlcmJQsuMii1ePGI4Fs77SE9x3OHutxHn3wS/Ss+WvOpxpCuQ0mAQCoEJRSeLnFoDihenQ6yPbtEwDIfD8WhxQAGCGE0NG0lzc/fmcrS1XXVB809l6mmoaqKN2AiQCAyHkCAMc5ACjOy+9yhWr5vpHMND6t6yS+8xAGzsrhOX3Q2tez5q+zKoTSHLLmx1TDFExOqOr8kwlCedS3pJpkvssWY4xJAGD7o9VAV1NEkc2ocrnAZG+dxPSyWAmioxpePsFoGY/YG6tUAoAUmVWe9dEaAJTQUewWQKWEASCkBAAspdkpCXo4gugZAFgNAJQTYgFARQYAJtm0WgCrlFISmFJKKQAApZR601CbK6bFe6rTtTeLv1Q7NZFnYByXAODyVTL5dBX/irei672EEuz3FtqGV+kyy84AGEcmDyHH5td82SN+CWu9z/4a1fE5vsshapi07l1kC64VZtmuqer3zLyVOENiCieEef60qJcZJN7s3JK/SbVcvR1VtX313a76GVU9msuMCx2hYp5ntxwhFEGO7u1Nyu1+1e3FjudREoDiAjBiwOimowqkqakqrbXWDIBprbWO76BKkola60ZXvffECfEFqmkdl67XQqJVtKA86o4R4wVVvC9qXVHFMcYYLQCkGGM0ZWikE3IAEAjlCBGV/5DUGiLXIAavH0LeQdW0kaXoVsbLVM1xgjUe3SSDxS/r5bnFdZu3Ik3A6SUAGDFf4b9ENX6dKptdpWlgnZd/3qLSZopkdh4VL6lKjDHGDADyfUtbJBRgP3V0TPU37CoJwYaShAAArJTCGC9zerNB/ktUOW8DLszzBixZw/e4vA/4ejtw+YoDN1KGBAAGY4zVKm+nyo2qxOwXOB57qyOqxydNblnbMgys/LYFa3Cry81b5VtZ1kOeZ1QVAEa4jXBlS5Uxlk99Zuv/8vO6hpMuU22/Ee+dwpiqrNQo/48V5YoD4bkShitLsx7zL60mCCGqNuUKCgAkWw5XVErhbcfVZldjaVd1ma5ghDSTm9gMUlyyq2oI551Uj+xqkKXsl0BKyZhUHJH98MOwHt1qZXkUiLlw1F9B1az5Q+2tAoCqaxLsFarhi1QRrydmPRpTdV+PV+PAW+HTsfJ4laps6gDVp1kAtdRMlleNqGJrrbUSQFprrS1uQjaE82X5oq5epppLSTK2yuDz/kOCEPJ+M/2y2neFkN8O1z2bSrcAASG9riKT1bGi+osZ67upopi3o1KFlcrj7lthldo9urVyu9PFakLweQxgGl3VAJBSSkndnuq+8or/XaZa2qCImcmaJMOFGwpUP9hlf84nLYlNV6uyWkV1K9AwALb+/z2nB2eqMe+IGtvVyzWrOVWiJM5E5Bo5lHtTfGInxZXFEcVpu2LX1SnV0ta/eT82Xcqd+LV49UVdVXvdyi22lu5VbzbYdRW78qoBAAh4ssN701XpnHPONlRpintzbauuaK2jMUm/hWpauznjSqA57gVcoUpzpKPzC0xVotvVtIzDcd+W0EfnMm26OvZWx8cKk3dQ3XZ5vkDVDQ6MPbCrKp+xsNWWi48RAADDNdHaPCfnW+ayrkYACCGEgG9B1Uy8VVTKkhHVBIDFdao0OxkJILlo6ghJibGDppPaFhYzXZ1mAXFvfJUZK2OMSfnzJzTGMkIRJ91AVSvx6DDGWQzAABRCAWTwavSytmITB3cMKDZiY0NmujrNrX7PW+Xj+Q1ewsQy1TcDq0aq4L58hck1uilVsR3d7YbjDVBlu/kbiKrCnrM0vKNlypqol4aiHdlbPrYX7N1UXbap7Hw//ZIkLYumM0dquqf9oACjjqP8FYja290GIVT3pZcILXRURf6W9A+oIgGgxgFU4/A5PsQupv4ZrlDlR1TXT5bLqAviEbepQuhIuelRW/L9d7mgV894aE4NaxpUHINkYWSUcbu298LifuG6/oKWe9XaAQDI9s2dUUxuyYTq9U/OElJ5u3uHvMu+C2PmyWM8v129CKELSWa3C9DGOPTII4888sgjjzzyyCOPPPLII4888sgjHyD/Ac8Ety+uAfnRAAAAAElFTkSuQmCC', 'admin', '2024-04-23 21:47:21');

ALTER TABLE sys_config MODIFY COLUMN `value` varchar(1024) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '值' ;

INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('10c2ccd4-6bf9-489b-842e-c38c6500ecb3', '70588803-52e4-433d-a61f-0a68e1febd72', '平台配置', 0, '', 9, '/platform/config', '/platform/config', NULL, 'ant-design:setting-outlined', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2024-04-19 09:59:31', 'admin', '2024-04-19 10:07:03', 0, NULL, NULL, 1, NULL, 'false', 0);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('2dac1900-14fe-41cd-a0c6-6c3c7458d7aa', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', '10c2ccd4-6bf9-489b-842e-c38c6500ecb3', 0, NULL, NULL, NULL, '2024-04-19 10:03:46', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('9162656b-e8f6-4db3-926e-7399fc4f9c5e', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', '10c2ccd4-6bf9-489b-842e-c38c6500ecb3', NULL, NULL);


-- ----------------------------
-- Records of sys_config
-- ----------------------------

INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('349ad6af-1eff-ad09-4fb4-493b22d61607', '网站配置-非白色logo', 'website_other_logo', '10d80605-a88f-42e0-aa5f-b60a2ef54a5d', NULL, NULL, 'admin', '2024-04-23 21:47:29');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('511ad6af-1eff-ad09-4fb4-493b22d61607', '网站配置-白色logo', 'website_white_logo', 'ca1a8548-6447-4c23-a9d9-af6edeffaa4c', NULL, NULL, 'admin', '2024-04-23 21:47:29');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5c9126af-1eff-ad09-4fb4-493b22d61607', '邮箱配置-邮箱登录名', 'email_username', '', NULL, NULL, 'admin', '2024-04-23 10:13:14');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5c9127af-1eff-ad09-4fb4-493b22d61607', '邮箱配置-SMTP服务器地址', 'email_host', '', NULL, NULL, 'admin', '2024-04-23 10:13:14');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5c9226af-1eff-ad09-4fb4-493b22d61607', '腾讯云人脸识别服务-app_id', 'qcloud_face_app_id', '', NULL, NULL, 'admin', '2024-04-23 10:18:41');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5c9a22af-1eff-ad09-4fb4-493b22d61607', '腾讯云人脸识别服务-secret', 'qcloud_face_secret', '', NULL, NULL, 'admin', '2024-04-23 10:18:41');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5c9ad6af-112f-ad09-4fb4-493b22d61607', '阿里云短信配置-regionId', 'ali_sms_region_id', '', NULL, NULL, 'admin', '2024-04-23 18:03:38');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5c9ad6af-1eff-a129-4fb4-493b22d61607', '邮箱配置-SMTP服务授权码', 'email_auth_code', '', NULL, NULL, 'admin', '2024-04-23 10:13:14');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5c9ad6af-1eff-a349-4fb4-493b22d61607', '邮箱配置-端口号', 'email_port', '', NULL, NULL, 'admin', '2024-04-23 10:13:14');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5c9ad6af-1eff-ad09-12b4-493b22d61607', '阿里云短信配置-accessKeyId', 'ali_sms_access_key_id', '', NULL, NULL, 'admin', '2024-04-23 18:03:38');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5c9ad6af-1eff-ad09-4fb4-493b22d12607', '邮箱配置-邮件发送方名称', 'email_nick_name', '开放签', NULL, NULL, 'admin', '2024-04-23 10:13:14');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5c9ad6af-1eff-ad09-4fb4-493b22d61607', '网站配置-系统标题', 'website_title', '开放签', NULL, NULL, 'admin', '2024-04-23 21:47:29');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5c9ad6af-1eff-ad09-4fb4-49b122d61607', '阿里云短信配置-accessKeySecret', 'ali_sms_access_key_secret', '', NULL, NULL, 'admin', '2024-04-23 18:03:38');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5c9ad6af-1eff-ad09-4fb4-49qq22d61607', '短信配置-短信签名', 'sms_sign', '', NULL, NULL, 'admin', '2024-04-23 18:03:38');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5c9ad6af-1eff-ad09-4fb4-ee3b22d61607', '短信模板-企业实名认证审核不通过通知', 'mes_template_entAuthFaild', '', NULL, NULL, 'admin', '2024-04-23 18:03:38');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5c9ad6af-1eff-ad09-4fb4-ll3b22d61607', '短信模板-企业添加内部成员通知', 'mes_template_inviteUser', '', NULL, NULL, 'admin', '2024-04-23 18:03:38');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5c9ad6af-1eff-ad09-4fb4-o88b22d61607', '短信模板-文件抄送通知（发起时通知）', 'mes_template_copyBegin', '', NULL, NULL, 'admin', '2024-04-23 18:03:38');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5c9ad6af-1eff-ad09-4fb4-o93b22d61607', '短信模板-文件填写通知', 'mes_template_writeTask', '', NULL, NULL, 'admin', '2024-04-23 18:03:38');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5c9ad6af-1eff-ad09-4fb4-pp3b22d61607', '短信模板-验证码模板', 'mes_template_captcha', '', NULL, NULL, 'admin', '2024-04-23 18:03:38');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5c9ad6af-1eff-ad09-4fb4-z93b22d61607', '短信模板-企业实名认证审核通过通知', 'mes_template_entAuthSuccess', '', NULL, NULL, 'admin', '2024-04-23 18:03:38');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5c9ad6af-1fff-ad09-4fb4-o93b22d61607', '短信模板-文件抄送通知（签署完成时通知）', 'mes_template_copySign', '', NULL, NULL, 'admin', '2024-04-23 18:03:38');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5c9ad6af-1ppf-ad09-4fb4-o93b22d61607', '短信模板-文件签署通知（接收方）', 'mes_template_signTaskOut', '', NULL, NULL, 'admin', '2024-04-23 18:03:38');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5c9adssf-1eff-ad09-4fb4-493b22d61607', '网站配置-copyright', 'website_copyright', 'QQ群：482074553  联系电话：15010993257  邮箱：service@resrun.cn  Copyright 2019-2023  北京资源律动科技有限公司  版权所有', NULL, NULL, 'admin', '2024-04-23 21:47:29');
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5co2o6af-1eff-ad09-4fb4-o93b22d61607', '短信模板-文件签署通知（发起方）', 'mes_template_signTaskIn', '', NULL, NULL, 'admin', '2024-04-23 18:03:38');

-- ----------------------------
-- Records of sys_app_info
-- ----------------------------
DELETE FROM `sys_app_info`;
INSERT INTO `sys_app_info` (`id`, `app_name`, `app_code`, `app_icon_address`, `app_icon`, `app_type`, `app_status`, `app_desc`, `app_address`, `app_front`, `default_flag`, `useful`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('13131313', '企业管理后台', 'app_00005', 'f51a5da5-4edc-42f4-ae77-cc6c0dc5cbad', '', 1, 1, '企业/组织/团队可通过该应用管理内部组织架构以及内部成员的权限分配', 'http://localhost/tenant', '/#/overview', 1, 0, 0, NULL, NULL, NULL, '2023-04-23 10:53:10', 'admin', '2023-12-13 15:14:43');
INSERT INTO `sys_app_info` (`id`, `app_name`, `app_code`, `app_icon_address`, `app_icon`, `app_type`, `app_status`, `app_desc`, `app_address`, `app_front`, `default_flag`, `useful`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('313123131', '开发管理后台', 'app_00003', 'd5f08d63-ce00-4ce8-923c-0331eec93839', '', 1, 1, '应用的构建、发布以及一些通用服务和工具的配置、使用', 'http://localhost/backstage', '/#/application', 0, 0, 0, NULL, NULL, NULL, '2023-04-23 10:53:50', '13341081511', '2023-10-24 17:38:51');
INSERT INTO `sys_app_info` (`id`, `app_name`, `app_code`, `app_icon_address`, `app_icon`, `app_type`, `app_status`, `app_desc`, `app_address`, `app_front`, `default_flag`, `useful`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('490489ab-d8b4-414c-ad77-d856962c286f', '开放签', 'opensign_0001', NULL, '', 0, 1, '开放签', 'http://localhost', 'dashboard/workbench', 1, 1, 0, NULL, NULL, 'admin', '2023-04-23 10:53:00', 'admin', '2023-12-12 20:49:48');
INSERT INTO `sys_app_info` (`id`, `app_name`, `app_code`, `app_icon_address`, `app_icon`, `app_type`, `app_status`, `app_desc`, `app_address`, `app_front`, `default_flag`, `useful`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('70588803-52e4-433d-a61f-0a68e1febd72', '运营管理后台', 'paas_manage_001', NULL, '', 1, 1, '运营管理后台', 'http://localhost/manage', '/#/usermanage/personal', 0, 1, 0, NULL, NULL, 'admin', '2023-04-23 10:53:20', 'admin', '2023-12-01 11:24:19');
INSERT INTO `sys_app_info` (`id`, `app_name`, `app_code`, `app_icon_address`, `app_icon`, `app_type`, `app_status`, `app_desc`, `app_address`, `app_front`, `default_flag`, `useful`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('70588803-52e4-ss3d-a61f-0a68e1fe12d', '手机端签署服务', 'opensign_0001', NULL, '', 0, 1, '手机端签署', 'http://localhost/mobile', '', 0, 1, 1, NULL, NULL, 'admin', '2023-04-23 10:53:20', 'admin', '2023-12-01 11:24:19');
INSERT INTO `sys_app_info` (`id`, `app_name`, `app_code`, `app_icon_address`, `app_icon`, `app_type`, `app_status`, `app_desc`, `app_address`, `app_front`, `default_flag`, `useful`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('70588803-52e4-ss3d-a61f-0a68e1febd72', '实名认证服务', 'opensign_0001', NULL, '', 0, 1, '实名认证服务', 'http://localhost/auth', '', 0, 1, 1, NULL, NULL, 'admin', '2023-04-23 10:53:20', 'admin', '2023-12-01 11:24:19');
INSERT INTO `sys_app_info` (`id`, `app_name`, `app_code`, `app_icon_address`, `app_icon`, `app_type`, `app_status`, `app_desc`, `app_address`, `app_front`, `default_flag`, `useful`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('70588803-52e4-ss3d-a61f-0a68e1febder', '站内信服务', 'opensign_0001', NULL, '', 0, 1, '系统站内信', 'http://localhost/message', '', 0, 1, 1, NULL, NULL, 'admin', '2023-04-23 10:53:20', 'admin', '2023-12-01 11:24:19');
INSERT INTO `sys_app_info` (`id`, `app_name`, `app_code`, `app_icon_address`, `app_icon`, `app_type`, `app_status`, `app_desc`, `app_address`, `app_front`, `default_flag`, `useful`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('70588803-52e4-ss3d-a61f-0a68e1febxbn', '手写签名服务', 'opensign_0001', NULL, '', 0, 1, '二维码扫描生成个人签名', 'http://localhost/handwriting/sign', '', 0, 1, 1, NULL, NULL, 'admin', '2023-04-23 10:53:20', 'admin', '2023-12-01 11:24:19');


-- ----------------------------
-- 2.0.2升级
-- ----------------------------

ALTER TABLE sign_re ADD COLUMN `control_change_flag` varchar(20) DEFAULT NULL COMMENT '控件变更状态' ;
ALTER TABLE sign_ru ADD COLUMN `control_change_flag` varchar(20) DEFAULT NULL COMMENT '控件变更状态' ;

CREATE TABLE `sys_user_config` (
  `id` varchar(36) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_id` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户ID',
  `name` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `type` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '类型',
  `value` varchar(32) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '值',
  `create_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户配置表';


ALTER TABLE sign_user_confirm_step ADD COLUMN `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标识';

-- ----------------------------
-- 隐藏菜单的sql脚本
-- ----------------------------
UPDATE `sys_permission` SET `hidden_flag` = 1,  `auth_visible` = 0 WHERE `id` = '6033bf17-2109-4622-afdc-bee2380f3012';
UPDATE `sys_permission` SET `hidden_flag` = 1,  `auth_visible` = 0 WHERE `id` = '23001cd0-f685-4732-8efc-4a6dfdcc251c';
UPDATE `sys_permission` SET `hidden_flag` = 1,  `auth_visible` = 0 WHERE `id` = 'e5beccf9-6005-497e-bc60-c751c118d413';
UPDATE `sys_permission` SET `hidden_flag` = 1,  `auth_visible` = 0 WHERE `id` = 'a255f616-815a-4845-b6aa-5fd998dac3a2';
UPDATE `sys_permission` SET `hidden_flag` = 1,  `auth_visible` = 0 WHERE `id` = 'cd686ddd-a565-4e73-b061-90eaf6c6b333';
UPDATE `sys_permission` SET `hidden_flag` = 1,  `auth_visible` = 0 WHERE `id` = '89d0feab-b72c-4c8d-bb31-702348fc7560';
UPDATE `sys_permission` SET `hidden_flag` = 1,  `auth_visible` = 0 WHERE `id` = 'f676e451-d0c5-4398-ad22-a9ca0f5d6aaa';
UPDATE `sys_permission` SET `hidden_flag` = 1,  `auth_visible` = 0 WHERE `id` = '0d41fc8e-99e4-49fe-9046-d46e632273b8';

-- ----------------------------
-- 去掉“设置主管”的按钮的sql脚本
-- ----------------------------
UPDATE `sys_permission` SET `delete_flag` = 1 WHERE `id` = '2e045ba8-ae2c-49d8-bf8c-bc1b92c11b3e';

-- ----------------------------
-- 隐藏开发管理后台的“应用管理”菜单
-- ----------------------------
UPDATE `sys_permission` SET `hidden_flag` = 1,  `auth_visible` = 0 WHERE `id` = '822424f4-f96f-4c1c-95e7-001faf9f4eea';
UPDATE `sys_app_info` SET `app_front` = '/#/msg/msgtpl' where id = '313123131';

-- ----------------------------
-- 修改“业务线管理”的类型，从“目录”改为“菜单”
-- ----------------------------
UPDATE `sys_permission` SET `menu_type` = 1 WHERE `id` = '37f98649-c5c3-4eb4-8d24-310ce16d6aec';
ALTER TABLE tenant_auth_log ADD COLUMN `auto_seal` tinyint(1) DEFAULT NULL COMMENT '生成系统默认印章' ;


-- ----------------------------
-- 2.0.3升级
-- ----------------------------

INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5c9ad6af-1eff-ad09-4fb4-49qq22d6ss07', '短信配置-短信发送通道', 'sms_channel', 'resrun', NULL, NULL, 'admin', '2024-05-06 14:26:45');
ALTER TABLE operate_log MODIFY COLUMN `request_param` varchar(2048) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '请求参数' ;


-- ----------------------------
-- 2.0.4升级
-- ----------------------------

ALTER TABLE `sign_re_doc_control` ADD COLUMN `properties` varchar(2048) DEFAULT NULL COMMENT '控件属性' ;
ALTER TABLE `sign_ru_doc_control` ADD COLUMN `properties` varchar(2048) DEFAULT NULL COMMENT '控件属性' ;
ALTER TABLE `sign_ru_doc_control` ADD COLUMN `control_order` tinyint(3) DEFAULT NULL COMMENT '顺序';
ALTER TABLE `sign_template_control` ADD COLUMN `control_order` tinyint(3) DEFAULT NULL COMMENT '顺序';

ALTER TABLE `annex_image` ADD COLUMN `image_width` varchar(20) DEFAULT NULL COMMENT '图片文件真实宽度';
ALTER TABLE `annex_image` ADD COLUMN `image_height` varchar(20) DEFAULT NULL COMMENT '图片文件真实高度';

-- ----------------------------
-- 2.0.5升级
-- ----------------------------

ALTER TABLE tenant_info_extend ADD COLUMN `sys_type` varchar(255) NOT NULL DEFAULT 'base' COMMENT '企业类型';
UPDATE tenant_info_extend set sys_type = 'base';
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5cpp6af-1eff-ad09-4fb4-o93dd2d61607', '系统版本', 'system_version', '204', NULL, NULL, 'admin', '2024-06-28 17:33:56');
ALTER TABLE certificate_info MODIFY COLUMN `cert_password` varchar(1024) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '证书密码';

ALTER TABLE tenant_auth_log MODIFY COLUMN `organization_no` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '身份证号码或单位证件号码';
ALTER TABLE tenant_auth_log MODIFY COLUMN `corporation_no` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '代表人证件号';
ALTER TABLE tenant_auth_log MODIFY COLUMN `phone` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号';

ALTER TABLE tenant_info_extend MODIFY COLUMN `organization_no` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '身份证号码或单位证件号码';
ALTER TABLE tenant_info_extend MODIFY COLUMN `corporation_no` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '代表人证件号';
ALTER TABLE tenant_info_extend MODIFY COLUMN `phone` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '手机号';

CREATE TABLE `user_seal_auth` (
  `id` varchar(64) NOT NULL,
  `tenant_user_id` varchar(64) NOT NULL COMMENT '用户id',
  `tenant_id` varchar(64) NOT NULL COMMENT '租户id',
  `sign_re_id` varchar(64) NOT NULL COMMENT '业务线id',
  `auth_time` datetime NOT NULL COMMENT '授权日期',
  `seal_id` varchar(64) NOT NULL COMMENT '签名id',
  `auth_status` int(3) NOT NULL COMMENT '授权状态',
  `create_by` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  `confirm_type` varchar(255) DEFAULT NULL COMMENT '校验类型',
  `type` varchar(255) DEFAULT NULL COMMENT '通道类型',
  `type_value` varchar(255) DEFAULT NULL COMMENT '通道对应值',
  `user_para` varchar(255) DEFAULT NULL COMMENT '用户提交值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户签名授权表';

CREATE TABLE `sign_re_notice` (
  `id` varchar(64) NOT NULL,
  `sign_re_id` varchar(64) NOT NULL COMMENT '业务线主表id',
  `notice_type` varchar(64) NOT NULL COMMENT '通知类型，1文件填写，2文件签署（发起方内部），3文件签署（外部接收方），4文件抄送（发起方内部 5文件抄送（外部）',
  `open_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否发送',
  `create_by` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标志：0未删除，1已删除',
  `delete_by` varchar(255) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务线通知控制表';

ALTER TABLE `tenant_auth_log` ADD COLUMN `submit_ip` varchar(2048) DEFAULT NULL COMMENT '提交ip地址' ;
ALTER TABLE `tenant_auth_log` ADD COLUMN `verify_info` varchar(2048) DEFAULT NULL COMMENT '验证码' ;

CREATE TABLE `sign_ru_operate_record` (
  `id` varchar(64) NOT NULL,
  `sign_ru_id` varchar(64) DEFAULT NULL COMMENT '业务线实例主表id',
  `cert_id` varchar(64) DEFAULT NULL COMMENT '使用证书id',
  `image_annex_id` varchar(64) DEFAULT NULL COMMENT '签章图片文件id',
  `confirm_order_no` varchar(64) DEFAULT NULL COMMENT '签署意愿校验订单号',
  `confirm_type` varchar(64) DEFAULT NULL COMMENT '签署意愿校验类型',
  `task_id` varchar(64) DEFAULT NULL COMMENT '任务节点id',
  `account_id` varchar(64) DEFAULT NULL COMMENT '操作人账号id',
  `tenant_id` varchar(64) DEFAULT NULL COMMENT '操作人租户id',
  `tenant_user_id` varchar(64) DEFAULT NULL COMMENT '操作人租户下用户ID',
  `operate_type` varchar(64) DEFAULT NULL COMMENT '操作类型',
  `action_type` varchar(64) DEFAULT NULL COMMENT '执行类型',
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间',
  `ip_addr` varchar(64) DEFAULT NULL COMMENT 'ip地址',
  `create_by` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标识',
  `delete_by` varchar(64) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务线实例-签署操作记录表';


CREATE TABLE `sign_ru_operate_doc_record` (
  `id` varchar(64) NOT NULL,
  `operate_record_id` varchar(64) DEFAULT NULL COMMENT '签署操作记录表id',
  `doc_id` varchar(64) DEFAULT NULL COMMENT '文档ID',
  `previous_doc_operate_id` varchar(64) DEFAULT NULL COMMENT '上次签约文件记录表id',
  `previous_doc_operate_annex_id` varchar(64) DEFAULT NULL COMMENT '上次签约文件id',
  `current_doc_operate_id` varchar(64) DEFAULT NULL COMMENT '本次签约文件记录表id',
  `current_doc_operate_annex_id` varchar(64) DEFAULT NULL COMMENT '本次签约文件id',
  `create_by` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(255) DEFAULT NULL COMMENT '更新人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `delete_flag` tinyint(1) DEFAULT '0' COMMENT '删除标识',
  `delete_by` varchar(64) DEFAULT NULL COMMENT '删除人',
  `delete_time` datetime DEFAULT NULL COMMENT '删除时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务线实例-签署操作文件记录表';

CREATE TABLE `sign_ru_keyword` (

 `id` varchar(64) NOT NULL COMMENT '主键',
 `ru_id` varchar(64) DEFAULT NULL COMMENT '业务线实例id',
 `signer_id` varchar(64) DEFAULT NULL COMMENT '签署方id，signerId或者senderId',
 `keyword` varchar(64) DEFAULT NULL COMMENT '关键字内容',
 `offset_x` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '横坐标偏移量',
 `offset_y` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '纵坐标偏移量',
 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='业务线实例-关键字设置';

CREATE TABLE `sign_ru_keyword_property` (

  `id` varchar(64) NOT NULL COMMENT '主键',
  `ru_id` varchar(64) DEFAULT NULL COMMENT '业务线实例id',
  `keyword_id` varchar(64) DEFAULT NULL COMMENT '关键字id',
  `property_type` varchar(64) DEFAULT NULL COMMENT '控件属性类型，包括关联文件，查询方式（全部，部分，正序，倒序，查询范围）',
  `property_value` varchar(64) DEFAULT NULL COMMENT '控件属性值',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='业务线实例-关键字设置属性表';

ALTER TABLE `sign_ru_doc_control` ADD COLUMN `setup_type` varchar(10) DEFAULT NULL COMMENT '位置position、关键字keyword' ;
ALTER TABLE `sign_ru_doc_control` ADD COLUMN `setup_id` varchar(64) DEFAULT NULL COMMENT '关键字设置表id' ;

-- ----------------------------
-- 2.0.6升级
-- ----------------------------

INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5cpp6af-1eff-ad09-4fb4-o93112d61607', '系统账号类型', 'system_account_type', 'phone_email', NULL, NULL, 'admin', '2024-07-16 11:45:08');
INSERT INTO `job_info` (`id`, `app_id`, `concurrency`, `designated_workers`, `dispatch_strategy`, `execute_type`, `extra`, `gmt_create`, `gmt_modified`, `instance_retry_num`, `instance_time_limit`, `job_description`, `job_name`, `job_params`, `lifecycle`, `max_instance_num`, `max_worker_count`, `min_cpu_cores`, `min_disk_space`, `min_memory_space`, `next_trigger_time`, `notify_user_ids`, `processor_info`, `processor_type`, `status`, `task_retry_num`, `time_expression`, `time_expression_type`) VALUES (19, 6, 5, NULL, 1, 1, NULL, '2024-04-11 15:53:53.643000', '2024-04-11 15:53:53.643000', 0, 0, '用户授权签章定时任务', '用户授权签章定时任务', NULL, NULL, 0, 0, 0, 0, 0, NULL, NULL, 'com.resrun.modules.opensign.task.UserAuthSealProcessor', 1, 1, 0, '0 10 0 ? * *', 2);
ALTER TABLE `sign_re_doc` ADD COLUMN `doc_order` int(3) DEFAULT '0'  COMMENT '签约文件顺序' ;
ALTER TABLE `sign_ru_doc` ADD COLUMN `doc_order` int(3) DEFAULT '0'  COMMENT '签约文件顺序' ;

INSERT INTO `message_template_type` (`id`, `parent_id`, `type_name`, `description`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('53b8220f-e0cd-40f0-b33d-4f0188a7853c', '', '邮件通知', NULL, 'admin', '2024-07-16 11:45:08', 'admin', '2024-07-16 11:45:08');

INSERT INTO `message_template` (`id`, `app_id`, `template_type`, `template_code`, `template_title`, `template_content`, `create_by`, `update_by`, `create_time`, `update_time`, `delete_flag`, `delete_time`, `delete_by`) VALUES ('3958cce3-18ef-4a1f-8a30-a441733c7bba', '490489ab-d8b4-414c-ad77-d856962c286f', '53b8220f-e0cd-40f0-b33d-4f0188a7853c', 'email_entAuthSuccess', '企业实名认证审核通过', '<p>您提交的${companyName}认证信息已审核通过，可点击${domain} 登录查看</p>', 'admin', NULL, '2024-03-26 19:49:30', NULL, 0, NULL, NULL);
INSERT INTO `message_template` (`id`, `app_id`, `template_type`, `template_code`, `template_title`, `template_content`, `create_by`, `update_by`, `create_time`, `update_time`, `delete_flag`, `delete_time`, `delete_by`) VALUES ('d080216f-b16e-497d-95e0-65e5510fd1cc', '490489ab-d8b4-414c-ad77-d856962c286f', '53b8220f-e0cd-40f0-b33d-4f0188a7853c', 'email_entAuthFaild', '企业实名认证审核不通过', '<p>您好，您提交的${companyName}认证审核未通过，原因${reason}，可点击${domain} 登录查看</p>', 'admin', NULL, '2024-03-26 19:48:13', NULL, 0, NULL, NULL);
INSERT INTO `message_template` (`id`, `app_id`, `template_type`, `template_code`, `template_title`, `template_content`, `create_by`, `update_by`, `create_time`, `update_time`, `delete_flag`, `delete_time`, `delete_by`) VALUES ('c2d086f4-d69e-4c8a-8197-ec7fc530466e', '490489ab-d8b4-414c-ad77-d856962c286f', '53b8220f-e0cd-40f0-b33d-4f0188a7853c', 'email_copySign', '文件抄送通知', '<p>${sender}发起了一份文件《${contract}》，抄送给你，请访问${domain}/#/contract/detail/base?code=${code} 查看</p>', 'admin', NULL, '2024-03-26 19:46:46', NULL, 0, NULL, NULL);
INSERT INTO `message_template` (`id`, `app_id`, `template_type`, `template_code`, `template_title`, `template_content`, `create_by`, `update_by`, `create_time`, `update_time`, `delete_flag`, `delete_time`, `delete_by`) VALUES ('81fee40d-a376-4689-a109-6359f259c925', '490489ab-d8b4-414c-ad77-d856962c286f', '53b8220f-e0cd-40f0-b33d-4f0188a7853c', 'email_copyBegin', '文件抄送通知', '<p>${sender}发起了一份文件《${contract}》，抄送给你，请访问${domain}/#/contract/detail/base?code=${code} 查看</p>', 'admin', NULL, '2024-03-26 19:46:07', NULL, 0, NULL, NULL);
INSERT INTO `message_template` (`id`, `app_id`, `template_type`, `template_code`, `template_title`, `template_content`, `create_by`, `update_by`, `create_time`, `update_time`, `delete_flag`, `delete_time`, `delete_by`) VALUES ('58c87ca6-9fe6-41be-8064-947b2e85906f', '490489ab-d8b4-414c-ad77-d856962c286f', '53b8220f-e0cd-40f0-b33d-4f0188a7853c', 'email_signTaskOut', '文件签署通知', '<p>${sender}给您发送了一份文件《${contract}》，请访问 ${domain}/#/contract/detail/base?code=${code} 完成签署</p>', 'admin', NULL, '2024-03-26 19:44:14', NULL, 0, NULL, NULL);
INSERT INTO `message_template` (`id`, `app_id`, `template_type`, `template_code`, `template_title`, `template_content`, `create_by`, `update_by`, `create_time`, `update_time`, `delete_flag`, `delete_time`, `delete_by`) VALUES ('31c87c59-2fe6-4907-b290-f66e215eeb1e', '490489ab-d8b4-414c-ad77-d856962c286f', '53b8220f-e0cd-40f0-b33d-4f0188a7853c', 'email_signTaskIn', '文件签署通知', '<p>${sender}给您发送了一份文件《${contract}》，请访问 ${domain}/#/contract/detail/base?code=${code} 完成签署</p>', 'admin', NULL, '2024-03-26 19:43:14', NULL, 0, NULL, NULL);
INSERT INTO `message_template` (`id`, `app_id`, `template_type`, `template_code`, `template_title`, `template_content`, `create_by`, `update_by`, `create_time`, `update_time`, `delete_flag`, `delete_time`, `delete_by`) VALUES ('c34184b7-396d-4732-b6f6-fb540eb0cba7', '490489ab-d8b4-414c-ad77-d856962c286f', '53b8220f-e0cd-40f0-b33d-4f0188a7853c', 'email_writeTask', '文件填写通知', '<p>${sender}给您发送了一份文件《${contract}》，有部分内容需要您填写，请访问 ${domain}/#/contract/detail/base?code=${code} 填写</p>\n<p>&nbsp;</p>', 'admin', 'admin', '2024-03-26 19:40:42', '2024-03-26 19:41:21', 0, NULL, NULL);
INSERT INTO `message_template` (`id`, `app_id`, `template_type`, `template_code`, `template_title`, `template_content`, `create_by`, `update_by`, `create_time`, `update_time`, `delete_flag`, `delete_time`, `delete_by`) VALUES ('a1a355c2-9148-4627-832a-9807a7323345', '13131313', '53b8220f-e0cd-40f0-b33d-4f0188a7853c', 'email_inviteUser', '企业添加内部成员通知', '<p>${companyName}添加您为企业成员，可点击${domain}/#/join?key=${key}&amp;type=2</p>\n<p>&nbsp;</p>', 'admin', 'admin', '2022-09-29 18:10:38', '2024-03-26 19:37:43', 0, NULL, NULL);


-- ----------------------------
-- 2.0.7升级
-- ----------------------------

DELETE 
FROM
  sys_permission 
WHERE
  id IN (
    '0ae1a7c0-9892-48f9-9f0c-1aa28cf6dcf0',
    '8fb55b34-66f0-46a2-9842-b20700c6b465',
    '283c45d1-d385-402e-9f76-2a3ae1cacfd3',
    '164e93b1-461e-4c6a-aec9-cdf0fa984158',
    '21de24a-dc26-4227-bdc7-5cd9ss19be5',
    '21de11a-dc26-4227-bdc7-5cd9dd919be5',
    '21de24a-dc26-4227-bdc7-5cd9dd919be5',
    '21de24a-dc26-4227-bss7-5cd9dd919be5',
    '21da524a-dc26-4227-bdc7-5cd9dd919be5',
    '21da114a-deee6-4227-bdc7-5cd9dd919be5',
    '21da524a-deee6-4227-bdc7-5cd9dd919be5',
    '21da524a-deee6-4527-bdc7-5cd9dd919be5',
    '21daww4a-deee6-4227-bdc7-5cd9dd919be5',
    '12bf4556-80b8-4b2d-b471-69651ffe5dd4',
    '12c5210f-6053-4664-8b51-52e0c1443639',
    '90d5c59b-55b6-4dcd-867c-ec0567667262',
  'd0aa8da7-7760-4135-aa80-2c354bbcdf0d' 
  );

INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('0ae1a7c0-9892-48f9-9f0c-1aa28cf6dcf0', '70588803-52e4-433d-a61f-0a68e1febd72', '日志审计', 1, '', 120, '/logs', '/sys/logs/index', NULL, 'ant-design:calendar-outlined', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-04-12 13:22:56', 'admin', '2023-12-08 15:03:04', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('8fb55b34-66f0-46a2-9842-b20700c6b465', '70588803-52e4-433d-a61f-0a68e1febd72', '任务执行记录', 1, '164e93b1-461e-4c6a-aec9-cdf0fa984158', 112, '/task/record', '/task/TaskInstance', NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-04-12 13:21:53', '13300000002', '2023-12-14 10:51:18', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('283c45d1-d385-402e-9f76-2a3ae1cacfd3', '70588803-52e4-433d-a61f-0a68e1febd72', '任务管理', 1, '164e93b1-461e-4c6a-aec9-cdf0fa984158', 111, '/task/manage', '/task/TaskScheduling', NULL, '', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-04-12 13:21:16', '13300000002', '2023-12-14 10:51:11', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('164e93b1-461e-4c6a-aec9-cdf0fa984158', '70588803-52e4-433d-a61f-0a68e1febd72', '任务调度中心', 0, '', 110, '/task', 'LAYOUT', NULL, 'ant-design:translation-outlined', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-04-12 13:20:09', 'admin', '2023-04-12 13:23:05', 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('21de24a-dc26-4227-bdc7-5cd9ss19be5', '70588803-52e4-433d-a61f-0a68e1febd72', '查询任务实例日志', 2, '8fb55b34-66f0-46a2-9842-b20700c6b465', 1, NULL, NULL, 'instance:log', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-07 16:18:50', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('21de11a-dc26-4227-bdc7-5cd9dd919be5', '70588803-52e4-433d-a61f-0a68e1febd72', '重试任务实例', 2, '8fb55b34-66f0-46a2-9842-b20700c6b465', 1, NULL, NULL, 'instance:retry', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-07 16:18:50', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('21de24a-dc26-4227-bdc7-5cd9dd919be5', '70588803-52e4-433d-a61f-0a68e1febd72', '停止任务实例', 2, '8fb55b34-66f0-46a2-9842-b20700c6b465', 1, NULL, NULL, 'instance:stop', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-07 16:18:50', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('21de24a-dc26-4227-bss7-5cd9dd919be5', '70588803-52e4-433d-a61f-0a68e1febd72', '查询任务实例详情', 2, '8fb55b34-66f0-46a2-9842-b20700c6b465', 1, NULL, NULL, 'instance:info', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-07 16:18:50', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('21da524a-dc26-4227-bdc7-5cd9dd919be5', '70588803-52e4-433d-a61f-0a68e1febd72', '新增任务', 2, '283c45d1-d385-402e-9f76-2a3ae1cacfd3', 1, NULL, NULL, 'task:add', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-07 16:18:50', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('21da114a-deee6-4227-bdc7-5cd9dd919be5', '70588803-52e4-433d-a61f-0a68e1febd72', '查询任务列表', 2, '283c45d1-d385-402e-9f76-2a3ae1cacfd3', 1, NULL, NULL, 'task:history', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-07 16:18:50', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('21da524a-deee6-4227-bdc7-5cd9dd919be5', '70588803-52e4-433d-a61f-0a68e1febd72', '停用任务', 2, '283c45d1-d385-402e-9f76-2a3ae1cacfd3', 1, NULL, NULL, 'task:able', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-07 16:18:50', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('21da524a-deee6-4527-bdc7-5cd9dd919be5', '70588803-52e4-433d-a61f-0a68e1febd72', '删除任务', 2, '283c45d1-d385-402e-9f76-2a3ae1cacfd3', 1, NULL, NULL, 'task:delete', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-07 16:18:50', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('21daww4a-deee6-4227-bdc7-5cd9dd919be5', '70588803-52e4-433d-a61f-0a68e1febd72', '运行任务', 2, '283c45d1-d385-402e-9f76-2a3ae1cacfd3', 1, NULL, NULL, 'task:run', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-01-07 16:18:50', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('12bf4556-80b8-4b2d-b471-69651ffe5dd4', '70588803-52e4-433d-a61f-0a68e1febd72', '模板编辑', 2, 'd0aa8da7-7760-4135-aa80-2c354bbcdf0d', 0, NULL, NULL, 'template:edit', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-10-13 11:05:42', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('12c5210f-6053-4664-8b51-52e0c1443639', '70588803-52e4-433d-a61f-0a68e1febd72', '模板详情', 2, 'd0aa8da7-7760-4135-aa80-2c354bbcdf0d', 0, NULL, NULL, 'template:info', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-10-13 13:22:47', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('90d5c59b-55b6-4dcd-867c-ec0567667262', '70588803-52e4-433d-a61f-0a68e1febd72', '模板新增', 2, 'd0aa8da7-7760-4135-aa80-2c354bbcdf0d', 0, NULL, NULL, 'template:add', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2023-10-13 11:05:59', NULL, NULL, 0, NULL, NULL, 1, '', NULL, 0);
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('d0aa8da7-7760-4135-aa80-2c354bbcdf0d', '70588803-52e4-433d-a61f-0a68e1febd72', '消息模板管理', 1, '', 100, '/msg/msgtpl', '/message/Template', NULL, 'ant-design:message-outlined', 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2023-04-12 13:08:19', 'admin', '2024-07-26 10:33:39', 0, NULL, NULL, 1, '', NULL, 0);

DELETE 
FROM
  sys_app_version_permission 
WHERE
  id IN (
    '084b3d60-935b-424a-a85d-61a5e9030ac8',
    '3377749e-c997-493d-b247-c9acb50c0ec3',
    '34f8fd42-63cc-4bc6-8ee8-167071a21403',
    '3d6acede-5157-4849-a9ca-7f622c0bbd70',
    '4cd2ea46-129b-4afa-bb25-8ceb19ce79e2',
    '5ccb5134-78d8-463c-b2f2-338d5e0d0081',
    '6247706b-78cc-429d-930e-117c174779e6',
    '758b55d2-f9f2-43d7-903a-d38af2108d55',
    '810bb795-3ca7-49ee-85ff-cf212f333bfc',
    '87f538cb-51b9-4136-9ddf-a1649186fb7f',
    '992d5e24-d74c-4074-9192-ae0739b9a056',
    'a485fd5e-667a-4115-82d5-cd1a578d2daa',
    'aa751922-ceb0-47be-9260-b91119a239e4',
    'b10534fb-8e61-464c-88c9-2d1b7a19e83e',
    'c6878894-52ea-43eb-8d6d-4af448e17d10',
  'd5e78393-ba96-4491-929a-7f1a884bf0ad',
  'f623163f-4391-4f2b-b29f-fc5c3ab7bacd');

INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('084b3d60-935b-424a-a85d-61a5e9030ac8', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', '21da524a-deee6-4227-bdc7-5cd9dd919be5', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('3377749e-c997-493d-b247-c9acb50c0ec3', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', '21de11a-dc26-4227-bdc7-5cd9dd919be5', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('34f8fd42-63cc-4bc6-8ee8-167071a21403', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', '12c5210f-6053-4664-8b51-52e0c1443639', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('3d6acede-5157-4849-a9ca-7f622c0bbd70', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', '0ae1a7c0-9892-48f9-9f0c-1aa28cf6dcf0', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('4cd2ea46-129b-4afa-bb25-8ceb19ce79e2', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', '283c45d1-d385-402e-9f76-2a3ae1cacfd3', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5ccb5134-78d8-463c-b2f2-338d5e0d0081', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', '21de24a-dc26-4227-bdc7-5cd9dd919be5', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('6247706b-78cc-429d-930e-117c174779e6', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', '164e93b1-461e-4c6a-aec9-cdf0fa984158', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('758b55d2-f9f2-43d7-903a-d38af2108d55', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', '21de24a-dc26-4227-bss7-5cd9dd919be5', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('810bb795-3ca7-49ee-85ff-cf212f333bfc', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', '12bf4556-80b8-4b2d-b471-69651ffe5dd4', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('87f538cb-51b9-4136-9ddf-a1649186fb7f', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', 'd0aa8da7-7760-4135-aa80-2c354bbcdf0d', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('992d5e24-d74c-4074-9192-ae0739b9a056', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', '21de24a-dc26-4227-bdc7-5cd9ss19be5', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('a485fd5e-667a-4115-82d5-cd1a578d2daa', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', '21daww4a-deee6-4227-bdc7-5cd9dd919be5', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('aa751922-ceb0-47be-9260-b91119a239e4', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', '21da114a-deee6-4227-bdc7-5cd9dd919be5', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('b10534fb-8e61-464c-88c9-2d1b7a19e83e', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', '21da524a-dc26-4227-bdc7-5cd9dd919be5', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('c6878894-52ea-43eb-8d6d-4af448e17d10', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', '21da524a-deee6-4527-bdc7-5cd9dd919be5', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('d5e78393-ba96-4491-929a-7f1a884bf0ad', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', '90d5c59b-55b6-4dcd-867c-ec0567667262', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('f623163f-4391-4f2b-b29f-fc5c3ab7bacd', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', '8fb55b34-66f0-46a2-9842-b20700c6b465', 0, NULL, NULL, '13300000000', '2023-12-14 10:05:00', NULL, NULL);

DELETE 
FROM
  sys_auth_group_permission 
WHERE
  id IN (
    '098978f5-41d3-498e-b115-9e7edc102d67',
    '0d8e1132-4f02-40de-9306-1656610c11dd',
    '345ac8a9-2958-4053-8952-707892af1f8e',
    '38e5128a-e95f-4b8d-82d8-b795f8c2506e',
    '428728e4-9dac-45da-9110-1f741dae05bf',
    '4e9d7da0-5aee-4b8c-b428-4ec85a4ec7fd',
    '7db87268-8323-42c8-8a0a-85eb5fa0fdb9',
    '843cf55e-61f9-45bd-9b30-9217c132f2c4',
    '8b65a1cd-a0a1-405c-8976-3098a759d9d4',
    '94189904-1439-4b69-b8b0-724f7d1e0000',
    '97882cd2-749d-414d-a8cd-72a6f26f648a',
    '992aaca0-ce6e-4943-9c99-7ffb356f6032',
    'a08a5253-f4d9-4508-8db5-8ab185389d06',
    'b367f90d-77f5-466a-a1db-7532763bd23d',
    'bf075769-9907-4d0c-a954-30ba2934790e',
  'f09cbff9-1145-4a01-a908-74550aeb4bed',
  'fe58e6c0-9c8b-4da0-b96a-e86754adf46e');


INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('098978f5-41d3-498e-b115-9e7edc102d67', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', '21de11a-dc26-4227-bdc7-5cd9dd919be5', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('0d8e1132-4f02-40de-9306-1656610c11dd', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', '283c45d1-d385-402e-9f76-2a3ae1cacfd3', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('345ac8a9-2958-4053-8952-707892af1f8e', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', '164e93b1-461e-4c6a-aec9-cdf0fa984158', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('38e5128a-e95f-4b8d-82d8-b795f8c2506e', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', '21da524a-dc26-4227-bdc7-5cd9dd919be5', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('428728e4-9dac-45da-9110-1f741dae05bf', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', '21da524a-deee6-4227-bdc7-5cd9dd919be5', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('4e9d7da0-5aee-4b8c-b428-4ec85a4ec7fd', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', '21da114a-deee6-4227-bdc7-5cd9dd919be5', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('7db87268-8323-42c8-8a0a-85eb5fa0fdb9', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', '90d5c59b-55b6-4dcd-867c-ec0567667262', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('843cf55e-61f9-45bd-9b30-9217c132f2c4', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', '21de24a-dc26-4227-bss7-5cd9dd919be5', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('8b65a1cd-a0a1-405c-8976-3098a759d9d4', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', '21de24a-dc26-4227-bdc7-5cd9ss19be5', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('94189904-1439-4b69-b8b0-724f7d1e0000', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', '0ae1a7c0-9892-48f9-9f0c-1aa28cf6dcf0', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('97882cd2-749d-414d-a8cd-72a6f26f648a', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', '21de24a-dc26-4227-bdc7-5cd9dd919be5', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('992aaca0-ce6e-4943-9c99-7ffb356f6032', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', 'd0aa8da7-7760-4135-aa80-2c354bbcdf0d', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('a08a5253-f4d9-4508-8db5-8ab185389d06', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', '8fb55b34-66f0-46a2-9842-b20700c6b465', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('b367f90d-77f5-466a-a1db-7532763bd23d', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', '21da524a-deee6-4527-bdc7-5cd9dd919be5', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('bf075769-9907-4d0c-a954-30ba2934790e', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', '12bf4556-80b8-4b2d-b471-69651ffe5dd4', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('f09cbff9-1145-4a01-a908-74550aeb4bed', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', '12c5210f-6053-4664-8b51-52e0c1443639', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('fe58e6c0-9c8b-4da0-b96a-e86754adf46e', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', '21daww4a-deee6-4227-bdc7-5cd9dd919be5', NULL, NULL);

INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('5600d9b6-3074-403c-b83b-883fb0394d22', '490489ab-d8b4-414c-ad77-d856962c286f', '授权业务线', 1, '', 3, 'business', '/business/index', NULL, NULL, 0, 0, 0, 0, NULL, 1, 0, NULL, 'admin', '2024-07-24 10:21:44', 'admin', '2024-07-24 15:35:53', 0, NULL, NULL, 1, NULL, 'false', 0);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('f74f88db-b97b-4c82-b42a-195dss77fec4', '1e80284a-ec81-4c56-b3c8-ced69a0d21fa', '5600d9b6-3074-403c-b83b-883fb0394d22', NULL, NULL);
INSERT INTO `sys_app_version_group_permission` (`id`, `group_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('f96ee2f1-0fe4-4ee0-b0c4-1389bd03eeee', 'ec8b30a7-d887-47a9-b7c2-d094b0eda350', '5600d9b6-3074-403c-b83b-883fb0394d22', NULL, NULL);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('c7f263f3-e578-4271-a152-839cfa9176d4', '490489ab-d8b4-414c-ad77-d856962c286f', '67265942-ca4a-41c9-be25-e9c85a95a686', '5600d9b6-3074-403c-b83b-883fb0394d22', 0, NULL, NULL, NULL, '2024-07-24 10:27:50', NULL, NULL);

UPDATE sys_app_info set delete_flag = 1 where id = '313123131';

ALTER TABLE `sign_ru_operate_record` ADD COLUMN `operate_reason` varchar(512) DEFAULT NULL COMMENT '操作原因' ;

INSERT INTO `message_template` (`id`, `app_id`, `template_type`, `template_code`, `template_title`, `template_content`, `create_by`, `update_by`, `create_time`, `update_time`, `delete_flag`, `delete_time`, `delete_by`) VALUES ('0c3bbb86-47f7-4200-8395-57dcbd04c270', '490489ab-d8b4-414c-ad77-d856962c286f', '53b8220f-e0cd-40f0-b33d-4f0188a7853c', 'email_contract_file', '${contract}-文件下载', '<p>${remarks}</p>\n<p>文件主题：${subject}</p>\n<p>发起方：${senderName}</p>\n<p>签署方：${signerName}</p>\n<p>文件内容见附件</p>', 'admin', 'admin', '2024-07-21 10:29:46', '2024-07-21 10:42:14', 0, NULL, NULL);

ALTER TABLE `operate_log` MODIFY COLUMN `request_param` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '请求参数';
ALTER TABLE `tenant_info_extend` MODIFY COLUMN `email` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '邮箱';
ALTER TABLE `sign_ru_doc` MODIFY COLUMN `doc_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '签约文件名称';
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5cpp6af-1eff-ad09-4fb4-o93dd2d61608', '事件证书使用模式（1、仅使用一次；2、有效期内可重复使用）', 'event_ca_use_model', '1', NULL, NULL, 'admin', '2024-09-05 10:37:52');

-- v2.0.8 免登录 新增 2024-11-25
ALTER TABLE `message_short_mapping` ADD COLUMN `no_login` tinyint(1) DEFAULT '0'  COMMENT '是否免登录';
ALTER TABLE `message_short_mapping` ADD COLUMN `page_url_expire_time` datetime DEFAULT NULL  COMMENT '链接失效时间';
ALTER TABLE `message_short_mapping` ADD UNIQUE INDEX(`meg_code`);

-- v2.0.9 新增个人实名认证扫脸 2025-4-2
CREATE TABLE `tenant_auth_face`  (
                                     `id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                     `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                     `id_card` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                     `face_order` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                     `create_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '创建人',
                                     `create_time` datetime NULL DEFAULT NULL COMMENT '创建日期',
                                     `update_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '更新人',
                                     `update_time` datetime NULL DEFAULT NULL COMMENT '更新日期',
                                     `auth_status` tinyint(2) NOT NULL COMMENT '0=默认状态  1=认证通过  2=认证失败',
                                     `user_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                     `auth_log_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
                                     PRIMARY KEY (`id`) USING BTREE,
                                     UNIQUE INDEX `face_order`(`face_order`, `user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;
ALTER TABLE `tenant_auth_log` ADD COLUMN `auth_method` tinyint(2) DEFAULT NULL COMMENT '认证方式' ;



-- V3.0.0版本 -2025-08-09  开放签与云盾初次发布上线

-- 修改签署业务端菜单名称
UPDATE `sys_permission` SET `name` = '验签' WHERE `id` = '06705052-19d4-4e89-8d78-b7ad5e23e943';
UPDATE `sys_permission` SET `name` = '签署' WHERE `id` = '4a150a15-fe3a-4534-97c3-ba89587eb886';
UPDATE `sys_permission` SET `name` = '发起' WHERE `id` = '5600d9b6-3074-403c-b83b-883fb0394d22';
UPDATE `sys_permission` SET `name` = '账号' WHERE `id` = '929dfa20-8337-4cee-afe5-b7827ccc2a6d';

-- 删除文档验签菜单
UPDATE `sys_permission` SET `delete_flag` = 1 WHERE `id` = '06705052-19d4-4e89-8d78-b7ad5e23e943';

-- 修改企业管理后台的“证书管理”菜单为“静默签授权”菜单
UPDATE `sys_permission` SET `name` = '静默签授权', `path` = '/silent/SilentSignAuth', `component` = '/silent/SilentSignAuth',`sort_no` = 35  WHERE id = 'f591252e-7651-45bb-9070-dfe4a15d6ea0' ;

-- 运营管理后台

-- 隐藏企业认证审核菜单
UPDATE `sys_permission` SET `delete_flag` = 1 WHERE `id` = '72dac2b0-7ce7-4e5a-88ad-fb6821305dff';

-- 隐藏证书管理菜单
UPDATE `sys_permission` SET `delete_flag` = 1 WHERE `id` = '0ca98386-4d83-408d-896b-fa486e8f61e0';
UPDATE `sys_permission` SET `delete_flag` = 1 WHERE `id` = '0b45502d-b4fe-45f4-9488-0f622c854534';
UPDATE `sys_permission` SET `delete_flag` = 1 WHERE `id` = 'fc8da44b-3a70-4864-a838-6244ad11880f';

-- 隐藏数字证书服务协议
UPDATE `sys_permission` SET `delete_flag` = 1 WHERE `id` = 'f9d05ba1-d802-4832-8f75-c0896db62d11';

-- 隐藏消息模板管理菜单
UPDATE `sys_permission` SET `delete_flag` = 1 WHERE `id` = 'd0aa8da7-7760-4135-aa80-2c354bbcdf0d';
UPDATE `sys_permission` SET `hidden_flag` = 0 WHERE `id` = 'f591252e-7651-45bb-9070-dfe4a15d6ea0' ;

-- 修改短信签名初始值
UPDATE `sys_config` SET `value` = '北京资源律动科技' WHERE `id` = '5c9ad6af-1eff-ad09-4fb4-49qq22d61607';

-- 删除任务调度中不再使用的任务
UPDATE `job_info` SET `status` = 99 WHERE `id` = 2;
UPDATE `job_info` SET `status` = 99 WHERE `id` = 14;
UPDATE `job_info` SET `status` = 99 WHERE `id` = 18;

-- 字段修改
ALTER TABLE `message_info_template` MODIFY COLUMN `mes_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '消息内容' AFTER `mes_title`;
		    
ALTER TABLE `sign_re_sign_confirm` COMMENT = '业务线配置-签署意愿数据表\r\n';
		    
ALTER TABLE `sign_re_sign_confirm` ADD COLUMN `agree_skip_willingness` tinyint(2) NULL DEFAULT NULL COMMENT '是否可以免意愿快捷签署' AFTER `signer_id`;
		    
ALTER TABLE `sign_ru_doc` MODIFY COLUMN `doc_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '签约文件名称' AFTER `doc_origin_id`;
		    
ALTER TABLE `sign_ru_operator` MODIFY COLUMN `signer_type` tinyint(1) NULL DEFAULT NULL COMMENT '签署方类型，1发起方，2个人接收方，3企业接收方' AFTER `operate_type`;
		    
ALTER TABLE `sign_ru_operator` MODIFY COLUMN `tenant_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '租户名称' AFTER `operate_name`;
		    
ALTER TABLE `sign_ru_sender` MODIFY COLUMN `sender_external_type` int(11) NULL DEFAULT NULL COMMENT '外部签署人类型，1手机号，2邮箱号' AFTER `sender_user_id`;
		    
ALTER TABLE `sign_ru_sender` MODIFY COLUMN `sender_external_value` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '外部签署人接受值' AFTER `sender_external_type`;
		    
ALTER TABLE `sign_ru_sign_confirm` COMMENT = '业务线实例-签署意愿数据表\r\n';
		    
ALTER TABLE `sign_ru_sign_confirm` ADD COLUMN `agree_skip_willingness` tinyint(2) NULL DEFAULT NULL COMMENT '是否可以免意愿快捷签署' AFTER `signer_id`;
		    
ALTER TABLE `sign_ru_signer` MODIFY COLUMN `signer_type` tinyint(1) NULL DEFAULT NULL COMMENT '签署方类型，1发起方，2个人接收方，3企业接收方' AFTER `sign_ru_id`;
		    
ALTER TABLE `sign_ru_task` ADD COLUMN `order_no` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '云盾签署订单信息' AFTER `sys_account_id`;
		    
ALTER TABLE `sign_ru_task` ADD COLUMN `yd_auth_sign_url` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '云盾意愿校验签署地址' AFTER `order_no`;
		    
ALTER TABLE `sign_ru_task` MODIFY COLUMN `tenant_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '租户名称' AFTER `task_link_type`;
		    
ALTER TABLE `sign_template_control` MODIFY COLUMN `properties` varchar(2048) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '控件属性' AFTER `interface_param_name`;
		    
ALTER TABLE `sign_user_confirm` MODIFY COLUMN `main_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '任务主表id' AFTER `id`;
		    
ALTER TABLE `sys_config` MODIFY COLUMN `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '名称' AFTER `id`;
		    
ALTER TABLE `sys_config` MODIFY COLUMN `value` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL COMMENT '值' AFTER `type`;
		    
ALTER TABLE `tenant_auth_log` ADD COLUMN `order_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '云端实名认证订单号' AFTER `verify_info`;
		    
ALTER TABLE `tenant_auth_log` ADD COLUMN `person_verify_method` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '个人认证方式：\r\nmobile: 实名手机号三要素认证；\r\nbank: 个人银行卡四要素认证\r\noffline: 人工审核认证' AFTER `order_no`;
		    
ALTER TABLE `tenant_auth_log` ADD COLUMN `company_verify_method` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '	企业认证方式：\r\nlegalRep：法定代表人本人认证；\r\ninvite：法定代表人授权认证，代理人邀请法定代表人在线授权认证；\r\nletter：上传授权书认证；' AFTER `person_verify_method`;
		    
ALTER TABLE `tenant_auth_log` ADD COLUMN `id_card_type` tinyint(2) NULL DEFAULT NULL COMMENT '个人/法人证件类型，\r\n1.中国居民身份证\r\n2.港澳居民居住证\r\n3.台湾居民居住证\r\n4.港澳居民来往内地通行证（即：回乡证）\r\n5.台湾居民来往大陆通行证（即：台胞证）\r\n6.外国人永久居留居住证（即：中国绿卡、永居证）\r\n7.国际护照' AFTER `auth_method`;
		    
ALTER TABLE `tenant_auth_log` ADD COLUMN `verify_time` datetime NULL DEFAULT NULL COMMENT '认证通过时间' AFTER `id_card_type`;
		    
ALTER TABLE `tenant_auth_log` ADD COLUMN `yundun_auth_url` varchar(900) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '云盾实名认证地址' AFTER `verify_time`;
		    
ALTER TABLE `tenant_auth_log` ADD COLUMN `order_status` tinyint(2) NULL DEFAULT NULL COMMENT '云盾订单状态\r\n-1、已撤销；\r\n1、已认证完成；\r\n2、已失效' AFTER `yundun_auth_url`;
		    
ALTER TABLE `tenant_auth_log` MODIFY COLUMN `tenant_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '租户id' AFTER `check_user`;
		    
ALTER TABLE `tenant_auth_log` MODIFY COLUMN `submit_ip` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提交ip地址' AFTER `auto_seal`;
		    
ALTER TABLE `tenant_auth_log` MODIFY COLUMN `verify_info` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '验证码' AFTER `submit_ip`;
		    
ALTER TABLE `tenant_info_extend` ADD COLUMN `order_no` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '云盾实名认证订单号' AFTER `sys_type`;
		    
ALTER TABLE `tenant_info_extend` ADD COLUMN `person_verify_method` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '个人认证方式：\r\nmobile: 实名手机号三要素认证；\r\nbank: 个人银行卡四要素认证\r\noffline: 人工审核认证' AFTER `order_no`;
		    
ALTER TABLE `tenant_info_extend` ADD COLUMN `company_verify_method` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '企业认证方式：\r\nlegalRep：法定代表人本人认证；\r\ninvite：法定代表人授权认证，代理人邀请法定代表人在线授权认证；\r\nletter：上传授权书认证；' AFTER `person_verify_method`;
		    
ALTER TABLE `tenant_info_extend` ADD COLUMN `id_card_type` tinyint(2) NULL DEFAULT NULL COMMENT '个人/法人证件类型，\r\n1.中国居民身份证\r\n2.港澳居民居住证\r\n3.台湾居民居住证\r\n4.港澳居民来往内地通行证（即：回乡证）\r\n5.台湾居民来往大陆通行证（即：台胞证）\r\n6.外国人永久居留居住证（即：中国绿卡、永居证）\r\n7.国际护照' AFTER `company_verify_method`;
		    
ALTER TABLE `tenant_info_extend` ADD COLUMN `verify_time` datetime NULL DEFAULT NULL COMMENT '认证通过时间' AFTER `id_card_type`;
		    
ALTER TABLE `third_service_record` MODIFY COLUMN `service_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '调用地址' AFTER `service_type`;

CREATE INDEX group_id ON sys_auth_group_member(group_id);
CREATE INDEX group_id ON sys_auth_group_permission(group_id);
ALTER TABLE `tenant_info_extend` ADD COLUMN `bank_card` varchar(64) DEFAULT NULL COMMENT '租户银行卡号' ;
UPDATE `tenant_info_extend` SET `auth_status` = 0;
UPDATE `job_info` SET `status` = 1 WHERE `id` = 15;
UPDATE `job_info` SET `status` = 1 WHERE `id` = 16;
UPDATE `job_info` SET `status` = 1 WHERE `id` = 17;


-- v3.0.1  -2025-09-09 项目包名修改
ALTER TABLE operate_log MODIFY COLUMN module_name varchar(255);
update sys_fill_rule  set rule_class = REPLACE(rule_class, 'com.resrun', 'com.kaifangqian');
update job_info  set processor_info = REPLACE(processor_info, 'com.resrun', 'com.kaifangqian');

-- v3.0.2  -2025-09-19 调整菜单
INSERT INTO `sys_permission` (`id`, `app_id`, `name`, `menu_type`, `parent_id`, `sort_no`, `path`, `component`, `perms`, `icon`, `route_flag`, `hidden_flag`, `keep_alive_flag`, `internal_or_external`, `description`, `status`, `rule_flag`, `form_table_id`, `create_by`, `create_time`, `update_by`, `update_time`, `delete_flag`, `delete_by`, `delete_time`, `auth_visible`, `fast_icon`, `fast_icon_address`, `fast_flag`) VALUES ('850397f1-2ac9-40c2-a19f-8ddfe180523b', '70588803-52e4-433d-a61f-0a68e1febd72', '运营管理权限', 2, '', 0, NULL, NULL, 'system:manage', NULL, 0, 0, 0, 0, NULL, 1, 1, NULL, 'admin', '2025-09-02 14:40:41', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_app_version_permission` (`id`, `app_info_id`, `app_version_id`, `permission_id`, `delete_flag`, `delete_by`, `delete_time`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('e83c31cd-250b-4d33-b24b-0cc9fb67cb17', '70588803-52e4-433d-a61f-0a68e1febd72', '2e1f31de-651b-49da-a89c-03f6b5daa9be', '850397f1-2ac9-40c2-a19f-8ddfe180523b', 0, NULL, NULL, NULL, '2025-09-04 14:17:42', NULL, NULL);
INSERT INTO `sys_auth_group_permission` (`id`, `group_id`, `app_id`, `permission_id`, `permission_perms`, `permission_data_id`) VALUES ('c4b7c959-c3da-421e-a5d0-3c8f237dc588', '07b3e2cd-eb1b-4e07-8844-685478909734', '70588803-52e4-433d-a61f-0a68e1febd72', '850397f1-2ac9-40c2-a19f-8ddfe180523b', NULL, NULL);

-- v3.1    -2025-10-15 增加非实名认证签署
INSERT INTO `sys_config` (`id`, `name`, `type`, `value`, `create_by`, `create_time`, `update_by`, `update_time`) VALUES ('5coo6af-1eff-ad09-4fb4-p93b22d61608', '个人签署实名认证：required（须实名认证）、allowed（允许不实名认证）、not_required（无需实名认证）', 'personal_sign_auth', 'required', NULL, NULL, 'admin', '2025-09-23 08:28:01');
ALTER TABLE `sign_re` ADD COLUMN `personal_sign_auth` varchar(20) NULL DEFAULT NULL COMMENT '个人签署实名认证：required（须实名认证）、allowed（允许不实名认证）、not_required（无需实名认证）';
ALTER TABLE `sign_re_sign_confirm` ADD COLUMN `personal_sign_auth` varchar(20) NULL DEFAULT NULL COMMENT '个人签署实名认证：required（须实名认证）、allowed（允许不实名认证）、not_required（无需实名认证）';
ALTER TABLE `sign_ru` ADD COLUMN `personal_sign_auth` varchar(20) NULL DEFAULT NULL COMMENT '个人签署实名认证：required（须实名认证）、allowed（允许不实名认证）、not_required（无需实名认证）';
ALTER TABLE `sign_ru` ADD COLUMN `send_type` varchar(20) NULL DEFAULT NULL COMMENT '发起类型：api(接口发起)；app（应用发起）';
ALTER TABLE `sign_ru_sign_confirm` ADD COLUMN `personal_sign_auth` varchar(20) NULL DEFAULT NULL COMMENT '个人签署实名认证：required（须实名认证）、allowed（允许不实名认证）、not_required（无需实名认证）';

-- v3.1.1    -2025-11-01 添加动态签署节点、审批、个人签名类型
ALTER TABLE `sign_ru` ADD COLUMN `auto_finish` tinyint(1) NULL DEFAULT NULL COMMENT '签署实例结束类型：0:手动结束；1:自动结束；';
ALTER TABLE `sign_person_seal` ADD COLUMN `seal_type` varchar(20) NULL DEFAULT NULL COMMENT 'TEMPLATE：模板生成、HAND：手写签名;';
ALTER TABLE `sign_re_sign_confirm` ADD COLUMN `seal_type` varchar(20) NULL DEFAULT NULL COMMENT '不限制：NOLIMIT；TEMPLATE：模板生成、HAND：手写签名;';
ALTER TABLE `sign_ru_sign_confirm` ADD COLUMN `seal_type` varchar(20) NULL DEFAULT NULL COMMENT '不限制：NOLIMIT；TEMPLATE：模板生成、HAND：手写签名;';
UPDATE `sys_permission` SET `delete_flag` = 0 WHERE `id` = '06705052-19d4-4e89-8d78-b7ad5e23e943';
UPDATE `sys_permission` SET `path` = '/contract/approval/:signReId?/:signRuId?/:__full__?/:type?/:isDetail?/:from?', `component` = '/contract/approval/index' WHERE `id` = '4f0121fb-328f-4061-a1e2-efa412262a81';
-- 这是最后一句
SET FOREIGN_KEY_CHECKS = 1;


