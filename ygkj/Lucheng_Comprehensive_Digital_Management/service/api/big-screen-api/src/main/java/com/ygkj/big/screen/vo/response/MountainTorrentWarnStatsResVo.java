package com.ygkj.big.screen.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/7/6
 */
@Data
@ApiModel("山洪报警统计回参")
public class MountainTorrentWarnStatsResVo {
    @ApiModelProperty("准备转移区县")
    Integer grade4County = 0;

    @ApiModelProperty("准备转移村落")
    Integer grade4villages = 0;

    @ApiModelProperty("准备转移乡镇")
    Integer grade4street = 0;

    @ApiModelProperty("准备转移人口")
    Integer grade4Person = 0;

    @ApiModelProperty("立即转移区县")
    Integer grade5County = 0;

    @ApiModelProperty("立即转移村落")
    Integer grade5villages = 0;

    @ApiModelProperty("立即转移乡镇")
    Integer grade5street = 0;

    @ApiModelProperty("立即转移人口")
    Integer grade5Person = 0;

    @ApiModelProperty("影响乡镇")
    Integer gradevillages = 0;

    List<ResultsWarnVo> town;

    String promptlyMoveVillage;

    String effectPeople;

    String warningTown;

    String warningMoveVillage;

    Object rows;
}
