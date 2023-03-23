package com.ygkj.data.management.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DataTableCommonUpdateVo {
    @ApiModelProperty("数据表id")
    @NotBlank(message = "请指定表id")
    private String tableId;
    @ApiModelProperty("数据json，所有字段和值拼凑成一个json,{'a':'aval','b':'bval'}")
    private String dataDetail;
}
