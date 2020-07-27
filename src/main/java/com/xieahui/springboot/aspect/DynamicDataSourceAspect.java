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
        String dsName = targetDataSource.value();
        dsName = StringUtils.isEmpty(dsName) ? DynamicDbSource.get() : dsName;
        if (!DynamicDataSourceContextHolder.containsDataSource(dsName)) {
            logger.error("DataSource [{}] not existing, Used default datasource [{}]", targetDataSource.value(), point.getSignature());
        } else {
            logger.debug("Use DataSource name = [{}] , signature = [{}]", dsName, point.getSignature());
            DynamicDataSourceContextHolder.setDataSourceType(dsName);
        }
    }

    @After("@annotation(targetDataSource)")
    public void restoreDataSource(JoinPoint point, TargetDataSource targetDataSource) {
        String dsName = targetDataSource == null ?
                DynamicDbSource.get() : StringUtils.isEmpty(targetDataSource.value()) ?
                DynamicDbSource.get() : targetDataSource.value();
        logger.debug("Revert DataSource = [{}] ,  signature = [{}]", dsName, point.getSignature());
        DynamicDataSourceContextHolder.clearDataSourceType();
        DynamicDbSource.remove();
    }

}
