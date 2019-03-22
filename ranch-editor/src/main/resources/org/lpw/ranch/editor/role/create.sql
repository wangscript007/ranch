DROP TABLE IF EXISTS t_editor_role;
CREATE TABLE t_editor_role
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_user CHAR(36) NOT NULL COMMENT '用户',
  c_editor CHAR(36) NOT NULL COMMENT '编辑器',
  c_type INT DEFAULT 0 COMMENT '类型：0-所有者；1-可编辑；2-仅浏览',
  c_password VARCHAR(255) DEFAULT NULL COMMENT '访问密码',
  c_template INT DEFAULT 0 COMMENT '编辑器模板',
  c_etype VARCHAR(255) DEFAULT NULL COMMENT '编辑器类型',
  c_state INT DEFAULT 0 COMMENT '编辑器状态',
  c_modify DATETIME DEFAULT NULL COMMENT '编辑器修改时间',
  c_create DATETIME DEFAULT NULL COMMENT '创建时间',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_user(c_user) USING HASH,
  KEY k_editor(c_editor) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
