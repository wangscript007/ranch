DROP TABLE IF EXISTS t_captcha;
CREATE TABLE t_captcha
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_key VARCHAR(255) NOT NULL COMMENT '引用key',
  c_name VARCHAR(255) DEFAULT NULL COMMENT '名称',
  c_width INT DEFAULT 0 COMMENT '宽度',
  c_height INT DEFAULT 0 COMMENT '高度',
  c_font_min INT DEFAULT 0 COMMENT '最小字号',
  c_font_max INT DEFAULT 0 COMMENT '最大字号',
  c_chars VARCHAR(255) DEFAULT NULL COMMENT '字符集',
  c_length INT DEFAULT 0 COMMENT '字符数',
  c_background INT DEFAULT 0 COMMENT '是否使用背景图片：0-否；1-是',

  PRIMARY KEY pk(c_id) USING HASH,
  UNIQUE KEY uk_key(c_key) USING HASH
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
