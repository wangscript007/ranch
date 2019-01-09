DROP TABLE IF EXISTS t_appstore_transaction;
CREATE TABLE t_appstore_transaction
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_user CHAR(36) NOT NULL COMMENT '用户',
  c_receipt CHAR(36) DEFAULT NULL COMMENT '收据',
  c_transaction_id VARCHAR(255) NOT NULL COMMENT '交易ID',
  c_product_id VARCHAR(255) NOT NULL COMMENT '产品ID',
  c_price INT DEFAULT 0 COMMENT '价格，单位：分',
  c_quantity INT DEFAULT 0 COMMENT '数量',
  c_amount INT DEFAULT 0 COMMENT '金额，单位：分',
  c_time DATETIME DEFAULT NULL COMMENT '时间',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_user(c_user) USING HASH,
  UNIQUE KEY uk_transaction_id(c_transaction_id) USING HASH,
  KEY k_product_id(c_product_id) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
