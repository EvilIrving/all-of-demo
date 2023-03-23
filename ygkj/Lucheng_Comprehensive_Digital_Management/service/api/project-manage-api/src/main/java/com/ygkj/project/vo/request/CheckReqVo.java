package com.ygkj.project.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class CheckReqVo extends PageEntity {

    @ApiModelProperty("工程编码")
    private String projectCode;

    @ApiModelProperty("工程类型 1海塘 2水库 3灌区 4圩区 5堤防 6水闸 7泵站 8渡槽 9倒虹吸 10输供水隧洞 11闸站")
    private Integer projectType;

    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

}
