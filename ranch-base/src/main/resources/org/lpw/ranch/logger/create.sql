DROP TABLE IF EXISTS t_logger;
CREATE TABLE t_logger
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_key VARCHAR(255) NOT NULL COMMENT '键',
  c_p0 VARCHAR(255) DEFAULT NULL COMMENT '参数0',
  c_p1 VARCHAR(255) DEFAULT NULL COMMENT '参数1',
  c_p2 VARCHAR(255) DEFAULT NULL COMMENT '参数2',
  c_p3 VARCHAR(255) DEFAULT NULL COMMENT '参数3',
  c_p4 VARCHAR(255) DEFAULT NULL COMMENT '参数4',
  c_p5 VARCHAR(255) DEFAULT NULL COMMENT '参数5',
  c_p6 VARCHAR(255) DEFAULT NULL COMMENT '参数6',
  c_p7 VARCHAR(255) DEFAULT NULL COMMENT '参数7',
  c_p8 VARCHAR(255) DEFAULT NULL COMMENT '参数8',
  c_p9 VARCHAR(255) DEFAULT NULL COMMENT '参数9',
  c_time DATETIME DEFAULT NULL COMMENT '时间',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_key_time(c_key,c_time) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
