DROP TABLE IF EXISTS t_stripe_transaction;
CREATE TABLE t_stripe_transaction
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_key VARCHAR(255) NOT NULL COMMENT '引用key',
  c_user CHAR(36) NOT NULL COMMENT '用户',
  c_order_no VARCHAR(255) NOT NULL COMMENT '订单号',
  c_trade_no VARCHAR(255) NOT NULL COMMENT '交易单号',
  c_amount INT DEFAULT 0 COMMENT '金额，单位：分',
  c_currency VARCHAR(255) DEFAULT NULL COMMENT '币种',
  c_response TEXT DEFAULT NULL COMMENT '验证结果',
  c_create DATETIME NOT NULL COMMENT '创建时间',
  c_finish DATETIME DEFAULT NULL COMMENT '完成时间',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_key(c_key) USING HASH,
  KEY k_user(c_user) USING HASH,
  UNIQUE KEY uk_order_no(c_order_no) USING HASH,
  UNIQUE KEY uk_trade_no(c_trade_no) USING HASH,
  KEY k_create(c_create) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
