DROP TABLE IF EXISTS t_access;
CREATE TABLE t_access
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_host VARCHAR(255) NOT NULL COMMENT '站点',
  c_uri VARCHAR(255) NOT NULL COMMENT '请求URI',
  c_user CHAR(36) NOT NULL COMMENT '用户',
  c_ip VARCHAR(255) DEFAULT NULL COMMENT 'IP',
  c_user_agent TEXT DEFAULT NULL COMMENT 'User Agent',
  c_referer TEXT DEFAULT NULL COMMENT '引用',
  c_header TEXT DEFAULT NULL COMMENT '头信息集',
  c_time DATETIME NOT NULL COMMENT '时间',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_host_uri(c_host,c_uri) USING BTREE,
  KEY k_uri(c_uri) USING BTREE,
  KEY k_user(c_user) USING HASH,
  KEY k_time(c_time) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
