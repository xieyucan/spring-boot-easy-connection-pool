package com.xieyucan.springboot.annotation;

import java.lang.annotation.*;

/**
 * 在方法上使用，用于指定使用哪个数据源
 * Created by xiehui1956(@)gmail.com on 2020/5/17
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDataSource {
    String value();
}
