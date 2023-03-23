package com.ygkj.big.screen.vo.response;

import com.ygkj.big.screen.model.MountainTorrentMonitorWarnValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.text.SimpleDateFormat;

/**
 * @author xq
 * @Description
 * @Date 2021/10/11
 */
@Data
@ApiModel("山洪预报监测统一兼容回参")
public class MtFcstMonWarnValueResVo {

    /**
     * 报警监测时刻
     */
    @ApiModelProperty("报警监测时刻")
    private String warnTm;

    /**
     * 监测指标id
     */
    @ApiModelProperty("监测指标id")
    private String indicatorId;

    /**
     * 实时降雨
     */
    @ApiModelProperty("实时降雨")
    private String rainfall;

    /**
     * 4，为准备转移，5为立即转移
     */
    @ApiModelProperty("4，为准备转移，5为立即转移")
    private String warnGradeId;

    /**
     * 超过临界值大小
     */
    @ApiModelProperty("超过临界值大小")
    private String overThresholdVal;

    /**
     * 降雨时刻
     */
    @ApiModelProperty("降雨时刻")
    private String tm;

    /**
     * 时段(h)
     */
    @ApiModelProperty("时段(h)")
    private String period;

    @ApiModelProperty("描述")
    private String desc;

    public MtFcstMonWarnValueResVo() {

    }

    public MtFcstMonWarnValueResVo(MountainTorrentMonitorWarnValue monitorValue) {
        if (monitorValue != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.warnTm = monitorValue.getWarnTm() == null ? null : sdf.format(monitorValue.getWarnTm());
            this.indicatorId = monitorValue.getIndicatorId();
            this.rainfall = monitorValue.getRealTimeRainfall() == null ? null : monitorValue.getRealTimeRainfall().toString();
            this.warnGradeId = monitorValue.getWarnGradeId();
            this.overThresholdVal = monitorValue.getOverThresholdVal() == null ? null : monitorValue.getOverThresholdVal().toString();
            this.tm = monitorValue.getTm() == null ? null : sdf.format(monitorValue.getTm());
            this.period = monitorValue.getPeriod() == null ? null : monitorValue.getPeriod().toString();
        }
    }

    public MtFcstMonWarnValueResVo(MountainTorrentIndicatorResVo monitorValue) {
        if (monitorValue != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.indicatorId = monitorValue.getId();
            this.rainfall = monitorValue.getRealTimeRainfall() == null ? null : monitorValue.getRealTimeRainfall();
            this.warnGradeId = monitorValue.getWarnGradeID() == null ? null : monitorValue.getWarnGradeID().toString();
            this.overThresholdVal = String.valueOf(monitorValue.getOverThresholdVal());
            this.tm = monitorValue.getTm() == null ? null : sdf.format(monitorValue.getTm());
            this.period = monitorValue.getPeriod() == null ? null : monitorValue.getPeriod().toString();
        }
    }

    public MtFcstMonWarnValueResVo(Villages villages) {
        if (villages != null) {
            this.warnTm = villages.getWarnTime();
            this.rainfall = villages.getForecastRainfall();
            this.warnGradeId = villages.getWarnGradeID();
            this.overThresholdVal = villages.getOverWarnIndex();
            this.tm = villages.getForecastTime();
            this.desc = villages.getDesc();
        }
    }

}
