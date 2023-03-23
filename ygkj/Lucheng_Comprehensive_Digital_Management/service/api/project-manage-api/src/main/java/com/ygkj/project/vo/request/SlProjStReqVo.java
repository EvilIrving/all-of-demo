package com.ygkj.project.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class SlProjStReqVo extends PageEntity {

    @ApiModelProperty("工程编码")
    private String projectCode;

    @ApiModelProperty("测站类型 RR水库水位站，PP雨量站，DD闸坝水位站，TT潮位站，ZG地下水位站，ZQ流量站，ZZ河道，SS墒情,PT泵站,SC视频,SM安全监测")
    private String stType;

    @ApiModelProperty("工程类型 1海塘 2水库 3灌区 4圩区 5堤防 6水闸 7泵站 8渡槽 9倒虹吸 10输供水隧洞 11闸站")
    private Integer projectType;

    @ApiModelProperty("站点名称")
    private String stName;

    @ApiModelProperty("是否主站")
    private String isMain;

    @ApiModelProperty("是否绑定的 true已绑定的 false全部的")
    private boolean bind;

    @ApiModelProperty("是否雨量站")
    private String isRainStation;

}
