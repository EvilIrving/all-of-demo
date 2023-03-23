package com.ygkj.data.management.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BusApiApplyPageQueryVo {

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("单页限定记录数")
    private Integer pageSize;

    @ApiModelProperty("页码")
    private Integer pageNum;

    @ApiModelProperty("接口分类")
    private String apiCategory;

    @ApiModelProperty("接口类别")
    private String apiState;

    @ApiModelProperty("前端忽略")
    private String applicantId;

    @ApiModelProperty("接口名称，对应搜索框的模糊搜索")
    private String apiName;

    @ApiModelProperty("接口审核状态")
    private String status;
}
