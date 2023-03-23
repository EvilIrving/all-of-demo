package com.ygkj.project.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @description bh_lg_ps_special_check
 * @author
 * @date 2022-04-27
 */
@Data
@ApiModel("bh_lg_ps_special_check")
public class BhLgPsSpecialCheck implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 检查时间
     */
    @ApiModelProperty("检查时间 yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date checkTm;

    /**
     * 指令编号
     */
    @ApiModelProperty("指令编号")
    private Integer orderCode;

    /**
     * 签发人
     */
    @ApiModelProperty("签发人")
    private String signer;

    /**
     * 检查原因
     */
    @ApiModelProperty("检查原因")
    private String checkReason;

    /**
     * 检查内容
     */
    @ApiModelProperty("检查内容")
    private String checkContent;

    /**
     * 责任单位
     */
    @ApiModelProperty("责任单位")
    private String responUnit;

    /**
     * 开展形式
     */
    @ApiModelProperty("开展形式")
    private String carryForm;

    /**
     * 责任人员及分工
     */
    @ApiModelProperty("责任人员及分工")
    private String peopleDow;

    /**
     * 责任单位负责人
     */
    @ApiModelProperty("责任单位负责人")
    private String unitPrincipal;

    /**
     * pdf文件
     */
    @ApiModelProperty("pdf文件")
    private String aid;

    @TableField(exist = false)
    private List<SysFile> aidList;

    /**
     * 工程编号
     */
    @ApiModelProperty("工程编号")
    private String pid;

    /**
     * 数据来源 1-标化 2-本地新增
     */
    @ApiModelProperty("数据来源 1-标化 2-本地新增")
    private Integer sourceType;

    /**
     * 问题类型
     */
    @ApiModelProperty("问题类型")
    private String problemType;

    public BhLgPsSpecialCheck() {
    }

}
