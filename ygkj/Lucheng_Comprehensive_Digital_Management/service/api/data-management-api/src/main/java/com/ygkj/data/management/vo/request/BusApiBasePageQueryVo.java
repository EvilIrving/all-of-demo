package com.ygkj.data.management.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class BusApiBasePageQueryVo {

    @ApiModelProperty("单页限定记录数")
    private Integer pageSize;

    @ApiModelProperty("页码")
    private Integer pageNum;

    @ApiModelProperty("接口分类")
    private String apiCategory;

    @ApiModelProperty("接口类别")
    private String apiState;

    @ApiModelProperty("接口对外发布地址")
    private String releaseMethod;

    @ApiModelProperty("接口名称，对应搜索框的模糊搜索")
    private String apiName;
}
