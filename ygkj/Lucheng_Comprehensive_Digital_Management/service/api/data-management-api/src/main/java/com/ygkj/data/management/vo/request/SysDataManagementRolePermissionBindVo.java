package com.ygkj.data.management.vo.request;

import lombok.Data;

@Data
public class SysDataManagementRolePermissionBindVo {

    private String roleId;

    private String permissionId;

    public SysDataManagementRolePermissionBindVo() {
    }

    public SysDataManagementRolePermissionBindVo(String roleId, String permissionId) {
        this.roleId = roleId;
        this.permissionId = permissionId;
    }
}
