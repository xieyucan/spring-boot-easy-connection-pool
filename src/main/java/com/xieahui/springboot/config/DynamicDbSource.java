package com.xieahui.springboot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 该方法数据源设置需要在AOP之前执行
 * Created by xiehui1956(@)gmail.com on 2020/7/21
 */
public class DynamicDbSource {

    static Logger logger = LoggerFactory.getLogger(DynamicDbSource.class);

    private final static ThreadLocal<String> dataSource = new ThreadLocal();

    public static void set(String targetDataSourceName) {
        logger.info("Set-Thread-Id = " + Thread.currentThread().getId() + " ;targetDataSourceName = " + targetDataSourceName);
        dataSource.set(targetDataSourceName);
    }

    public static String get() {
        logger.info("Get-Thread-Id = " + Thread.currentThread().getId() + " ;targetDataSourceName = " + dataSource.get());
        return dataSource.get();
    }

    public static void remove() {
        logger.info("Remove-Thread-Id = " + Thread.currentThread().getId() + " ;targetDataSourceName = " + dataSource.get());
        dataSource.remove();
    }
}
