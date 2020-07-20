## SpringBoot动态配置多数据源连接池
### 背景
#### 数据部后端服务特点：
	1. 需要对接多种数据源
	2. 数据源中存储的数据量较大
	3. 需要为不同的业务部门、数据看板、数据报表提供数据服务
#### 数据部后端服务痛点：
	1. 服务中多数据源配置繁琐、切换逻辑复杂
	2. 使用连接池，多数据源多连接池配置逻辑复杂易出错；不使用连接池，系统性能较低且容易拉垮数据源
	3. 大型系统中由于数据源过多数据源会集中在数据库中管理，这样更加需要动态管理配置数据源
### 插件简介
基于上述项目背景，统一封装了多数据源连接池动态化配置插件，连接池使用的是业界号称"最快的"Hikari（SpringBoot2默认的连接池）；只需在项目中引入插件，在属性文件中添加必要的数据源连接配置信息及连接池参数，就可以通过注解进而动态切换数据源；无论是读写分离、主从配置还是一主多从的数据源均可通过此动态配置生成DataSource连接池。
项目中各部分注释比较详细，对于二次开发扩展也非常方便，同时在启动过程会打印出详细的连接池配置流程以及连接池中的表信息：

![启动图片](https://github.com/xieyucan/easy-connection-pool-demo/blob/master/images/start.jpeg)

### 插件功能特点
	1. 动态支持多数据源连接池配置
	2. 理论上支持无限多个数据源连接池
	3. 通过注解切换数据源
	4. 对于数据库读写分离、一主多从等业务场景非常适用
	5. 代码逻辑简单，扩展来自原生Spring接口；注释详细，易于二次开发
### 使用实例
和该项目配置的[实例项目](https://github.com/xieyucan/easy-connection-pool-demo) 可以点击查看

#### 在MyBatis中的用法
演示项目 [easy-mybatis](https://github.com/xieyucan/easy-connection-pool-demo/tree/master/easy-mybatis)

演示用例 [easy-mybatis-MyBatisApplicationTest](https://github.com/xieyucan/easy-connection-pool-demo/tree/master/easy-mybatis/src/test/java/com/xieahui/easy/mybatis)

#### 在JPA中的用法
演示项目 [easy-jpa](https://github.com/xieyucan/easy-connection-pool-demo/tree/master/easy-jpa)

演示用例 [easy-jpa-JpaApplicationTest](https://github.com/xieyucan/easy-connection-pool-demo/blob/master/easy-jpa/src/test/java/com/xieahui/easy/jpa)

#### 在JdbcTemplate中的用法
演示项目 [easy-jdbctemplate](https://github.com/xieyucan/easy-connection-pool-demo/tree/master/easy-jdbctemplate)

演示用例 [easy-jpa-JdbcTemplateApplicationTest](https://github.com/xieyucan/easy-connection-pool-demo/tree/master/easy-jdbctemplate/src/test/java/com/xieahui/easy/jdbctemplate)


### 使用说明
插件是基于SpringBoot开发maven管理的，使用步骤如下：

1.添加插件maven依赖
```java
<dependency>
    <groupId>com.xieahui.springboot</groupId>
    <artifactId>spring-boot-easy-connection-pool</artifactId>
    <version>1.0-RELEASE</version>
</dependency>
```

2.在启动类上添加注解开启动态数据源
```java
@EnableDynamicDataSource
```

3.必要的连接属性配置
说明：spring.datasource.names属性配置数据资源名（如果连接的数据源较少，请在此处移除掉）。默认数据源使用了JPA，使用的持久层技术有JdbcUtils,Hibernate,IBatis和MyBatis,JdbcTemplate,Jpa（现有项目最常用的持久层技术是JPA+JdbcTemplate；持久层技术方案众多，好坏只有自己去品）。能解决业务场景问题、开发效率高用的开心就好。

```java
server.port=8080
#db,数据源名字
spring.datasource.names=db1,db2,db3
spring.datasource.type=com.zaxxer.hikari.util.DriverDataSource
#jpa
spring.jpa.database=mysql
spring.jpa.show-sql=true
spring.jpa.generate-ddl=true
spring.jpa.hibernate.ddl-auto=update
#mysql-1
#表示使用基于DriverManager的配置
spring.datasource.hikari.jdbcUrl=jdbc:mysql://localhost:3306/db
#数据库连接用户名
spring.datasource.hikari.username=root
#数据库连接密码
spring.datasource.hikari.password=root
#数据库连接驱动
spring.datasource.hikari.driverClassName=com.mysql.cj.jdbc.Driver
#池中维护的最小空闲连接数,如果空闲连接低于此值并且总连接数小于maximumPoolSize，则HC将快速有效的添加其他连接
spring.datasource.hikari.minimumIdle=10
#池中维护的最大连接数
spring.datasource.hikari.maximumPoolSize=15
#控制连接是否自动提交事务
spring.datasource.hikari.autoCommit=true
#空闲连接闲置时间
spring.datasource.hikari.idleTimeout=30000
#连接池名称
spring.datasource.hikari.poolName=HikariCP1
#连接最长生命周期,如果不等于0且小于30秒则会被重置回30秒
spring.datasource.hikari.maxLifetime=1800000
#从池中获取连接的最大等待时间默认30000毫秒，如果小于250毫秒则被重置会30秒
spring.datasource.hikari.connectionTimeout=30000
#测试连接有效性最大超时时间,默认5秒如果小于250毫秒，则重置回5秒
spring.datasource.hikari.validationTimeout=5000
#测试连接是否有效SQL，PS:不同数据库的测试SQL有可能不一样！
spring.datasource.hikari.connectionTestQuery=SELECT 1
#控制默认情况下从池中获取的Connections是否处于只读模式
spring.datasource.hikari.readOnly=false
#是否在其自己的事务中隔离内部池查询，例如连接活动测试。通常使用默认值，默认值：false
spring.datasource.hikari.isolateInternalQueries=false
#此属性控制是否注册JMX管理Bean
spring.datasource.hikari.registerMbeans=false
#此属性控制是否可以通过JMX（Java Management Extensions，即Java管理扩展，它提供了一种在运行时动态管理资源的体系结构）挂起和恢复池
spring.datasource.hikari.allowPoolSuspension=false
#mysql-2
spring.datasource.db1.hikari.jdbcUrl=jdbc:mysql://localhost:3306/db1
spring.datasource.db1.hikari.username=root
spring.datasource.db1.hikari.password=root
spring.datasource.db1.hikari.driverClassName=com.mysql.cj.jdbc.Driver
spring.datasource.db1.hikari.minimumIdle=10
spring.datasource.db1.hikari.maximumPoolSize=15
spring.datasource.db1.hikari.autoCommit=true
spring.datasource.db1.hikari.idleTimeout=30000
spring.datasource.db1.hikari.poolName=HikariCP2
spring.datasource.db1.hikari.maxLifetime=1800000
spring.datasource.db1.hikari.connectionTimeout=30000
spring.datasource.db1.hikari.validationTimeout=5000
spring.datasource.db1.hikari.connectionTestQuery=SELECT 1
spring.datasource.db1.hikari.readOnly=false
spring.datasource.db1.hikari.isolateInternalQueries=false
spring.datasource.db1.hikari.registerMbeans=false
spring.datasource.db1.hikari.allowPoolSuspension=false
#mysql-2
spring.datasource.db2.hikari.jdbcUrl=jdbc:mysql://localhost:3306/db2
spring.datasource.db2.hikari.username=root
spring.datasource.db2.hikari.password=root
spring.datasource.db2.hikari.driverClassName=com.mysql.cj.jdbc.Driver
spring.datasource.db2.hikari.minimumIdle=10
spring.datasource.db2.hikari.maximumPoolSize=15
spring.datasource.db2.hikari.autoCommit=true
spring.datasource.db2.hikari.idleTimeout=30000
spring.datasource.db2.hikari.poolName=HikariCP3
spring.datasource.db2.hikari.maxLifetime=1800000
spring.datasource.db2.hikari.connectionTimeout=30000
spring.datasource.db2.hikari.validationTimeout=5000
spring.datasource.db2.hikari.connectionTestQuery=SELECT 1
spring.datasource.db2.hikari.readOnly=false
spring.datasource.db2.hikari.isolateInternalQueries=false
spring.datasource.db2.hikari.registerMbeans=false
spring.datasource.db2.hikari.allowPoolSuspension=false
#clickHouse-1
spring.datasource.db3.hikari.jdbcUrl=jdbc:clickhouse://
spring.datasource.db3.hikari.username=root
spring.datasource.db3.hikari.password=root
spring.datasource.db3.hikari.driverClassName=ru.yandex.clickhouse.ClickHouseDriver
spring.datasource.db3.hikari.minimumIdle=5
spring.datasource.db3.hikari.maximumPoolSize=10
spring.datasource.db3.hikari.autoCommit=true
spring.datasource.db3.hikari.idleTimeout=30000
spring.datasource.db3.hikari.poolName=HikariCP4
spring.datasource.db3.hikari.maxLifetime=1800000
spring.datasource.db3.hikari.connectionTimeout=30000
spring.datasource.db3.hikari.validationTimeout=5000
spring.datasource.db3.hikari.connectionTestQuery=SELECT 1
spring.datasource.db3.hikari.readOnly=false
spring.datasource.db3.hikari.isolateInternalQueries=false
spring.datasource.db3.hikari.registerMbeans=false
spring.datasource.db3.hikari.allowPoolSuspension=false
```

4.代码中应用
由于数据源动态切换是使用Aspect+注解完成的，所以调用时需要将Bean交给Spring的IOC容器管理。只有这样Spring才能通过AOP加强，触发我们的切换逻辑。
```java
Controller:
@Resource
private StudentService studentService;
	
@GetMapping("/fcn")
public List<String> findCHNames() {
	return studentService.findClickHouseColumnName();
}

Service:
@Resource
private JdbcTemplate jdbcTemplate;

@TargetDataSource("db3")
public List<String> findClickHouseColumnName() {
	String sql = "show tables";
	List<String> strings = jdbcTemplate.queryForList(sql, String.class);
return strings;
}

```

### 源码地址
https://github.com/xieyucan/spring-boot-easy-connection-pool

### 后续更新说明
如此插件使用的人较多，后续将会增加动态读取数据库中的连接配置信息并添加链接管理界面及连接池管理信息。以上，如有问题可以邮件联系我。祝好！





