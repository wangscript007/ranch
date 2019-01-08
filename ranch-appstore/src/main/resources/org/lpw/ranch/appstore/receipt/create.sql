DROP TABLE IF EXISTS t_appstore_receipt;
CREATE TABLE t_appstore_receipt
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_product_id VARCHAR(255) NOT NULL COMMENT '产品ID',
  c_price INT DEFAULT 0 COMMENT '价格，单位：分',
  c_quantity INT DEFAULT 0 COMMENT '数量',
  c_amount INT DEFAULT 0 COMMENT '金额，单位：分',
  c_status INT DEFAULT 0 COMMENT '状态',
  c_md5 CHAR(32) NOT NULL COMMENT '请求MD5',
  c_request TEXT DEFAULT NULL COMMENT '请求数据',
  c_response TEXT DEFAULT NULL COMMENT '返回数据',
  c_time DATETIME DEFAULT NULL COMMENT '时间',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_product_id(c_product_id) USING HASH,
  KEY k_md5(c_md5) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
