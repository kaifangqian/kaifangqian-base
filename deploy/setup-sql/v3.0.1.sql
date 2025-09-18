use opensign;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
ALTER TABLE operate_log MODIFY COLUMN module_name varchar(255);
update sys_fill_rule  set rule_class = REPLACE(rule_class, 'com.resrun', 'com.kaifangqian');
update job_info  set processor_info = REPLACE(processor_info, 'com.resrun', 'com.kaifangqian');
SET FOREIGN_KEY_CHECKS = 1;

