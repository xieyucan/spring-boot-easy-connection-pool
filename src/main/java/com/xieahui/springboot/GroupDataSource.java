package com.xieahui.springboot;

import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * Created by xiehui1956(@)gmail.com on 2020/8/3
 */
public final class GroupDataSource implements Serializable {

    /**
     * 分组名
     */
    private String groupName;

    /**
     * 连接池名称-poolName
     */
    private String groupId;

    /**
     * 负载均衡类型
     */
    private LoadBalanceType balanceType;

    public GroupDataSource() {
    }

    public GroupDataSource(String groupId) {
        this.groupId = groupId;
    }

    public GroupDataSource(String groupName, String groupId, LoadBalanceType balanceType) {
        this.groupName = groupName;
        this.groupId = groupId;
        this.balanceType = balanceType;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public LoadBalanceType getBalanceType() {
        if (StringUtils.isEmpty(balanceType))
            return LoadBalanceType.ROUND_ROBIN;
        return balanceType;
    }

    public void setBalanceType(LoadBalanceType balanceType) {
        this.balanceType = balanceType;
    }
}
