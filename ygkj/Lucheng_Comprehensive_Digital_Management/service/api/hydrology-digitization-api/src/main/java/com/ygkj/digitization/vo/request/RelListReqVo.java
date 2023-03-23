package com.ygkj.digitization.vo.request;

import com.ygkj.digitization.model.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class RelListReqVo extends PageEntity {

    @ApiModelProperty(value = "类型 1旱情 2水位 潮位的时候不用传")
    private Integer type;

    @ApiModelProperty("名称")
    private String name;
}
