package com.ygkj.gragh.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/5/18
 */
@Data
@ApiModel("天文潮请求接口")
public class AstroTideReqVo {

    @ApiModelProperty("测站编码")
    String stcds;

    @ApiModelProperty("测站名称")
    String originStnm;

    @ApiModelProperty("起始时刻")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date start;

    @ApiModelProperty("结束时刻")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date end;
}
