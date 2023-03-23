package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class JobSettingDetailResVo {

    @ApiModelProperty("人员列表")
    private List<JobPersonResVo> people;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("岗位名称")
    private String jobName;

    @ApiModelProperty(hidden = true)
    private String name;

    @ApiModelProperty(hidden = true)
    private String telphone;

    @ApiModelProperty("人员兼岗情况")
    private String alsoHappening;

    @ApiModelProperty("是否物业化")
    private String isProperty;

    @ApiModelProperty("岗位人员资格证书")
    private String personCredentials;

    @ApiModelProperty("在岗人数")
    private Integer jouCount;

    @ApiModelProperty("入职条件")
    private String entryRequirement;

    @ApiModelProperty("岗位职责")
    private String jobDuty;

}
