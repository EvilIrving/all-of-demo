package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author xq
 * @Description
 * @Date 2021/7/13
 */
@Data
public class AllRealProjectReqVo implements Serializable {

    private static final long serialVersionUID = 6892508976707260371L;

    @ApiModelProperty("工程类型")
    private String projectType;

    @ApiModelProperty("所属流域")
    private String bas;

    @ApiModelProperty("行政区划")
    private String adcd;

    @ApiModelProperty("工程名称")
    private String projectName;

    @ApiModelProperty("工程等别")
    private String engGrad;

    @ApiModelProperty("工程规模")
    private String engScal;

    @ApiModelProperty("工程编码")
    private String projectCode;
}
