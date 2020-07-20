package com.xieahui.springboot.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiehui1956(@)gmail.com on 2020/5/17
 */
public class DynamicDataSourceContextHolder {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal();

    public static List<String> dataSourceIds = new ArrayList();

    public static void setDataSourceType(String dataSourceType) {
        contextHolder.set(dataSourceType);
    }

    public static String getDataSourceType() {
        return contextHolder.get();
    }

    public static void clearDataSourceType() {
        contextHolder.remove();
    }

    /**
     * 判断指定DataSource当前是否存在
     *
     * @param dataSourceId 数据源名
     * @return 验证数据源是否初始化
     */
    public static boolean containsDataSource(String dataSourceId) {
        return dataSourceIds.contains(dataSourceId);
    }
}
