package com.ygkj.project.vo.response;

import com.ygkj.gragh.model.StRsvrR;
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
@ApiModel("水库复盘回参")
public class RsvrReplayResVo extends RsvrStationWaterResVo {

    @ApiModelProperty("是否超警")
    boolean overWarn = false;

    @ApiModelProperty("超警时长")
    Integer overWarnMinutes;

    @ApiModelProperty("最高水位")
    Double highestWaterLevel;

    @ApiModelProperty("最高水位时刻")
    Date highestWaterLevelTm;

    @ApiModelProperty("最高水位对应库容")
    Double highestCapacity;

    @ApiModelProperty("最低水位")
    Double lowestWaterLevel;

    @ApiModelProperty("最低水位时刻")
    Date lowestWaterLevelTm;

    @ApiModelProperty("最低水位对应库容")
    Double lowestCapacity;

    @ApiModelProperty("拦蓄水量")
    Double capacitySub;

    @ApiModelProperty("起始时刻水位")
    Double startWaterLevel;

    @ApiModelProperty("起始水位时刻")
    Date startWaterLevelTm;

    @ApiModelProperty("起始水位对应库容")
    Double startCapacity;

    @ApiModelProperty("结束水位")
    Double endWaterLevel;

    @ApiModelProperty("结束水位时刻")
    Date endWaterLevelTm;

    @ApiModelProperty("结束水位对应库容")
    Double endCapacity;

    @ApiModelProperty("增蓄水量")
    Double startEndCapacitySub;

    @ApiModelProperty("泄洪量")
    Double drainage = 0D;

    @ApiModelProperty("水位列表")
    List<StRsvrR> waterLevelList;
}
