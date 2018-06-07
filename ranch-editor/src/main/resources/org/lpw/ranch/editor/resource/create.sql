DROP TABLE IF EXISTS t_editor_resource;
CREATE TABLE t_editor_resource
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_type VARCHAR(255) NOT NULL COMMENT '分类',
  c_sort INT DEFAULT 0 COMMENT '顺序',
  c_name VARCHAR(255) NOT NULL COMMENT '名称',
  c_label VARCHAR(255) DEFAULT NULL COMMENT '说明',
  c_uri VARCHAR(255) DEFAULT NULL COMMENT '资源URI地址',
  c_state INT DEFAULT 0 COMMENT '状态：0-待审核；1-审核通过；2-审核拒绝；3-已上架；4-已下架',
  c_user CHAR(36) NOT NULL COMMENT '用户',
  c_source VARCHAR(255) DEFAULT NULL COMMENT '来源ID',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_type(c_type) USING HASH,
  KEY k_name(c_name) USING HASH,
  KEY k_state(c_state) USING HASH,
  KEY k_user(c_user) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
