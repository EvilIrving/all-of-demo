package com.ygkj.project.vo.response;

import com.ygkj.project.model.ProjectOversee;
import com.ygkj.project.model.ProjectOverseeWork;
import com.ygkj.project.model.SysFile;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class ProjectOverseeResVo extends ProjectOversee {

    @ApiModelProperty("创建人名称")
    private String createName;

    @ApiModelProperty("督办状态 0未处置 1已处置 -1超期未处置")
    private Integer status;

    @ApiModelProperty("true可以填报 false不可填报")
    private boolean updateFlag;

    @ApiModelProperty("内部人员列表")
    private List<ProjectOverseeWork> insideList;

    @ApiModelProperty("外部人员列表")
    private List<ProjectOverseeWork> outsideList;

    @ApiModelProperty("问题附件列表")
    private List<SysFile> problemFileList;

    @ApiModelProperty("整改附件列表")
    private List<SysFile> workFileList;

    @ApiModelProperty("工程名称")
    private String projectName;
}
