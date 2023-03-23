package com.ygkj.digitization.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author xq
 * @Description 日常管理-查询参数
 * @Date 2021/2/1
 */
@Data
@ApiModel("日常管理-查询参数")
public class GenManUpperQueryVo {

    //    @ApiModelProperty("起始时间")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    Date start;
//
//    @ApiModelProperty("结束时间")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    Date end;
    @ApiModelProperty("年份")
    Integer year;

    @ApiModelProperty("任务名")
    String taskName;
    @ApiModelProperty("行政区划代码")
    String adcd;

    @ApiModelProperty("页码")
    int pageNum;
    @ApiModelProperty("页面大小")
    int pageSize;
}
