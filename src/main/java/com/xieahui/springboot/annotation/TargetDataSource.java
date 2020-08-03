package com.xieahui.springboot.annotation;

import com.xieahui.springboot.config.Contains;

import java.lang.annotation.*;

/**
 * 在方法上使用，用于指定使用哪个数据源
 * Created by xiehui1956(@)gmail.com on 2020/5/17
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {

    /**
     * 默认dataSource名称
     *
     * @return
     */
    String value() default "";

    /**
     * 组名称
     * 指定使用哪个分组
     * PS: 使用分组策略中,该字段必传。切不可重复,重复分组名只去第一个
     *
     * @return
     */
    String groupName() default "";

    /**
     * 分组连接
     * 指定使用分组中的那个链接
     *
     * @return
     */
    String groupId() default "";

    /**
     * 指定使用负载均衡策略
     * PS: 默认使用轮询策略
     *
     * @return
     */
    String balanceType() default Contains.LoadBalanceType.ROUND_ROBIN;

}
