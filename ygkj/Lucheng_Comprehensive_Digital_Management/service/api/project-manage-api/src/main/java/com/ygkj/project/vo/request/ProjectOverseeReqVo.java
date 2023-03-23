package com.ygkj.project.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ProjectOverseeReqVo extends PageEntity {

    @ApiModelProperty(hidden = true)
    private String userId;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("工程编码")
    private String projectCode;

    @ApiModelProperty("督办状态 0未处置 1已处置 -1超期未处置")
    private Integer status;
}
