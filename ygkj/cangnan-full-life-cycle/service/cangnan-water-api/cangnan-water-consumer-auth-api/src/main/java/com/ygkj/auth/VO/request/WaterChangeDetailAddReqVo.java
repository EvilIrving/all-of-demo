package com.ygkj.auth.VO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author: huangzh
 * @Date: 2020/9/10 17:57
 * @Description:
 */
@Data
public class WaterChangeDetailAddReqVo {

    @ApiModelProperty(value = "水域变化信息id")
    private String waterChangeId;

    @ApiModelProperty(value = "年度")
    private String waterYear;

    @ApiModelProperty("水域面积（单位：m²）")
    private Double waterArea;

    @ApiModelProperty("库容量（单位：m³）")
    private Double capacity;

    @ApiModelProperty("实际岸线（m）")
    private Double shoreline;

    @ApiModelProperty("河底高程（m）")
    private Double waterHeight;

    /*@ApiModelProperty("范围线类型(水面线、临水线、范围线)")
    private String scopeLineType;

    @ApiModelProperty("范围线文件路径)")
    private String filePath;

    @ApiModelProperty("范围线文件名)")
    private String fileName;*/

    @ApiModelProperty("范围线点位集合)")
    private String linePoint;

    @ApiModelProperty("临水线点位集合")
    private String waterfrontLinePoint;

    @ApiModelProperty("水面线点位集合")
    private String surfaceLinePoint;
}
