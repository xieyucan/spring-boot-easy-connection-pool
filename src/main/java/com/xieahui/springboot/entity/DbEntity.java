package com.xieahui.springboot.entity;

import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * Created by xiehui1956(@)gmail.com on 2020/7/20
 */
public class DbEntity implements Serializable {

    /**
     * 数据库主键
     */
    private long id;

    /**
     * 驱动类
     * 建议必填！
     * driver_class_name
     */
    private String driverClassName = "com.mysql.cj.jdbc.Driver";

    /**
     * 数据库连接地址
     * 建议必填！
     * jdbc_url
     * jdbc:mysql://ip:port/db
     */
    private String jdbcUrl;

    /**
     * 连接池名称
     * 建议必填！
     * pool_name
     */
    private String poolName;

    /**
     * 连接用户名
     * username
     */
    private String username;

    /**
     * 连接密码
     * password
     */
    private String password;

    /**
     * 最小空闲连接数
     * minimum_idle
     */
    private int minimumIdle = 5;

    /**
     * 最大连接数
     * maximum_pool_size
     */
    private int maximumPoolSize = 10;

    /**
     * 测试连接是否有效SQL
     * connection_test_query
     */
    private String connectionTestQuery = "SELECT 1";

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getPoolName() {
        if (StringUtils.isEmpty(poolName))
            return String.valueOf(System.currentTimeMillis());
        return poolName;
    }

    public void setPoolName(String poolName) {
        this.poolName = poolName;
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

    public String getConnectionTestQuery() {
        return connectionTestQuery;
    }

    public void setConnectionTestQuery(String connectionTestQuery) {
        this.connectionTestQuery = connectionTestQuery;
    }

    @Override
    public String toString() {
        return "DbEntity{" +
                ", driverClassName='" + driverClassName + '\'' +
                ", jdbcUrl='" + jdbcUrl + '\'' +
                ", poolName='" + poolName + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", minimumIdle=" + minimumIdle +
                ", maximumPoolSize=" + maximumPoolSize +
                ", connectionTestQuery='" + connectionTestQuery + '\'' +
                '}';
    }
}
