package com.ygkj.project.vo.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author xq
 * @Description
 * @Date 2021/4/13
 */
@Data
@ApiModel(description = "摄像头查询入参")
public class HikCameraQueryReqVo {
    @ApiModelProperty("摄像头名称")
    String cameraName;
    @ApiModelProperty("摄像头区域编码")
    String regionIndexCode;
    @ApiModelProperty("摄像头编码")
    String cameraIndexCode;
    @ApiModelProperty("国标编码")
    String gbIndexCode;
    @ApiModelProperty("区域树和摄像头的哈希值")
    private Integer regionCameraHash;

    @ApiModelProperty("行政区划编码")
    String areaCode;
    @ApiModelProperty("在线状态:0为下线,1为在线")
    String status;

    @ApiModelProperty("页码，最小为1，仅分页接口有效")
    int pageNum = 1;
    @ApiModelProperty("页大小，最小为1，仅分页接口有效")
    int pageSize = 10;

    @ApiModelProperty("排序column列名")
    String orderColumn;

    @ApiModelProperty("升序降序,false asc/true desc")
    Boolean ascOrDesc = false;

    @ApiModelProperty("不允许落点为空")
    boolean coordinateNotNull = true;

    @ApiModelProperty("不包含的摄像头编码，多个摄像头用逗号隔开")
    String excludeCameraIndexCode;

    @ApiModelProperty("在线离线时长统计时间段起始日期，格式yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date stiStart;

    @ApiModelProperty("在线离线时长统计时间段截止日期，格式yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date stiEnd;

    /**
     * 为了兼容之前的接口，默认为true
     */
    @ApiModelProperty("是否带入绑定的工程信息，默认为true")
    boolean withProjInfo = true;

    @ApiModelProperty("工程类型，仅关联查询时有用")
    String projectType;

    @ApiModelProperty("不属于的工程类型，仅关联查询时有用")
    String notBelongProjType;

    @ApiModelProperty("是否带入运维人员信息，默认为false")
    boolean withOpsInfo = false;

    @ApiModelProperty("运维人员名称，用于模糊搜索")
    String opsName;

    @ApiModelProperty("导出策略，默认为0，0：用于大屏组件详表导出接口；1：用于报警管理列表导出")
    Integer exportStrategy = 0;
}
