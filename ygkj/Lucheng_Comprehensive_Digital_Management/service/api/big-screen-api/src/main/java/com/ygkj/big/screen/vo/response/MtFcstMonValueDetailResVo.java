package com.ygkj.big.screen.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xq
 * @Description
 * @Date 2021/10/11
 */
@ApiModel("山洪预报监测细节回参")
@Data
public class MtFcstMonValueDetailResVo extends MountainTorrentDetailResVo {
    private static final long serialVersionUID = 6245250301119414595L;

    @ApiModelProperty("预报")
    MtFcstMonWarnValueResVo predictWarnValue;

    @ApiModelProperty("实时监测")
    MtFcstMonWarnValueResVo monitorWarnValue;

    @ApiModelProperty("过去N分钟监测")
    MtFcstMonWarnValueResVo pastMonitorWarnValue;

    @ApiModelProperty("过去N分钟")
    Integer pastMinutes;

    public boolean warned() {
        return predictWarnValue != null || monitorWarnValue != null || pastMonitorWarnValue != null;
    }
}
