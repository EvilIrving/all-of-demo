package com.ygkj.project.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author hucong
 * @description 工程监督-监理月报表
 * @date 2021-07-09
 */
@Data
@ApiModel("工程监督-监理月报表")
public class ProjSupervisionReportReqVo extends PageEntity {

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
     * 工程编码
     */
    @ApiModelProperty("工程编码")
    private String projectCode;

    /**
     * 上传时间
     */
    @ApiModelProperty("上传时间 格式:yyyy-MM-dd")
    private String uploadTime;

    /**
     * 上传人id
     */
    @ApiModelProperty(value = "上传人id", hidden = true)
    private String uploadId;

    /**
     * 类型 0:监理月报 1:质量评定 2:缺陷备案
     */
    @ApiModelProperty("类型 0:监理月报 1:质量评定 2:缺陷备案")
    private String type;

    public ProjSupervisionReportReqVo() {
    }

}