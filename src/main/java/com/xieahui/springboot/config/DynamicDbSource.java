package com.xieahui.springboot.config;

import com.xieahui.springboot.GroupDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 该方法数据源设置需要在AOP之前执行
 * Created by xiehui1956(@)gmail.com on 2020/7/21
 */
public class DynamicDbSource {

    static Logger logger = LoggerFactory.getLogger(DynamicDbSource.class);

    private final static ThreadLocal<GroupDataSource> dataSource = new ThreadLocal();

    public static void set(String targetDataSourceId) {
        logger.info("Set-Thread-Id = " + Thread.currentThread().getId() + " ;targetDataSourceName = " + targetDataSourceId);
        dataSource.set(new GroupDataSource(targetDataSourceId));
    }

    public static void set(String targetDataSourceGroupName, String targetDataSourceId, String targetBalanceType) {
        logger.info("Set-Thread-Id = " + Thread.currentThread().getId()
                + " ;targetDataSourceGroupName = " + targetDataSourceGroupName
                + " ;targetDataSourceName = " + targetDataSourceId
                + " ;targetBalanceType = " + targetBalanceType);
        dataSource.set(new GroupDataSource(targetDataSourceGroupName, targetDataSourceId, targetBalanceType));
    }

    public static String get() {
        logger.info("Get-Thread-Id = " + Thread.currentThread().getId() + " ;targetDataSourceName = " + dataSource.get());
        return dataSource.get().getGroupId();
    }

    public static GroupDataSource getGroupDataSource() {
        logger.info("Get-Thread-Id = " + Thread.currentThread().getId() + " ;targetDataSourceName = " + dataSource.get());
        return dataSource.get();
    }

    public static void remove() {
        logger.info("Remove-Thread-Id = " + Thread.currentThread().getId() + " ;targetDataSourceName = " + dataSource.get());
        dataSource.remove();
    }
}
