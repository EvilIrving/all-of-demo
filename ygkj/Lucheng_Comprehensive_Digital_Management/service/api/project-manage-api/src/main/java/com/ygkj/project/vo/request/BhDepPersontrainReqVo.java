package com.ygkj.project.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BhDepPersontrainReqVo extends PageEntity {
    @ApiModelProperty("编号")
    private String id;


    @ApiModelProperty("培训人员")
    private String person;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @ApiModelProperty("培训时间")
    private Date time;


    @ApiModelProperty("培训经费")
    private BigDecimal fund;



    @ApiModelProperty("附件")
    private String aid;


    @ApiModelProperty("工程编码")
    private String pjcd;


    @ApiModelProperty("执行情况")
    private String execStatus;



    @ApiModelProperty("数据来源 1-标化 2-本地新增")
    private int sourceType;


    @ApiModelProperty("删除标记 0未删除 1已删除")
    private int delFlag;
}
