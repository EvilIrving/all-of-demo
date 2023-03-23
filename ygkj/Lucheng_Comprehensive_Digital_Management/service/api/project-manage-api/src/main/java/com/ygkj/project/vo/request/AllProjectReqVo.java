package com.ygkj.project.vo.request;

import com.ygkj.project.model.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("工程筛选入参")
public class AllProjectReqVo extends PageEntity {

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

    public AllProjectReqVo() {
    }

    public AllProjectReqVo(String projectType, String projectCode) {
        this.projectType = projectType;
        this.projectCode = projectCode;
    }

    public AllProjectReqVo(String projectType, String projectCode, String projectName) {
        this.projectType = projectType;
        this.projectName = projectName;
        this.projectCode = projectCode;
    }
}
