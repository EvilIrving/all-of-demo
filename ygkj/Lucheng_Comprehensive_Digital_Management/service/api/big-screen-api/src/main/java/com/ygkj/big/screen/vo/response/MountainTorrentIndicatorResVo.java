package com.ygkj.big.screen.vo.response;

import com.ygkj.big.screen.model.ResultsWarnIndicator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/7/5
 */
@Data
@ApiModel("山洪防治指标数据")
public class MountainTorrentIndicatorResVo extends ResultsWarnIndicator {
    private static final long serialVersionUID = 7035875288223212147L;

    @ApiModelProperty("实时降雨")
    private String realTimeRainfall;

    private Integer warnGradeID;

    private double overThresholdVal;

    Date tm;
}
