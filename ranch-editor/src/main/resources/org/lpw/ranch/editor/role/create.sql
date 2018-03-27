DROP TABLE IF EXISTS t_editor_role;
CREATE TABLE t_editor_role
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_user CHAR(36) NOT NULL COMMENT '用户',
  c_editor CHAR(36) NOT NULL COMMENT '编辑器',
  c_type INT DEFAULT 0 COMMENT '类型：0-所有者；1-可编辑；2-仅浏览',
  c_modify DATETIME DEFAULT NULL COMMENT '修改时间',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_user(c_user) USING HASH,
  KEY k_editor(c_editor) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
