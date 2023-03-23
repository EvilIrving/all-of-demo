package com.ygkj.auth.VO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author: huangzh
 * @Date: 2020/9/16 17:37
 * @Description: 办理事项新增请求体
 */
@Data
public class ProjectMatterAddReqVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("事项名称")
    private String matterName;

    @ApiModelProperty("事项编码")
    private String matterNo;

    @ApiModelProperty("子项名称")
    private String chileMatterName;

    @ApiModelProperty("孙项名称")
    private String grandsonMatterName;

    @ApiModelProperty("项目阶段")
    private String projectStage;

    @ApiModelProperty("办理天数")
    private Integer processDays;

    private List<ProjectMatterFileReqVO> projectMatterFileReqVOS;
}
