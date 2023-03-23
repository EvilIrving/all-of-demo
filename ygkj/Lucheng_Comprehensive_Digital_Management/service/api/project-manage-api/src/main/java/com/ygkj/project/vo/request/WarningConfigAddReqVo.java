package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class WarningConfigAddReqVo {

    @ApiModelProperty("报警配置id,阈值更新接口返回")
    private String id;

    @ApiModelProperty("报警类型")
    private String warningType;

    @ApiModelProperty("模板id")
    private String templateId;

    @ApiModelProperty("模板名称")
    private String templateName;

    @ApiModelProperty("报警方式")
    private String pattren;

    @ApiModelProperty("启用状态（0停用，1启用）")
    private Integer status;

    /*@ApiModelProperty("测站id,多个以逗号分隔")
    private String stCodes;*/

    @ApiModelProperty("报警接收人id,多个以逗号分隔")
    private String userIds;

    /*@ApiModelProperty("降雨历时")
    private String rainfallPeriod;

    @ApiModelProperty("设定阈值")
    private Double threshold;

    @ApiModelProperty("测站类型")
    private String stType;

    @ApiModelProperty("阈值类型")
    private Double thresholdType;*/
}
