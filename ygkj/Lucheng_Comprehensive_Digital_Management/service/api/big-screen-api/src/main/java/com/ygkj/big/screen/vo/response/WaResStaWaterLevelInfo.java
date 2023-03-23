package com.ygkj.big.screen.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author fml
 * @date 2021-03-31 9:20
 * @description 水库关联水位测站信息
 */
@Data
public class WaResStaWaterLevelInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("水库代码")
    private String resCode;

    @ApiModelProperty("水库名称")
    private String resName;

    @ApiModelProperty("总库容")
    private Double totCap;

    @ApiModelProperty("坝址控制流域面积(集水面积)")
    private Double watShedArea;

    @ApiModelProperty("行政区划")
    private String adcd;

    @ApiModelProperty("测站代码")
    private String stCode;

    @ApiModelProperty("测站名称")
    private String stName;

    @ApiModelProperty("汛限水位")
    private Double limitLevel;

    @ApiModelProperty("警戒水位")
    private Double warningLevel;

    @ApiModelProperty("实时水位")
    private Double z;

    @ApiModelProperty("实时库容")
    private Double realCapacity;

    @ApiModelProperty("状态（0不正常，1正常）")
    private String status = "1";

    @ApiModelProperty(value = "比率", hidden = true)
    private Double rate;
}
