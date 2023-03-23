package com.ygkj.project.vo.response;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author hucong
 * @description 工程监督-监理月报表
 * @date 2021-07-09
 */
@Data
@ApiModel("工程监督-监理月报表")
public class ProjSupervisionReportRespVo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private String id;

    /**
     * 文件名称
     */
    @ApiModelProperty("文件名称")
    private String name;

    /**
     * 年份
     */
    @ApiModelProperty("年份")
    private String year;

    /**
     * 月份
     */
    @ApiModelProperty("月份")
    private String month;

    /**
     * 附件
     */
    @ApiModelProperty("附件")
    private String fileUrl;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 上传时间
     */
    @ApiModelProperty("上传时间")
    private Date uploadTime;

    /**
     * 上传人姓名
     */
    @ApiModelProperty("上传人姓名")
    private String uploadName;

    /**
     * 工程编码
     */
    @ApiModelProperty("工程编码")
    private String projectCode;

    /**
     * 工程名称
     */
    @ApiModelProperty("工程名称")
    private String projectName;

    public ProjSupervisionReportRespVo() {
    }

}