DROP TABLE IF EXISTS t_aliyun;
CREATE TABLE t_aliyun
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_key VARCHAR(255) NOT NULL COMMENT '引用key',
  c_name VARCHAR(255) NOT NULL COMMENT '名称',
  c_region_id VARCHAR(255) DEFAULT NULL COMMENT '接入区域',
  c_access_key_id VARCHAR(255) DEFAULT NULL COMMENT '访问key ID',
  c_access_key_secret VARCHAR(255) DEFAULT NULL COMMENT '访问key密钥',

  PRIMARY KEY pk(c_id) USING HASH,
  UNIQUE KEY uk_key(c_key) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
