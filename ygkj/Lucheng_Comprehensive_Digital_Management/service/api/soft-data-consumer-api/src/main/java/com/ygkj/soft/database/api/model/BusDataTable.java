package com.ygkj.soft.database.api.model;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Huang.zh
 * @date 2020/9/8 8:54
 * @Description: 记录数据源中的表
 */

@Data
@ApiModel("数据源所有表格名称")
public class BusDataTable implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @ApiModelProperty("主键")
    private String id;

    /**
     * 所属数据源的id
     */
    @ApiModelProperty("所属数据源的id")
    private String dataSourceId;

    /**
     * 表格名称
     */
    @ApiModelProperty("表格名称")
    private String tableName;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remarks;

    /**
     * 排序标记
     */
    @ApiModelProperty("排序标记")
    private String sort;

    /**
     * 最后更新时间
     */
    @ApiModelProperty("最后更新时间")
    private String updateAt;

    /**
     * 总记录数
     */
    @ApiModelProperty("总记录数")
    private Integer rowsNum;

    @ApiModelProperty("资源目录Id")
    private String resourceId;

    /**
     * 业务分类
     */
    @ApiModelProperty("业务分类")
    private String category;

    public BusDataTable() {
    }

    public BusDataTable(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }
}

