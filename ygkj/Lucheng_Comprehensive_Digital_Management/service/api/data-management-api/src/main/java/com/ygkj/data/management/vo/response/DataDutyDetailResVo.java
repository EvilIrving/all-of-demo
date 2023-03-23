package com.ygkj.data.management.vo.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("数据责任详情返回值")
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class DataDutyDetailResVo {

    @ApiModelProperty("表名称")
    private String dataName;    // 表名称

    @ApiModelProperty("表责任人")
    private String dutyUser;        // 表责任人

    @ApiModelProperty("所在部门")
    private String dutyDepartment;  // 所在部门

    @ApiModelProperty("数据源数量")
    private Integer dsCount;        // 数据源数量

    @ApiModelProperty("最新数据情况")
    private BusDataCellDataValueResVo latestDsInfo;
}
