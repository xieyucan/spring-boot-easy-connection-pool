package com.xieahui.springboot;

import com.xieahui.springboot.annotation.TargetDataSource;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * Created by xiehui1956(@)gmail.com on 2020/8/3
 */
public final class GroupDataSource implements Serializable {

    private String groupName;

    private String groupId;

    private String balanceType;

    public GroupDataSource() {
    }

    public GroupDataSource(String groupId) {
        this.groupId = groupId;
    }

    public GroupDataSource(String groupName, String groupId, String balanceType) {
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

    public String getBalanceType() {
        if (StringUtils.isEmpty(balanceType))
            return TargetDataSource.LoadBalanceType.ROUND_ROBIN;
        return balanceType;
    }

    public void setBalanceType(String balanceType) {
        this.balanceType = balanceType;
    }
}
