package com.ygkj.data.management.vo.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("数据管理系统权限返回参数封装")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class SysDataManagementRoleResVo {

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 角色名称
     */
    @ApiModelProperty("角色名称")
    private String roleName;

    /**
     * 角色简介
     */
    @ApiModelProperty("角色简介")
    private String description;

    /**
     * 部门id，关联到main_db中的系统部门表sys_dept
     */
    @ApiModelProperty("部门id，关联到main_db中的系统部门表sys_dept")
    private String deptId;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("拥有该权限的人")
    private String roleUser;

    @ApiModelProperty("查询权限1-是 0-否")
    private Boolean operQuery;
    @ApiModelProperty("新增权限1-是 0-否")
    private Boolean operCreate;
    @ApiModelProperty("修改权限1-是 0-否")
    private Boolean operUpdate;
    @ApiModelProperty("删除权限1-是 0-否")
    private Boolean operDelete;

    @ApiModelProperty("业务类型 BASIC 基础数据  REALTIME 实时数据")
    private String busType;


    /**
     * 下一步操作限定的角色id，用于审批流程
     */
    @ApiModelProperty("下一步操作限定的角色id，用于审批流程")
    private String nextId;

    private Boolean marked = Boolean.FALSE;

    private SysDataManagementRoleResVo nextRole;
}
