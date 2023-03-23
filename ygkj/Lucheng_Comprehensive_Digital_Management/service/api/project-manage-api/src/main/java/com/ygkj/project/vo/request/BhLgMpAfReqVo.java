package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author lxl
 * @create 2022-04-28 17:13
 * @description
 */
@Data
@ApiModel
public class BhLgMpAfReqVo {


    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private String id;

    /**
     * 检查时间
     */
    @ApiModelProperty("检查时间  格式 yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date tm;

    /**
     * 参加人员
     */
    @ApiModelProperty("参加人员")
    private String joinUid;

    /**
     * 参加人员
     */
    @ApiModelProperty("参加人员名称")
    private String joinUser;

    /**
     * 水管单位负责人
     */
    @ApiModelProperty("水管单位负责人")
    private String responseUid;

    /**
     * 水管单位负责人
     */
    @ApiModelProperty("水管单位负责人名称")
    private String responseUser;

    /**
     * 隐患数量
     */
    @ApiModelProperty("隐患数量")
    private Integer hitCount;

    /**
     * 问题类型
     */
    @ApiModelProperty("问题类型")
    private String problemType;

    /**
     * 检查照片
     */
    @ApiModelProperty("检查照片")
    private String checkPic;

    /**
     * 定期（汛后）检查记录表
     */
    @ApiModelProperty("定期（汛后）检查记录表")
    private String fileIds;

    @ApiModelProperty("工程id")
    private String pid;

}
