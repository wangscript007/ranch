DROP TABLE IF EXISTS t_doc;
CREATE TABLE t_doc
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_key VARCHAR(255) NOT NULL COMMENT '类型KEY',
  c_owner CHAR(36) NOT NULL COMMENT '所有者ID',
  c_author CHAR(36) NOT NULL COMMENT '作者ID',
  c_score_min INT DEFAULT 0 COMMENT '最小分值',
  c_score_max INT DEFAULT 0 COMMENT '最大分值',
  c_sort INT DEFAULT 0 COMMENT '顺序',
  c_subject VARCHAR(255) NOT NULL COMMENT '标题',
  c_image VARCHAR(255) DEFAULT NULL COMMENT '主图URI地址',
  c_thumbnail VARCHAR(255) DEFAULT NULL COMMENT '缩略图URI地址',
  c_summary TEXT DEFAULT NULL COMMENT '摘要',
  c_label TEXT DEFAULT NULL COMMENT '标签',
  c_content TEXT NOT NULL COMMENT '内容',
  c_read INT DEFAULT 0 COMMENT '阅读次数',
  c_favorite INT DEFAULT 0 COMMENT '收藏次数',
  c_comment INT DEFAULT 0 COMMENT '评论次数',
  c_score INT DEFAULT 0 COMMENT '得分',
  c_time DATETIME NOT NULL COMMENT '时间',
  c_audit INT DEFAULT 0 COMMENT '审核：0-待审核；1-审核通过；2-审核不通过',

  PRIMARY KEY pk_doc(c_id),
  KEY k_doc_key(c_audit,c_key),
  KEY k_doc_owner(c_audit,c_owner),
  KEY k_doc_author(c_author,c_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
