package com.ygkj.data.management.vo.request;

import com.ygkj.utils.StringUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("数据表查询参数封装")
public class DataTableCommonPageQueryVo {

    @ApiModelProperty("单页限定记录数")
    private Integer pageSize;

    @ApiModelProperty("页码")
    private Integer pageNum;

    @ApiModelProperty("表id")
    @NotNull(message = "数据表id不能为空")
    private String tableId;

    @ApiModelProperty("dataId，查单条用")
    private String dataId;

    @ApiModelProperty("查询参数json")
    private String paramJson;

    @ApiModelProperty("查询输入框的值")
    private String queryValue;

    @ApiModelProperty("排序字段值")
    private String sortColumn;

    @ApiModelProperty("是否正序，true升序，false降序")
    private Boolean ascending;

    @ApiModelProperty("业务类型 BASIC 基础数据  REALTIME 实时数据")
    private String busType;

    public String getBusType() {
        return StringUtils.isEmpty(this.busType) ? "BASIC" : this.busType;
    }

    /**
     * 是否跳过权限
     */
    @ApiModelProperty("是否跳过权限检查-默认不跳过")
    private boolean skipPermission = false;
}
