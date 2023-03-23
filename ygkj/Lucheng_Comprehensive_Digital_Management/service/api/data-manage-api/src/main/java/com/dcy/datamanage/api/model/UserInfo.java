package com.dcy.datamanage.api.model;

import lombok.Data;

@Data
public class UserInfo {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 部门id
     */
    private String deptId;

    /**
     * 用户类型 1管理员 2科室管理员 3普通用户
     */
    private String roleType;
}
