package com.dcy.datamanage.biz.enums;

import lombok.Getter;

@Getter
public enum DataManageRoleEnum {

    ADMIN("管理员","1"),
    DEPT_ADMIN("科室管理员","2"),
    GUEST("普通用户","3");

    private String roleName;

    private String roleType;

    DataManageRoleEnum(String roleName, String roleType){
        this.roleName = roleName;
        this.roleType = roleType;
    }

    public static String typeByRole(String roleName){
        for (DataManageRoleEnum dataManageRoleEnum:values()){
            if(dataManageRoleEnum.roleName.equals(roleName)){
                return dataManageRoleEnum.roleType;
            }
        }
        return "";
    }
}
