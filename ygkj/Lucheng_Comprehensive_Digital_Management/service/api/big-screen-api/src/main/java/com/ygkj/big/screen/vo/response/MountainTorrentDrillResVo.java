package com.ygkj.big.screen.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/7/5
 */
@Data
@ApiModel("山洪演练回参")
public class MountainTorrentDrillResVo implements Serializable {

    private static final long serialVersionUID = 6239880357244928316L;

    @ApiModelProperty("报警山洪村庄对象")
    List<MountainTorrentDetailResVo> warnList;

    @ApiModelProperty("正常山洪村庄对象")
    List<MountainTorrentDetailResVo> normList;

    @ApiModelProperty("统计结果")
    MountainTorrentWarnStatsResVo warnStats;

    String type = "山洪";

    int warnNum;


}
