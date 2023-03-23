package com.ygkj.gragh.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lxl
 * @create 2021-08-06 15:47
 * @description 水厂运行状态 筛选条件类
 */
@Data
@ApiModel("水厂运行状态 筛选条件类")
public class WaterOperateReqVo extends PageEntity {

    /**
     * 行政区划
     */
    @ApiModelProperty("行政区划编码")
    private String adcd;

    @ApiModelProperty("运行状态 供水状态 0-异常 1-正常")
    private String supplyStatus;

    @ApiModelProperty("异常原因  0-供水异常  1-水质异常")
    private String abnormal;

    @ApiModelProperty("关键字")
    private String key;

    @ApiModelProperty("监测开始时间")
    private String startTime;

    @ApiModelProperty("监测结束时间")
    private String endTime;

}
