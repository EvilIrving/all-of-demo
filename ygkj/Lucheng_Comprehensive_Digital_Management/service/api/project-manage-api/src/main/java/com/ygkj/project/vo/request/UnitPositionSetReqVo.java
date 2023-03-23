package com.ygkj.project.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UnitPositionSetReqVo extends PageEntity {
    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("单位类型 1-运行单位 2-物业单位")
    private String type;

    @ApiModelProperty("工程名称")
    private String projectName;


    @ApiModelProperty("工程类别")
    private String projectCategory;

    @ApiModelProperty("工程规模")
    private String projectScale;


    @ApiModelProperty("所属乡镇（街道）")
    private String townAdnm;

    @ApiModelProperty("部门id")
    private String deptId;
}
