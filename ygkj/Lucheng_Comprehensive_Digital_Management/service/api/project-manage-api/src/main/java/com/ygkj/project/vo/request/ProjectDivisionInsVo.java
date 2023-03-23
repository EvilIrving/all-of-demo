package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hucong
 * @description 项目划分表
 * @date 2021-07-08
 */
@Data
@ApiModel("项目划分新增入参")
public class ProjectDivisionInsVo implements Serializable {

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

    public ProjectDivisionInsVo() {
    }

}
