package com.ygkj.platform.management.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class SysThresholdPageQueryVo extends PageEntity {

    @ApiModelProperty("人员名称")
    private String username;

    @ApiModelProperty("部门名称")
    private String department;

    @ApiModelProperty("查详情的时候传入")
    private Long id;

    @ApiModelProperty("前端忽略")
    private List<Long> ids;

    @ApiModelProperty("阈值类型：登录/菜单")
    private String thresholdType;

    @ApiModelProperty("菜单id")
    private String menuId;
}
