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

DROP TABLE IF EXISTS t_user_auth;
CREATE TABLE t_user_auth
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_user CHAR(36) DEFAULT NULL COMMENT '用户ID',
  c_uid VARCHAR(255) NOT NULL COMMENT '认证ID',
  c_time DATETIME DEFAULT NULL COMMENT '绑定时间',
  c_type INT DEFAULT 0 COMMENT '类型：0-绑定ID；1-自有账号；其他为第三方账号',

  PRIMARY KEY pk(c_id) USING HASH,
  UNIQUE KEY uk_uid(c_uid) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS t_user_online;
CREATE TABLE t_user_online
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_user CHAR(36) NOT NULL COMMENT '用户',
  c_ip VARCHAR(255) NOT NULL COMMENT 'IP地址',
  c_sid VARCHAR(255) NOT NULL COMMENT 'Session ID',
  c_sign_in DATETIME DEFAULT NULL COMMENT '登入时间',
  c_last_visit DATETIME NOT NULL COMMENT '最后访问时间',

  PRIMARY KEY pk(c_id) USING HASH,
  UNIQUE KEY uk_sid(c_sid) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS m_user_online;
CREATE TABLE m_user_online
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_user CHAR(36) NOT NULL COMMENT '用户',
  c_ip VARCHAR(255) NOT NULL COMMENT 'IP地址',
  c_sid VARCHAR(255) NOT NULL COMMENT 'Session ID',
  c_sign_in DATETIME DEFAULT NULL COMMENT '登入时间',
  c_last_visit DATETIME NOT NULL COMMENT '最后访问时间',

  PRIMARY KEY pk(c_id) USING HASH,
  UNIQUE KEY uk_sid(c_sid) USING HASH
) ENGINE=Memory DEFAULT CHARSET=utf8;
