DROP TABLE IF EXISTS t_editor_graphic;
CREATE TABLE t_editor_graphic
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_type VARCHAR(255) NOT NULL COMMENT '分类',
  c_sort INT DEFAULT 0 COMMENT '顺序',
  c_name VARCHAR(255) NOT NULL COMMENT '名称',
  c_label VARCHAR(255) DEFAULT NULL COMMENT '说明',
  c_data TEXT DEFAULT NULL COMMENT '数据',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_type(c_type) USING HASH,
  KEY k_name(c_name) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
