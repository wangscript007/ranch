DROP TABLE IF EXISTS t_editor_file;
CREATE TABLE t_editor_file
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_editor CHAR(36) NOT NULL COMMENT '编辑器',
  c_type VARCHAR(255) NOT NULL COMMENT '类型',
  c_uri VARCHAR(255) DEFAULT NULL COMMENT '保存地址',
  c_size BIGINT DEFAULT 0 COMMENT '文件大小',
  c_download INT DEFAULT 0 COMMENT '下载次数',
  c_time DATETIME DEFAULT NULL COMMENT '时间',

  PRIMARY KEY pk(c_id) USING HASH,
  UNIQUE KEY uk_editor_type(c_editor,c_type) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
