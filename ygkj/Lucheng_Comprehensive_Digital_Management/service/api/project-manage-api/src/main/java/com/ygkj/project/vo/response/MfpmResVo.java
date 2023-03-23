package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
public class MfpmResVo {

    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("类型")
    private String mfType;

    @ApiModelProperty("类型名称")
    private String mfTypeName;

    @ApiModelProperty("部位")
    private String position;

    @ApiModelProperty("维养项目")
    private String project;

}
