package com.ygkj.soft.database.api.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Huang.zh
 * @date 2020/9/15 9:35
 * @Description: 目录划分实体类
 */
@Data
@ApiModel("bus_data_resource")
public class BusDataResource implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 划分名称
     */
    @ApiModelProperty("划分名称")
    private String name;

    /**
     * 数据源id
     */
    @ApiModelProperty("数据源id")
    private String dataSourceId;

    public BusDataResource() {
    }
}
