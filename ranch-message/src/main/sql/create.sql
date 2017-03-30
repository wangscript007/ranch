DROP TABLE IF EXISTS t_message;
CREATE TABLE t_message
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_sender CHAR(36) NOT NULL COMMENT '发送者ID',
  c_type INT DEFAULT 0 COMMENT '接收者类型：0-好友；1-群组',
  c_receiver CHAR(36) NOT NULL COMMENT '接收者ID',
  c_format INT DEFAULT 0 COMMENT '消息格式：0-文本；1-图片；2-音频；3-视频；4-文件；5-红包；6-转账；7-名片；8-公告；9-通知',
  c_content VARCHAR(2048) NOT NULL COMMENT '内容',
  c_time DATETIME NOT NULL COMMENT '发送时间',
  c_code VARCHAR(64) NOT NULL COMMENT '校验码',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_time(c_time) USING BTREE,
  UNIQUE KEY uk_code(c_code) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS m_message;
CREATE TABLE m_message
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_sender CHAR(36) NOT NULL COMMENT '发送者ID',
  c_type INT DEFAULT 0 COMMENT '接收者类型：0-好友；1-群组',
  c_receiver CHAR(36) NOT NULL COMMENT '接收者ID',
  c_format INT DEFAULT 0 COMMENT '消息格式：0-文本；1-图片；2-音频；3-视频；4-文件；5-红包；6-转账；7-名片；8-公告；9-通知',
  c_content VARCHAR(2048) NOT NULL COMMENT '内容',
  c_time DATETIME NOT NULL COMMENT '发送时间',
  c_code VARCHAR(64) NOT NULL COMMENT '校验码',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_time(c_time) USING BTREE,
  UNIQUE KEY uk_code(c_code) USING HASH
) ENGINE=Memory DEFAULT CHARSET=utf8;
