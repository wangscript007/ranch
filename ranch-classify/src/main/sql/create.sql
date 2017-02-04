DROP TABLE IF EXISTS t_classify;
CREATE TABLE t_classify
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_code VARCHAR(255) NOT NULL COMMENT '编码',
  c_name VARCHAR(255) NOT NULL COMMENT '名称',
  c_label TEXT DEFAULT NULL COMMENT '标签',
  c_recycle INT DEFAULT 0 COMMENT '回收站：0-否，1-是',

  PRIMARY KEY pk_classify(c_id) USING HASH,
  KEY k_classify_code(c_recycle,c_code) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
