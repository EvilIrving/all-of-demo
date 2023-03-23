package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProRelStVo {

    @ApiModelProperty("工程编码")
    private String projectCode;

    @ApiModelProperty("测站类型 1潮位站 2雨量站 3流量站 4水质,5.安全监测,6水闸水位站,7水库水位站")
    private Integer stType;

    @ApiModelProperty("工程类型 1海塘 2水库 3灌区 4圩区 5堤防 6水闸 7泵站 8渡槽 9倒虹吸 10输供水隧洞 11闸站")
    private Integer projectType;

    @ApiModelProperty(hidden = true)
    private List<String> stTypeList = new ArrayList<>();

}
