package com.ygkj.soft.database.api.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Huang.zh
 * @date 2020/11/9 13:43
 * @Description:
 */
@Data
public class BusDataTablePageQueryVo {
    @ApiModelProperty("数据源id")
    private String dataSourceId;
    @ApiModelProperty("分类id")
    private String resourceId;
    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("remarks")
    private String remarks;
    @ApiModelProperty("当前页码")
    private Integer page;
    @ApiModelProperty("当前限定每页记录数")
    private Integer rows;
    @ApiModelProperty("是否分页")
    private boolean fetchAll;
}
