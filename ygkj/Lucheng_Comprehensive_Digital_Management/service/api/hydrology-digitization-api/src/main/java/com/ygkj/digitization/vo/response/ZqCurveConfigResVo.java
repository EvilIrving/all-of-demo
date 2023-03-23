package com.ygkj.digitization.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/6/21
 */
@Data
@ApiModel("河道水位流量曲线配置列表回参")
public class ZqCurveConfigResVo {
    @ApiModelProperty("测站编码")
    String mgstcd;

    @ApiModelProperty("测站名称")
    String stName;

    @ApiModelProperty("曲线名称")
    String lnnm;

    @ApiModelProperty("起始时间")
    Date bgtm;

    @ApiModelProperty("是否是最新的")
    boolean newest;
}
