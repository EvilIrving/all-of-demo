package com.ygkj.data.management.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("数据角色用户")
public class DataRoleUserResVo {

    @ApiModelProperty("列名")
    private String colName;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("拥有该角色的人")
    private String roleUsers;


    private String deptIds;
}
