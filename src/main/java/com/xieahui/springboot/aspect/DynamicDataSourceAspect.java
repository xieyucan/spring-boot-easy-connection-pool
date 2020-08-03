package com.xieahui.springboot.aspect;

import com.xieahui.springboot.annotation.TargetDataSource;
import com.xieahui.springboot.config.DynamicDataSourceContextHolder;
import com.xieahui.springboot.config.DynamicDbSource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;


/**
 * 切换数据源Advice
 * 保证该AOP在@Transactional之前执行
 * Created by xiehui1956(@)gmail.com on 2020/5/17
 */
@Aspect
@Order(-1)
@Component
public class DynamicDataSourceAspect {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Before("@annotation(targetDataSource)")
    public void changeDataSource(JoinPoint point, TargetDataSource targetDataSource) {
        String dsName = targetDataSource.value(), groupName = null, balanceType = null;

        //注解未分组配置
        if (StringUtils.isEmpty(dsName)) {
            dsName = targetDataSource.groupId();
            groupName = targetDataSource.groupName();
            balanceType = targetDataSource.balanceType();
        }

        // db未分组配置
        if (StringUtils.isEmpty(dsName)) {
            dsName = DynamicDbSource.get();

            //db分组配置
            if (StringUtils.isEmpty(dsName)) {
                dsName = DynamicDbSource.getGroupDataSource().getGroupId();
                groupName = DynamicDbSource.getGroupDataSource().getGroupName();
                balanceType = DynamicDbSource.getGroupDataSource().getBalanceType();
            }
        }

        if (DynamicDataSourceContextHolder.containsDataSource(dsName)) {

            logger.debug("Use DataSource name = [{}] , signature = [{}]", dsName, point.getSignature());
            DynamicDataSourceContextHolder.setDataSourceName(dsName);
        } else if (DynamicDataSourceContextHolder.containsDataSourceGroup(dsName)) {

            logger.debug("Use DataSource name = [{}] , signature = [{}]", dsName, point.getSignature());
            DynamicDataSourceContextHolder.setDataSourceGroup(groupName, dsName, balanceType);
        } else {

            logger.error("DataSource [{}] not existing, Used default datasource [{}]", dsName, point.getSignature());
        }
    }

    @After("@annotation(targetDataSource)")
    public void restoreDataSource(JoinPoint point, TargetDataSource targetDataSource) {
        String dsName = targetDataSource.value();

        //注解未分组配置
        if (StringUtils.isEmpty(dsName)) {
            dsName = targetDataSource.groupId();
        }

        // db未分组配置
        if (StringUtils.isEmpty(dsName)) {
            dsName = DynamicDbSource.get();

            //db分组配置
            if (StringUtils.isEmpty(dsName)) {
                dsName = DynamicDbSource.getGroupDataSource().getGroupId();
            }
        }

        if (!StringUtils.isEmpty(dsName)) {
            logger.debug("Revert DataSource = [{}] ,  signature = [{}]", dsName, point.getSignature());
            DynamicDataSourceContextHolder.clearDataSource();
            DynamicDbSource.remove();
        }
    }

}
