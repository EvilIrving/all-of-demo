package com.ygkj.gragh.vo.request;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class HourRainfallQueryVo {

    @ApiModelProperty("天数")
    private Integer days;
    @ApiModelProperty("时间节点")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;
    @ApiModelProperty("测站编码")
    private String stationCode;
    @ApiModelProperty("前端忽略")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date start;
    @ApiModelProperty("前端忽略")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date end;
    /**
     * 20210625 新增需求，将每个时刻向未来推，
     */
    @ApiModelProperty("是否向未来推")
    private Boolean timeForward;
    @ApiModelProperty("年份")
    private Integer year;
    @ApiModelProperty("测站名称")
    private String stationName;
}
