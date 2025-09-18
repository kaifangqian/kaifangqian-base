use opensign;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
-- V3.0.0版本

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
SET FOREIGN_KEY_CHECKS = 1;