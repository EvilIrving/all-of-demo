package com.ygkj.big.screen.vo.request;

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
public class RsvrReplayReqVo {
    @ApiModelProperty("水库编号，多个水库编号用半角逗号隔开")
    String resCodes;

    @ApiModelProperty("水库编号，多个水库编号用半角逗号隔开")
    String resName;

    @ApiModelProperty("测站测量时间点")
    String start;

    @ApiModelProperty("测站测量时间点")
    String end;

    @ApiModelProperty("工程行政区划编码")
    String resAdcd;

    @ApiModelProperty("流域：飞云江、瓯江、鳌江")
    String resBas;

    @ApiModelProperty("水库工程等级：大（Ⅰ）型、大（Ⅱ）型、中型、小（Ⅰ）型、小（Ⅱ）型")
    String engScales;

    @ApiModelProperty("排序column列名")
    String orderColumn;

    @ApiModelProperty("升序降序,false asc/true desc")
    Boolean ascOrDesc = false;

    @ApiModelProperty("返回值是否带水位曲线")
    Boolean waterLevelCurve = false;

    @ApiModelProperty("起始时刻水位库容")
    boolean startWaterLevelCapacity = false;

    @ApiModelProperty("结束时刻水位库容")
    boolean endWaterLevelCapacity = false;

    @ApiModelProperty("最高水位对应库容")
    boolean highestWaterLevelCapacity = false;

    @ApiModelProperty("最低水位对应库容")
    boolean lowestWaterLevelCapacity = false;
}
