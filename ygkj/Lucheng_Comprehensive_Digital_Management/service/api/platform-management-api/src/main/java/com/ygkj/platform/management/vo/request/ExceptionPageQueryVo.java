package com.ygkj.platform.management.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class ExceptionPageQueryVo extends PageEntity {

    @ApiModelProperty("所属系统模块")
    private String system;

    @ApiModelProperty("异常类型")
    private String exception;
}
