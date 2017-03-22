DROP TABLE IF EXISTS t_group;
CREATE TABLE t_group
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_owner CHAR(36) NOT NULL COMMENT '所有者ID',
  c_name VARCHAR(256) DEFAULT NULL COMMENT '名称',
  c_note VARCHAR(256) DEFAULT NULL COMMENT '公告',
  c_member INT DEFAULT 0 COMMENT '成员数',
  c_audit INT DEFAULT 0 COMMENT '新成员是否需要审核：0-否；1-是',
  c_create DATETIME NOT NULL COMMENT '创建时间',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_owner(c_owner) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS t_group_member;
CREATE TABLE t_group_member
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_group CHAR(36) NOT NULL COMMENT '群组ID',
  c_user CHAR(36) NOT NULL COMMENT '用户ID',
  c_nick VARCHAR(256) DEFAULT NULL COMMENT '群组昵称',
  c_reason VARCHAR(256) DEFAULT NULL COMMENT '申请加入理由',
  c_type INT DEFAULT 0 COMMENT '类型：0-待审核；1-普通成员；2-管理员；3-所有者',
  c_join DATETIME DEFAULT NULL COMMENT '加入时间',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_group(c_group) USING HASH,
  KEY k_user(c_user) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
