package com.ygkj.flood.vo.request;

import com.ygkj.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lxl
 * @create 2022-05-20 13:53
 * @description
 */
@Data
public class AttWarehouseReqVo extends PageEntity {

    @ApiModelProperty("id")
    private String id;


    @ApiModelProperty("物资名称")
    private String materialName;


    @ApiModelProperty("类型")
    private String type;


    @ApiModelProperty("仓库id")
    private String warehouseId;


    @ApiModelProperty("数量")
    private Integer num;



}
