package com.ygkj.big.screen.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/8/1
 */
@Data
@ApiModel("山洪基础信息统计回参")
public class MountainTorrentBaseInfoStats {

    @ApiModelProperty("涉及县市区")
    Integer involveCounty = 0;

    @ApiModelProperty("防治乡镇")
    Integer prevTown = 0;

    @ApiModelProperty("防治村落")
    Integer prevVillage = 0;

    @ApiModelProperty("影响人口")
    Integer effectPop = 0;

    @ApiModelProperty("防治区域")
    Integer prevAreaNum = 0;

    @ApiModelProperty("危险区")
    Integer dangerArea = 0;

    @ApiModelProperty("高危险区")
    Integer highDangerArea = 0;

    @ApiModelProperty("极高危险区")
    Integer superDangerArea = 0;

}
