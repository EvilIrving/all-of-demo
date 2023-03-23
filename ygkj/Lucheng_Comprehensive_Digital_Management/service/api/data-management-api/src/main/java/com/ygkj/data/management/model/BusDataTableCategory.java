package com.ygkj.data.management.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class BusDataTableCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @ApiModelProperty("id")
    private String id;

    /**
     * 资源目录名称
     */
    @ApiModelProperty("资源目录名称")
    private String categoryName;

    /**
     * 父级目录id
     */
    @ApiModelProperty("parentId")
    private String parentId;
    /**
     * 排序号
     */
    @ApiModelProperty("排序号")
    private Integer orderNumber;

    private Integer count;

    List<BusDataTableCategory> children;

    List<BusDataTable> tables;

    public BusDataTableCategory() {
    }


}

