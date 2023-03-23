package com.ygkj.gragh.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/5/29
 */
@Data
@ApiModel("河网复盘回参")
public class RivernetReplayResVo extends RivernetWaterLevelResVo {
    @ApiModelProperty("是否超警")
    boolean overWarn = false;

    @ApiModelProperty("超警时长")
    Integer overWarnMinutes;

    @ApiModelProperty("最高水位")
    Double highestWaterLevel;

    @ApiModelProperty("最高水位")
    Date highestWaterLevelTm;

    @ApiModelProperty("最低水位")
    Double lowestWaterLevel;

    @ApiModelProperty("最低水位")
    Date lowestWaterLevelTm;

    @ApiModelProperty("水情列表")
    List<StPrrwtResVo> waterLevelList;
}
