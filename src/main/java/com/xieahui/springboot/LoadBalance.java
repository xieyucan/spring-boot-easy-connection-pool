package com.xieahui.springboot;

/**
 * Created by xiehui1956(@)gmail.com on 2020/8/1
 */
public interface LoadBalance<T> {

    /**
     * 获取分组连接
     *
     * @return
     */
    T select();
}
