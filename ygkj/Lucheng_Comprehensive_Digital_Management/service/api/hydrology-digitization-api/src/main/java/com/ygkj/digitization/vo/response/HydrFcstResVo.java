package com.ygkj.digitization.vo.response;

import com.ygkj.digitization.model.AttHydrFcstBase;
import com.ygkj.digitization.model.AttHydrFcstValBase;
import com.ygkj.enums.StationType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author xq
 * @Description
 * @Date 2021/9/22
 */
@ApiModel("水文预报回参")
@Data
public class HydrFcstResVo extends AttHydrFcstBase {

    private static final long serialVersionUID = 1616917279770128614L;

    /**
     * 水文预报值
     */
    @ApiModelProperty("水文预报值")
    List<AttHydrFcstValBase> hydrFcstValList;

    /**
     * 水文监测站经度
     */
    @ApiModelProperty("水文监测站经度")
    private BigDecimal stLong;

    /**
     * 水文监测站维度
     */
    @ApiModelProperty("水文监测站维度")
    private BigDecimal stLat;

    /**
     * 汛限水位
     */
    @ApiModelProperty("汛限水位")
    private Double limitLevel;

    /**
     * 防洪高水位
     */
    @ApiModelProperty("防洪高水位")
    private Double floodLevel;

    /**
     * 保证水位
     */
    @ApiModelProperty("保证水位")
    private Double guaranteeLevel;

    /**
     * 警戒水位
     */
    @ApiModelProperty("警戒水位")
    private Double warningLevel;

    @ApiModelProperty("警戒状态：0正常,1超警戒/超汛限,2超保证/超防洪高")
    private Integer warnState;

    public void buildWarnState() {
        Integer warnState = 0;
        if (StationType.RESERVOIR.type().equals(super.getStType())) {
            if (super.getPeakLevel() != null) {
                if (floodLevel != null && floodLevel > 0D) {
                    if (super.getPeakLevel().doubleValue() > floodLevel) {
                        warnState = 2;
                    }
                }
                if (limitLevel != null && limitLevel > 0D) {
                    if (super.getPeakLevel().doubleValue() > limitLevel) {
                        warnState = 1;
                    }
                }
            }

        } else if (StationType.RIVER.type().equals(super.getStType())) {
            if (guaranteeLevel != null && guaranteeLevel > 0D) {
                if (super.getPeakLevel().doubleValue() > guaranteeLevel) {
                    warnState = 2;
                }
            }
            if (warningLevel != null && warningLevel > 0D) {
                if (super.getPeakLevel().doubleValue() > warningLevel) {
                    warnState = 1;
                }
            }
        }
        this.warnState = warnState;
    }

}
