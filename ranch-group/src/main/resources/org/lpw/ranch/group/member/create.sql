DROP TABLE IF EXISTS t_group_member;
CREATE TABLE t_group_member
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_group CHAR(36) NOT NULL COMMENT '群组ID',
  c_user CHAR(36) NOT NULL COMMENT '用户ID',
  c_nick VARCHAR(255) DEFAULT NULL COMMENT '群组昵称',
  c_reason VARCHAR(255) DEFAULT NULL COMMENT '申请加入理由',
  c_type INT DEFAULT 0 COMMENT '类型：0-待审核；1-普通成员；2-管理员；3-所有者',
  c_introducer CHAR(36) DEFAULT NULL COMMENT '介绍人ID',
  c_join DATETIME DEFAULT NULL COMMENT '加入时间',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_group(c_group) USING HASH,
  KEY k_user(c_user) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
