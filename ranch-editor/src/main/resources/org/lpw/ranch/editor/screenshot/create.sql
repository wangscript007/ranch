DROP TABLE IF EXISTS t_editor_screenshot;
CREATE TABLE t_editor_screenshot
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_editor CHAR(36) NOT NULL COMMENT '编辑器',
  c_index INT DEFAULT 0 COMMENT '序号',
  c_page VARCHAR(255) DEFAULT NULL COMMENT '页面',
  c_uri VARCHAR(255) DEFAULT NULL COMMENT '资源URI地址',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_editor(c_editor) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
