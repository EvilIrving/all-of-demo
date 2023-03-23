package com.ygkj.digitization.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author xq
 * @Description 值班人员管理导出接口查询参数
 * @Date 2021/1/17
 */
@Data
@ApiModel("值班人员管理导出接口查询参数")
public class MonitorPersonToExcelQueryVo {

    @ApiModelProperty("人员类型：1：副班人员，2：值班人员，3：带班领导")
    Integer personType;
    @ApiModelProperty("姓名")
    String username;
}
