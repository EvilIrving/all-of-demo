package com.ygkj.digitization.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author xq
 * @Description 业务管理-新增或修改参数
 * @Date 2021/2/1
 */
@Data
@ApiModel("业务管理-新增或修改参数")
public class BusManAddUpdateVo {
    /**
     * id
     */
    @ApiModelProperty("id,新增此项则为空")
    private String id;

    /**
     * 任务名称
     */
    @ApiModelProperty("任务名称")
    private String taskName;

    /**
     * 执行单位id
     */
    @ApiModelProperty("执行单位id")
    private String exeDeptId;

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

    @ApiModelProperty("类别")
    private String category;

    @ApiModelProperty("进度要求")
    private String progress;

    @ApiModelProperty("完成情况")
    private String completion;

    @ApiModelProperty("责任人员")
    private String respPerson;

    @ApiModelProperty("行政区划编码")
    private String adcd;

}
