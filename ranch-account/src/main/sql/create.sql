DROP TABLE IF EXISTS t_account;
CREATE TABLE t_account
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_user CHAR(36) NOT NULL COMMENT '用户',
  c_owner VARCHAR(255) DEFAULT NULL COMMENT '所有者',
  c_type INT DEFAULT 0 COMMENT '类型',
  c_balance INT DEFAULT 0 COMMENT '余额',
  c_deposit INT DEFAULT 0 COMMENT '存入总额',
  c_withdraw INT DEFAULT 0 COMMENT '取出总额',
  c_reward INT DEFAULT 0 COMMENT '奖励总额',
  c_profit INT DEFAULT 0 COMMENT '盈利总额',
  c_consume INT DEFAULT 0 COMMENT '消费总额',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_user(c_user) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
