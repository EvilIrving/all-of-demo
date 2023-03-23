package com.ygkj.project.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @description 物业管理-定期监测表
 * @author
 * @date 2022-04-28
 */
@Data
@ApiModel("物业管理-定期监测表")
public class MoRegularMonitor implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private String id;

    /**
     * 年份
     */
    @ApiModelProperty("年份")
    private Integer year;

    /**
     * 监测日期
     */
    @ApiModelProperty("监测日期")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date monitorDate;

    /**
     * 监测单位
     */
    @ApiModelProperty("监测单位")
    private String monitorUnit;

    /**
     * 安全监测分析报告文件ids
     */
    @ApiModelProperty("安全监测分析报告文件ids")
    private String analyzeReport;

    /**
     * 安全监测整编报告
     */
    @ApiModelProperty("安全监测整编报告")
    private String reorganizationReport;

    /**
     * 与前期对比是否异常（正常/异常）
     */
    @ApiModelProperty("与前期对比是否异常（正常/异常）")
    private String compared;

    /**
     * 水库管理单位负责人
     */
    @ApiModelProperty("水库管理单位负责人")
    private String manager;


    /**
     * 监测类型
     */
    @ApiModelProperty("监测类型")
    private String monitorType;

    /**
     * 删除标记 0未删除 1已删除
     */
    @ApiModelProperty("删除标记 0未删除 1已删除")
    private Integer delFlag;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 创建人
     */
    @ApiModelProperty("创建人")
    private String createId;

    /**
     * 修改时间
     */
    @ApiModelProperty("修改时间")
    private Date updateTime;

    /**
     * 修改人
     */
    @ApiModelProperty("修改人")
    private String updateId;

    public MoRegularMonitor() {
    }

}
