package com.ygkj.project.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BhLgLmrDisDomGsReqVo {
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("调令编号")
    private String soId;


    @ApiModelProperty("闸门名称")
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @ApiModelProperty("实际开启时间")
    private Date actlOpenTm;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @ApiModelProperty("实际关闭时间")
    private Date actlCloseTm;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @ApiModelProperty("状态报告时间")
    private Date reportTm;


    @ApiModelProperty("开度")
    private BigDecimal opening;


    @ApiModelProperty("流量")
    private BigDecimal flow;


    @ApiModelProperty("数据来源 1-标化 2-本地新增")
    private Integer sourceType;
}
