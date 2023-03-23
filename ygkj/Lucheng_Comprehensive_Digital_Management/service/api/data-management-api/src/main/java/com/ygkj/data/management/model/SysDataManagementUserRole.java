package com.ygkj.data.management.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author Huangzh
 * @description 用户-角色关联关系表（数据管理系统专用）
 * @date 2021-06-23
 */
@Data
@ApiModel("用户-角色关联关系")
public class SysDataManagementUserRole implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private Integer id;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    private String userId;

    /**
     * 角色id
     */
    @ApiModelProperty("角色id")
    private String roleId;

    public SysDataManagementUserRole() {
    }

}

