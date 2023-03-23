package com.ygkj.big.screen.vo.response;

import com.ygkj.big.screen.model.*;
import com.ygkj.big.screen.model.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 地图点位
 */
@Data
public class RpMapPointsRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("水源地")
    private List<AttSwhsBase> swhsBases;

    @ApiModelProperty("水库")
    private List<AttResBase> resBases;

    @ApiModelProperty("3水质监测")
    private List<AttCwsstationBase> cwsStas;

    @ApiModelProperty("水位监测")
    private List<AttStBase> waterStas;

    @ApiModelProperty("雨量监测")
    private List<AttStBase> rainStas;

    @ApiModelProperty("河湖全景图")
    private List<RiverUnionInfo> ruInfos;

    @ApiModelProperty("摄像机")
    private List<HikvisionCameraRecord> cameraRecords;

    @ApiModelProperty("四乱点位")
    private List<WtDynProblem> dynProblems;
}
