package com.ygkj.water.project.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.ygkj.water.project.converter.CameraStatusConverter;
import com.ygkj.water.project.converter.StTypeConverter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MonitoringStationResVo {

    @ApiModelProperty("监测站点编码")
    @ExcelProperty(value = "监测站点编码")
    @ColumnWidth(21)
    private String stCode;

    @ApiModelProperty("监测站点名称")
    @ExcelProperty(value = "监测站点名称")
    @ColumnWidth(21)
    private String stName;

    @ApiModelProperty("城市名称")
    @ExcelProperty(value = "城市名称")
    @ColumnWidth(17)
    private String cityAdnm;

    @ApiModelProperty("区县名称")
    @ExcelProperty(value = "区县名称")
    @ColumnWidth(17)
    private String countryAdnm;

    @ApiModelProperty("经度")
    @ExcelProperty(value = "经度")
    @ColumnWidth(17)
    private String longitude;

    @ApiModelProperty("纬度")
    @ExcelProperty(value = "纬度")
    @ColumnWidth(17)
    private String latitude;

    @ApiModelProperty("状态 0离线,1在线")
    @ExcelProperty(value = "状态", converter = CameraStatusConverter.class)
    @ColumnWidth(9)
    private Integer status;

    @ApiModelProperty("监测站点类型 RR水库水位站，ZZ河道，DD闸坝水位站，TT潮位站，PP雨量站，SC视频")
    @ExcelProperty(value = "监测站点类型", converter = StTypeConverter.class)
    @ColumnWidth(21)
    private String stType;
}
