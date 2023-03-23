package com.ygkj.data.management.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class MyWarningPageQueryVo extends PageEntity {

    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("状态：已处理/未处理")
    private String status;

    private String userId;
}
