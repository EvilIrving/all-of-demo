package com.ygkj.data.management.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("数据表详情参数封装")
public class DataTableCommonDetailQueryVo {

    @ApiModelProperty("表id")
    @NotNull(message = "数据表id不能为空")
    private String tableId;

    @ApiModelProperty("dataId，查单条用")
    @NotNull(message = "数据id不能为空")
    private String dataId;

    /**
     * 是否跳过权限
     */
    @ApiModelProperty("是否跳过权限检查-默认不跳过")
    private boolean skipPermission = false;
}
