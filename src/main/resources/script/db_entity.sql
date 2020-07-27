CREATE TABLE `easy_pool_demo`.`db_entity` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `driver_class_name` VARCHAR(45) NULL DEFAULT 'com.mysql.cj.jdbc.Driver' COMMENT '驱动类',
  `jdbc_url` VARCHAR(45) NULL COMMENT '数据库连接地址,必填！',
  `pool_name` VARCHAR(45) NULL COMMENT '连接池名称,必填！',
  `username` VARCHAR(45) NULL COMMENT '用户名',
  `password` VARCHAR(45) NULL COMMENT '密码',
  `minimum_idle` INT NULL DEFAULT 5 COMMENT '最小空闲连接数',
  `maximum_pool_size` INT NULL DEFAULT 10 COMMENT '最大连接数',
  `connection_test_query` VARCHAR(45) NULL DEFAULT 'SELECT 1' COMMENT '测试连接是否有效SQL',
  PRIMARY KEY (`id`));