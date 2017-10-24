INSERT INTO t_user(c_id,c_password,c_code,c_grade) VALUES(UUID(),'05dcde47c3fc72c48a37d9ff0436eb14','rootcode',99);
INSERT INTO t_user_auth VALUES(UUID(),(SELECT c_id FROM t_user WHERE c_grade=99),'root',NULL,1);
