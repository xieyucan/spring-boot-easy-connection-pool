package com.xh.springboot;

import com.zaxxer.hikari.HikariConfig;

import java.io.Serializable;

/**
 * Created by xiehui1956(@)gmail.com on 2020/6/9
 */
public class DataSourceConfig implements Serializable {

    private String dataSourceName;

    private HikariConfig hikariConfig;

    public DataSourceConfig(String dataSourceName, HikariConfig hikariConfig) {
        this.dataSourceName = dataSourceName;
        this.hikariConfig = hikariConfig;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public HikariConfig getHikariConfig() {
        return hikariConfig;
    }

    public void setHikariConfig(HikariConfig hikariConfig) {
        this.hikariConfig = hikariConfig;
    }
}
