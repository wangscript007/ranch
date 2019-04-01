DROP TABLE IF EXISTS t_editor_buy;
CREATE TABLE t_editor_buy
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_user CHAR(36) NOT NULL COMMENT '用户',
  c_editor CHAR(36) NOT NULL COMMENT '编辑器',
  c_price INT DEFAULT 0 COMMENT '价格，单位：分',
  c_time DATETIME DEFAULT NULL COMMENT '时间',

  PRIMARY KEY pk(c_id) USING HASH,
  UNIQUE KEY uk_editor_user(c_editor,c_user) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
