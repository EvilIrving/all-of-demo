package com.ygkj.warning.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class SysWarnGroupAddVo {

    @ApiModelProperty("修改用")
    private Long id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("所属任务类名")
    private String taskClass;
}
