package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ForwordWarnAnalyseReqVo {

    @ApiModelProperty("前端忽略")
    private String id;

    @ApiModelProperty("报警记录id")
    private String recordId;

    @ApiModelProperty("报警分析")
    private String warningType;

    @ApiModelProperty("报警内容")
    private String warnContent;

    @ApiModelProperty("转发方式")
    private String forwordType;

    @ApiModelProperty("用户ids，以逗号分隔")
    private String userIds;
}
