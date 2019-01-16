DROP TABLE IF EXISTS t_log;
CREATE TABLE t_log
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_type VARCHAR(255) NOT NULL COMMENT '类型',
  c_user CHAR(36) DEFAULT NULL COMMENT '用户',
  c_ip VARCHAR(255) DEFAULT NULL COMMENT 'IP',
  c_header TEXT DEFAULT NULL COMMENT '请求头',
  c_parameter TEXT DEFAULT NULL COMMENT '请求参数',
  c_time DATETIME DEFAULT NULL COMMENT '时间',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_type(c_type) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
