package com.ygkj.data.management.vo.request;

import com.ygkj.entity.PageEntity;
import com.ygkj.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("数据管理系统权限查询参数封装")
public class SysDataManagementRoleQueryVo extends PageEntity {

    @ApiModelProperty("id，查询单条用")
    private String id;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("部门id")
    private String deptId;

    @ApiModelProperty("业务类型 BASIC 基础数据  REALTIME 实时数据")
    private String busType;

    public String getBusType() {
        return StringUtils.isEmpty(this.busType) ? "BASIC" : this.busType;
    }


}
