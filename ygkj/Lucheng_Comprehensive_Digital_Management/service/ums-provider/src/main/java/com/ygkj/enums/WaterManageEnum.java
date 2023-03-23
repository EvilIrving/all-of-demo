package com.ygkj.enums;

public enum WaterManageEnum {

    /**
     * 公共前缀
     */
    BASE("https://sgpt.zjwater.com/service/v1"),

    /**
     * 获取token
     */
    TOKEN("/token/getAccessToken"),

    /**
     * 获取所有部门
     */
    ORG_SCOPE("/api/102/auth/getOrgScope"),

    /**
     * 根据部门ID查询部门详细信息
     */
    DEPT("/api/102/dept/getDeptInfo", "deptId"),

    /**
     * 根据上级部门获取所有的下级部门信息
     */
    SUB_DEPT("/api/102/dept/listAllSubDepts", "parentId"),

    /**
     * 获取部门用户
     */
    DEPT_USER("/api/102/user/listDeptUser", "deptId"),

    /**
     * 获取用户信息
     */
    USER("/api/102/user/getUserInfo", "userCode");


    private String url;
    private String paramKey;

    WaterManageEnum(String url) {
        this.url = url;
    }

    WaterManageEnum(String url, String paramKey) {
        this.url = url;
        this.paramKey = paramKey;
    }

    public String getUrl() {
        return url;
    }

    public String getParamKey() {
        return paramKey;
    }
}
