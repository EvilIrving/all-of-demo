package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author lxl
 * @create 2022-04-21 17:25
 * @description 隐患任务节点请求类
 */
@Data
@ApiModel("维修养护任务节点 （隐患处理数据）")
public class MtTaskNodeReqVo {

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private String id;

    /**
     * 任务id
     */
    @ApiModelProperty("任务id")
    private String taskId;

    /**
     * 执行顺序
     */
    @ApiModelProperty("执行顺序")
    private Integer sort;

    /**
     * 操作时间
     */
    @ApiModelProperty("操作时间 时间格式：yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date tm;

    /**
     * 操作人员
     */
    @ApiModelProperty("操作人员")
    private String operator;

    /**
     * 意见
     */
    @ApiModelProperty("意见")
    private String opinion;

    /**
     * 状态（待处理、已通过、已驳回、已审核）
     */
    @ApiModelProperty("状态（1待处理、2已通过、3已驳回、4已处理、5待审核 、6归档）")
    private Integer status;

    /**
     * 处理前图片
     */
    @ApiModelProperty("处理前图片，多文件id 用 , 隔开")
    private String imagesBefore;

    /**
     * 处理后图片
     */
    @ApiModelProperty("处理后图片")
    private String imagesAfter;

    /**
     * 处理文件
     */
    @ApiModelProperty("处理文件")
    private String fileIds;

    public MtTaskNodeReqVo() {
    }

}

