package com.ygkj.project.vo.request;

import com.ygkj.project.model.ProjectOversee;
import com.ygkj.project.model.ProjectOverseeWork;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class OverseeReqVo extends ProjectOversee {

    @ApiModelProperty("内部人员列表")
    private List<ProjectOverseeWork> insideList;

    @ApiModelProperty("外部人员列表")
    private List<ProjectOverseeWork> outsideList;

    @ApiModelProperty("工程名称")
    private String projectName;
}
