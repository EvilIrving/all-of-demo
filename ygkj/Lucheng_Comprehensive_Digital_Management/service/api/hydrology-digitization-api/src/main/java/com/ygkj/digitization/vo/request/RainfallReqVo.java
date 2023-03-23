package com.ygkj.digitization.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class RainfallReqVo {

    @ApiModelProperty("开始日期(yyyy-MM-dd)")
    private String startDate;

    @ApiModelProperty("结束日期(yyyy-MM-dd)")
    private String endDate;

    @ApiModelProperty(value = "测站编码集合", hidden = true)
    private List<String> stcdList;

    @ApiModelProperty(value = "表名", hidden = true)
    private String tableName;
}
