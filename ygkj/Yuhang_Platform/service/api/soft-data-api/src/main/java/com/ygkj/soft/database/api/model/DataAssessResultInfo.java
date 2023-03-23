package com.ygkj.soft.database.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author fml
 * @date 2021-05-20 16:46
 * @description 数据考核结果信息
 */
@Data
@ApiModel("数据考核结果信息")
public class DataAssessResultInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("科室id")
    private String deptId;

    @ApiModelProperty("科室名")
    private String deptName;

    @ApiModelProperty("表id")
    private String tableId;

    @ApiModelProperty("表名")
    private String tableName;

    @ApiModelProperty("评分月(yyyyMM)")
    private String assessMonth;

    @ApiModelProperty("分数")
    private Integer score;

    @ApiModelProperty("评分类型(1科室总评分，2科室表评分)")
    private Integer scoreType;

    @ApiModelProperty("超期时间")
    private Integer overdue;

    @ApiModelProperty("超期时间单元(1分/2时/3天)")
    private String overdueUnit;

    @ApiModelProperty("统计的表数量")
    private Integer tableNum;

    @ApiModelProperty("更新频次")
    private Integer frequency;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("删除标记(0未删，1已删)")
    private int delFlag;
}
