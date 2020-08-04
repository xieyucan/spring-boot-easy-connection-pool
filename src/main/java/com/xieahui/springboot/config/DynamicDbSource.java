package com.xieahui.springboot.config;

import com.xieahui.springboot.GroupDataSource;
import com.xieahui.springboot.LoadBalanceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * 该方法数据源设置需要在AOP之前执行
 * Created by xiehui1956(@)gmail.com on 2020/7/21
 */
public class DynamicDbSource {

    static Logger logger = LoggerFactory.getLogger(DynamicDbSource.class);

    private final static ThreadLocal<GroupDataSource> dataSource = new ThreadLocal<GroupDataSource>();

    /**
     * 设置连接池
     *
     * @param targetDataSourceId
     */
    public static void set(String targetDataSourceId) {
        logger.info("Set-Thread-Id = " + Thread.currentThread().getId() + " ;targetDataSourceName = " + targetDataSourceId);
        dataSource.set(new GroupDataSource(targetDataSourceId));
    }

    /**
     * 设置分组连接池
     *
     * @param targetDataSourceGroupName 分组名
     */
    public static void setGroupName(String targetDataSourceGroupName) {
        dataSource.set(new GroupDataSource(targetDataSourceGroupName, null, LoadBalanceType.ROUND_ROBIN));
    }

    /**
     * 设置分组连接池
     *
     * @param targetDataSourceGroupName 分组名
     * @param targetDataSourceId        分组连接池名
     */
    public static void setGroupNamePoolName(String targetDataSourceGroupName, String targetDataSourceId) {
        dataSource.set(new GroupDataSource(targetDataSourceGroupName, targetDataSourceId, LoadBalanceType.ROUND_ROBIN));
    }

    /**
     * 设置分组连接池
     *
     * @param targetDataSourceGroupName 分组名
     * @param targetDataSourceId        分组连接池名
     * @param targetBalanceType         负载均衡类型
     */
    public static void setGroupNamePoolNameBalanceType(String targetDataSourceGroupName, String targetDataSourceId
            , LoadBalanceType targetBalanceType) {
        targetDataSourceId = StringUtils.isEmpty(targetDataSourceId) ? "" : targetDataSourceId;
        logger.info("Set-Thread-Id = " + Thread.currentThread().getId()
                + " ;targetDataSourceGroupName = " + targetDataSourceGroupName
                + " ;targetDataSourceName = " + targetDataSourceId
                + " ;targetBalanceType = " + targetBalanceType);
        dataSource.set(new GroupDataSource(targetDataSourceGroupName, targetDataSourceId, targetBalanceType));
    }

    public static String get() {
        GroupDataSource groupDataSource = dataSource.get();
        return null == groupDataSource ? "" : groupDataSource.getGroupId();
    }

    public static GroupDataSource getGroupDataSource() {
        return dataSource.get();
    }

    public static void remove() {
        logger.info("Remove-Thread-Id = " + Thread.currentThread().getId() + " ;targetDataSourceName = " + dataSource.get());
        dataSource.remove();
    }
}
