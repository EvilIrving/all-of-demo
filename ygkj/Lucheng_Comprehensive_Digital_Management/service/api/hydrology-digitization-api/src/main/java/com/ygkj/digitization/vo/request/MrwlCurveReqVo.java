package com.ygkj.digitization.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/6/22
 */
@Data
@ApiModel("山区河道水位流量曲线入参")
public class MrwlCurveReqVo {

    @ApiModelProperty("测站编码")
    String stCode;

    @ApiModelProperty("曲线名称")
    String lnnm;

    @ApiModelProperty("数据时间，格式为：yyyy-MM-dd 00:00:00 精度为天")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date dataTime;

    Integer hours;
}
