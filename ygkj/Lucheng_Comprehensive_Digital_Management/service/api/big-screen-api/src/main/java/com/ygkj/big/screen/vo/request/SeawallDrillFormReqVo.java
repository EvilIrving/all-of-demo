package com.ygkj.big.screen.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/7/2
 */
@Data
@ApiModel("海塘演练表单入参")
public class SeawallDrillFormReqVo {
    @ApiModelProperty("演练潮位时刻，格式为 yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date dataTime;

    @ApiModelProperty("演练潮位")
    double drillTdz;

    @ApiModelProperty("演练潮位站名称")
    String stName;

    @ApiModelProperty("演练潮位编码")
    String stCode;
}
