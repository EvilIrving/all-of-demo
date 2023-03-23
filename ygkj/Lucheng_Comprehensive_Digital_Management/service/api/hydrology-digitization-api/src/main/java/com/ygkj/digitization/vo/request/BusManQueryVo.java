package com.ygkj.digitization.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;

import java.util.Date;

/**
 * @author xq
 * @Description 日常业务管理-业务管理市级界面查询参数
 * @Date 2021/2/1
 */
@Data
@ApiModel("日常业务管理-业务管理市级界面查询参数")
public class BusManQueryVo {
    @ApiModelProperty("任务名称")
    String taskName;
    @ApiModelProperty("行政区划编码")
    String adcd;
    @ApiModelProperty("任务状态")
    Integer taskState;

    @ApiModelProperty("年份")
    Integer year;

    @ApiModelProperty("页码")
    int pageNum;
    @ApiModelProperty("页大小")
    int pageSize;
}
