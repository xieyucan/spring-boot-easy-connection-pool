package com.xieahui.springboot.annotation;

import com.xieahui.springboot.aspect.DynamicDataSourceAspect;
import com.xieahui.springboot.config.DynamicDataSourceRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启动态数据源支持
 * Created by xiehui1956(@)gmail.com on 2020/6/14
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({DynamicDataSourceRegister.class, DynamicDataSourceAspect.class})
public @interface EnableDynamicDataSource {
}
