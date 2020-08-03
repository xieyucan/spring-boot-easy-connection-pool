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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 动态数据源注册
 * Created by xiehui1956(@)gmail.com on 2020/5/17
 */
public class DynamicDataSourceRegister implements ImportBeanDefinitionRegistrar, EnvironmentAware {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceRegister.class);

    // 数据源
    private DataSource defaultDataSource;

    private Map<String, DataSource> customDataSources = new ConcurrentHashMap();

    private Map<String, List<String>> customDataSourcesGroup = new ConcurrentHashMap();

    /**
     * 加载多数据源配置
     *
     * @param env
     */
    @Override
    public void setEnvironment(Environment env) {
        initDefaultDataSource(env);
        initCustomDbDataSources(env);
        initCustomDataSources(env);
    }

    /**
     * 2.0.4 初始化主数据源
     */
    private void initDefaultDataSource(Environment env) {
        // 读取主数据源
        DataSource dataSource = buildDataSource("", env);
        logger.info("*** Create DataSource Default Success! ***");
        logger.info("***Print-Tables-Start***:");
        printDbTable(dataSource);
        logger.info("***Print-Tables-End***.\n");
        defaultDataSource = dataSource;
    }

    /**
     * 初始化更多数据源-读取配置文件
     */
    private void initCustomDataSources(Environment environment) {
        String dsPrefixes = environment.getProperty(Contains.getDsNameKey());
        //添加分组数据
        customDataSourcesGroup.putAll(buildGroupDataSource(environment));
        if (!StringUtils.isEmpty(dsPrefixes)) {
            Arrays.stream(dsPrefixes.split(",")).forEach(dsPrefix -> {
                DataSource dataSource = buildDataSource(dsPrefix, environment);
                logger.info("*** Create DataSource {} Success! ***", dsPrefix);
                logger.info("***Print-Tables-Start***:");
                printDbTable(dataSource);
                logger.info("***Print-Tables-End***.\n");
                customDataSources.put(dsPrefix, dataSource);
            });
        }
    }

    /**
     * 初始化更多数据源-读取数据库问价
     * spring.datasource.db.open=true
     */
    private void initCustomDbDataSources(Environment environment) {
        if (environment.getProperty(Contains.getDbOpen(), Boolean.class) == null ? false : true) {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(this.defaultDataSource);
            List<Map<String, Object>> entityList = queryDbEntityList(jdbcTemplate);
            //添加分组数据
            customDataSourcesGroup.putAll(buildGroupDataSource(entityList));
            for (int i = 0; i < entityList.size(); i++) {
                Map<String, Object> dbEntity = entityList.get(i);
                HikariConfig hikariConfig = buildHikariConfig(dbEntity);
                DataSource dataSource = new HikariDataSource(hikariConfig);
                logger.info("*** Create DataSource {} Success! ***", dbEntity.get("pool_name"));
                logger.info("***Print-Tables-Start***:");
                printDbTable(dataSource);
                logger.info("***Print-Tables-End***.\n");
                customDataSources.put(String.valueOf(dbEntity.get("pool_name")), dataSource);
            }
        }
    }

    public HikariConfig buildHikariConfig(Map<String, Object> dbEntity) {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(String.valueOf(dbEntity.get("driver_class_name")));
        hikariConfig.setJdbcUrl(String.valueOf(dbEntity.get("jdbc_url")));
        hikariConfig.setPoolName(String.valueOf(dbEntity.get("pool_name")));
        hikariConfig.setUsername(String.valueOf(dbEntity.get("username")));
        hikariConfig.setPassword(String.valueOf(dbEntity.get("password")));
        hikariConfig.setMinimumIdle(Integer.parseInt(String.valueOf(dbEntity.get("minimum_idle"))));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(String.valueOf(dbEntity.get("maximum_pool_size"))));
        hikariConfig.setConnectionTestQuery(String.valueOf(dbEntity.get("connection_test_query")));
        return hikariConfig;
    }

    public List<Map<String, Object>> queryDbEntityList(JdbcTemplate jdbcTemplate) {
        List<Map<String, Object>> result = new ArrayList();
        jdbcTemplate.query("SELECT " +
                        "driver_class_name, " +
                        "jdbc_url, " +
                        "pool_name, " +
                        "group_name, " +
                        "balance_type, " +
                        "username, " +
                        "password, " +
                        "minimum_idle, " +
                        "maximum_pool_size, " +
                        "connection_test_query " +
                        "FROM db_entity"
                , rs -> {
                    while (rs.next()) {
                        Map<String, Object> item = new HashMap();
                        item.put("driver_class_name", rs.getString("driver_class_name"));
                        item.put("jdbc_url", rs.getString("jdbc_url"));
                        item.put("pool_name", rs.getString("pool_name"));
                        item.put("group_name", rs.getString("group_name"));
                        item.put("balance_type", rs.getString("balance_type"));
                        item.put("username", rs.getString("username"));
                        item.put("password", rs.getString("password"));
                        item.put("minimum_idle", rs.getString("minimum_idle"));
                        item.put("maximum_pool_size", rs.getString("maximum_pool_size"));
                        item.put("connection_test_query", rs.getString("connection_test_query"));
                        result.add(item);
                    }
                    return result;
                });
        return result;
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
        //添加分组数据源
        DynamicDataSourceContextHolder.dataSourceGroupIds.putAll(customDataSourcesGroup);

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

    /**
     * 按照group_name分组数据源
     *
     * @param entityList
     * @return
     */
    public Map<String, List<String>> buildGroupDataSource(List<Map<String, Object>> entityList) {
        Map<String, List<String>> result = new ConcurrentHashMap();
        for (Map.Entry<String, List<Map<String, Object>>> entryItem :
                Optional.ofNullable(entityList).orElse(new ArrayList<Map<String, Object>>()).stream()
                        .filter(entryItem -> !StringUtils.isEmpty(String.valueOf(entryItem.get("group_name"))))
                        .collect(Collectors.groupingBy(entryItem -> String.valueOf(entryItem.get("group_name"))))
                        .entrySet()) {

            String key = entryItem.getKey();
            List<String> groupIdList = entryItem.getValue().stream()
                    .map(mapItem -> String.valueOf(mapItem.get("pool_name"))).collect(Collectors.toList());
            if ("null".equals(key))
                continue;

            logger.info("*** Create Group  = {} DataSourceList = {} Success! ***", key, groupIdList);
            result.put(key, groupIdList);
        }
        return result;
    }

    /**
     * 按照group_name分组数据源
     *
     * @param environment
     * @return
     */
    public Map<String, List<String>> buildGroupDataSource(Environment environment) {
        Map<String, List<String>> result = new ConcurrentHashMap();
        String dsPrefixes = environment.getProperty(Contains.getDsNameKey());
        if (!StringUtils.isEmpty(dsPrefixes)) {
            for (String dsPrefix : dsPrefixes.split(",")) {
                String group_name = environment.getProperty(Contains.getDsPoolPrefix(dsPrefix, "groupName"));

                if (StringUtils.isEmpty(group_name) || StringUtils.isEmpty(dsPrefix))
                    continue;

                List<String> groupIdList = new ArrayList();
                if (result.containsKey(group_name)) {
                    groupIdList = result.get(group_name);
                }

                groupIdList.add(dsPrefix);
                result.put(group_name, groupIdList);
                logger.info("*** Create Group  = {} DataSource = {} Success! ***", group_name, dsPrefix);
            }
        }
        return result;
    }

}