DROP TABLE IF EXISTS t_user;
CREATE TABLE t_user
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_password CHAR(32) DEFAULT NULL COMMENT '密码',
  c_secret CHAR(32) DEFAULT NULL COMMENT '安全密码',
  c_idcard VARCHAR(255) DEFAULT NULL COMMENT '身份证号',
  c_name VARCHAR(255) DEFAULT NULL COMMENT '姓名',
  c_nick VARCHAR(255) DEFAULT NULL COMMENT '昵称',
  c_mobile CHAR(11) DEFAULT NULL COMMENT '手机号',
  c_email VARCHAR(255) DEFAULT NULL COMMENT 'Email地址',
  c_portrait VARCHAR(255) DEFAULT NULL COMMENT '头像',
  c_gender INT DEFAULT 0 COMMENT '性别：0-未知；1-男；2-女',
  c_birthday DATE DEFAULT NULL COMMENT '出生日期',
  c_code CHAR(8) NOT NULL COMMENT '唯一编码',
  c_register DATETIME DEFAULT NULL COMMENT '注册时间',
  c_grade INT DEFAULT 0 COMMENT '等级：<50为用户；>=50为管理员；99为超级管理员',
  c_state INT DEFAULT 0 COMMENT '状态：0-正常；1-禁用',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_mobile(c_mobile) USING HASH,
  UNIQUE KEY uk_code(c_code) USING HASH,
  KEY k_register(c_register) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--INSERT INTO t_user(c_id,c_password,c_grade) VALUES(UUID(),'05dcde47c3fc72c48a37d9ff0436eb14',99);
--INSERT INTO t_user_auth VALUES(UUID(),'','root',NULL,1);
--UPDATE t_user_auth SET c_user=(SELECT c_id FROM t_user);
