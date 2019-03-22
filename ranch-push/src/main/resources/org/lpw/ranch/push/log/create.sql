DROP TABLE IF EXISTS t_push_log;
CREATE TABLE t_push_log
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_user CHAR(36) NOT NULL COMMENT '用户',
  c_receiver VARCHAR(255) NOT NULL COMMENT '接收地址',
  c_app_code VARCHAR(255) NOT NULL COMMENT 'APP编码',
  c_sender VARCHAR(255) NOT NULL COMMENT '推送器',
  c_push TEXT DEFAULT NULL COMMENT '推送配置',
  c_args TEXT DEFAULT NULL COMMENT '参数集',
  c_time DATETIME DEFAULT NULL COMMENT '时间',
  c_state INT DEFAULT 0 COMMENT '状态：0-新建；1-已推送；2-已阅读；3-推送失败',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_user_time(c_user,c_time) USING BTREE,
  KEY k_receiver_time(c_receiver,c_time) USING BTREE,
  KEY k_app_code_time(c_app_code,c_time) USING BTREE,
  KEY k_sender_time(c_sender,c_time) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
