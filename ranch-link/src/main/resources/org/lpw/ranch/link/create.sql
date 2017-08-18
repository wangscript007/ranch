DROP TABLE IF EXISTS t_link;
CREATE TABLE t_link
(
  c_id CHAR(36) NOT NULL COMMENT '主键',
  c_type VARCHAR(255) NOT NULL COMMENT '类型',
  c_id1 CHAR(36) NOT NULL COMMENT '连接ID1',
  c_id2 CHAR(36) NOT NULL COMMENT '连接ID2',
  c_json TEXT DEFAULT NULL COMMENT '属性集',
  c_time DATETIME NOT NULL COMMENT '时间',

  PRIMARY KEY pk(c_id) USING HASH,
  UNIQUE KEY uk_type_id1_id2(c_type,c_id1,c_id2) USING HASH,
  KEY k_type_id1_time(c_type,c_id1,c_time) USING BTREE,
  KEY k_type_id2_time(c_type,c_id2,c_time) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
