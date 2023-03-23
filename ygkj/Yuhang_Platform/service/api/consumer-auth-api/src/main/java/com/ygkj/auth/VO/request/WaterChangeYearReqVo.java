package com.ygkj.auth.VO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: fml
 * @Date: 2020/10/20 16:17
 * @Description: 水域详情年度信息请求数据
 */
@Data
public class WaterChangeYearReqVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "水域变化信息id")
    private String waterChangeId;

    @ApiModelProperty(value = "年度(多个以 ，隔开)")
    private String waterYear;

    @ApiModelProperty(value = "字段(1水域面积，2库容量，3河底高程，4实际岸线),多个以，隔开")
    private String field;

}
