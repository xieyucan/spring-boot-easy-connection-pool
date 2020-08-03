package com.xieahui.springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 轮询
 * Created by xiehui1956(@)gmail.com on 2020/8/1
 */
public class RoundRobinLoadBalance implements LoadBalance {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public List<String> dataSourceIds;

    public static final Map<String, AtomicInteger> SEQUENCE = new ConcurrentHashMap();

    private String groupName;

    public RoundRobinLoadBalance(List<String> dataSourceIds, String groupName) {
        this.dataSourceIds = dataSourceIds;
        this.groupName = groupName;
        if (!SEQUENCE.containsKey(groupName))
            SEQUENCE.put(groupName, new AtomicInteger());
    }

    @Override
    public String select() {
        AtomicInteger staticWeightSequence = SEQUENCE.get(groupName);
        List<String> localEnabledUrls = dataSourceIds;
        if (localEnabledUrls.isEmpty()) {
            Assert.notEmpty(localEnabledUrls, "Unable to get connection: there are no enabled dataSource");
        }
        String select = localEnabledUrls.get((staticWeightSequence.getAndIncrement() & Integer.MAX_VALUE) % localEnabledUrls.size());
        SEQUENCE.put(groupName, staticWeightSequence);
        logger.info("*** LoadBalance-RoundRobinLoadBalance Select {} Success! ***", select);
        return select;
    }
}
