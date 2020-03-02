DROP TABLE IF EXISTS t_user_crosier;
CREATE TABLE t_user_crosier
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_grade INT DEFAULT 0 COMMENT '等级',
  c_uri VARCHAR(255) NOT NULL COMMENT 'URI',
  c_parameter VARCHAR(255) DEFAULT NULL COMMENT '参数',

  PRIMARY KEY pk(c_id) USING HASH,
  UNIQUE KEY uk_grade_uri(c_grade,c_uri) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
