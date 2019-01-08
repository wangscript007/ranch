DROP TABLE IF EXISTS t_appstore;
CREATE TABLE t_appstore
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_product_id VARCHAR(255) NOT NULL COMMENT '产品ID',
  c_name VARCHAR(255) DEFAULT NULL COMMENT '名称',
  c_amount INT DEFAULT 0 COMMENT '金额，单位：分',

  PRIMARY KEY pk(c_id) USING HASH,
  UNIQUE KEY uk_product_id(c_product_id) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
