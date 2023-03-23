package com.ygkj.big.screen.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/5/10
 */
@Data
@ApiModel("积水点查询-入参")
public class FloodPointReqVo {
    @ApiModelProperty("名称，多个名称用半角逗号隔开")
    String name;

    @ApiModelProperty("往前推天数")
    Integer recentDays;

    @ApiModelProperty("行政区划编码")
    String areaCode;

    @ApiModelProperty("数据时间")
    Date dataTime;

    @ApiModelProperty("是否有积水")
    Boolean ponding;

    @ApiModelProperty("排序column列名")
    String orderColumn;

    @ApiModelProperty("升序降序,false asc/true desc")
    Boolean ascOrDesc = false;
}
