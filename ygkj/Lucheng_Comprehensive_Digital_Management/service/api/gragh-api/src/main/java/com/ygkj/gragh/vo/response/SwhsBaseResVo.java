package com.ygkj.gragh.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class SwhsBaseResVo {

    @ApiModelProperty("水源地名称")
    private String swhsName;

    @ApiModelProperty("水源地编码")
    private String swhsCode;

    @ApiModelProperty("所属乡镇")
    private String townAdnm;

    @ApiModelProperty("水源地类型")
    private String wainWasoType;

    @ApiModelProperty("最大日供水能力")
    private String dateMaxWat;

    @ApiModelProperty("服务人口")
    private String desAnnWasuPop;

    @ApiModelProperty("负责人")
    private String master;

    @ApiModelProperty("联系电话")
    private String masterPhone;

    @ApiModelProperty("城市名称")
    private String cityAdnm;

    @ApiModelProperty("县/区名称")
    private String countryAdnm;

    @ApiModelProperty("标示牌")
    private List<String> fileList;
}
