package com.ygkj.gragh.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class WainBaseResVo {

    @ApiModelProperty("取水口名称")
    private String wainName;

    @ApiModelProperty("城市名称")
    private String cityAdnm;

    @ApiModelProperty("县名称")
    private String countryAdnm;

    @ApiModelProperty("所属取水户名称")
    private String wiuName;

    @ApiModelProperty("取水口类型")
    private String mainWainUse;

    @ApiModelProperty("取水点类型")
    private String wainWasoType;

    @ApiModelProperty("取水点方式")
    private String wainType;
}
