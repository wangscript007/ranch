DROP TABLE IF EXISTS t_friend;
CREATE TABLE t_friend
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_owner CHAR(36) NOT NULL COMMENT '所有者ID',
  c_user CHAR(36) DEFAULT NULL COMMENT '好友ID',
  c_memo VARCHAR(255) DEFAULT NULL COMMENT '备注',
  c_state INT DEFAULT 0 COMMENT '状态：0-待对方确认；1-待己方确认；2-已通过；3-已拒绝；4-已拉黑',
  c_create DATETIME NOT NULL COMMENT '创建时间',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_owner(c_owner) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
