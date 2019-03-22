DROP TABLE IF EXISTS t_doc;
CREATE TABLE t_doc
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_author CHAR(36) NOT NULL COMMENT '作者ID',
  c_category VARCHAR(255) NOT NULL COMMENT '类别',
  c_sort INT DEFAULT 0 COMMENT '顺序',
  c_subject VARCHAR(255) NOT NULL COMMENT '标题',
  c_image VARCHAR(255) DEFAULT NULL COMMENT '主图URI地址',
  c_thumbnail VARCHAR(255) DEFAULT NULL COMMENT '缩略图URI地址',
  c_summary TEXT DEFAULT NULL COMMENT '摘要',
  c_label TEXT DEFAULT NULL COMMENT '标签',
  c_type VARCHAR(255) DEFAULT NULL COMMENT '类型',
  c_source TEXT DEFAULT NULL COMMENT '内容源',
  c_content TEXT DEFAULT NULL COMMENT '内容',
  c_json TEXT DEFAULT NULL COMMENT '扩展属性集',
  c_read INT DEFAULT 0 COMMENT '阅读次数',
  c_favorite INT DEFAULT 0 COMMENT '收藏次数',
  c_comment INT DEFAULT 0 COMMENT '评论次数',
  c_praise INT DEFAULT 0 COMMENT '点赞数',
  c_score INT DEFAULT 0 COMMENT '得分',
  c_create DATETIME DEFAULT NULL COMMENT '创建时间',
  c_modify DATETIME DEFAULT NULL COMMENT '更新时间',
  c_audit INT DEFAULT 0 COMMENT '审核：0-待审核；1-审核通过；2-审核不通过',
  c_audit_remark VARCHAR(255) DEFAULT NULL COMMENT '审核备注',
  c_recycle INT DEFAULT 0 COMMENT '回收站；0-否，1-是',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_recycle_audit(c_recycle,c_audit) USING BTREE,
  KEY k_recycle_author(c_recycle,c_author,c_modify) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
