package com.ygkj.project.vo.response;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author hucong
 * @description 项目划分表
 * @date 2021-07-08
 */
@Data
@ApiModel("项目划分表")
public class ProjectDivisionRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private String id;

    /**
     * 父id，当为父单位工程时，传-1
     */
    @ApiModelProperty("父id，当为父单位工程时，传-1")
    private String pid;

    /**
     * 单位工程名称
     */
    @ApiModelProperty("单位工程名称")
    private String name;

    /**
     * 项目工程编码
     */
    @ApiModelProperty("项目工程编码")
    private String projectCode;

    /**
     * 工程名称
     */
    @ApiModelProperty("工程名称")
    private String projectName;

    /**
     * 上传人姓名
     */
    @ApiModelProperty("上传人姓名")
    private String uploadName;

    @ApiModelProperty("子单位工程list")
    private List<ProjectDivisionRespVo> children;

    public ProjectDivisionRespVo() {
    }

}