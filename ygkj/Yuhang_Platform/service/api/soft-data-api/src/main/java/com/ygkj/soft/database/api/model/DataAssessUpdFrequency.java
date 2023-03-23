package com.ygkj.soft.database.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author fml
 * @date 2021-05-20 16:43
 * @description 数据考核-数据更新频次信息
 */
@Data
@ApiModel("数据考核-数据更新频次信息")
public class DataAssessUpdFrequency implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("表id")
    private String tableId;

    @ApiModelProperty("表名")
    private String tableName;

    @ApiModelProperty("更新频次")
    private Integer frequency;

    @ApiModelProperty("更新频次单元(1分/2时/3天)")
    private String unit;

    @ApiModelProperty("具体更新时间(例如更新频次为1天的，执行更新时间为每天8点，格式为mm:ss)")
    private String specificTime;

    @ApiModelProperty("归属科室(多个以逗号分隔)")
    private String deptIds;

    @ApiModelProperty("科室名(避免跨库查询，多个以逗号分隔)")
    private String deptNames;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("删除标记(0未删，1已删)")
    private Integer delFlag;

    @ApiModelProperty("数据评分规则信息")
    private DataAssessScoreRule dataAssessScoreRule;
}
