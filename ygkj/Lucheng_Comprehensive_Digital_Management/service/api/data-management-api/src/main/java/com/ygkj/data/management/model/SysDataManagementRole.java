package com.ygkj.data.management.model;

import com.ygkj.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Huangzh
 * @description 数据管理系统专用角色表
 * @date 2021-06-23
 */
@Data
@ApiModel("数据管理系统专用角色表")
public class SysDataManagementRole implements Serializable {

    private static final long serialVersionUID = 1L;

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

    /**
     * 下一步操作限定的角色id，用于审批流程
     */
    @ApiModelProperty("下一步操作限定的角色id，用于审批流程")
    private String nextId;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createId;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String updateId;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    @ApiModelProperty("是否删除 0-未删除 1-已删除")
    private boolean delFlag;

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

    public String getBusType() {
        return StringUtils.isEmpty(this.busType) ? "BASIC" : this.busType;
    }

    public SysDataManagementRole() {
    }

}

