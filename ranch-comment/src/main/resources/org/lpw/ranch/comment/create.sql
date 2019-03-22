DROP TABLE IF EXISTS t_comment;
CREATE TABLE t_comment
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_key VARCHAR(255) DEFAULT NULL COMMENT '服务KEY',
  c_owner CHAR(36) NOT NULL COMMENT '所有者ID',
  c_author CHAR(36) NOT NULL COMMENT '作者ID',
  c_subject VARCHAR(255) DEFAULT NULL COMMENT '标题',
  c_label VARCHAR(255) DEFAULT NULL COMMENT '标签',
  c_content TEXT DEFAULT NULL COMMENT '内容',
  c_score INT DEFAULT 0 COMMENT '评分',
  c_praise INT DEFAULT 0 COMMENT '点赞数',
  c_time DATETIME NOT NULL COMMENT '时间',
  c_audit INT DEFAULT 0 COMMENT '审核：0-待审核；1-审核通过；2-审核不通过',
  c_audit_remark VARCHAR(255) DEFAULT NULL COMMENT '审核备注',
  c_recycle INT DEFAULT 0 COMMENT '回收站：0-否，1-是',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_audit(c_recycle,c_audit,c_time) USING BTREE,
  KEY k_audit_owner(c_recycle,c_audit,c_owner,c_time) USING BTREE,
  KEY k_author(c_recycle,c_author,c_time) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
