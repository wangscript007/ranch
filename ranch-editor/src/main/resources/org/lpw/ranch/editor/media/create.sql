DROP TABLE IF EXISTS t_editor_media;
CREATE TABLE t_editor_media
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_user CHAR(36) NOT NULL COMMENT '用户',
  c_editor CHAR(36) NOT NULL COMMENT '编辑器',
  c_type INT DEFAULT 0 COMMENT '类型：0-背景；1-图片；2-音频；3-视频',
  c_url VARCHAR(255) DEFAULT NULL COMMENT 'URL地址',
  c_name VARCHAR(255) DEFAULT NULL COMMENT '文件名',
  c_time DATETIME DEFAULT NULL COMMENT '时间',

  PRIMARY KEY pk(c_id) USING HASH,
  KEY k_user(c_user) USING HASH,
  KEY k_editor(c_editor) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
