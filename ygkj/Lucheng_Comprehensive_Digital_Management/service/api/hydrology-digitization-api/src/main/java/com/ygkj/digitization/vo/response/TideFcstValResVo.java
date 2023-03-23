package com.ygkj.digitization.vo.response;

import com.ygkj.digitization.model.AttTideFcstValBase;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author xq
 * @Description
 * @Date 2021/9/22
 */
@Api(tags = "风暴潮预报值回参")
@Data
public class TideFcstValResVo extends AttTideFcstValBase {
    private static final long serialVersionUID = -2930359307909973406L;

    /**
     * 测站名称
     */
    @ApiModelProperty("测站名称")
    private String stName;

    /**
     * 测站类型
     */
    @ApiModelProperty("测站类型")
    private String stType;

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

    @ApiModelProperty("蓝色警戒水位")
    private Double blueLevel;

    @ApiModelProperty("黄色警戒水位")
    private Double yellowLevel;

    @ApiModelProperty("橙色警戒水位")
    private Double orangeLevel;

    @ApiModelProperty("红色警戒水位")
    private Double redLevel;

    @ApiModelProperty("警戒状态：0正常,1蓝色,2黄色,3橙色,4红色")
    private Integer warnState;

    public void buildWarnState() {
        int warnState = 0;
        if (super.getTdz() != null) {
            if (this.getRedLevel() != null && super.getTdz().doubleValue() >= this.getRedLevel()) {
                warnState = 4;
            } else if (this.getOrangeLevel() != null && super.getTdz().doubleValue() >= this.getOrangeLevel()) {
                warnState = 3;
            } else if (this.getYellowLevel() != null && super.getTdz().doubleValue() >= this.getYellowLevel()) {
                warnState = 2;
            } else if (this.getBlueLevel() != null && super.getTdz().doubleValue() >= this.getBlueLevel()) {
                warnState = 1;
            }
        }
        this.setWarnState(warnState);
    }
}
