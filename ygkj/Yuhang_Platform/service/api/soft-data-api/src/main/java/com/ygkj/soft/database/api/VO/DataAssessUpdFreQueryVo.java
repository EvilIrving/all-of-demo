package com.ygkj.soft.database.api.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fml
 * @date 2021-05-20 17:58
 * @description 数据更新频次查询信息
 */
@Data
public class DataAssessUpdFreQueryVo {

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

    @ApiModelProperty("科室id")
    private String deptId;

    @ApiModelProperty("科室名")
    private String deptName;

    @ApiModelProperty("当前页")
    private Integer page = 1;
    @ApiModelProperty("每页规定记录数")
    private Integer rows = 10;
    @ApiModelProperty("是否查询全部")
    private boolean fetchAll;
}
