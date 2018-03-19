DROP TABLE IF EXISTS t_editor;
CREATE TABLE t_editor
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_type VARCHAR(255) NOT NULL COMMENT '类型',
  c_sort INT DEFAULT 0 COMMENT '顺序',
  c_name VARCHAR(255) DEFAULT NULL COMMENT '名称',
  c_width INT DEFAULT 0 COMMENT '宽度',
  c_height INT DEFAULT 0 COMMENT '高度',
  c_image VARCHAR(255) DEFAULT NULL COMMENT '预览图',
  c_json TEXT DEFAULT NULL COMMENT '扩展属性集',
  c_create DATETIME DEFAULT NULL COMMENT '创建时间',
  c_modify DATETIME DEFAULT NULL COMMENT '修改时间',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_type_sort_create(c_type,c_sort,c_create) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
