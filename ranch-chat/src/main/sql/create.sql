DROP TABLE IF EXISTS t_chat_room;
CREATE TABLE t_chat_room
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_owner CHAR(36) DEFAULT NULL COMMENT '所有者ID',
  c_title VARCHAR(255) DEFAULT NULL COMMENT '房间名称',
  c_member INT DEFAULT 0 COMMENT '成员数',
  c_newest DATETIME NOT NULL COMMENT '最新消息时间',
  c_create DATETIME NOT NULL COMMENT '创建时间',
  c_code CHAR(8) NOT NULL COMMENT '编号',

  PRIMARY KEY pk_chat_room(c_id) USING HASH,
  KEY k_chat_room_owner(c_owner) USING HASH,
  UNIQUE KEY uk_chat_room_code(c_code) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS t_chat_member;
CREATE TABLE t_chat_member
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_room CHAR(36) NOT NULL COMMENT '房间ID',
  c_user CHAR(36) NOT NULL COMMENT '用户ID',
  c_nick VARCHAR(255) DEFAULT NULL COMMENT '房间昵称',
  c_type INT DEFAULT 0 COMMENT '类型：0-普通成员；1-管理员；2-所有者',

  PRIMARY KEY pk_chat_member(c_id) USING HASH,
  KEY k_chat_member_room(c_room) USING HASH,
  KEY k_chat_member_user(c_user) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS t_chat_message;
CREATE TABLE t_chat_message
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_sender CHAR(36) NOT NULL COMMENT '发送者ID',
  c_receiver CHAR(36) NOT NULL COMMENT '接收者ID',
  c_type INT DEFAULT 0 COMMENT '类型：0-文本；1-图片；2-音频；3-视频',
  c_content TEXT DEFAULT NULL COMMENT '内容',
  c_time DATETIME NOT NULL COMMENT '发送时间',

  PRIMARY KEY pk_chat_message(c_id) USING HASH,
  KEY k_chat_message_sender(c_sender,c_time) USING BTREE,
  KEY k_chat_message_receiver(c_receiver,c_time) USING BTREE,
  KEY k_chat_message_time(c_time) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
