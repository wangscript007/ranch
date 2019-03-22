DROP TABLE IF EXISTS t_appstore_receipt;
CREATE TABLE t_appstore_receipt
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_user CHAR(36) NOT NULL COMMENT '用户',
  c_status INT DEFAULT 0 COMMENT '状态',
  c_request TEXT DEFAULT NULL COMMENT '请求数据',
  c_response TEXT DEFAULT NULL COMMENT '返回数据',
  c_time DATETIME DEFAULT NULL COMMENT '时间',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_user(c_user) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
