DROP TABLE IF EXISTS t_recycle;
CREATE TABLE t_recycle
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_recycle INT DEFAULT 0 COMMENT '回收站；0-否，1-是',

  PRIMARY KEY pk_recycle(c_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS t_audit;
CREATE TABLE t_audit
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_audit INT DEFAULT 0 COMMENT '审核：0-待审核；1-审核通过；2-审核不通过',
  c_audit_remark VARCHAR(255) DEFAULT NULL COMMENT '审核备注',

  PRIMARY KEY pk_audit(c_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
