DROP TABLE IF EXISTS t_user_auth;
CREATE TABLE t_user_auth
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_user CHAR(36) DEFAULT NULL COMMENT '用户ID',
  c_uid VARCHAR(255) NOT NULL COMMENT '认证ID',
  c_time DATETIME DEFAULT NULL COMMENT '绑定时间',
  c_type INT DEFAULT 0 COMMENT '类型：0-绑定ID；1-自有账号；其他为第三方账号',
  c_nick VARCHAR(255) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '第三方账号昵称',

  PRIMARY KEY pk(c_id) USING HASH,
  UNIQUE KEY uk_uid(c_uid) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO t_user_auth VALUES('ranch-user-auth-system-0000000000000','ranch-user-system-000000000000000000','system',NOW(),0,NULL);
INSERT INTO t_user_auth VALUES('ranch-user-auth-root-999999999999999','ranch-user-root-99999999999999999999','root',NOW(),0,NULL);