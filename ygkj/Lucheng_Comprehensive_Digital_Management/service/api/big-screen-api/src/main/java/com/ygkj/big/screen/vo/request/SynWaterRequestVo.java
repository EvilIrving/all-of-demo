package com.ygkj.big.screen.vo.request;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@ApiModel("水雨工情---水位测站查询参数封装")
public class SynWaterRequestVo {
    @ApiModelProperty(value = "行政区划编码")
    private String areaCode;

    @ApiModelProperty(value = "所属流域编码")
    private String basinCode;

//    @ApiModelProperty(value = "工程类型")
//    private String projectType;
//    @ApiModelProperty(value = "工程编码")
//    private String projectCode;

    @ApiModelProperty(value = "测站名称")
    private String stationName;

    @ApiModelProperty(value = "测站类型：PP雨量站，RR水库水位站,ZZ河道水位站,DD堰闸,TT 潮位站")
    private String stationType;

    @ApiModelProperty(value = "是否超警")
    private boolean warning;

    @ApiModelProperty(value = "传大中型水库可查询19座大中型水库的实时水位")
    private String resGrade;

    @ApiModelProperty("测站编码，多个测站编码使用多个半角逗号隔开")
    private String stCodes;

    @ApiModelProperty(value = "数据时间,为空查询最新的，否则就查询距离该时间点最近的")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dataTime;

    @ApiModelProperty("潮位是否是天文潮位")
    private boolean isAstrotd = false;

    @ApiModelProperty("潮位是否是天文潮位")
    private Double overWarnLevel;
}
