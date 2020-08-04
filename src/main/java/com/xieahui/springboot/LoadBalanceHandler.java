package com.xieahui.springboot;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by xiehui1956(@)gmail.com on 2020/8/4
 */
public class LoadBalanceHandler<T> implements InvocationHandler {

    private final LoadBalance<T> loadBalance;

    public LoadBalanceHandler(LoadBalance<T> loadBalance) {
        this.loadBalance = loadBalance;
    }

    @Override
    public T invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object invoke = method.invoke(this.loadBalance, args);
        return (T) invoke;
    }
}
