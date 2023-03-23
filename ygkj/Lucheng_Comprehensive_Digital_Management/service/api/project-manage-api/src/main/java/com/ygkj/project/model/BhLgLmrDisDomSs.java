package com.ygkj.project.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class BhLgLmrDisDomSs implements Serializable {

    private static final long serialVersionUID = 1L;


    @ApiModelProperty("id")
    private String id;


    @ApiModelProperty("调令编号")
    private String soId;


    @ApiModelProperty("泄洪洞名称")
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @ApiModelProperty("实际开启时间")
    private Date actlOpenTm;


    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @ApiModelProperty("实际关闭时间")
    private Date actlCloseTm;


    @ApiModelProperty("开度")
    private BigDecimal opening;


    @ApiModelProperty("流量")
    private BigDecimal flow;


    @ApiModelProperty("数据来源 1-标化 2-本地新增")
    private Integer sourceType;


    @ApiModelProperty("删除标记 0未删除 1已删除")
    private int delFlag;
}
