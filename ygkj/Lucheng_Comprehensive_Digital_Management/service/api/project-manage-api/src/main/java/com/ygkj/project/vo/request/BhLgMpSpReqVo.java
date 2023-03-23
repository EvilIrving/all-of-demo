package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author lxl
 * @create 2022-04-28 17:18
 * @description
 */
@Data
public class BhLgMpSpReqVo {


    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private String id;

    /**
     * 检查时间
     */
    @ApiModelProperty("检查时间 格式 yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date tm;

    /**
     * 检查人员
     */
    @ApiModelProperty("检查人员")
    private String checkUid;

    /**
     * 检查人员
     */
    @ApiModelProperty("检查人员名称")
    private String checkUser;

    /**
     * 组织单位
     */
    @ApiModelProperty("组织单位")
    private String unitName;

    /**
     * 组织单位id
     */
    @ApiModelProperty("组织单位id")
    private String unitId;

    /**
     * 检查照片
     */
    @ApiModelProperty("检查照片")
    private String checkPic;

    /**
     * 存在问题
     */
    @ApiModelProperty("存在问题")
    private String problemDesc;

    /**
     * 问题类型
     */
    @ApiModelProperty("问题类型")
    private String problemType;

    /**
     * 检查报告
     */
    @ApiModelProperty("检查报告")
    private String fileIds;

    @ApiModelProperty("工程id")
    private String pid;


}
