DROP TABLE IF EXISTS t_device;
CREATE TABLE t_device
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_user CHAR(36) NOT NULL COMMENT '用户',
  c_app_code VARCHAR(255) NOT NULL COMMENT 'APP编码',
  c_type VARCHAR(255) DEFAULT NULL COMMENT '类型：android、ios',
  c_mac_id VARCHAR(255) NOT NULL COMMENT 'Mac ID',
  c_version VARCHAR(255) DEFAULT NULL COMMENT '版本号',
  c_time DATETIME DEFAULT NULL COMMENT '时间',

  PRIMARY KEY pk(c_id) USING HASH,
  UNIQUE KEY uk_app_code_mac_id(c_app_code,c_mac_id) USING BTREE,
  KEY k_app_code_type(c_app_code,c_type) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
