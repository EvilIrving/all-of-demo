package com.ygkj.project.vo.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class MfuResVo {

    @ApiModelProperty("年份")
    private Integer year;

    @ApiModelProperty("类型")
    private String mfType;

    @ApiModelProperty("类型名称")
    private String mfTypeName;

    @ApiModelProperty("部位")
    private String position;

    @ApiModelProperty("养护项目")
    private String name;

    @ApiModelProperty("人员名称")
    private String realName;

    @ApiModelProperty("计划开始时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date stTm;

    @ApiModelProperty("计划完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date compTm;

    @ApiModelProperty("实际完成时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date completeTime;

    @ApiModelProperty("维养合同验收时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date mfuAcceptTime;

    @ApiModelProperty("维养合同验收记录")
    private String mfuAcceptRecord;

    @ApiModelProperty("验收记录附件")
    private String mfuAcceptAid;

    @ApiModelProperty("维养合同")
    private String mfuContract;

    @ApiModelProperty("养护前照片")
    private String bfpic;

    @ApiModelProperty("养护后照片")
    private String afpic;

    @ApiModelProperty("生成维养记录表")
    private String mfuAid;
}
