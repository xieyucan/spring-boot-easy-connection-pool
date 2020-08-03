CREATE TABLE `db_entity` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `driver_class_name` varchar(45) DEFAULT 'com.mysql.cj.jdbc.Driver' COMMENT '驱动类',
  `jdbc_url` varchar(45) DEFAULT NULL COMMENT '数据库连接地址<jdbc:mysql://ip:port/db>,必填！',
  `pool_name` varchar(45) NOT NULL COMMENT '连接池名称,必填！',
  `username` varchar(45) DEFAULT NULL COMMENT '用户名',
  `password` varchar(45) DEFAULT NULL COMMENT '密码',
  `minimum_idle` int(11) DEFAULT '5' COMMENT '最小空闲连接数',
  `maximum_pool_size` int(11) DEFAULT '10' COMMENT '最大连接数',
  `connection_test_query` varchar(45) DEFAULT 'SELECT 1' COMMENT '测试连接是否有效SQL',
  `group_name` varchar(45) DEFAULT NULL COMMENT '分组名',
  `balance_type` varchar(45) DEFAULT NULL COMMENT '负载均衡类型',
  PRIMARY KEY (`id`),
  UNIQUE KEY `pool_name_UNIQUE` (`pool_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
