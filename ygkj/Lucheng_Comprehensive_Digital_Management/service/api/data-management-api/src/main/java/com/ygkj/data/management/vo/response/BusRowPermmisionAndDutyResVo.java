package com.ygkj.data.management.vo.response;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 行数据相关角色及责任人
 */
@Data
@ApiModel("数行数据相关角色及责任人")
public class BusRowPermmisionAndDutyResVo {

    @ApiModelProperty("表角色用户")
    private List<DataRoleUserResVo> tableRoleUser;

    @ApiModelProperty("行角色用户")
    private List<DataRoleUserResVo> rowRoleUser;

    @ApiModelProperty("列角色用户")
    private List<DataRoleUserResVo> columnRoleUser;

    private String deptIds;
}
