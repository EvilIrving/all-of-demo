package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SlProjStRResVo {

    @ApiModelProperty("工程编码")
    private String projectCode;

    @ApiModelProperty("站点编码")
    private String stcd;

    @ApiModelProperty("站点名称")
    private String stName;

    @ApiModelProperty("测站类型 RR水库水位站，PP雨量站，DD闸坝水位站，TT潮位站，ZG地下水位站，ZQ流量站，ZZ河道，SS墒情,PT泵站,SC视频,SM安全监测")
    private String stType;

    @ApiModelProperty("工程类型 1海塘 2水库 3灌区 4圩区 5堤防 6水闸 7泵站 8渡槽 9倒虹吸 10输供水隧洞 11闸站")
    private Integer projectType;

    @ApiModelProperty("是否主站 0否 1是")
    private Integer isMain;

    @ApiModelProperty("所在市")
    private String cityAdnm;

    @ApiModelProperty("所在区/县")
    private String countryAdnm;
}
