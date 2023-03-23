package com.ygkj.gragh.vo.request;


import com.ygkj.enums.EngScalEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.Optional;

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

    @ApiModelProperty(value = "测站类型：PP雨量站，RR水库水位站,ZZ河道水位站,DD堰闸,TT 潮位站 SS墒情,PT泵站工情")
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

    /**
     * true 只查询温瑞平水闸测站
     * false 只查询除温瑞平水闸测站以外的测站
     * null 查询所有的水闸测站
     */
    @ApiModelProperty("是否查询温瑞平水闸")
    private Boolean isWrp = null;

    @ApiModelProperty("是否只查询开启的水闸")
    private Boolean openGate = null;

    @ApiModelProperty("报汛等级 重点true 一般 false")
    private Boolean isFocus;

    @ApiModelProperty("水库类型：大中型，小（Ⅰ）型，小（Ⅱ）型")
    private String engScal;

    @ApiModelProperty("河道水位类型：平原河网/山区河道/其他")
    private String riverStationType;

    @ApiModelProperty("是否只显示主测站")
    private Boolean main;

    @ApiModelProperty("水库筛选项，是否绑定工程")
    private Boolean bindProj;

    @ApiModelProperty("水库筛选项，是否使用特征水位")
    private boolean projFeatureLevel = true;

    private Boolean online;

    public void checkEngScal() {
        // 有可能传空字符串，
        if (!StringUtils.isEmpty(engScal)) {
            EngScalEnum scalEnum = EngScalEnum.getByDesc(engScal);
            if (scalEnum == null) {
                throw new IllegalArgumentException("engScal&".concat(engScal));
            }
        }
    }
}
