DROP TABLE IF EXISTS t_doc_topic;
CREATE TABLE t_doc_topic
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_doc CHAR(36) NOT NULL COMMENT '文档',
  c_classify CHAR(36) NOT NULL COMMENT '分类',
  c_sort INT DEFAULT 0 COMMENT '顺序',
  c_subject VARCHAR(255) DEFAULT NULL COMMENT '标题',
  c_label TEXT DEFAULT NULL COMMENT '标签',
  c_time DATETIME DEFAULT NULL COMMENT '时间',
  c_audit INT DEFAULT 0 COMMENT '审核：0-待审核；1-审核通过；2-审核不通过',
  c_recycle INT DEFAULT 0 COMMENT '回收站；0-否，1-是',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_doc(c_doc) USING HASH,
  KEY k_classify(c_classify) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
