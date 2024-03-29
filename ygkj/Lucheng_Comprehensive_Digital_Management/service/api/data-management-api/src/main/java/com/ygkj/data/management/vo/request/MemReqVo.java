package com.ygkj.data.management.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("內存信息入参")
public class MemReqVo implements Serializable {

    @ApiModelProperty("页数")
    private Integer pageSize;

    @ApiModelProperty("第几页")
    private Integer pageNum;

    @ApiModelProperty("查询开始时间 格式：yyyy-MM-dd HH")
    private String startTime;

    @ApiModelProperty("结束时间 格式：yyyy-MM-dd HH")
    private String endTime;

}
