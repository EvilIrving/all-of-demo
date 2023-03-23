package com.ygkj.data.management.vo.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class SysDataManagementRoleReverseResVo {

    /**
     * id
     */
    private String id;

    /**
     * 角色名称
     */
    private String roleName;

    /**
     * 角色简介
     */
    private String description;

    /**
     * 部门id，关联到main_db中的系统部门表sys_dept
     */
    private String deptId;


    /**
     * 下一步操作限定的角色id，用于审批流程
     */
    private String nextId;

    /**
     * 上一步角色
     */
    private List<SysDataManagementRoleReverseResVo> previousRoles;
}
