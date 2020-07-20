package com.xieahui.springboot.config;

/**
 * Created by xiehui1956(@)gmail.com on 2020/5/22
 */
public class Contains {

    public static class DbTemplate {
        /**
         * mysql连接模板
         * jdbc:mysql://ip:port/db?params
         */
        public static final String JDBC_MYSQL = "jdbc:mysql://%s/%s?%s";

        /**
         * oracle连接模板
         * jdbc:oracle:thin:@ip:port:db
         */
        public static final String JDBC_ORACLE = "jdbc:oracle:thin:@%s:%s";

        /**
         * clickhouse连接模板
         * jdbc:clickhouse://ip:port/db
         */
        public static final String JDBC_CLICKHOUSE = "jdbc:clickhouse://%s/%s";

        /**
         * sql-server连接模板
         * jdbc:microsoft:sqlserver://ip:port;DatabaseName=db
         */
        public static final String JDBC_SQL_SERVER = "jdbc:microsoft:sqlserver://%s;DatabaseName=%s";

        /**
         * db2-连接模板
         * jdbc:db2://ip:port/db
         */
        public static final String JDBC_DB2 = "jdbc:db2://%s/%s";

    }

    /**
     * 获取数据源名key
     *
     * @return 数据源key
     */
    public static String getDsNameKey() {
        return String.format("%s.%s", DB_SETTING_PREFIX, DB_NAME);
    }

    /**
     * 获取默认连接池类型
     *
     * @return 连接池类型
     */
    public static String getDefaultType() {
        return getType("");
    }

    /**
     * 获取指定的连接池类型
     *
     * @param dataSourceName 数据源名称
     * @return 连接池类型
     */
    public static String getType(String dataSourceName) {
        return String.format(NAME_FORMAT, DB_SETTING_PREFIX, dataSourceName, DatasourceBaseProperty.TYPE);
    }

    /**
     * 获取默认驱动类
     *
     * @return 驱动类
     */
    public static String getDefaultDriverClassName() {
        return getDriverClassName("");
    }

    /**
     * 获取指定驱动类
     *
     * @param dataSourceDriverClassName 驱动名
     * @return 驱动类
     */
    public static String getDriverClassName(String dataSourceDriverClassName) {
        return String.format(NAME_FORMAT, DB_SETTING_PREFIX, dataSourceDriverClassName, DatasourceBaseProperty.DRIVER_CLASS_NAME);
    }

    /**
     * 获取默认连接
     *
     * @return 默认连接
     */
    public static String getDefaultUrl() {
        return getUrl("");
    }

    /**
     * 获取指定连接
     *
     * @param datasourceUrl 连接URL
     * @return 指定连接
     */
    public static String getUrl(String datasourceUrl) {
        return String.format(NAME_FORMAT, DB_SETTING_PREFIX, datasourceUrl, DatasourceBaseProperty.URL);
    }

    /**
     * 获取默认连接用户名
     *
     * @return 连接用户名
     */
    public static String getDefaultUsername() {
        return getUsername("");
    }

    /**
     * 获取指定连接用户名
     *
     * @param datasourceUsername 连接名
     * @return 用户名
     */
    public static String getUsername(String datasourceUsername) {
        return String.format(NAME_FORMAT, DB_SETTING_PREFIX, datasourceUsername, DatasourceBaseProperty.USERNAME);
    }

    /**
     * 获取默认连接密码
     *
     * @return 连接密码
     */
    public static String getDefaultPassword() {
        return getPassword("");
    }

    /**
     * 获取指定连接密码
     *
     * @param password 密码
     * @return 密码
     */
    public static String getPassword(String password) {
        return String.format(NAME_FORMAT, DB_SETTING_PREFIX, password, DatasourceBaseProperty.PASSWORD);
    }

    /**
     * 获取默认dsPrefix
     *
     * @param fieldName 字段名
     * @return dsPrefix
     */
    public static String getDefaultDsPoolPrefix(String fieldName) {
        return getDsPoolPrefix("", fieldName);
    }

    /**
     * 连接池前缀
     *
     * @param dsName 数据源名
     * @param fieldName 字段名
     * @return 连接池前缀
     */
    public static String getDsPoolPrefix(String dsName, String fieldName) {
        if (null != dsName && dsName.trim().length() > 0) {
            dsName = "." + dsName;
        }
        return String.format(DS_POOL_FORMAT, dsName, fieldName);
    }

    /**
     * 数据库名
     */
    private static String DB_NAME = "names";

    /**
     * 属性名构建格式
     */
    private static final String NAME_FORMAT = "%s.%s.%s";

    /**
     * 连接池前缀模板
     */
    private static final String DS_POOL_FORMAT = "spring.datasource%s.hikari.%s";

    /**
     * 数据源配置前缀
     */
    public static String DB_SETTING_PREFIX = "spring.datasource";

    /**
     * 连接池
     */
    public static final String DATASOURCE_TYPE = "com.zaxxer.hikari.HikariDataSource";

    /**
     * 数据源基础配置字段
     */
    public static class DatasourceBaseProperty {

        /**
         * 连接池类型
         */
        public static final String TYPE = "type";

        /**
         * 驱动类
         */
        public static final String DRIVER_CLASS_NAME = "driver-class-name";

        /**
         * 连接池类型
         */
        public static final String URL = "url";

        /**
         * 用户名
         */
        public static final String USERNAME = "username";

        /**
         * 密码
         */
        public static final String PASSWORD = "password";
    }

}
