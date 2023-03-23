package com.ygkj.soft.database.api.VO;

import com.ygkj.soft.database.api.model.DataAssessUpdFrequency;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fml
 * @date 2021-05-21 14:08
 * @description
 */
@Data
public class DataAssessInfo extends DataAssessUpdFrequency {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("分数规则0~19的时间范围(以频次表时间单元为准)")
    private Integer column1;

    @ApiModelProperty("分数规则20~39的时间范围")
    private Integer column2;

    @ApiModelProperty("分数规则40~59的时间范围")
    private Integer column3;

    @ApiModelProperty("分数规则60~79的时间范围")
    private Integer column4;

    @ApiModelProperty("分数规则80~100的时间范围")
    private Integer column5;

    /*@ApiModelProperty("表格名称")
    private String tableName;*/

    @ApiModelProperty("数据库名称")
    private String dataBaseName;
}
