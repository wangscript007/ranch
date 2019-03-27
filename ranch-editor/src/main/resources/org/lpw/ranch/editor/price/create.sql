DROP TABLE IF EXISTS t_editor_price;
CREATE TABLE t_editor_price
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_type VARCHAR(255) NOT NULL COMMENT '类型',
  c_group VARCHAR(255) NOT NULL COMMENT '分组',
  c_amount INT DEFAULT 0 COMMENT '价格，单位：分',
  c_vip INT DEFAULT 0 COMMENT 'VIP价格，单位：分',
  c_limited INT DEFAULT 0 COMMENT '限时价格，单位：分',
  c_time DATETIME DEFAULT NULL COMMENT '限时时间',

  PRIMARY KEY pk(c_id) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
