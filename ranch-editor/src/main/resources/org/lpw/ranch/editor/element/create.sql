DROP TABLE IF EXISTS t_editor_element;
CREATE TABLE t_editor_element
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_editor CHAR(36) NOT NULL COMMENT '编辑器',
  c_parent CHAR(36) NOT NULL COMMENT '父元素',
  c_sort INT DEFAULT 0 COMMENT '顺序',
  c_json LONGTEXT DEFAULT NULL COMMENT '扩展属性集',
  c_text TEXT DEFAULT NULL COMMENT '文本',
  c_create DATETIME DEFAULT NULL COMMENT '创建时间',
  c_modify BIGINT DEFAULT 0 COMMENT '修改时间',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_editor_parent(c_editor,c_parent) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
