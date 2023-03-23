package com.ygkj.project.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hucong
 * @description 工程监督-监理月报表
 * @date 2021-07-09
 */
@Data
@ApiModel("工程监督-监理月报 新增或修改入参")
public class ProjSupervisionReportInsVo implements Serializable {

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
     * 工程编码
     */
    @ApiModelProperty("工程编码")
    private String projectCode;

    /**
     * 类型 0:监理月报 1:质量评定 2:缺陷备案
     */
    @ApiModelProperty("类型 0:监理月报 1:质量评定 2:缺陷备案")
    private String type;

    public ProjSupervisionReportInsVo() {
    }

}