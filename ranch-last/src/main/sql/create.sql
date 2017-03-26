DROP TABLE IF EXISTS t_last;
CREATE TABLE t_last
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_user CHAR(36) NOT NULL COMMENT '用户ID',
  c_type VARCHAR(255) NOT NULL COMMENT '类型',
  c_json TEXT DEFAULT NULL COMMENT '扩展数据',
  c_time DATETIME DEFAULT NULL COMMENT '时间',

  PRIMARY KEY pk(c_id) USING HASH,
  UNIQUE KEY uk_user_type(c_user,c_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
