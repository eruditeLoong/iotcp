ALTER TABLE `iotcp`.`jform_scene` 
ADD COLUMN `is_auto_rotate` INT(1) NULL DEFAULT 0 COMMENT '自动旋转' AFTER `is_default_view`,
ADD COLUMN `is_device_relation` INT(1) NULL DEFAULT 0 COMMENT '设备关系' AFTER `is_auto_rotate`,
ADD COLUMN `is_device_panel` INT(1) NULL DEFAULT 0 COMMENT '设备列表' AFTER `is_device_relation`,
ADD COLUMN `is_label_display` INT(1) NULL DEFAULT 0 COMMENT '标签显示' AFTER `is_device_panel`,
ADD COLUMN `is_stats_display` INT(1) NULL DEFAULT 0 COMMENT '性能控件' AFTER `is_label_display`,
ADD COLUMN `is_device_status` INT(1) NULL DEFAULT 0 COMMENT '设备统计' AFTER `is_stats_display`,
ADD COLUMN `is_shadow_display` INT(1) NULL DEFAULT 0 COMMENT '显示阴影' AFTER `is_device_status`;

-- zhouwr 2019/8/19  --
CREATE TABLE `act_id_info` (
  `ID_` varchar(64) COLLATE utf8_bin NOT NULL,
  `REV_` int(11) DEFAULT NULL,
  `USER_ID_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `TYPE_` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `KEY_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `VALUE_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `PASSWORD_` longblob,
  `PARENT_ID_` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
