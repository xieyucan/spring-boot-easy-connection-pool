package com.xh.springboot;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.BeanUtils;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 从数据库连接中获取
 * Created by xiehui1956(@)gmail.com on 2020/6/9
 */
public class DataSourceFromDbHandler implements DataSourceHandler {

    private List<DbConfig> dbConfigList;

    public DataSourceFromDbHandler(List<DbConfig> dbConfigList) {
        this.dbConfigList = dbConfigList;
    }

    @Override
    public Map<String, DataSource> getCustomDataSources() {
        Map<String, DataSource> customDataSources = new ConcurrentHashMap();

        List<DataSourceConfig> dataSourceConfigList = new ArrayList();
        for (DbConfig dbConfig : dbConfigList) {
            HikariConfig hikariConfig = new HikariConfig();
            BeanUtils.copyProperties(dbConfig, hikariConfig);
            customDataSources.put(dbConfig.getDbName(), new HikariDataSource(hikariConfig));
        }
        return customDataSources;
    }

}
