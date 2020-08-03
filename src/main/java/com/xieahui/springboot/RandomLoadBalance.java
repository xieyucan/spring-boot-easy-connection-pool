package com.xieahui.springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Random;

/**
 * 随机
 * Created by xiehui1956(@)gmail.com on 2020/8/1
 */
public class RandomLoadBalance implements LoadBalance {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public List<String> dataSourceIds;

    private final ThreadLocal<Random> randomThreadLocal = new ThreadLocal();

    public RandomLoadBalance(List<String> dataSourceIds) {
        this.dataSourceIds = dataSourceIds;
    }

    @Override
    public String select() {
        List<String> localEnabledUrls = dataSourceIds;
        if (localEnabledUrls.isEmpty()) {
            Assert.notEmpty(localEnabledUrls, "Unable to get connection: there are no enabled dataSource");
        }
        Random random = this.randomThreadLocal.get();
        if (random == null) {
            this.randomThreadLocal.set(new Random());
            random = this.randomThreadLocal.get();
        }

        int index = random.nextInt(localEnabledUrls.size());
        String select = localEnabledUrls.get(index);
        logger.info("*** LoadBalance-RoundRobinLoadBalance Select {} Success! ***", select);
        return select;
    }
}
