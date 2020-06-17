package com.xieahui.springboot.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态数据源注册
 * Created by xiehui1956(@)gmail.com on 2020/5/17
 */
public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceRegister.class);

    // 数据源
    private DataSource defaultDataSource;
    private Map<String, DataSource> customDataSources = new ConcurrentHashMap();

    //加载多数据源配置
    @Override
    public void setEnvironment(Environment env) {
        initDefaultDataSource(env);
        initCustomDataSources(env);
    }

    /**
     * 2.0.4 初始化主数据源
     */
    private void initDefaultDataSource(Environment env) {
        // 读取主数据源
        DataSource hikariDataSource = buildDataSource("", env);
        logger.info("***Print-Tables-Start***:");
        printDbTable(hikariDataSource);
        logger.info("***Print-Tables-End***.\n");
        defaultDataSource = hikariDataSource;
    }

    //初始化更多数据源
    private void initCustomDataSources(Environment environment) {
        String dsPrefixes = environment.getProperty(Contains.getDsNameKey());
        Assert.notNull(dsPrefixes, "DataSource Name Can Not Empty!");
        Arrays.stream(dsPrefixes.split(",")).forEach(dsPrefix -> {
            DataSource dataSource = buildDataSource(dsPrefix, environment);
            logger.info("***Print-Tables-Start***:");
            printDbTable(dataSource);
            logger.info("***Print-Tables-End***.\n");
            customDataSources.put(dsPrefix, dataSource);
        });
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
        Map<Object, Object> targetDataSources = new HashMap();
        // 将主数据源添加到更多数据源中
        targetDataSources.put("dataSource", defaultDataSource);
        DynamicDataSourceContextHolder.dataSourceIds.add("dataSource");
        // 添加更多数据源
        targetDataSources.putAll(customDataSources);
        for (String key : customDataSources.keySet()) {
            DynamicDataSourceContextHolder.dataSourceIds.add(key);
        }

        // 创建DynamicDataSource
        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClass(DynamicDataSource.class);
        beanDefinition.setSynthetic(true);
        MutablePropertyValues mpv = beanDefinition.getPropertyValues();
        mpv.addPropertyValue("defaultTargetDataSource", defaultDataSource);
        mpv.addPropertyValue("targetDataSources", targetDataSources);
        registry.registerBeanDefinition("dataSource", beanDefinition);

        logger.info("Dynamic DataSource Registry");
    }

    public DataSource buildDataSource(String dsName, Environment environment) {
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

        DataSource hikariDataSource = new HikariDataSource(hikariConfig);
        return hikariDataSource;
    }

    /**
     * 打印数据库表
     *
     * @param dataSource 数据源
     */
    public void printDbTable(DataSource dataSource) {
        try {
            Connection connection = dataSource.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("show tables");
            ResultSet executeQuery = preparedStatement.executeQuery();
            while (executeQuery.next()) {
                logger.info(executeQuery.getString(1));
            }
        } catch (Exception throwAbles) {
            throwAbles.printStackTrace();
        }
    }
}