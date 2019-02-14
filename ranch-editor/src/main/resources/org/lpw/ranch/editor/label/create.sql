DROP TABLE IF EXISTS t_editor_label;
CREATE TABLE t_editor_label
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_editor CHAR(36) NOT NULL COMMENT '编辑器',
  c_name VARCHAR(255) NOT NULL COMMENT '名称',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_editor(c_editor) USING HASH,
  KEY k_name(c_name) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
