DROP TABLE IF EXISTS t_dbtool_column;
CREATE TABLE t_dbtool_column
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_table CHAR(36) NOT NULL COMMENT '表',
  c_sort INT DEFAULT 0 COMMENT '显示顺序',
  c_name VARCHAR(255) DEFAULT NULL COMMENT '名称',
  c_type VARCHAR(255) DEFAULT NULL COMMENT '数据类型',
  c_nullable INT DEFAULT 0 COMMENT '是否可为NULL：0-否；1-是',
  c_memo VARCHAR(255) DEFAULT NULL COMMENT '备注',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_table(c_table) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
