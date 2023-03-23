package com.ygkj.data.management.vo.request;

import com.ygkj.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("数据管理系统用户角色关联新增入参")
public class SysDataManagementUserRoleAddVo {

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("角色id")
    private String roleId;

    @ApiModelProperty("业务类型 BASIC 基础数据  REALTIME 实时数据")
    private String busType;

    public String getBusType() {
        return StringUtils.isEmpty(this.busType) ? "BASIC" : this.busType;
    }
}
