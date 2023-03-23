package com.ygkj.project.vo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class JobPersonResVo {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("人员名称")
    private String name;

    @ApiModelProperty("联系电话")
    private String telphone;

    @ApiModelProperty("职务 该字段为空时展示岗位名称")
    private String duty;

    @ApiModelProperty("岗位名称")
    private String jobName;

    @ApiModelProperty("职称")
    private String jobtitle;

    @ApiModelProperty("身份证")
    private String idCard;

    @ApiModelProperty("学历")
    private String education;

    @ApiModelProperty("专业")
    private String profession;
}
