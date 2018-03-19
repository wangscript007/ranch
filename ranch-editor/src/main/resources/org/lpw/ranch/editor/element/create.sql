DROP TABLE IF EXISTS t_editor_element;
CREATE TABLE t_editor_element
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_editor CHAR(36) NOT NULL COMMENT '编辑器',
  c_parent CHAR(36) NOT NULL COMMENT '父元素',
  c_sort INT DEFAULT 0 COMMENT '顺序',
  c_type VARCHAR(255) DEFAULT NULL COMMENT '类型',
  c_keyword VARCHAR(255) DEFAULT NULL COMMENT '关键词',
  c_x INT DEFAULT 0 COMMENT 'X位置',
  c_y INT DEFAULT 0 COMMENT 'Y位置',
  c_width INT DEFAULT 0 COMMENT '宽度',
  c_height INT DEFAULT 0 COMMENT '高度',
  c_json TEXT DEFAULT NULL COMMENT '扩展属性集',
  c_create DATETIME DEFAULT NULL COMMENT '创建时间',
  c_modify DATETIME DEFAULT NULL COMMENT '修改时间',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_editor_parent(c_editor,c_parent) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
