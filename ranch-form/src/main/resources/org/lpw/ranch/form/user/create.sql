DROP TABLE IF EXISTS t_form_user;
CREATE TABLE t_form_user
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_user CHAR(36) NOT NULL COMMENT '用户',
  c_form CHAR(36) NOT NULL COMMENT '表单',
  c_type INT DEFAULT 0 COMMENT '类型：0-所有者；1-可设计；2-可编辑；3-可分享；4-只读',
  c_create DATETIME DEFAULT NULL COMMENT '创建时间',
  c_join DATETIME DEFAULT NULL COMMENT '加入时间',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_user_create(c_user,c_create) USING BTREE,
  UNIQUE KEY uk_form_user(c_form,c_user) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
