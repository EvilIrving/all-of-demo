package com.ygkj.project.vo.response;

import com.ygkj.gragh.model.StRsvrR;
import com.ygkj.gragh.model.StWasR;
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
@ApiModel("水闸复盘回参")
public class WagaReplayResVo extends WagaStationWaterResVo {

    @ApiModelProperty("最高闸上水位")
    Double highestUpz;

    @ApiModelProperty("最高闸上水位时间")
    Date highestUpzTm;

    @ApiModelProperty("最低闸上水位")
    Double lowestUpz;

    @ApiModelProperty("最低闸上水位时间")
    Date lowestUpzTm;

    @ApiModelProperty("开闸时长")
    Integer openGateMinutes;

    @ApiModelProperty("泄流量")
    Double drainage;

    @ApiModelProperty("工情列表")
    List<StWasR> waterLevelList;
}
