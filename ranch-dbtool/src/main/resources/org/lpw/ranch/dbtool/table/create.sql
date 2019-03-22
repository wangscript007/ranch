DROP TABLE IF EXISTS t_dbtool_table;
CREATE TABLE t_dbtool_table
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_schema CHAR(36) NOT NULL COMMENT '数据库',
  c_sort INT DEFAULT 0 COMMENT '显示顺序',
  c_group CHAR(36) DEFAULT NULL COMMENT '分组ID',
  c_name VARCHAR(255) DEFAULT NULL COMMENT '名称',
  c_memo VARCHAR(255) DEFAULT NULL COMMENT '备注',
  c_columns INT DEFAULT 0 COMMENT '列数量',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_schema(c_schema) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
