package com.ygkj.big.screen.vo.request;

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
@ApiModel("河网复盘入参")
public class RivernetReplayReqVo {
    @ApiModelProperty("河网名称")
    String rnName;

    @ApiModelProperty("行政区划编码")
    String adcd;

    @ApiModelProperty("流域")
    String bas;

    @ApiModelProperty("复盘时刻：格式是\"yyyy-MM-dd HH:mm:ss\"，不传则为最新值")
    String start;

    @ApiModelProperty("复盘时刻：格式是\"yyyy-MM-dd HH:mm:ss\"，不传则为最新值")
    String end;

    @ApiModelProperty("排序column列名")
    String orderColumn;

    @ApiModelProperty("升序降序,false asc/true desc")
    Boolean ascOrDesc = false;

    @ApiModelProperty("是否带水位曲线")
    Boolean waterLevelCurve = false;
}
