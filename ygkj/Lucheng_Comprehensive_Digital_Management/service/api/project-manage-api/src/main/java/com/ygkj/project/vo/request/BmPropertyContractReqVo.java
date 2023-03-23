package com.ygkj.project.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ygkj.entity.PageEntity;
import com.ygkj.project.model.SysFile;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class BmPropertyContractReqVo extends PageEntity {
    @ApiModelProperty("id")
    private String id;


    @ApiModelProperty("物业合同名称")
    private String contractName;


    @ApiModelProperty("委托方单位编码")
    private String contractClient;

    @ApiModelProperty("委托方单位名称")
    private String contractClientNm;


    @ApiModelProperty("物业管理单位")
    private String contractUnit;

    @ApiModelProperty("物业管理单位名称")
    private String contractUnitNm;


    @ApiModelProperty("物业委托管理内容")
    private String contractContent;


    @ApiModelProperty("行政区划代码")
    private String adcd;


    @ApiModelProperty("起始年份")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    private Date starttm;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone="GMT+8")
    @ApiModelProperty("终止年份")
    private Date endtm;


    @ApiModelProperty("专项委托资质")
    private String qualification;


    @ApiModelProperty("总合同经费")
    private String funds;


    @ApiModelProperty("合同事项 合同事项 1.运行操作 2. 维修养护 3 工程检查")
    private String contractMatter;


    @ApiModelProperty("合同文件")
    private String contractFile;


    @ApiModelProperty("创建时间")
    private Date createTime;


    @ApiModelProperty("创建人")
    private String creatorId;


    @ApiModelProperty("修改时间")
    private Date updateTime;


    @ApiModelProperty("修改人")
    private String updatorId;


    @ApiModelProperty("是否删除 0-未删除 1-已删除")
    private int delFlag;


    @ApiModelProperty("数据来源 1-运管 2-系统新增")
    private String sourceType;

    @ApiModelProperty("筛选关键词")
    private String key;

    private List<SysFile> files;
}
