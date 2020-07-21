CREATE TABLE `easy_pool_demo`.`db_entity` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `driver_class_name` VARCHAR(45) NULL DEFAULT 'com.mysql.cj.jdbc.Driver',
  `jdbc_url` VARCHAR(45) NULL,
  `pool_name` VARCHAR(45) NULL,
  `username` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  `minimum_idle` INT NULL DEFAULT 5,
  `maximum_pool_size` INT NULL DEFAULT 10,
  `connection_test_query` VARCHAR(45) NULL DEFAULT 'SELECT 1',
  PRIMARY KEY (`id`));