package com.xieahui.springboot;

/**
 * 定义负载均衡类型
 * <p>
 * Created by xiehui1956(@)gmail.com on 2020/8/3
 */
public enum LoadBalanceType {

    /**
     * 随机
     */
    RANDOM,

    /**
     * 轮询
     */
    ROUND_ROBIN
}
