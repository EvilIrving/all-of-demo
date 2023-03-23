package com.ygkj.project.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ygkj.project.model.SysFile;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author lxl
 * @create 2022-04-28 14:30
 * @description
 */
@Data
public class MoRegularMonitorVo {

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
     * 安全监测分析报告文件
     */
    List<SysFile> analyzeReportList;

    /**
     * 安全监测整编报告文件
     */
    List<SysFile> reorganizationReportList;

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
    @ApiModelProperty("监测类型  沉降观测，位移观测，渗流监测")
    private String monitorType;

}
