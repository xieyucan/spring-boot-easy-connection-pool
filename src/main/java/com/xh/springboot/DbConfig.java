package com.xh.springboot;

import java.io.Serializable;

/**
 * 数据库映射字段
 * Created by xiehui1956(@)gmail.com on 2020/6/9
 */
public class DbConfig implements Serializable {

    /**
     * 数据库名
     * 建议必填！
     */
    private String dbName;

    /**
     * 驱动类
     * 建议必填！
     */
    private String driverClassName;

    /**
     * 数据库连接地址
     * 建议必填！
     */
    private String jdbcUrl;

    /**
     * 用户名
     * 建议必填！
     */
    private String username;

    /**
     * 密码
     * 建议必填！
     */
    private String password;

    /**
     * 连接池名称
     * 建议必填！
     */
    private String poolName = "HikariCP";

    /**
     * 池中维护的最小空闲连接数,如果空闲连接低于此值并且总连接数小于maximumPoolSize，则HC将快速有效的添加其他连接
     */
    private int minimumIdle = 10;

    /**
     * 池中维护的最大连接数
     */
    private int maximumPoolSize = 20;

    /**
     * 控制连接是否自动提交事务
     */
    private boolean autoCommit = true;

    /**
     * 空闲连接闲置时间
     */
    private int idleTimeout = 30000;

    /**
     * 连接最长生命周期,如果不等于0且小于30秒则会被重置回30秒
     */
    private int maxLifetime = 1800000;

    /**
     * 从池中获取连接的最大等待时间默认30000毫秒，如果小于250毫秒则被重置会30秒
     */
    private int connectionTimeout = 30000;

    /**
     * 测试连接有效性最大超时时间,默认5秒如果小于250毫秒，则重置回5秒
     */
    private int validationTimeout = 5000;

    /**
     * 测试连接是否有效SQL，PS:不同数据库的测试SQL有可能不一样！错误的测试sql，表现出来是无法获取有效连接。
     */
    private String connectionTestQuery = "SELECT 1";

    /**
     * 控制默认情况下从池中获取的Connections是否处于只读模式
     */
    private boolean readOnly = false;

    /**
     * 是否在其自己的事务中隔离内部池查询，例如连接活动测试。通常使用默认值，默认值：false
     */
    private boolean isolateInternalQueries = false;

    /**
     * 此属性控制是否注册JMX管理Bean
     */
    private boolean registerMbeans = false;

    /**
     * 此属性控制是否可以通过JMX（Java Management Extensions，即Java管理扩展，它提供了一种在运行时动态管理资源的体系结构）挂起和恢复池
     */
    private boolean allowPoolSuspension = false;

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getMinimumIdle() {
        return minimumIdle;
    }

    public void setMinimumIdle(int minimumIdle) {
        this.minimumIdle = minimumIdle;
    }

    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public int getIdleTimeout() {
        return idleTimeout;
    }

    public void setIdleTimeout(int idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    public String getPoolName() {
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }

    public int getMaxLifetime() {
        return maxLifetime;
    }

    public void setMaxLifetime(int maxLifetime) {
        this.maxLifetime = maxLifetime;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getValidationTimeout() {
        return validationTimeout;
    }

    public void setValidationTimeout(int validationTimeout) {
        this.validationTimeout = validationTimeout;
    }

    public String getConnectionTestQuery() {
        return connectionTestQuery;
    }

    public void setConnectionTestQuery(String connectionTestQuery) {
        this.connectionTestQuery = connectionTestQuery;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isIsolateInternalQueries() {
        return isolateInternalQueries;
    }

    public void setIsolateInternalQueries(boolean isolateInternalQueries) {
        this.isolateInternalQueries = isolateInternalQueries;
    }

    public boolean isRegisterMbeans() {
        return registerMbeans;
    }

    public void setRegisterMbeans(boolean registerMbeans) {
        this.registerMbeans = registerMbeans;
    }

    public boolean isAllowPoolSuspension() {
        return allowPoolSuspension;
    }

    public void setAllowPoolSuspension(boolean allowPoolSuspension) {
        this.allowPoolSuspension = allowPoolSuspension;
    }
}
