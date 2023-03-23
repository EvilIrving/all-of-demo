package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@ApiModel("维修养护资金申报")
public class BhLgMfd implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    private String id;


    @ApiModelProperty("年份")
    private Integer year;


    @ApiModelProperty("维养资金申报文件")
    private String aid;


    @ApiModelProperty("年度维修养护申报资金")
    private BigDecimal dclrFunds;


    @ApiModelProperty("工程编号")
    private String pid;


    @ApiModelProperty("年度维修养护计划文件")
    private String mtnPlan;


    @ApiModelProperty("上级下达的年度维修养护资金（万元）")
    private BigDecimal superiorfunds;


    @ApiModelProperty("管理单位自筹的年度维修养护资金（万元）")
    private BigDecimal selffunds;


    @ApiModelProperty("已使用的维修养护资金")
    private BigDecimal usedamount;


    @ApiModelProperty("已支付的维修养护资金")
    private BigDecimal paidamount;


    @ApiModelProperty("数据来源 1-标化 2-本地新增")
    private int sourceType;

    @ApiModelProperty("申报单位")
    private String declarationUnit;


    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    @ApiModelProperty("申报时间")
    private Date declarationTime;


    @ApiModelProperty("批复单位")
    private String approvalUnit;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    @ApiModelProperty("批复时间")
    private Date approvalTime;


    @ApiModelProperty("删除标记 0未删除 1已删除")
    private int delFlag;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    @ApiModelProperty("创建时间")
    private Date createTime;


    @ApiModelProperty("创建人")
    private String createId;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    @ApiModelProperty("修改时间")
    private Date updateTime;


    @ApiModelProperty("修改人")
    private String updateId;

    @TableField(exist = false)
    private List<SysFile> aidFiles;

    @TableField(exist = false)
    private List<SysFile> mtnPlanFiles;
}
