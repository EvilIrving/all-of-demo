package com.ygkj.gragh.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/6/3
 */
@Data
@ApiModel("潮位复盘入参")
public class TideReplayReqVo {
    @ApiModelProperty("潮位测站编码")
    String stCode;

    @ApiModelProperty("潮位测站名称")
    String stName;

    @ApiModelProperty("行政区划编码")
    String adcd;

    @ApiModelProperty("流域")
    String bas;

    @ApiModelProperty("复盘时刻：格式是\"yyyy-MM-dd HH:mm:ss\"，不传则为最新值")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date start;

    @ApiModelProperty("复盘时刻：格式是\"yyyy-MM-dd HH:mm:ss\"，不传则为最新值")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date end;

    @ApiModelProperty("排序column列名")
    String orderColumn;

    @ApiModelProperty("升序降序,false asc/true desc")
    boolean ascOrDesc = false;

    @ApiModelProperty("是否带潮位曲线")
    boolean tideCurve = false;
}
