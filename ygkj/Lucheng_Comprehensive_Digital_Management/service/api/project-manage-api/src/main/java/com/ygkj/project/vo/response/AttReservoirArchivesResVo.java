package com.ygkj.project.vo.response;

import com.ygkj.project.model.SysFile;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lxl
 */
@Data
@ApiModel("中大型水库防洪应急预案响应")
public class AttReservoirArchivesResVo {

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

    /**
     * 应急预案文件信息
     */
    @ApiModelProperty("应急预案文件信息")
    private SysFile sysFile;


    @ApiModelProperty("水库名称")
    private String resName;


    @ApiModelProperty("左下角经度")
    private Double lowLeftLong;


    @ApiModelProperty("左下角维度")
    private Double lowLeftLat;


    @ApiModelProperty("右上角经度")
    private Double upLeftLon;


    @ApiModelProperty("右上角维度")
    private Double upRightLat;


    @ApiModelProperty("水库所在位置")
    private String resLoc;


    @ApiModelProperty("水库类型")
    private String resType;

}
