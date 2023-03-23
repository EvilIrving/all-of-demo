package com.ygkj.soft.database.api.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fml
 * @date 2021-05-21 10:23
 * @description
 */
@Data
public class DataAssessResultQueryVo {

    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("科室名")
    private String deptName;

    @ApiModelProperty("表名")
    private String tableName;

    @ApiModelProperty("评分月(yyyyMM)")
    private String assessMonth;

    @ApiModelProperty("分数起始值")
    private Integer scoreBegin;

    @ApiModelProperty("分数最大值")
    private Integer scoreEnd;

    @ApiModelProperty("评分类型(1科室总评分，2科室表评分)")
    private Integer scoreType;

    @ApiModelProperty("当前页")
    private Integer page = 1;
    @ApiModelProperty("每页规定记录数")
    private Integer rows = 10;
    @ApiModelProperty("是否查询全部")
    private boolean fetchAll;
}
