package com.ygkj.project.api.vo.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProjectRelResVo {

    @ApiModelProperty("主键")
    private String dtsCmfId;

    @ApiModelProperty("工程编码")
    private String prcd;

    @ApiModelProperty("工程类型")
    private String projectCategory;

    @ApiModelProperty("相关工程/测站编码")
    private String relCode;

    @ApiModelProperty("相关工程或站点类型 1水闸 2泵站 3闸站 4水位 5雨量 6摄像头")
    private Integer relType;

    @ApiModelProperty("是否主站 false不是主站 true主站")
    private Boolean main;

    @ApiModelProperty("相关工程/测站名称")
    private String relName;

    @ApiModelProperty("相关工程/测站经度")
    private String lgtd;

    @ApiModelProperty("相关工程/测站纬度")
    private String lttd;

}
