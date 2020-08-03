package com.xieahui.springboot.config;

import com.xieahui.springboot.LoadBalance;
import com.xieahui.springboot.LoadBalanceType;
import com.xieahui.springboot.RandomLoadBalance;
import com.xieahui.springboot.RoundRobinLoadBalance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiehui1956(@)gmail.com on 2020/5/17
 */
public class DynamicDataSourceContextHolder {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceContextHolder.class);

    private static final ThreadLocal<String> contextHolder = new ThreadLocal();

    public static List<String> dataSourceIds = new ArrayList();

    public static Map<String, List<String>> dataSourceGroupIds = new HashMap();

    /**
     * 通过数据源名称设置当前数据源
     *
     * @param dataSourceName 数据源名称
     */
    public static void setDataSourceName(String dataSourceName) {
        contextHolder.set(dataSourceName);
    }

    /**
     * 通过数据源分组设置当前数据源
     *
     * @param dataSourceGroupName 数据源分组
     */
    public static void setDataSourceGroup(String dataSourceGroupName, String dataSourceGroupId, LoadBalanceType balanceType) {

        if (StringUtils.isEmpty(dataSourceGroupId)) {

            List<String> dataSourceGroupList = dataSourceGroupIds.get(dataSourceGroupName);

            //轮询
            LoadBalance loadBalance = new RoundRobinLoadBalance(dataSourceGroupList, dataSourceGroupName);

            //随机
            if (LoadBalanceType.RANDOM.equals(balanceType)) {
                loadBalance = new RandomLoadBalance(dataSourceGroupList);
            }

            dataSourceGroupId = loadBalance.select();
            logger.debug("Use DataSource name = [{}] ", dataSourceGroupId);
        }

        contextHolder.set(dataSourceGroupId);
    }

    public static String getDataSource() {
        return contextHolder.get();
    }

    public static void clearDataSource() {
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

    /**
     * 判断指定Group的DataSource当前是否存在
     *
     * @param groupName 数据源名
     * @return
     */
    public static boolean containsDataSourceGroup(String groupName) {
        return dataSourceGroupIds.containsKey(groupName);
    }
}
