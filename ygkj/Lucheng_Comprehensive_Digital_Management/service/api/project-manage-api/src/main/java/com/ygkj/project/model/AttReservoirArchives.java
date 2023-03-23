package com.ygkj.project.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author lxl
 * @description 大中型水库档案
 * @date 2021-05-04
 */
@Data
@ApiModel("大中型水库档案")
public class AttReservoirArchives implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 水库工程编码
     */
    @ApiModelProperty("水库工程编码")
    private String reservoirCode;

    /**
     * 水库运行档案id，关联到系统统一附件表sys_file
     */
    @ApiModelProperty("水库运行档案id，关联到系统统一附件表sys_file")
    private String runFileId;

    /**
     * 水库建设档案id，关联到系统统一附件表sys_file
     */
    @ApiModelProperty("水库建设档案id，关联到系统统一附件表sys_file")
    private String constructFileId;

    /**
     * 水库规划档案id，关联到系统统一附件表sys_file
     */
    @ApiModelProperty("水库规划档案id，关联到系统统一附件表sys_file")
    private String programFileId;

    /**
     * 水库计划档案id，关联到系统统一附件表sys_file
     */
    @ApiModelProperty("水库计划档案id，关联到系统统一附件表sys_file")
    private String planningFileId;

    /**
     * 水库应急预案id，关联到系统统一附件表sys_file
     */
    @ApiModelProperty("水库应急预案id，关联到系统统一附件表sys_file")
    private String emergencyFileId;

    /**
     * 上传年份
     */
    @ApiModelProperty("上传年份")
    private Integer year;

    public AttReservoirArchives() {
    }

}
