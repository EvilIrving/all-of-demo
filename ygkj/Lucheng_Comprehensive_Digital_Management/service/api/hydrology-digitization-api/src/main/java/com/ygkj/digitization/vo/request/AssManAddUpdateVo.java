package com.ygkj.digitization.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author xq
 * @Description 任务书下达-新增或修改参数
 * @Date 2021/2/1
 */
@Data
@ApiModel("任务书下达-新增或修改参数")
public class AssManAddUpdateVo {
    /**
     * id
     */
    @ApiModelProperty("id,新增此项则为空")
    private String id;

    /**
     * 项目名称
     */
    @ApiModelProperty("项目名称")
    private String itemName;


    /**
     * 任务要求
     */
    @ApiModelProperty("任务要求")
    private String taskRequest;

    /**
     * 计划完成次数
     */
    @ApiModelProperty("计划完成次数")
    private String planFinishTimes;

    /**
     * 执行起始时间
     */
    @ApiModelProperty("执行起始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date exeStartTime;

    /**
     * 执行结束时间
     */
    @ApiModelProperty("执行结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date exeEndTime;

    /**
     * 执行单位id
     */
    @ApiModelProperty("执行单位id")
    private String exeDeptId;

    @ApiModelProperty("文件名称")
    private String fileName;

    @ApiModelProperty("文件附件")
    private String fileUrl;

    @ApiModelProperty("上传人姓名")
    private String uploadName;

    /**
     * 执行单位所属行政编码
     */
    @ApiModelProperty("行政编码")
    private String adcd;
}
