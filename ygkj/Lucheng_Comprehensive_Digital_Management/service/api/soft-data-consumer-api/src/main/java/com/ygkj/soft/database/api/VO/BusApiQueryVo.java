package com.ygkj.soft.database.api.VO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @描述 接口参数操作类
 * @参数
 * @返回值
 * @创建人 wd
 * @创建时间 2020/9/17 21:26
 * @修改人和其它信息
 */

@Data
@ApiModel("接口信息查询实体")
public class BusApiQueryVo {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("接口名称")
    private String apiName;

    @ApiModelProperty("对外地址")
    private String outUrl;

    @ApiModelProperty("接口状态 1-启用 2-停用")
    private String apiStatus;

    @ApiModelProperty("当前页")
    private Integer page;
    @ApiModelProperty("每页规定记录数")
    private Integer rows;

    @ApiModelProperty("所属部门")
    private String belongDept;

    @ApiModelProperty("创建人")
    private String createdBy;

    @ApiModelProperty("分类ID")
    private String categoryId;

    @ApiModelProperty("接口类型 1-服务接口 2-数据接口")
    private Integer apiType;

}
