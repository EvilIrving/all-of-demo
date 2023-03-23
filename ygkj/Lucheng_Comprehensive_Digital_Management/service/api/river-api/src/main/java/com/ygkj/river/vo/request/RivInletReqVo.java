package com.ygkj.river.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/8/6
 */
@Data
@ApiModel("雨水口/排污口 查询入参")
public class RivInletReqVo {

    @ApiModelProperty("道路名称")
    String road;

    @ApiModelProperty("行政区划编码")
    String areaCode;

    @ApiModelProperty("1.雨水口，2. 排污口")
    Integer recordType;

}
