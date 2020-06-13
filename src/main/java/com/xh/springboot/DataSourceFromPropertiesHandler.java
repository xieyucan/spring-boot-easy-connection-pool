package com.xh.springboot;

import com.xh.springboot.config.Contains;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 从属性配置文件中获取连接池配置
 * Created by xiehui1956(@)gmail.com on 2020/6/9
 */
public class DataSourceFromPropertiesHandler implements DataSourceHandler {

    private Environment environment;

    public DataSourceFromPropertiesHandler(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Map<String, DataSource> getCustomDataSources() {
        Map<String, DataSource> customDataSources = new ConcurrentHashMap();
        for (DataSourceConfig dataSourceConfig : buildDataSourceConfig()) {
            DataSource hikariDataSource = new HikariDataSource(dataSourceConfig.getHikariConfig());
            customDataSources.put(dataSourceConfig.getDataSourceName(), hikariDataSource);
        }
        return customDataSources;
    }

    public List<DataSourceConfig> buildDataSourceConfig() {
        String dsPrefixes = environment.getProperty(Contains.getDsNameKey());
        Assert.notNull(dsPrefixes, "DataSource Name Can Not Empty!");
        List<DataSourceConfig> dataSourceConfigList = new ArrayList();
        Arrays.stream(dsPrefixes.split(",")).forEach(dsPrefix -> {
            HikariConfig hikariConfig = buildHikariConfig(dsPrefix, environment);
            dataSourceConfigList.add(new DataSourceConfig(dsPrefix, hikariConfig));
        });
        return dataSourceConfigList;
    }

    public HikariConfig buildHikariConfig(String dsName, Environment environment) {
        HikariConfig hikariConfig = new HikariConfig();
        Field[] fields = hikariConfig.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                String propertyValue = environment.getProperty(Contains.getDsPoolPrefix(dsName, field.getName()));
                if (null != propertyValue && propertyValue.trim().length() > 0) {
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), HikariConfig.class);
                    Method writeMethod = propertyDescriptor.getWriteMethod();
                    Class<?> type = field.getType();
                    if (type == int.class || type == Integer.class) {
                        writeMethod.invoke(hikariConfig, Integer.valueOf(propertyValue));
                    } else if (type == String.class) {
                        writeMethod.invoke(hikariConfig, propertyValue);
                    } else if (type == boolean.class || type == Boolean.class) {
                        writeMethod.invoke(hikariConfig, Boolean.valueOf(propertyValue));
                    } else if (type == long.class || type == Long.class) {
                        writeMethod.invoke(hikariConfig, Long.valueOf(propertyValue));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return hikariConfig;
    }
}
