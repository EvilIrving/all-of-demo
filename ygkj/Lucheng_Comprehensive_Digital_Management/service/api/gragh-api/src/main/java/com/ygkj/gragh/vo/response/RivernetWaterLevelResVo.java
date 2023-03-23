package com.ygkj.gragh.vo.response;

import com.ygkj.gragh.model.AttRnBase;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/5/27
 */
@Data
@ApiModel("河网查询回参")
public class RivernetWaterLevelResVo extends AttRnBase {

    private static final long serialVersionUID = -5825947828659186709L;

    @ApiModelProperty("实时水位")
    Double waterLevel;

    @ApiModelProperty("超警戒值")
    Double overWarn;

    @ApiModelProperty("超保证值")
    Double overGuarantee;

    @ApiModelProperty("行政区划")
    String areaName;

    @ApiModelProperty("经度")
    BigDecimal longitude;

    @ApiModelProperty("纬度")
    BigDecimal latitude;

    @ApiModelProperty("实时库容")
    Double waterLevelCapacity;

    @ApiModelProperty("警戒水位对应库容")
    Double warnLevelCapacity;

    /**
     * 纳洪能力
     */
    @ApiModelProperty("至警戒水位纳蓄水能力")
    Integer warnStorageAbility;

    /**
     * 纳洪量
     */
    @ApiModelProperty("警戒与实时库容插值")
    Double warnStorageSub;


    /**
     * 水文监测站类型
     */
    @ApiModelProperty("水文监测站类型")
    private String stType;

    /**
     * 水文监测站经度
     */
    @ApiModelProperty("水文监测站经度")
    private BigDecimal stLong;

    /**
     * 水文监测站纬度
     */
    @ApiModelProperty("水文监测站纬度")
    private BigDecimal stLat;

    @ApiModelProperty("数据时刻")
    private Date dataTime;

    @ApiModelProperty("超警戒值")
    private Double overLimitLevel;

    /**
     * 和上个时刻对比,0持平，1上升，-1下降
     */
    @ApiModelProperty("和上个时刻对比,0持平，1上升，-1下降")
    private Integer compareLast = 0;

    public void computeOverLimitLevel() {
        if (waterLevel != null && super.getWarningLevel() != null && waterLevel > super.getWarningLevel().doubleValue()) {
            overLimitLevel = waterLevel - super.getWarningLevel().doubleValue();
        }
    }

}
