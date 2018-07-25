DROP TABLE IF EXISTS t_doc_relation;
CREATE TABLE t_doc_relation
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_doc CHAR(36) NOT NULL COMMENT '文档',
  c_relate CHAR(36) DEFAULT NULL COMMENT '关联文档',
  c_type VARCHAR(255) DEFAULT NULL COMMENT '类型：previous-前一篇；next-后一篇；alike-相似的',
  c_sort INT DEFAULT 0 COMMENT '顺序',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_doc(c_doc) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
