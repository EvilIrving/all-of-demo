package com.ygkj.soft.database.api.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *@描述   接口参数操作类
 *@参数
 *@返回值
 *@创建人  wd
 *@创建时间  2020/9/17 21:26
 *@修改人和其它信息
 */

@Data
@ApiModel("接口审批查询实体")
public class ApiApplyQueryVo {

    @ApiModelProperty("接口名称")
    private String apiName ;

    @ApiModelProperty("申请状态 0-待审核 1-审核通过 2-驳回")
    private Integer applyStatus;

    @ApiModelProperty("接口类型 1-服务接口 2-数据接口")
    private Integer apiType ;

    @ApiModelProperty("分类ID")
    private String categoryId ;

    @ApiModelProperty(value = "当前页")
    private Integer page;

    @ApiModelProperty("每页规定记录数")
    private Integer rows;
}
